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
 * Created by guanquan.wang at 2019-05-24 21:50
 */
public class PrimaryKeys {
    /**
     * The Column list.
     */
    private List<Column> columnList = Lists.newArrayList();
    /**
     * The Pk name.
     */
    private String pkName;

    /**
     * Add column.
     *
     * @param column the column
     */
    public void addColumn(Column column){
        this.columnList.add(column);
    }

    /**
     * Gets column list.
     *
     * @return the column list
     */
    public List<Column> getColumnList() {
        return columnList;
//        return Lists.sortedCopy(columnList);
    }

    /**
     * Sets column list.
     *
     * @param columnList the column list
     */
    public void setColumnList(List<Column> columnList) {
        this.columnList = columnList;
    }

    /**
     * Gets pk name.
     *
     * @return the pk name
     */
    public String getPkName() {
        return pkName;
    }

    /**
     * Sets pk name.
     *
     * @param pkName the pk name
     */
    public void setPkName(String pkName) {
        this.pkName = pkName;
    }
}
