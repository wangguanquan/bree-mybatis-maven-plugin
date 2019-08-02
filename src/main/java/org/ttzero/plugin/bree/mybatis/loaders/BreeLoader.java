/*
 * Copyright (c) 2019, guanquan.wang@yandex.com All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ttzero.plugin.bree.mybatis.loaders;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.ttzero.plugin.bree.mybatis.BreeException;
import org.ttzero.plugin.bree.mybatis.model.Gen;
import org.ttzero.plugin.bree.mybatis.model.config.CfAssociation;
import org.ttzero.plugin.bree.mybatis.model.config.CfCollection;
import org.ttzero.plugin.bree.mybatis.model.config.CfOperation;
import org.ttzero.plugin.bree.mybatis.model.config.CfResultMap;
import org.ttzero.plugin.bree.mybatis.model.config.CfTable;
import org.ttzero.plugin.bree.mybatis.model.config.OperationMethod;
import org.ttzero.plugin.bree.mybatis.model.dbtable.Column;
import org.ttzero.plugin.bree.mybatis.model.dbtable.Table;
import org.ttzero.plugin.bree.mybatis.model.java.Base;
import org.ttzero.plugin.bree.mybatis.model.java.Dao;
import org.ttzero.plugin.bree.mybatis.model.java.Do;
import org.ttzero.plugin.bree.mybatis.model.java.DoMapper;
import org.ttzero.plugin.bree.mybatis.model.java.Field;
import org.ttzero.plugin.bree.mybatis.model.java.ResultMap;
import org.ttzero.plugin.bree.mybatis.model.java.Vo;
import org.ttzero.plugin.bree.mybatis.model.java.XmlMapper;
import org.ttzero.plugin.bree.mybatis.model.repository.CfTableRepository;
import org.ttzero.plugin.bree.mybatis.model.repository.DataObjectConfig;
import org.ttzero.plugin.bree.mybatis.model.repository.JavaConfig;
import org.ttzero.plugin.bree.mybatis.model.repository.JavaProperty;
import org.ttzero.plugin.bree.mybatis.model.repository.TableRepository;
import org.ttzero.plugin.bree.mybatis.model.repository.VoConfig;
import org.ttzero.plugin.bree.mybatis.utils.ConfigUtil;
import org.ttzero.plugin.bree.mybatis.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import org.ttzero.plugin.bree.mybatis.enums.MultiplicityEnum;
import org.ttzero.plugin.bree.mybatis.enums.ParamTypeEnum;
import org.ttzero.plugin.bree.mybatis.enums.TypeMapEnum;
import org.ttzero.plugin.bree.mybatis.model.java.domapper.DoMapperMethod;
import org.ttzero.plugin.bree.mybatis.model.java.domapper.DoMapperMethodParam;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/**
 * 主逻辑
 * Created by guanquan.wang at 2019-05-24 09:05
 */
public class BreeLoader extends AbstractLoader {
    /**
     * The constant LOG.
     */
    private static final Log LOG = new SystemStreamLog();

    /**
     * The Table repository.
     */
    private TableRepository tableRepository = new TableRepository();

    /**
     * The Gen xml file repository.
     */
    private CfTableRepository cfTableRepository = new CfTableRepository();

    /**
     * The constant RESULT_MANY.
     */
    private static final String RESULT_MANY = "List<{0}>";

    /**
     * Load.
     *
     * @param gen        the gen
     * @param connection the connection
     * @param tablesFile the tables file
     * @throws Exception the exception
     */
    @Override
    public void load(Gen gen, Connection connection, File tablesFile) throws Exception {
        // 解析所有table.xml(为生成sqlMap.xml做准备)
        Map<String, CfTable> cfTableMap = Maps.newHashMap();
        File[] files = tablesFile.listFiles((f, name) -> name.endsWith(".xml"));
        if (files == null) {
            LOG.error("缺少table.xml");
            return;
        }
        for (File file : files) {
            cfTableMap.put(file2DbName(file), cfTableRepository.gainCfTable(file));
        }

        List<String> needGenTableNames = preNeedGenTableNames(ConfigUtil.cmd, cfTableMap);

        // 获取需要重新生成的表(为重新生成Mapper.xml,DO,Mapper.java 准备)
        Map<String, Table> tableMap = Maps.newHashMap();

        for (String tbName : needGenTableNames) {
            tableMap.put(tbName,
                tableRepository.gainTable(connection, tbName, cfTableMap.get(tbName)));
        }

        // 根据需要重新生成的表 准备数据
        for (String tbName : needGenTableNames) {
            CfTable cfTable = cfTableMap.get(tbName);
            Table table = tableMap.get(tbName);
            // 准备DO
            Do doClass = preDo(table, cfTable.getColumns());
            gen.addDo(doClass);

            // 准备Mapper.xml
            XmlMapper xmlMapper = new XmlMapper();
            // 准备resultMap对应的对象
//            Map<String, Column> tbColumnMap = Maps.newHashMap();
//            for (Column column : table.getColumnList()) {
//                tbColumnMap.put(column.getSqlName(), column);
//            }
//            Map<String, Fields> fieldMap = Maps.newHashMap();
//            for (Fields field : doClass.getFieldsList()) {
//                fieldMap.put(field.getName(), field);
//            }
            Map<String, ResultMap> resultMaps = Maps.newHashMap();

            preResultMap(gen, tbName, cfTable, table, xmlMapper, resultMaps);

            // 准备Mapper接口
            DoMapper doMapper = preDOMapper(gen, cfTable, table, doClass, resultMaps);
            gen.addDoMapper(doMapper);

            // TODO

            // 准备DAO类
            if (!ConfigUtil.config.isIgnoreDao()) {
                Dao dao = preDAO(cfTable, table, doClass, resultMaps);
                getClassAndImport(dao, doMapper.getPackageName() + "." + doMapper.getClassName());
                dao.setDoMapper(doMapper);
                gen.addDao(dao);
            }

            // xml-mapper
            xmlMapper.setCfTable(cfTable);
            xmlMapper.setDoClass(doClass);
            xmlMapper.setDoMapper(doMapper);
            xmlMapper.setTable(table);
            String xmlMapperPath = ConfigUtil.config.getXmlMapperPath();
            if (StringUtil.isEmpty(xmlMapperPath) || "resources".equals(xmlMapperPath.replaceAll("/|\\\\", ""))) {
                xmlMapperPath = "resources/" + doMapper.getClassPath();
            }
            xmlMapper.setClassPath(xmlMapperPath);

            gen.addXmlMapper(xmlMapper);

            // TODO Service
            // TODO Controller


            // FIXME check exists author
            // FIXME don't rewrite if author exists
            gen.setAuthor(System.getProperty("user.name"));
        }

    }

