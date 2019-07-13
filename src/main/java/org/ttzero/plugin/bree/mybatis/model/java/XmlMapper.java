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

package org.ttzero.plugin.bree.mybatis.model.java;

import com.google.common.collect.Lists;
import org.ttzero.plugin.bree.mybatis.model.config.CfTable;
import org.ttzero.plugin.bree.mybatis.model.dbtable.Table;

import java.util.List;

/**
 * Created by guanquan.wang at 2019-05-24 11:11
 */
public class XmlMapper {
    /**
     * The Cf table.
     */
    private CfTable cfTable;

    /**
     * The Table.
     */
    private Table table;

    /**
     * The Do class.
     */
    private Do doClass;

    /**
     * The Do mapper.
     */
    private DoMapper doMapper;
    /**
     * The Result maps.
     */
    private List<ResultMap> resultMaps = Lists.newArrayList();

    /**
     * The Class path.
     */
    private String classPath;

    /**
     * Gets cf table.
     *
     * @return the cf table
     */
    public CfTable getCfTable() {
        return cfTable;
    }

    /**
     * Sets cf table.
     *
     * @param cfTable the cf table
     */
    public void setCfTable(CfTable cfTable) {
        this.cfTable = cfTable;
    }

    /**
     * Gets table.
     *
     * @return the table
     */
    public Table getTable() {
        return table;
    }

    /**
     * Sets table.
     *
     * @param table the table
     */
    public void setTable(Table table) {
        this.table = table;
    }

    /**
     * Gets do class.
     *
     * @return the do class
     */
    public Do getDoClass() {
        return doClass;
    }

    /**
     * Sets do class.
     *
     * @param doClass the do class
     */
    public void setDoClass(Do doClass) {
        this.doClass = doClass;
    }

    /**
     * Gets do mapper.
     *
     * @return the do mapper
     */
    public DoMapper getDoMapper() {
        return doMapper;
    }

    /**
     * Sets do mapper.
     *
     * @param doMapper the do mapper
     */
    public void setDoMapper(DoMapper doMapper) {
        this.doMapper = doMapper;
    }

    /**
     * Gets result maps.
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
     * Returns the class path
     *
     * @return the classPath value
     */
    public String getClassPath() {
        return classPath;
    }

    /**
     * Setting the class path
     *
     * @param classPath the class path
     */
    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

}
