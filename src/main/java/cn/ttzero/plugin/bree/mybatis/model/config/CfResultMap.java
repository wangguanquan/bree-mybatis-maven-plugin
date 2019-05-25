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

package cn.ttzero.plugin.bree.mybatis.model.config;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by guanquan.wang at 2019-05-24 14:16
 */
public class CfResultMap {
    /**
     * The Name.
     */
    private String name;
    /**
     * The Type. A fully qualified Java class name, or a type alias (see the
     * table above for the list of built-in type aliases).
     */
    private String type;

    /**
     * The Remark.
     */
    private String remark;
    /**
     * The Columns.
     */
    private List<CfColumn> columns = Lists.newArrayList();
    /**
     * The Collection.
     */
    private List<CfCollection> collections = Lists.newArrayList();
    /**
     * The Association.
     */
    private List<CfAssociation> associations = Lists.newArrayList();

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets columns.
     *
     * @return the columns
     */
    public List<CfColumn> getColumns() {
        return columns;
    }

    /**
     * Add column.
     *
     * @param column the column
     */
    public void addColumn(CfColumn column) {
        this.columns.add(column);
    }

    /**
     * Gets remark.
     *
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * Sets remark.
     *
     * @param remark the remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
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

}