    /**
     * Pre need gen table names list.
     *
     * @param cmd        the cmd
     * @param cfTableMap the cf table map
     * @return the list
     */
    private List<String> preNeedGenTableNames(String cmd, Map<String, CfTable> cfTableMap) {
        List<String> needGenTableNames = Lists.newArrayList();
        if ("*".equals(cmd.trim())) {
            needGenTableNames = Lists.newArrayList(cfTableMap.keySet());
        } else {
            for (String tableName : StringUtils.split(cmd.toUpperCase())) {
                tableName = tableName.toUpperCase();
                boolean flag = true;
                for (String splitTableSuffix : ConfigUtil.getCurrentDb().getSplitSuffixs()) {
                    if (tableName.endsWith(splitTableSuffix)) {
                        needGenTableNames.add(tableName.substring(0, tableName.length() - splitTableSuffix.length()));
                        flag = false;
                        break;
                    }
                }
                if (flag) {
                    needGenTableNames.add(tableName);
                }
            }
        }
        return needGenTableNames;
    }

    /**
     * Pre result map.
     *
     * @param gen        the gen
     * @param tbName     the tb name
     * @param cfTable    the cf table
     * @param table      the table
     * @param xmlMapper  the xml mapper
     * @param resultMaps the result maps
     */
    private void preResultMap(Gen gen, String tbName, CfTable cfTable, Table table,
                              XmlMapper xmlMapper, Map<String, ResultMap> resultMaps) {
        List<CfResultMap> resultMapList = cfTable.getResultMaps();
        DataObjectConfig doConfig = ConfigUtil.config.getDoConfig();
        String namespace;
        if (StringUtil.isEmpty(namespace = doConfig.getNamespace())) {
            namespace = "do";
        }
        String prefix = doConfig.getPrefix()
            , suffix = doConfig.getSuffix();
        if (prefix == null) prefix = "";
        if (suffix == null) suffix = "";
        for (CfResultMap cfResultMap : resultMapList) {
            ResultMap resultMap = new ResultMap();
            resultMap.setTableName(table.getName());
            resultMap.setId(cfResultMap.getId());
            resultMap.setType(cfResultMap.getType());

            String type = cfResultMap.getType();
            int index = type.lastIndexOf('.');
            if (index < 0) {
                resultMap.setClassName(prefix + cfResultMap.getType() + suffix);
                resultMap.setClassPath(ConfigUtil.getCurrentDb().getGenPackagePath() + "/" + namespace);
                resultMap.setPackageName(ConfigUtil.getCurrentDb().getGenPackage() + "." + namespace);
            } else {
                resultMap.setClassName(type.substring(index + 1));
                String packageName = type.substring(0, index);
                resultMap.setClassPath(packageName.replace('.', '/'));
                resultMap.setPackageName(packageName);
            }

            resultMap.setDesc(cfResultMap.getRemark());
            doColumn(gen, tbName, resultMap, cfResultMap.getColumns(), null);

            List<CfCollection> cfCollectionList = cfResultMap.getCollections();
            List<CfAssociation> cfAssociationList = cfResultMap.getAssociations();
            if (!cfAssociationList.isEmpty() || !cfCollectionList.isEmpty()) {

                DocumentFactory factory = DocumentFactory.getInstance();
                Element rootElement = factory.createElement("root");

                for (CfAssociation assoc : cfAssociationList) {
                    Field field = new Field();
                    field.setJavaType(assoc.getJavaType());
                    field.setName(assoc.getProperty());
                    field.setDesc(assoc.getRemark());

                    resultMap.addField(field);

                    deepAssociation(gen, tbName, assoc, rootElement, namespace, prefix, suffix);
                }

                for (CfCollection coll : cfCollectionList) {
                    Field field = new Field();
                    resultMap.addImport("java.util.List");
                    field.setJavaType(MessageFormat.format(RESULT_MANY, coll.getOfType()));
                    field.setName(coll.getProperty());
                    field.setDesc(coll.getRemark());

                    resultMap.addField(field);
                    deepCollection(gen, tbName, coll, rootElement, namespace, prefix, suffix);
                }

                // inner xml
                resultMap.setInnerXML(writeToByteArray(factory.createDocument(rootElement)));
            }
            resultMaps.put(cfResultMap.getId(), resultMap);
            xmlMapper.addResultMap(resultMap);
            gen.addResultMap(resultMap);
        }
    }

