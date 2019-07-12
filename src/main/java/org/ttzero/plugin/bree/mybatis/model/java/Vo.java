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
 * Created by guanquan.wang at 2019-05-24 17:16
 */
public class Vo extends Do {
    /**
     * The Package name.
     */
    private String basePackageName;

    /**
     * The Class path.
     */
    private String baseClassPath;

    /**
     * The Result type.
     */
    private String resultType;

    /**
     * Gets base package name.
     *
     * @return the base package name
     */
    public String getBasePackageName() {
        return basePackageName;
    }

    /**
     * Sets base package name.
     *
     * @param basePackageName the base package name
     */
    public void setBasePackageName(String basePackageName) {
        this.basePackageName = basePackageName;
    }

    /**
     * Gets base class path.
     *
     * @return the base class path
     */
    public String getBaseClassPath() {
        return baseClassPath;
    }

    /**
     * Sets base class path.
     *
     * @param baseClassPath the base class path
     */
    public void setBaseClassPath(String baseClassPath) {
        this.baseClassPath = baseClassPath;
    }

    /**
     * Gets result type.
     *
     * @return the result type
     */
    public String getResultType() {
        return resultType;
    }

    /**
     * Sets result type.
     *
     * @param resultType the result type
     */
    public void setResultType(String resultType) {
        this.resultType = resultType;
    }
}
