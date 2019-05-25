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

/**
 * Created by guanquan.wang at 2019-05-24 14:16
 */
public class CfColumn {
    /**
     * The Name.
     */
    private String name;
    /**
     * The JavaType.
     */
    private String javaType;

    /**
     * The Sql type.
     */
    private String sqlType;

    /**
     * The Remark.
     */
    private String remark;
    /**
     * The Related column.
     */
    private String relatedColumn;
    /**
     * The key
     */
    private String key;

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
     * Gets javaType.
     *
     * @return the javaType
     */
    public String getJavaType() {
        return javaType;
    }

    /**
     * Sets javaType.
     *
     * @param javaType the javaType
     */
    public void setJavaType(String javaType) {
        this.javaType = javaType;
    }

    /**
     * Gets related column.
     *
     * @return the related column
     */
    public String getRelatedColumn() {
        return relatedColumn;
    }

    /**
     * Sets related column.
     *
     * @param relatedColumn the related column
     */
    public void setRelatedColumn(String relatedColumn) {
        this.relatedColumn = relatedColumn;
    }

    /**
     * Gets sql type.
     *
     * @return the sql type
     */
    public String getSqlType() {
        return sqlType;
    }

    /**
     * Sets sql type.
     *
     * @param sqlType the sql type
     */
    public void setSqlType(String sqlType) {
        this.sqlType = sqlType;
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

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
