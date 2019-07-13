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

/**
 * Created by guanquan.wang at 2019-05-24 21:50
 */
public class Column implements Comparable<Column> {
    /**
     * The column name.
     */
    private String column;

    /**
     * The java field
     */
    private String property;

    /**
     * The jdbc type.
     */
    private String jdbcType;

    /**
     * The Java type.
     */
    private String javaType;

    /**
     * The Remark.
     */
    private String remark;

    /**
     * The Related column.
     */
    private String relatedColumn;

    /**
     * The Default value.
     */
    private String defaultValue;

    /**
     * The flag mark the column is primary key
     */
    private boolean primaryKey;

    /**
     * The mybatis result type handler
     */
    private String typeHandler;

    /**
     * Mark the column name is a reserved word
     */
    private boolean reserved;

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
     * Returns jdbc type.
     *
     * @return the sql type
     */
    public String getJdbcType() {
        return jdbcType;
    }

    /**
     * Setting jdbc type.
     *
     * @param jdbcType the jdbc type
     */
    public void setJdbcType(String jdbcType) {
        this.jdbcType = jdbcType;
    }

    /**
     * Returns java type.
     *
     * @return the java type
     */
    public String getJavaType() {
        return javaType;
    }

    /**
     * Setting java type.
     *
     * @param javaType the java type
     */
    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    /**
     * Returns the column.
     *
     * @return the column name
     */
    public String getColumn() {
        return column;
    }

    /**
     * Setting column name.
     *
     * @param column the column name
     */
    public void setColumn(String column) {
        this.column = column;
    }

    /**
     * Returns the field property
     *
     * @return the field property
     */
    public String getProperty() {
        return property;
    }

    /**
     * Setting the field property
     *
     * @param property the field property
     */
    public void setProperty(String property) {
        this.property = property;
    }


    /**
     * Returns related column.
     *
     * @return the related column
     */
    public String getRelatedColumn() {
        return relatedColumn;
    }

    /**
     * Setting related column.
     *
     * @param relatedColumn the related column
     */
    public void setRelatedColumn(String relatedColumn) {
        this.relatedColumn = relatedColumn;
    }

    /**
     * Returns default value.
     *
     * @return the default value
     */
    public String getDefaultValue() {
        return defaultValue;
    }

    /**
     * Setting default value.
     *
     * @param defaultValue the default value
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Returns current column is primary key
     *
     * @return true if primary key
     */
    public boolean isPrimaryKey() {
        return primaryKey;
    }

    /**
     * Setting current column is primary key
     *
     * @param primaryKey is primary key
     */
    public void setPrimaryKey(boolean primaryKey) {
        this.primaryKey = primaryKey;
    }

    /**
     * Returns the type handler
     *
     * @return the type handler referer
     */
    public String getTypeHandler() {
        return typeHandler;
    }

    /**
     * Setting the result type handler
     *
     * @param typeHandler result type handler
     */
    public void setTypeHandler(String typeHandler) {
        this.typeHandler = typeHandler;
    }

    /**
     * Returns the column name is reserved
     *
     * @return the reserved value
     */
    public boolean isReserved() {
        return reserved;
    }

    /**
     * Setting the column name is reserved
     *
     * @param reserved the reserved value
     */
    public void setReserved(boolean reserved) {
        this.reserved = reserved;
    }

    /**
     * Compare to int.
     *
     * @param other the other Column
     * @return the int
     */
    @Override
    public int compareTo(Column other) {
        if (this == other) {
            return 0;
        }
        // Null last
        if (other == null) return  1;
        if (column.length() == other.column.length()) {
            return column.compareTo(other.column);
        }
        return column.length() - other.column.length();
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Column && ((Column) obj).column.equalsIgnoreCase(column);
    }

    @Override
    public int hashCode() {
        return column.hashCode();
    }
}