    private String writeToByteArray(Document doc) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            //write the created document to an arbitrary file
            OutputFormat format = OutputFormat.createPrettyPrint();
            XMLWriter writer = new XMLWriter(baos, format);
            writer.write(doc);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            throw new BreeException("参数设置错误#{}中,未正确使用 table=");
        }
        byte[] bytes = baos.toByteArray();
        // 去掉<?xml version="1.0" encoding="UTF-8"?>标记信息
        return new String(bytes, 49, bytes.length - 58, StandardCharsets.UTF_8)
            .replace("  ", "    ").replace("<", "    <");
    }

    private void deepCollection(Gen gen, String tbName, CfCollection coll, Element e, String namespace, String prefix, String suffix) {
        Element subEle = e.addElement("collection");
        if (StringUtils.isNotEmpty(coll.getProperty())) {
            subEle.addAttribute("property", coll.getProperty());
        }
        if (StringUtils.isNotEmpty(coll.getOfType())) {
            subEle.addAttribute("ofType", ConfigUtil.getCurrentDb().getGenPackage() + "." + namespace + "."  + coll.getOfType());
        } else if (StringUtils.isNotEmpty(coll.getResultMap())) {
            subEle.addAttribute("resultMap", coll.getResultMap());
        }


        ResultMap resultMap = new ResultMap();
        resultMap.setTableName("");
        resultMap.setId(coll.getProperty());
        resultMap.setType(coll.getOfType());
        resultMap.setClassName(prefix + coll.getOfType() + suffix);
        resultMap.setClassPath(ConfigUtil.getCurrentDb().getGenPackagePath() + "/" + namespace);
        resultMap.setPackageName(ConfigUtil.getCurrentDb().getGenPackage() + "." + namespace);
        resultMap.setDesc(coll.getRemark());
        doColumn(gen, tbName, resultMap, coll.getColumns(), subEle);
        gen.addResultMap(resultMap);

        List<CfAssociation> cfAssociationList = coll.getAssociations();
        List<CfCollection> cfCollectionList = coll.getCollections();
        if (!cfAssociationList.isEmpty() || !cfCollectionList.isEmpty()) {

            subAssociation(resultMap, gen, tbName, subEle, cfAssociationList, namespace, prefix, suffix);

            subCollection(resultMap, gen, tbName, subEle, cfCollectionList, namespace, prefix, suffix);
        }
    }

    private void deepAssociation(Gen gen, String tbName, CfAssociation assoc, Element e, String namespace, String prefix, String suffix) {
        Element subEle = e.addElement("association");
        if (StringUtils.isNotEmpty(assoc.getProperty())) {
            subEle.addAttribute("property", assoc.getProperty());
        }
        if (StringUtils.isNotEmpty(assoc.getJavaType())) {
            subEle.addAttribute("javaType", ConfigUtil.getCurrentDb().getGenPackage() + "." + namespace + "." + assoc.getJavaType());
        } else if (StringUtils.isNotEmpty(assoc.getResultMap())) {
            subEle.addAttribute("resultMap", assoc.getResultMap());
        }
        if (StringUtils.isNotEmpty(assoc.getColumn())) {
            subEle.addAttribute("column", assoc.getColumn());
        }
        if (StringUtils.isNotEmpty(assoc.getSelect())) {
            subEle.addAttribute("select", assoc.getSelect());
        }

        ResultMap resultMap = new ResultMap();
        resultMap.setTableName("");
        resultMap.setId(assoc.getProperty());
        resultMap.setType(assoc.getJavaType());
        resultMap.setClassName(prefix + assoc.getJavaType() + suffix);
        resultMap.setClassPath(ConfigUtil.getCurrentDb().getGenPackagePath() + "/" + namespace);
        resultMap.setPackageName(ConfigUtil.getCurrentDb().getGenPackage() + "." + namespace);
        resultMap.setDesc(assoc.getRemark());
        doColumn(gen, tbName, resultMap, assoc.getColumns(), subEle);
        gen.addResultMap(resultMap);

        List<CfAssociation> cfAssociationList = assoc.getAssociations();
        List<CfCollection> cfCollectionList = assoc.getCollections();
        if (!cfAssociationList.isEmpty() || !cfCollectionList.isEmpty()) {

            subAssociation(resultMap, gen, tbName, subEle, cfAssociationList, namespace, prefix, suffix);

            subCollection(resultMap, gen, tbName, subEle, cfCollectionList, namespace, prefix, suffix);
        }
    }

    private void subAssociation(ResultMap resultMap, Gen gen, String tbName, Element subEle, List<CfAssociation> cfAssociationList, String namespace, String prefix, String suffix) {
        for (CfAssociation subAssoc : cfAssociationList) {
            Field field = new Field();
            field.setJavaType(subAssoc.getJavaType());
            field.setName(subAssoc.getProperty());
            field.setDesc(subAssoc.getRemark());

            resultMap.addField(field);

            deepAssociation(gen, tbName, subAssoc, subEle, namespace, prefix, suffix);
        }
    }

    private void subCollection(ResultMap resultMap, Gen gen, String tbName, Element subEle, List<CfCollection> cfCollectionList, String namespace, String prefix, String suffix) {
        for (CfCollection subColl : cfCollectionList) {
            Field field = new Field();
            resultMap.addImport("java.util.List");
            field.setJavaType(MessageFormat.format(RESULT_MANY, subColl.getOfType()));
            field.setName(subColl.getProperty());
            field.setDesc(subColl.getRemark());

            resultMap.addField(field);
            deepCollection(gen, tbName, subColl, subEle, namespace, prefix, suffix);
        }
    }


    /**
     * @param gen
     * @param tbName
     * @param resultMap
     * @param cfColumns
     */
    private void doColumn(Gen gen, String tbName, ResultMap resultMap, List<Column> cfColumns, Element e) {
        for (Column column : cfColumns) {
            Validate.notEmpty(column.getColumn(), tbName
                + ".xml 配置有误 BreeLoader.preResultMap Gen=" + gen);
            // 生成XML <resultMap> 标签使用
//            Column column = new Column();
//            column.setJavaName(CamelCaseUtils.toCamelCase(cfColumn.getName()));
//            column.setJavaType(cfColumn.getJavaType());
//            column.setSqlName(cfColumn.getName());
//            column.setSqlType(cfColumn.getSqlType());
//            column.setRemarks(cfColumn.getRemark());
            resultMap.addColumn(column);

            // 生成ResultMap.java文件使用
            Field field = new Field();
            field.setJavaType(getClassAndImport(resultMap, column.getJavaType()));
            field.setName(column.getProperty());
            field.setDesc(column.getRemark());

            resultMap.addField(field);

            if (e != null) {
                Element ele;
                if ("ID".equals(column.getColumn()) || column.isPrimaryKey()) {
                    ele = e.addElement("id");
                } else {
                    ele = e.addElement("result");
                }
                ele.addAttribute("column", column.getColumn());
                ele.addAttribute("property", column.getProperty());
                ele.addAttribute("jdbcType", column.getJdbcType());
                ele.addAttribute("javaType", column.getJavaType());
            }
        }
    }

    /**
     * Pre dao dao.
     *
     * @param cfTable    the cf table
     * @param table      the table
     * @param doClass    the do class
     * @param resultMaps the result maps
     * @return the dao
     */
    private Dao preDAO(CfTable cfTable, Table table, Do doClass,
                       Map<String, ResultMap> resultMaps) {
        Dao dao = new Dao();
        JavaConfig javaConfig = ConfigUtil.config.getDaoConfig();
        String property;
        // The suffix
        if (StringUtil.isEmpty(property = javaConfig.getSuffix())) {
            property = "Dao";
        }
        String prefix;
        if (StringUtil.isEmpty(prefix = javaConfig.getPrefix())) {
            prefix = "";
        }
        dao.setClassName(prefix + table.getJavaName() + property);
        // The namespace, default "mapper"
        if (StringUtil.isEmpty(property = javaConfig.getNamespace())) {
            property = "dao";
        }
        dao.setPackageName(ConfigUtil.getCurrentDb().getGenPackage() + "." + property);
        dao.setClassPath(ConfigUtil.getCurrentDb().getGenPackagePath() + "/" + property);
        dao.setDesc(cfTable.getRemark());
        dao.setTableName(cfTable.getName());

        // append java config
        addJavaConfig(dao, javaConfig, null);

        Map<String, String> columnTypeMap = Maps.newHashMap();
//        Map<String, String> columnDescMap = Maps.newHashMap();
        for (Column column : table.getColumnList()) {
            columnTypeMap.put(column.getProperty(), column.getJavaType());
//            columnDescMap.put(column.getJavaName(), column.getRemarks());
        }

        for (CfOperation operation : cfTable.getOperations()) {
            preDAOMethod(doClass, resultMaps, dao, operation, columnTypeMap);
        }
        return dao;
    }

    /**
     * Pre dao method.
     *
     * @param doClass    the do class
     * @param resultMaps the result maps
     * @param dao        the dao
     * @param operation  the operation
     * @param columnMap  the column map
     */
    private void preDAOMethod(Do doClass, Map<String, ResultMap> resultMaps, Dao dao,
                              CfOperation operation, Map<String, String> columnMap) {

        DoMapperMethod method = new DoMapperMethod();
        method.setName(operation.getId());
        method.setDesc(operation.getRemark());
        method.setSql(operation.getSqlDesc());

        String resultType;
        if (operation.getMultiplicity() == MultiplicityEnum.paging) {
            Vo paging = new Vo();
            VoConfig voConfig = ConfigUtil.config.getVoConfig();
            String namespace;
            if (StringUtil.isEmpty(namespace = voConfig.getNamespace())) {
                namespace = "vo";
            }
            String prefix = voConfig.getPrefix()
                , suffix = voConfig.getSuffix();
            if (prefix == null) prefix = "";
            if (suffix == null) suffix = "Vo";

            String vo = operation.getVo();
            int index = vo.lastIndexOf('.');
            if (index < 0) {
                paging.setClassName(prefix + StringUtil.upperFirst(operation.getVo()) + suffix);
                paging.setPackageName(ConfigUtil.getCurrentDb().getGenPackage() + "." + namespace);
            } else {
                paging.setClassName(vo.substring(index + 1));
                paging.setPackageName(vo.substring(0, index));
            }

            resultType = getClassAndImport(dao, paging.getPackageName() + "." + paging.getClassName());
            DoMapperMethodParam param = new DoMapperMethodParam(resultType,
                StringUtil.lowerFirst(operation.getVo()));
            method.setPagingFlag("true");
            if (operation.isNoCount()) {
                method.makeNoCount();
            }
            method.addParam(param);
        } else {
            preMethodParam(doClass, dao, operation, method, columnMap);
            resultType = operationResultType(doClass, dao, operation, resultMaps);
        }
        method.setReturnClass(resultType);

        dao.addMethod(method);
    }

    /**
     * Pre do mapper do mapper.
     *
     * @param gen        the gen
     * @param cfTable    the cf table
     * @param table      the table
     * @param doClass    the do class
     * @param resultMaps the result maps
     * @return the do mapper
     */
    private DoMapper preDOMapper(Gen gen, CfTable cfTable, Table table, Do doClass,
                                 Map<String, ResultMap> resultMaps) {
        DoMapper doMapper = new DoMapper();
        JavaConfig javaConfig = ConfigUtil.config.getDoMapperConfig();
        String property;
        // The suffix
        if (StringUtil.isEmpty(property = javaConfig.getSuffix())) {
            property = "Mapper";
        }
        String prefix;
        if (StringUtil.isEmpty(prefix = javaConfig.getPrefix())) {
            prefix = "";
        }
        doMapper.setClassName(prefix + doClass.getClassName() + property);
        // The namespace, default "mapper"
        if (StringUtil.isEmpty(property = javaConfig.getNamespace())) {
            property = "mapper";
        }
        doMapper.setPackageName(ConfigUtil.getCurrentDb().getGenPackage() + "." + property);
        doMapper.setClassPath(ConfigUtil.getCurrentDb().getGenPackagePath() + "/" + property);
        doMapper.setDesc(cfTable.getRemark());
        doMapper.setTableName(cfTable.getName());

        // add java config
        addJavaConfig(doMapper, javaConfig, null);

        Map<String, String> columnTypeMap = Maps.newHashMap();
        Map<String, String> columnDescMap = Maps.newHashMap();
        for (Column column : table.getColumnList()) {
            String fieldName = column.getProperty();
            columnTypeMap.put(fieldName, column.getJavaType());
            columnDescMap.put(fieldName, column.getRemark());
        }

        for (CfOperation operation : cfTable.getOperations()) {
            if (operation.getMultiplicity() == MultiplicityEnum.paging) {//分页
                prePagingMethod(gen, cfTable, table, doClass, resultMaps, doMapper, columnTypeMap,
                    columnDescMap, operation);
            } else {
                preMethod(doClass, resultMaps, doMapper, operation, columnTypeMap);
                if (StringUtil.isNotEmpty(operation.getVo())) {
                    gen.addVo(createVo(cfTable, table, columnTypeMap, columnDescMap, operation));
                }
            }
        }
        return doMapper;
    }

    /**
     * Pre paging method.
     *
     * @param gen           the gen
     * @param cfTable       the cf table
     * @param table         the table
     * @param doClass       the do class
     * @param resultMaps    the result maps
     * @param doMapper      the do mapper
     * @param columnTypeMap the column type map
     * @param columnDescMap the column desc map
     * @param operation     the operation
     */
    private void prePagingMethod(Gen gen, CfTable cfTable, Table table, Do doClass,
                                 Map<String, ResultMap> resultMaps, DoMapper doMapper,
                                 Map<String, String> columnTypeMap,
                                 Map<String, String> columnDescMap, CfOperation operation) {
        DoMapperMethod pagingResultMethod = new DoMapperMethod();


        pagingResultMethod.setName(operation.getId() + "Result");
        pagingResultMethod.setPagingName(operation.getId());
        pagingResultMethod.setDesc(operation.getRemark());
        pagingResultMethod.setSql(operation.getSqlDesc());
        pagingResultMethod.setPagingFlag("true");

        Vo paging = createVo(cfTable, table, columnTypeMap, columnDescMap, operation);

        String pagingResultType = operationResultType(doClass, paging, operation, resultMaps);
        paging.setResultType(pagingResultType);
        gen.addVo(paging);
        gen.setUseBasePage(ConfigUtil.config.getVoConfig().isUseBasePageVo());

        // paging import到doMapper
        getClassAndImport(doMapper, paging.getPackageName() + "." + paging.getClassName());
        getClassAndImport(doMapper, "java.util.List");

        // 方法返回结果
        DoMapperMethodParam pagingParam = new DoMapperMethodParam(paging.getClassName(),
            StringUtil.lowerFirst(operation.getVo()));
        pagingResultMethod.addParam(pagingParam);

        String resultType = operationResultType(doClass, doMapper, operation, resultMaps);

        paging.setResultType(resultType);
        pagingResultMethod.setReturnClass("List<" + resultType + ">");
        // If exists customize count
        if (!operation.isNoCount() && !operation.isCustomizeCount()) {
            DoMapperMethod pagingCountMethod = pagingResultMethod.deepClone();
            pagingCountMethod.setName(operation.getId() + "Count");
            pagingCountMethod.setReturnClass("int");
            doMapper.addMethod(pagingCountMethod);
        } else {
            pagingResultMethod.makeNoCount();
        }

        doMapper.addMethod(pagingResultMethod);
    }

    private Vo createVo(CfTable cfTable, Table table, Map<String, String> columnTypeMap
        , Map<String, String> columnDescMap, CfOperation operation) {
        VoConfig voConfig = ConfigUtil.config.getVoConfig();
        String namespace = voConfig.getNamespace();
        if (StringUtil.isEmpty(namespace)) {
            namespace = "vo";
        }
        String prefix = voConfig.getPrefix()
            , suffix = voConfig.getSuffix();
        if (prefix == null) prefix = "";
        if (suffix == null) suffix = "Vo";

        Vo paging = new Vo();

        String vo = operation.getVo();
        int index = vo.lastIndexOf('.');
        if (index < 0) {
            paging.setClassName(prefix + vo + suffix);
            paging.setClassPath(ConfigUtil.getCurrentDb().getGenPackagePath() + "/" + namespace);
            paging.setPackageName(ConfigUtil.getCurrentDb().getGenPackage() + "." + namespace);
        } else {
            paging.setClassName(vo.substring(index + 1));
            String packageName = vo.substring(0, index);
            paging.setClassPath(packageName.replace('.', '/'));
            paging.setPackageName(packageName);
        }

        paging.setDesc(StringUtil.join(table.getName(), cfTable.getRemark()));
        paging.setTableName(cfTable.getName());

        // append java config
        addJavaConfig(paging, voConfig, operation.getMultiplicity());

        if (operation.getMultiplicity() == MultiplicityEnum.paging
            && voConfig.isUseBasePageVo() && paging.getExtend() == null) {
            // BasePage package is same as Vo
            paging.setExtend(new JavaProperty("BasePage", null));
        }

        List<DoMapperMethodParam> params = preMethodParams(paging, operation, columnTypeMap);
        boolean hasForeach = !operation.getPrimitiveForeachParams().isEmpty();
        Map<String, String> foreachMap = null;
        if (hasForeach) {
            foreachMap = Maps.newHashMap();
            for (Map.Entry<String, String> entry : operation.getPrimitiveForeachParams().entrySet()) {
                foreachMap.put(entry.getValue(), entry.getKey());
            }
        }
        for (DoMapperMethodParam param : params) {
            Field field = new Field();
            field.setName(param.getParam());
            field.setJavaType(param.getParamType());
            field.setDesc(getFieldDesc(param, columnDescMap, foreachMap));
            paging.addField(field);
        }
        return paging;
    }

    /**
     * Pre method.
     *
     * @param doClass    the do class
     * @param resultMaps the result maps
     * @param doMapper   the do mapper
     * @param operation  the operation
     * @param columnMap  the column map
     */
    private void preMethod(Do doClass, Map<String, ResultMap> resultMaps, DoMapper doMapper,
                           CfOperation operation, Map<String, String> columnMap) {
        DoMapperMethod method = new DoMapperMethod();
        method.setName(operation.getId());
        method.setDesc(operation.getRemark());
        method.setSql(operation.getSqlDesc());
        preMethodParam(doClass, doMapper, operation, method, columnMap);
        String resultType = operationResultType(doClass, doMapper, operation, resultMaps);
        method.setReturnClass(resultType);
        doMapper.addMethod(method);
    }

    /**
     * Pre method param.
     *
     * @param doClass   the do class
     * @param doMapper  the do mapper
     * @param operation the operation
     * @param method    the method
     * @param columnMap the column map
     */
    private void preMethodParam(Do doClass, Base doMapper, CfOperation operation,
                                DoMapperMethod method, Map<String, String> columnMap) {
        boolean hasVo = StringUtil.isNotEmpty(operation.getVo());
        if (hasVo) {
            operation.setParamType(ParamTypeEnum.object);
        }

        if (operation.getParamType() == ParamTypeEnum.object) {
            String vo, voName = null;
            if (hasVo) {
                // get vo package
                vo = operation.getVo();
                int index = vo.lastIndexOf('.');
                if (index < 0) {
                    VoConfig voConfig = ConfigUtil.config.getVoConfig();
                    String namespace = voConfig.getNamespace();
                    if (StringUtil.isEmpty(namespace)) {
                        namespace = "vo";
                    }
                    String prefix = voConfig.getPrefix()
                        , suffix = voConfig.getSuffix();
                    if (prefix == null) prefix = "";
                    if (suffix == null) suffix = "Vo";

                    voName = StringUtil.lowerFirst(vo);
                    vo = ConfigUtil.getCurrentDb().getGenPackage() + "." + namespace + '.' + prefix + vo + suffix;
                } else {
                    voName = vo.substring(index + 1);
                }
            } else {
                vo = doClass.getPackageName() + '.' + doClass.getClassName();
            }
            method.addParam(new DoMapperMethodParam(getClassAndImport(doMapper, vo), voName != null ? voName : "entity"));
        } else if (operation.getParamType() == ParamTypeEnum.multiple) {
            method.addParam(new DoMapperMethodParam(getClassAndImport(doMapper,
                "java.util.List"), "entities"));
        } else {
            method.setParams(preMethodParams(doMapper, operation, columnMap));
        }
    }

    /**
     * Pre method params list.
     *
     * @param doMapper  the do mapper
     * @param operation the operation
     * @param columnMap the column map
     * @return the list
     */
    private List<DoMapperMethodParam> preMethodParams(Base doMapper, CfOperation operation,
                                                      Map<String, String> columnMap) {
        List<DoMapperMethodParam> params = Lists.newArrayList();
        Map<String, String> foreachParams = operation.getPrimitiveForeachParams();
        java.util.Collection<String> foreachValues = foreachParams.values();
        for (Map.Entry pm : operation.getPrimitiveParams().entrySet()) {
            String pmName = (String) pm.getKey();
            String pmType = (String) pm.getValue();
            // 如果是DO中的属性 则不需要在处理
            String columnType = columnMap.get(pmName);

            TypeMapEnum typeMapEnum = TypeMapEnum.getByJdbcTypeWithOther(pmType);

            if (StringUtils.isBlank(columnType)) {
                columnType = typeMapEnum == TypeMapEnum.OTHER ? pmType : typeMapEnum.getJavaType();

            }
            String custJavaType = ConfigUtil.config.getTypeMap().get(columnType);

            String paramType = getClassAndImport(doMapper, custJavaType == null ? columnType : custJavaType);

            String foreachName = foreachParams.get(pmName);
            boolean isNotForeach = StringUtils.isBlank(foreachName);
            if (isNotForeach && !foreachValues.contains(pmName)) {
                params.add(new DoMapperMethodParam(paramType, pmName));
            } else if (!isNotForeach) {
                getClassAndImport(doMapper, "java.util.List");
                params.add(new DoMapperMethodParam("List<" + paramType + ">", foreachName));
            }
        }
        // 仅有一个参数且类型为数组时需要添加注解
        if (params.size() == 1) {
            String paramType = params.get(0).getParamType();
            Pattern pattern = Pattern.compile("^List|$\\[\\]$");
            params.get(0).setpAnnot(pattern.matcher(paramType).find());
        }
        return params;
    }

    /**
     * Operation result type string.
     *
     * @param doClass    the do class
     * @param base       the do mapper
     * @param operation  the operation
     * @param resultMaps the result maps
     * @return the string
     */
    private String operationResultType(Do doClass, Base base, CfOperation operation,
                                       Map<String, ResultMap> resultMaps) {

        if (operation.getOperation() == OperationMethod.insert
            || operation.getOperation() == OperationMethod.update
            || operation.getOperation() == OperationMethod.delete) {
            return "int";
        }
        String resultType;
        if (!StringUtils.isBlank(operation.getResultType())) {
            resultType = getClassAndImport(base, operation.getResultType());
        } else if (!StringUtils.isBlank(operation.getResultMap())) {
            if (!"BaseResultMap".equals(operation.getResultMap())) {
                ResultMap resultMap = resultMaps.get(operation.getResultMap());
                Validate.notNull(resultMap, "BreeLoader.operationResultType 自定义ResultMap出错 table = "
                    + doClass.getTableName() + " DO=" + doClass);
                resultType = getClassAndImport(base,
                    resultMap.getPackageName() + "." + resultMap.getClassName());
            } else {
                resultType = getClassAndImport(base, doClass.getPackageName() + "." + doClass.getClassName());
            }
        } else {
            resultType = getClassAndImport(base,
                doClass.getPackageName() + "." + doClass.getClassName());
        }

        if (MultiplicityEnum.many == operation.getMultiplicity()) {
            base.addImport("java.util.List");
            return MessageFormat.format(RESULT_MANY, resultType);
        }
        return resultType;
    }

    /**
     * Pre do do.
     *
     * @param table     the table
     * @param cfColumns the cf columns
     * @return the do
     */
    private Do preDo(Table table, List<Column> cfColumns) {
        Do doClass = new Do();
        // do config
        DataObjectConfig doConfig = ConfigUtil.config.getDoConfig();
        String className = table.getJavaName();
        String s;
        if (StringUtil.isNotEmpty(s = doConfig.getPrefix())) {
            className = s + className;
        }
        if (StringUtil.isNotEmpty(s = doConfig.getSuffix())) {
            className = className + s;
        }
        doClass.setClassName(className);
        String namespace = doConfig.getNamespace();
        // default package name is "do"
        if (StringUtil.isEmpty(namespace)) {
            namespace = "do";
        }
        doClass.setPackageName(ConfigUtil.getCurrentDb().getGenPackage() + "." + namespace);
        doClass.setClassPath(ConfigUtil.getCurrentDb().getGenPackagePath() + "/" + namespace);
        doClass.setDesc(table.getRemark());
        doClass.setTableName(table.getName());

        // 不在DO中输出地字段
        Set<String> ignoreField = doConfig.getIgnoreFields();
        for (Column cfColumn : cfColumns) {
            if (!StringUtils.isBlank(cfColumn.getRelatedColumn())) {
                ignoreField.add(cfColumn.getRelatedColumn());
            }
        }

        for (Column column : table.getColumnList()) {
            // 提出不需要在DO中出现的字段
            if (ignoreField.contains(column.getColumn())
                || ignoreField.contains(column.getProperty())) continue;
            Field field = new Field();
            field.setName(column.getProperty());
            field.setDesc(column.getRemark());
            field.setJavaType(getClassAndImport(doClass, column.getJavaType()));
            doClass.addField(field);
        }

        // add class config
        addJavaConfig(doClass, doConfig, null);

        // TODO collection
        return doClass;
    }

    /**
     * Gets class and import.
     *
     * @param base      the base
     * @param classType the class type
     * @return the class and import
     */
    private String getClassAndImport(Base base, String classType) {
        // TODO 类型不指定可以被允许
//        Validate.notEmpty(classType,
//                "BreeLoader.getClassAndImport error classType 不能为 null Base=" + base);
        if (StringUtils.isEmpty(classType)) {
            return TypeMapEnum.OTHER.getJavaType();
        }
        int lastIdx = StringUtils.lastIndexOf(classType, ".");
        if (lastIdx > 0) {
            base.addImport(classType);
        }
        // 返回方法
        return StringUtils.substring(classType, lastIdx + 1);
    }

    /**
     * Append the java config
     *
     * @param base the {@link Base}
     * @param config the {@link JavaConfig}
     */
    private void addJavaConfig(Base base, JavaConfig config, MultiplicityEnum multiplicity) {
        // The extend
        JavaProperty extend = config.getExtend();
        if (extend != null && (multiplicity == null || extend.testCondition(multiplicity.name()))) {
            extend.setClassName(replaceValue(extend.getClassName(), base));
            base.addImport(extend.getImportPath());
            base.setExtend(config.getExtend());
        }

        // The implement array
        for (JavaProperty jp : config.getImplementArray()) {
            base.addImport(jp.getImportPath());
            jp.setClassName(replaceValue(jp.getImportPath(), base));
        }
        base.setImplementArray(config.getImplementArray());

        // The annotation array
        for (JavaProperty jp : config.getAnnotationArray()) {
            base.addImport(jp.getImportPath());
            jp.setClassName(replaceValue(jp.getClassName(), base));
        }
        base.setAnnotationArray(config.getAnnotationArray());
    }


    private Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
    private String replaceValue(String msg, Base base) {
        if (StringUtil.isEmpty(msg)) return null;
        Matcher matcher = pattern.matcher(msg);
        StringBuffer buffer = new StringBuffer();
        while (matcher.find()) {
            matcher.appendReplacement(buffer, value(matcher.group(1), base));
        }
        matcher.appendTail(buffer);
        return buffer.toString();
    }

    private String value(String key, Base base) {
        String value;
        int n;
        if ((n = key.lastIndexOf('.')) > 0) {
            key = key.substring(n + 1);
        }
        switch (key) {
            case "classPath"   : value = base.getClassPath();   break;
            case "className"   : value = base.getClassName();   break;
            case "tableName"   : value = base.getTableName();   break;
            case "packageName" : value = base.getPackageName(); break;
            case "desc"        : value = base.getDesc();        break;
            default            : value = key;
        }
        return value;
    }

    /**
     * Returns the param description
     *
     * @param param the param name
     * @param foreachMap the foreach params
     * @return the field description
     */
    private String getFieldDesc(DoMapperMethodParam param, Map<String, String> columnDescMap, Map<String, String> foreachMap) {
        String desc = columnDescMap.get(param.getParam());
        if (foreachMap != null && StringUtil.isEmpty(desc)) {
            desc = columnDescMap.get(foreachMap.get(param.getParam()));
        }
        return desc;
    }
}
