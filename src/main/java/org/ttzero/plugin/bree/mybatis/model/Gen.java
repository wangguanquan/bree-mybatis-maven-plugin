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

package org.ttzero.plugin.bree.mybatis.model;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Sets;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.collect.Lists;
import org.ttzero.plugin.bree.mybatis.BreeException;
import org.ttzero.plugin.bree.mybatis.model.dbtable.Table;
import org.ttzero.plugin.bree.mybatis.model.java.Dao;
import org.ttzero.plugin.bree.mybatis.model.java.Do;
import org.ttzero.plugin.bree.mybatis.model.java.DoMapper;
import org.ttzero.plugin.bree.mybatis.model.java.Field;
import org.ttzero.plugin.bree.mybatis.model.java.ResultMap;
import org.ttzero.plugin.bree.mybatis.model.java.Vo;
import org.ttzero.plugin.bree.mybatis.model.java.XmlMapper;
import org.ttzero.plugin.bree.mybatis.model.repository.JavaProperty;

/**
 * Created by guanquan.wang at 2019-05-24 17:25
 */
public class Gen {
    /**
     * table放在哪个目录中,默认是${dbName}Tables The Tables path.
     */
    private String tablesPath;

    /**
     * The Bree root.
     */
    private String breeRoot;

    /**
     * The Out root.
     */
    private String outRoot;

    /**
     * The Database name.
     */
    private String dataBaseName;

    /**
     * The Db type.
     */
    private String dbType;

    /**
     * The author
     */
    private String author;

    /**
     * The Tables.
     */
    private List<Table> tables = Lists.newArrayList();

    /**
     * The Result maps.
     */
    private List<ResultMap> resultMaps = Lists.newArrayList();

    /**
     * The Dos.
     */
    private List<Do> dos = Lists.newArrayList();

    /**
     * The Daos.
     */
    private List<Dao> daos = Lists.newArrayList();

    /**
     * The Do mappers.
     */
    private List<DoMapper> doMappers = Lists.newArrayList();

    /**
     * The Xml mappers.
     */
    private List<XmlMapper> xmlMappers = Lists.newArrayList();

    /**
     * The vo list.
     */
    private List<Vo> voList = Lists.newArrayList();

    private Set<String> existVoPath = Sets.newHashSet();

    /**
     * Paging base vo
     */
    private boolean useBasePage;

    /**
     * Returns tables path.
     *
     * @return the tables path
     */
    public String getTablesPath() {
        return tablesPath;
    }

    /**
     * Setting tables path.
     *
     * @param tablesPath the tables path
     */
    public void setTablesPath(String tablesPath) {
        this.tablesPath = tablesPath;
    }

    /**
     * Returns bree root.
     *
     * @return the bree root
     */
    public String getBreeRoot() {
        return breeRoot;
    }

    /**
     * Setting bree root.
     *
     * @param breeRoot the bree root
     */
    public void setBreeRoot(String breeRoot) {
        this.breeRoot = breeRoot;
    }

    /**
     * Returns data base name.
     *
     * @return the data base name
     */
    public String getDataBaseName() {
        return dataBaseName;
    }

    /**
     * Setting data base name.
     *
     * @param dataBaseName the data base name
     */
    public void setDataBaseName(String dataBaseName) {
        this.dataBaseName = dataBaseName;
    }

    /**
     * Returns tables.
     *
     * @return the tables
     */
    public List<Table> getTables() {
        return tables;
    }

    /**
     * Add table.
     *
     * @param table the table
     */
    public void addTable(Table table) {
        this.tables.add(table);
    }

    /**
     * Returns db type.
     *
     * @return the db type
     */
    public String getDbType() {
        return dbType;
    }

    /**
     * Setting db type.
     *
     * @param dbType the db type
     */
    public void setDbType(String dbType) {
        this.dbType = dbType;
    }

    /**
     * Returns dos.
     *
     * @return the dos
     */
    public List<Do> getDos() {
        return dos;
    }

    /**
     * Add do.
     *
     * @param doClass the do class
     */
    public void addDo(Do doClass) {
        this.dos.add(doClass);
    }

