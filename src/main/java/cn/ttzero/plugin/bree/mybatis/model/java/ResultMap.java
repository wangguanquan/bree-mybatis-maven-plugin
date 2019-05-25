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

package cn.ttzero.plugin.bree.mybatis.model.java;

import cn.ttzero.plugin.bree.mybatis.model.dbtable.Column;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by guanquan.wang at 2019-05-24 11:54
 */
public class ResultMap extends DO {
    /**
     * The Name.
     */
    private String name;
    /**
     * The Type.
     */
    private String type;
    /**
     * The Column list.
     */
    private List<Column> columnList = Lists.newArrayList();
    /**
     * 内联xml（将内部的多层collection或association转换为xml）
     */
    private String innerXML;

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
     * Gets column list.
     *
     * @return the column list
     */
    public List<Column> getColumnList() {
        return columnList;
    }

    /**
     * Add column.
     *
     * @param column the column
     */
    public void addColumn(Column column) {
        this.columnList.add(column);
    }

    public String getInnerXML() {
        return innerXML;
    }

    public void setInnerXML(String innerXML) {
        this.innerXML = innerXML;
    }
}
