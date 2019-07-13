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

package org.ttzero.plugin.bree.mybatis.model.config;

import com.google.common.collect.Lists;
import org.ttzero.plugin.bree.mybatis.model.dbtable.Column;

import java.util.List;

/**
 * Created by guanquan.wang at 2018-04-03 15:27
 */
public class CfAssociation {
    /**
     * 映射到列结果的字段或属性
     */
    private String property;
    /**
     * 集合类具体对象
     */
    private String javaType;
    /**
     * The Column
     */
    private String column;
    /**
     * The Select
     */
    private String select;
    /**
     * 集合
     */
    private String resultMap;
    /**
     * remark
     */
    private String remark;
    /**
     * The Columns.
     */
    private List<Column> columns = Lists.newArrayList();

    /**
     * The Association.
     */
    private List<CfAssociation> associations = Lists.newArrayList();
    /**
     * The Collection.
     */
    private List<CfCollection> collections = Lists.newArrayList();

    public String getProperty() {
        return property;
    }

    public void setProperty(String property) {
        this.property = property;
    }

    public String getJavaType() {
        return javaType;
    }

    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    public String getColumn() {
        return column;
    }

    public void setColumn(String column) {
        this.column = column;
    }

    public String getSelect() {
        return select;
    }

    public void setSelect(String select) {
        this.select = select;
    }

    public String getResultMap() {
        return resultMap;
    }

    public void setResultMap(String resultMap) {
        this.resultMap = resultMap;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * Gets columns.
     *
     * @return the columns
     */
    public List<Column> getColumns() {
        return columns;
    }

    /**
     * Add column.
     *
     * @param column the column
     */
    public void addColumn(Column column) {
        this.columns.add(column);
    }
    /**
     * Add association.
     *
     * @return the associations
     */
    public List<CfAssociation> getAssociations() {
        return associations;
    }
    /**
     * Add association.
     *
     * @param association the association
     */
    public void addAssociation(CfAssociation association) {
        this.associations.add(association);
    }
    /**
     * Gets collections.
     *
     * @return the collections
     */
    public List<CfCollection> getCollections() {
        return collections;
    }
    /**
     * Add collection.
     *
     * @param collection the collection
     */
    public void addCollection(CfCollection collection) {
        this.collections.add(collection);
    }
}
