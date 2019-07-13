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

import org.ttzero.plugin.bree.mybatis.model.java.domapper.Sql;
import com.google.common.collect.Lists;
import org.ttzero.plugin.bree.mybatis.model.dbtable.Column;

import java.util.List;

/**
 * Created by guanquan.wang at 2019-05-24 21:50
 */
public class CfTable {
    /**
     * The table name.
     */
    private String name;
    /**
     * The Sequence.
     */
    private String sequence;
    /**
     * The Physical name. 物理表名,分库分表使用
     */
    private String physicalName;
    /**
     * The Remark.
     */
    private String remark;

    /**
     * The Columns.
     */
    private List<Column> columns = Lists.newArrayList();
    /**
     * The Result maps.
     */
    private List<CfResultMap> resultMaps = Lists.newArrayList();

    /**
     * The Operations.
     */
    private List<CfOperation> operations = Lists.newArrayList();

    /**
     * The Db columns.
     */
    private List<Column> dbColumns = Lists.newArrayList();

    /**
     * The sql tag
     */
    private List<Sql> sqlList = Lists.newArrayList();

    /**
     * Returns table name.
     *
     * @return the table name
     */
    public String getName() {
        return name;
    }

    /**
     * Returns sequence.
     *
     * @return the sequence
     */
    public String getSequence() {
        return sequence;
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
     * Returns remarks.
     *
     * @return the remarks
     */
    public String getRemark() {
        return remark;
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
     * Setting sequence.
     *
     * @param sequence the sequence
     */
    public void setSequence(String sequence) {
        this.sequence = sequence;
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
     * Setting remark.
     *
     * @param remark the remark
     */
    public void setRemark(String remark) {
        this.remark = remark;
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
     * Returns result maps.
     *
     * @return the result maps
     */
    public List<CfResultMap> getResultMaps() {
        return resultMaps;
    }

    /**
     * Returns operations.
     *
     * @return the operations
     */
    public List<CfOperation> getOperations() {
        return operations;
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
     * Add result map.
     *
     * @param resultMap the result map
     */
    public void addResultMap(CfResultMap resultMap) {
        this.resultMaps.add(resultMap);
    }

    /**
     * Add operation.
     *
     * @param operation the operation
     */
    public void addOperation(CfOperation operation) {
        this.operations.add(operation);
    }

    /**
     * Returns db columns.
     *
     * @return the db columns
     */
    public List<Column> getDbColumns() {
        return dbColumns;
    }

    /**
     * Setting db columns.
     *
     * @param dbColumns the db columns
     */
    public void setDbColumns(List<Column> dbColumns) {
        this.dbColumns = dbColumns;
    }

    /**
     * Append sql tag
     *
     * @param id the sql id
     * @param content the sql content
     * @param remark the sql doc
     */
    public void addSqlTag(String id, String content, String remark) {
        sqlList.add(new Sql(id, content, remark));
    }

    /**
     * Append sql tag
     *
     * @param sql the {@link Sql}
     */
    public void addSqlTag(Sql sql) {
        sqlList.add(sql);
    }

    /**
     * Returns the sql tags
     *
     * @return the sql tags
     */
    public List<Sql> getSqlList() {
        return sqlList;
    }
}
