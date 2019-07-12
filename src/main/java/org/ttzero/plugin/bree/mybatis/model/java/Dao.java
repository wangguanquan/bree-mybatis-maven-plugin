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

/**
 * Created by guanquan.wang at 2019-05-24 23:54
 */
public class Dao extends DoMapper {
    /**
     * The Do mapper.
     */
    private DoMapper doMapper;

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
}