    /**
     * Returns daos.
     *
     * @return the daos
     */
    public List<Dao> getDaos() {
        return daos;
    }

    /**
     * Add dao.
     *
     * @param dao the dao
     */
    public void addDao(Dao dao) {
        this.daos.add(dao);
    }

    /**
     * Returns out root.
     *
     * @return the out root
     */
    public String getOutRoot() {
        return outRoot;
    }

    /**
     * Setting out root.
     *
     * @param outRoot the out root
     */
    public void setOutRoot(String outRoot) {
        this.outRoot = outRoot;
    }

    /**
     * To string string.
     *
     * @return the string
     */
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    /**
     * Returns do mappers.
     *
     * @return the do mappers
     */
    public List<DoMapper> getDoMappers() {
        return doMappers;
    }

    /**
     * Add do mapper.
     *
     * @param doMapper the do mapper
     */
    public void addDoMapper(DoMapper doMapper) {
        this.doMappers.add(doMapper);
    }

    /**
     * Returns xml mappers.
     *
     * @return the xml mappers
     */
    public List<XmlMapper> getXmlMappers() {
        return xmlMappers;
    }

    /**
     * Add xml mapper.
     *
     * @param xmlMapper the xml mapper
     */
    public void addXmlMapper(XmlMapper xmlMapper) {
        this.xmlMappers.add(xmlMapper);
    }

    /**
     * Returns result maps.
     *
     * @return the result maps
     */
    public List<ResultMap> getResultMaps() {
        return resultMaps;
    }

    /**
     * Add result map.
     *
     * @param resultMap the result map
     */
    public void addResultMap(ResultMap resultMap) {
        this.resultMaps.add(resultMap);
    }

    /**
     * Returns vo list.
     *
     * @return the vos
     */
    public List<Vo> getVoList() {
        return voList;
    }

    /**
     * Add vo.
     *
     * @param vo the {@link Vo}
     */
    public void addVo(Vo vo) {
        String fullPath = vo.getBaseClassPath() + vo.getClassName();
        if (existVoPath.contains(fullPath)) {
            for (Vo p : voList) {
                if (fullPath.equals(p.getBaseClassPath() + p.getClassName())) {
                    // merge fields
                    List<Field> list = vo.getFieldList();
                    for (Field e : list) {
                        p.addField(e);
                    }

                    // Merge implements, extends, annotations and fields
                    if (p.getExtend() == null) {
                        p.setExtend(vo.getExtend());
                    } else if (vo.getExtend() != null && !p.getExtend().equals(vo.getExtend())) {
                        throw new BreeException("Vo [" + vo.getClassName() + "] has diff extend ["
                            + p.getExtend().getClassName() + " and " + vo.getExtend().getClassName() + "]");
                    }
                    if (p.getExtend() != null) {
                        p.addImport(p.getExtend().getImportPath());
                    }

                    List<JavaProperty> implementArray = merge(p.getImplementArray(), vo.getImplementArray());
                    p.setImplementArray(implementArray);
                    for (JavaProperty jp : implementArray) {
                        p.addImport(jp.getImportPath());
                    }

                    List<JavaProperty> annotationArray = merge(p.getAnnotationArray(), vo.getAnnotationArray());
                    p.setAnnotationArray(annotationArray);
                    for (JavaProperty jp : annotationArray) {
                        p.addImport(jp.getImportPath());
                    }
                }
            }
            return;
        }
        this.voList.add(vo);
        existVoPath.add(fullPath);
    }

    private List<JavaProperty> merge(List<JavaProperty> l1, List<JavaProperty> l2) {
        if (l1 == null) {
            return l2;
        } else if (l2 != null) {
            for (JavaProperty jp : l2) {
                if (!l1.contains(jp)) {
                    l1.add(jp);
                }
            }
        }
        return l1;
    }

    public boolean isUseBasePage() {
        return useBasePage;
    }

    public void setUseBasePage(boolean useBasePage) {
        this.useBasePage = useBasePage;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }
}
