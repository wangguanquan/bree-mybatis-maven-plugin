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

import org.ttzero.plugin.bree.mybatis.model.dbtable.Column;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by guanquan.wang at 2019-05-24 14:16
 */
public class CfResultMap {
    /**
     * The id of result map.
     */
    private String id;
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
    private List<Column> columns = Lists.newArrayList();
    /**
     * The Collection.
     */
    private List<CfCollection> collections = Lists.newArrayList();
    /**
     * The Association.
     */
    private List<CfAssociation> associations = Lists.newArrayList();

    /**
     * Returns the result id.
     *
     * @return the resultMap's id
     */
    public String getId() {
        return id;
    }

    /**
     * Setting the id of result map.
     *
     * @param id the id of result map
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Setting type.
     *
     * @param type the type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Returns columns.
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
     * Returns remark.
     *
     * @return the remark
     */
    public String getRemark() {
        return remark;
    }

    /**
     * Setting remark.
     *
     * @param remark the remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * Returns collections.
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
