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

package org.ttzero.plugin.bree.mybatis.model.java.domapper;

import java.io.Serializable;

/**
 * Created by guanquan.wang at 2019-05-24 23:59
 */
public class DoMapperMethodParam implements Serializable {
    /**
     * The Param type.
     */
    private String paramType;
    /**
     * The Param.
     */
    private String param;
    /**
     * Add @Param("xxx") Annotation
     */
    private boolean pAnnot;

    /**
     * Instantiates a new Do mapper method param.
     *
     * @param paramType the param type
     * @param param     the param
     */
    public DoMapperMethodParam(String paramType, String param) {
        this.param = param;
        this.paramType = paramType;
    }

    /**
     * Gets param type.
     *
     * @return the param type
     */
    public String getParamType() {
        return paramType;
    }

    /**
     * Sets param type.
     *
     * @param paramType the param type
     */
    public void setParamType(String paramType) {
        this.paramType = paramType;
    }

    /**
     * Gets param.
     *
     * @return the param
     */
    public String getParam() {
        return param;
    }

    /**
     * Sets param.
     *
     * @param param the param
     */
    public void setParam(String param) {
        this.param = param;
    }

    public boolean ispAnnot() {
        return pAnnot;
    }

    public void setpAnnot(boolean pAnnot) {
        this.pAnnot = pAnnot;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof DoMapperMethodParam) {
            DoMapperMethodParam ob = (DoMapperMethodParam) obj;
            return (param + paramType).equals(ob.param + ob.paramType);
        }
        return false;
    }
}
