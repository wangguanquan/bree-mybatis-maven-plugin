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

package org.ttzero.plugin.bree.mybatis.model.dbtable;

import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by guanquan.wang at 2019-05-24 14:18
 */
public class Table {
    /**
     * The table name.
     */
    private String name;

    /**
     * The Java name.
     */
    private String javaName;

    /**
     * The Remark.
     */
    private String remark;

    /**
     * The Column list.
     */
    private List<Column> columnList = Lists.newArrayList();

    /**
     * The Primary keys.
     */
    private PrimaryKeys primaryKeys;

    /**
     * The Physical name.
     */
    private String physicalName;
    /**
     * Create default operation
     */
    private boolean createDefaultOperation = true;

    /**
     * Returns primary keys.
     *
     * @return the primary keys
     */
    public PrimaryKeys getPrimaryKeys() {
        return primaryKeys;
    }

    /**
     * Setting primary keys.
     *
     * @param primaryKeys the primary keys
     */
    public void setPrimaryKeys(PrimaryKeys primaryKeys) {
        this.primaryKeys = primaryKeys;
    }

    /**
     * Returns table name.
     *
     * @return the table name
     */
    public String getName() {
        return name;
    }

    /**
     * Setting table name.
     *
     * @param name the table name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns java name.
     *
     * @return the java name
     */
    public String getJavaName() {
        return javaName;
    }

    /**
     * Setting java name.
     *
     * @param javaName the java name
     */
    public void setJavaName(String javaName) {
        this.javaName = javaName;
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
     * Returns column list.
     *
     * @return the column list
     */
    public List<Column> getColumnList() {
        return columnList;
//        return Lists.sortedCopy(columnList);
    }

    /**
     * Add column.
     *
     * @param column the column
     */
    public void addColumn(Column column) {
        this.columnList.add(column);
    }

    /**
     * Setting column list.
     *
     * @param columnList the column list
     */
    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }

    /**
     * Returns physical name.
     *
     * @return the physical name
     */
    public String getPhysicalName() {
        return physicalName;
    }

    /**
     * Setting physical name.
     *
     * @param physicalName the physical name
     */
    public void setPhysicalName(String physicalName) {
        this.physicalName = physicalName;
    }

    /**
     * Returns the createDefaultOperation
     *
     * @return true if create default operation
     */
    public boolean isCreateDefaultOperation() {
        return createDefaultOperation;
    }

    /**
     * Setting the createDefaultOperation
     *
     * @param createDefaultOperation a flag
     */
    public void setCreateDefaultOperation(boolean createDefaultOperation) {
        this.createDefaultOperation = createDefaultOperation;
    }
}
