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

package cn.ttzero.plugin.bree.mybatis.dataloaders;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.ttzero.plugin.bree.mybatis.common.FileNameSelector;
import cn.ttzero.plugin.bree.mybatis.exception.BreeException;
import cn.ttzero.plugin.bree.mybatis.model.Gen;
import cn.ttzero.plugin.bree.mybatis.model.config.*;
import cn.ttzero.plugin.bree.mybatis.model.dbtable.Table;
import cn.ttzero.plugin.bree.mybatis.model.java.*;
import cn.ttzero.plugin.bree.mybatis.model.repository.*;
import cn.ttzero.plugin.bree.mybatis.utils.CamelCaseUtils;
import cn.ttzero.plugin.bree.mybatis.utils.ConfigUtil;
import cn.ttzero.plugin.bree.mybatis.utils.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import cn.ttzero.plugin.bree.mybatis.enums.MultiplicityEnum;
import cn.ttzero.plugin.bree.mybatis.enums.ParamTypeEnum;
import cn.ttzero.plugin.bree.mybatis.enums.TypeMapEnum;
import cn.ttzero.plugin.bree.mybatis.model.dbtable.Column;
import cn.ttzero.plugin.bree.mybatis.model.java.domapper.DOMapperMethod;
import cn.ttzero.plugin.bree.mybatis.model.java.domapper.DOMapperMethodParam;
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
        File[] files = tablesFile.listFiles(new FileNameSelector("xml"));
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
            tableMap.put(StringUtils.upperCase(tbName),
                tableRepository.gainTable(connection, tbName, cfTableMap.get(tbName)));
        }

        // 根据需要重新生成的表 准备数据
        for (String tbName : needGenTableNames) {
            CfTable cfTable = cfTableMap.get(tbName);
            Table table = tableMap.get(tbName);
            // 准备DO
            DO doClass = preDo(table, cfTable.getColumns());
            gen.addDO(doClass);

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
            DOMapper doMapper = preDOMapper(gen, cfTable, table, doClass, resultMaps);
            gen.addDOMapper(doMapper);

            // TODO

            // 准备DAO类
            if (!ConfigUtil.config.isIgnoreDao()) {
                DAO dao = preDAO(gen, cfTable, table, doClass, resultMaps);
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
            if (StringUtil.isEmpty(xmlMapperPath)) {
                xmlMapperPath = doMapper.getClassPath();
            }
            xmlMapper.setClassPath(xmlMapperPath);

            gen.addXmlMapper(xmlMapper);

            // TODO Service
            // TODO Controller

            // Test
            gen.setUseBasePage(ConfigUtil.config.getVoConfig().isUseBasePageVo());

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
        if (StringUtils.equals(StringUtils.trim(cmd), "*")) {
            needGenTableNames = Lists.newArrayList(cfTableMap.keySet());
        } else {
            for (String tableName : Lists
                .newArrayList(StringUtils.split(StringUtils.upperCase(cmd)))) {
                boolean flag = true;
                for (String splitTableSuffix : ConfigUtil.getCurrentDb().getSplitSuffixs()) {
                    if (StringUtils.endsWithIgnoreCase(tableName, splitTableSuffix)) {
                        needGenTableNames.add(StringUtils.replace(tableName, splitTableSuffix, ""));
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
        VoConfig voConfig = ConfigUtil.config.getVoConfig();
        String namespace;
        if (StringUtil.isEmpty(namespace = voConfig.getNamespace())) {
            namespace = "vo";
        }
        String prefix = voConfig.getPrefix()
            , suffix = voConfig.getSuffix();
        if (prefix == null) prefix = "";
        if (suffix == null) suffix = "";
        for (CfResultMap cfResultMap : resultMapList) {
            ResultMap resultMap = new ResultMap();
            resultMap.setTableName(table.getSqlName());
            resultMap.setName(cfResultMap.getName());
            resultMap.setType(cfResultMap.getType());
            resultMap.setClassName(prefix + cfResultMap.getType() + suffix);
            resultMap.setClassPath(ConfigUtil.getCurrentDb().getGenPackagePath() + "/" + namespace);
            resultMap.setPackageName(ConfigUtil.getCurrentDb().getGenPackage() + "." + namespace);
            resultMap.setDesc(cfResultMap.getRemark());
            doCfColumn(gen, tbName, resultMap, cfResultMap.getColumns(), null);

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
            resultMaps.put(cfResultMap.getName(), resultMap);
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
        return new String(bytes, 49, bytes.length - 58)
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
        resultMap.setName(coll.getProperty());
        resultMap.setType(coll.getOfType());
        resultMap.setClassName(prefix + coll.getOfType() + suffix);
        resultMap.setClassPath(ConfigUtil.getCurrentDb().getGenPackagePath() + "/" + namespace);
        resultMap.setPackageName(ConfigUtil.getCurrentDb().getGenPackage() + "." + namespace);
        resultMap.setDesc(coll.getRemark());
        doCfColumn(gen, tbName, resultMap, coll.getColumns(), subEle);
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
        resultMap.setName(assoc.getProperty());
        resultMap.setType(assoc.getJavaType());
        resultMap.setClassName(prefix + assoc.getJavaType() + suffix);
        resultMap.setClassPath(ConfigUtil.getCurrentDb().getGenPackagePath() + "/" + namespace);
        resultMap.setPackageName(ConfigUtil.getCurrentDb().getGenPackage() + "." + namespace);
        resultMap.setDesc(assoc.getRemark());
        doCfColumn(gen, tbName, resultMap, assoc.getColumns(), subEle);
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
    private void doCfColumn(Gen gen, String tbName, ResultMap resultMap, List<CfColumn> cfColumns, Element e) {
        for (CfColumn cfColumn : cfColumns) {
            Validate.notEmpty(cfColumn.getName(), tbName
                + ".xml 配置有误 BreeLoader.preResultMap Gen=" + gen);
            // 生成XML <resultMap> 标签使用
            Column column = new Column();
            column.setJavaName(CamelCaseUtils.toCamelCase(cfColumn.getName()));
            column.setJavaType(cfColumn.getJavaType());
            column.setSqlName(cfColumn.getName());
            column.setSqlType(cfColumn.getSqlType());
            column.setRemarks(cfColumn.getRemark());
            resultMap.addColumn(column);

            // 生成ResultMap.java文件使用
            Field field = new Field();
            field.setJavaType(getClassAndImport(resultMap, column.getJavaType()));
            field.setName(column.getJavaName());
            field.setDesc(column.getRemarks());

            resultMap.addField(field);

            if (e != null) {
                Element ele;
                if ("ID".equals(cfColumn.getName()) || StringUtils.isNotEmpty(cfColumn.getKey())) {
                    ele = e.addElement("id");
                } else {
                    ele = e.addElement("result");
                }
                ele.addAttribute("column", column.getSqlName());
                ele.addAttribute("property", column.getJavaName());
                ele.addAttribute("jdbcType", column.getSqlType());
                ele.addAttribute("javaType", column.getJavaType());
            }
        }
    }

    /**
     * Pre dao dao.
     *
     * @param gen        the gen
     * @param cfTable    the cf table
     * @param table      the table
     * @param doClass    the do class
     * @param resultMaps the result maps
     * @return the dao
     */
    private DAO preDAO(Gen gen, CfTable cfTable, Table table, DO doClass,
                       Map<String, ResultMap> resultMaps) {
        DAO dao = new DAO();
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
        dao.setTableName(cfTable.getSqlname());

        // append java config
        addJavaConfig(dao, javaConfig);

        Map<String, String> columnTypeMap = Maps.newHashMap();
//        Map<String, String> columnDescMap = Maps.newHashMap();
        for (Column column : table.getColumnList()) {
            columnTypeMap.put(column.getJavaName(), column.getJavaType());
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
    private void preDAOMethod(DO doClass, Map<String, ResultMap> resultMaps, DAO dao,
                              CfOperation operation, Map<String, String> columnMap) {

        DOMapperMethod method = new DOMapperMethod();
        method.setName(operation.getName());
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

            paging.setClassName(prefix + StringUtil.upperFirst(operation.getVo()) + suffix);
            paging.setPackageName(ConfigUtil.getCurrentDb().getGenPackage() + "." + namespace);
            resultType = getClassAndImport(dao,
                paging.getPackageName() + "." + paging.getClassName());
            DOMapperMethodParam param = new DOMapperMethodParam(resultType,
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
    private DOMapper preDOMapper(Gen gen, CfTable cfTable, Table table, DO doClass,
                                 Map<String, ResultMap> resultMaps) {
        DOMapper doMapper = new DOMapper();
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
        doMapper.setTableName(cfTable.getSqlname());

        // add java config
        addJavaConfig(doMapper, javaConfig);

        Map<String, String> columnTypeMap = Maps.newHashMap();
        Map<String, String> columnDescMap = Maps.newHashMap();
        for (Column column : table.getColumnList()) {
            String fieldName = column.getJavaName();
            columnTypeMap.put(fieldName, column.getJavaType());
            columnDescMap.put(fieldName, column.getRemarks());
        }

        for (CfOperation operation : cfTable.getOperations()) {
            if (operation.getMultiplicity() == MultiplicityEnum.paging) {//分页
                prePagingMethod(gen, cfTable, table, doClass, resultMaps, doMapper, columnTypeMap,
                    columnDescMap, operation);
            } else {
                preMethod(doClass, resultMaps, doMapper, operation, columnTypeMap);
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
    private void prePagingMethod(Gen gen, CfTable cfTable, Table table, DO doClass,
                                 Map<String, ResultMap> resultMaps, DOMapper doMapper,
                                 Map<String, String> columnTypeMap,
                                 Map<String, String> columnDescMap, CfOperation operation) {
        DOMapperMethod pagingResultMethod = new DOMapperMethod();
        VoConfig voConfig = ConfigUtil.config.getVoConfig();
        String namespace;
        if (StringUtil.isEmpty(namespace = voConfig.getNamespace())) {
            namespace = "vo";
        }
        String prefix = voConfig.getPrefix()
            , suffix = voConfig.getSuffix();
        if (prefix == null) prefix = "";
        if (suffix == null) suffix = "Vo";

        pagingResultMethod.setName(operation.getName() + "Result");
        pagingResultMethod.setPagingName(operation.getName());
        pagingResultMethod.setDesc(operation.getRemark());
        pagingResultMethod.setSql(operation.getSqlDesc());
        pagingResultMethod.setPagingFlag("true");

        Vo paging = new Vo();
        paging.setClassName(prefix + StringUtil.upperFirst(operation.getVo()) + suffix);
        paging.setPackageName(ConfigUtil.getCurrentDb().getGenPackage() + "." + namespace);
        paging.setClassPath(ConfigUtil.getCurrentDb().getGenPackagePath() + "/" + namespace);
        if (voConfig.isUseBasePageVo()) {
//            paging.setBasePackageName(ConfigUtil.getCurrentDb().getGenDalCommonPackage() + "." + paging);
//            paging.setBaseClassPath(ConfigUtil.getCurrentDb().getGenDalCommonPackagePath() + "/" + paging);
            getClassAndImport(paging, paging.getPackageName() + ".BasePage");
        }
        paging.setDesc(StringUtil.join(table.getSqlName(), cfTable.getRemark()));
        paging.setTableName(cfTable.getSqlname());

        String pagingResultType = operationResultType(doClass, paging, operation, resultMaps);

        // append java config
        addJavaConfig(paging, voConfig);

        paging.setResultType(pagingResultType);
        List<DOMapperMethodParam> params = preMethodParams(paging, operation, columnTypeMap);
        boolean hasForeach = !operation.getPrimitiveForeachParams().isEmpty();
        Map<String, String> foreachMap = null;
        if (hasForeach) {
            foreachMap = Maps.newHashMap();
            for (Map.Entry<String, String> entry : operation.getPrimitiveForeachParams().entrySet()) {
                foreachMap.put(entry.getValue(), entry.getKey());
            }
        }
        for (DOMapperMethodParam param : params) {
            Field field = new Field();
            field.setName(param.getParam());
            field.setJavaType(param.getParamType());
            field.setDesc(getFieldDesc(param, columnDescMap, foreachMap));
            paging.addField(field);
        }
        gen.addVo(paging);
        // paging import到doMapper
        getClassAndImport(doMapper, paging.getPackageName() + "." + paging.getClassName());
        getClassAndImport(doMapper, "java.util.List");

        // 方法返回结果
        DOMapperMethodParam pagingParam = new DOMapperMethodParam(paging.getClassName(),
            StringUtil.lowerFirst(operation.getVo()));
        pagingResultMethod.addParam(pagingParam);

        String resultType = operationResultType(doClass, doMapper, operation, resultMaps);

        paging.setResultType(resultType);
        pagingResultMethod.setReturnClass("List<" + resultType + ">");
        if (!operation.isNoCount()) {
            DOMapperMethod pagingCountMethod = pagingResultMethod.deepClone();
            pagingCountMethod.setName(operation.getName() + "Count");
            pagingCountMethod.setReturnClass("int");
            doMapper.addMethod(pagingCountMethod);
        } else {
            pagingResultMethod.makeNoCount();
        }

        doMapper.addMethod(pagingResultMethod);
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
    private void preMethod(DO doClass, Map<String, ResultMap> resultMaps, DOMapper doMapper,
                           CfOperation operation, Map<String, String> columnMap) {
        DOMapperMethod method = new DOMapperMethod();
        method.setName(operation.getName());
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
    private void preMethodParam(DO doClass, Base doMapper, CfOperation operation,
                                DOMapperMethod method, Map<String, String> columnMap) {

        if (operation.getParamType() == ParamTypeEnum.object) {
            method.addParam(new DOMapperMethodParam(getClassAndImport(doMapper,
                doClass.getPackageName() + "." + doClass.getClassName()), "entity"));
        } else if (operation.getParamType() == ParamTypeEnum.multiple) {
            method.addParam(new DOMapperMethodParam(getClassAndImport(doMapper,
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
    private List<DOMapperMethodParam> preMethodParams(Base doMapper, CfOperation operation,
                                                      Map<String, String> columnMap) {
        List<DOMapperMethodParam> params = Lists.newArrayList();
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
                params.add(new DOMapperMethodParam(paramType, pmName));
            } else if (!isNotForeach) {
                getClassAndImport(doMapper, "java.util.List");
                params.add(new DOMapperMethodParam("List<" + paramType + ">", foreachName));
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
    private String operationResultType(DO doClass, Base base, CfOperation operation,
                                       Map<String, ResultMap> resultMaps) {

        if (StringUtils.startsWithIgnoreCase(operation.getName(), "insert")
            || StringUtils.startsWithIgnoreCase(operation.getName(), "update")
            || StringUtils.startsWithIgnoreCase(operation.getName(), "delete")) {
            return "Long";
        }
        //返回类不为null
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

        //返回一行
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
    private DO preDo(Table table, List<CfColumn> cfColumns) {
        DO doClass = new DO();
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
        doClass.setTableName(table.getSqlName());

        // 不在DO中输出地字段
        Set<String> ignoreField = doConfig.getIgnoreFields();
        for (CfColumn cfColumn : cfColumns) {
            if (!StringUtils.isBlank(cfColumn.getRelatedColumn())) {
                ignoreField.add(cfColumn.getRelatedColumn());
            }
        }

        for (Column column : table.getColumnList()) {
            // 提出不需要在DO中出现的字段
            if (ignoreField.contains(column.getSqlName())
                || ignoreField.contains(column.getJavaName())) continue;
            Field field = new Field();
            field.setName(column.getJavaName());
            field.setDesc(column.getRemarks());
            field.setJavaType(getClassAndImport(doClass, column.getJavaType()));
            doClass.addField(field);
        }

        // add class config
        addJavaConfig(doClass, doConfig);

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
    private void addJavaConfig(Base base, JavaConfig config) {
        // The extend
        JavaProperty extend = config.getExtend();
        if (extend != null) {
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
    private String getFieldDesc(DOMapperMethodParam param, Map<String, String> columnDescMap, Map<String, String> foreachMap) {
        String desc = columnDescMap.get(param.getParam());
        if (foreachMap != null && StringUtil.isEmpty(desc)) {
            desc = columnDescMap.get(foreachMap.get(param.getParam()));
        }
        return desc;
    }
}
