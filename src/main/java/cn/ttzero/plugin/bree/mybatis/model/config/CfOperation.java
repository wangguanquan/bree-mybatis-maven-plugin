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

import cn.ttzero.plugin.bree.mybatis.enums.MultiplicityEnum;
import cn.ttzero.plugin.bree.mybatis.enums.ParamTypeEnum;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by guanquan.wang at 2019-05-24 15:58
 */
public class CfOperation {

    /**
     * The Name.
     */
    private String name;
    /**
     * Paging param name
     */
    private String vo;
    /**
     * The Param type.
     */
    private ParamTypeEnum paramType;
    /**
     * The Multiplicity.
     */
    private MultiplicityEnum multiplicity;
    /**
     * The Remark.
     */
    private String remark;
    /**
     * The Resulttype.
     */
    private String resultType;
    /**
     * The Resultmap.
     */
    private String resultMap;
    /**
     * The Timeout.
     */
    private Long timeout;

    /**
     * The Cdata.
     */
    private String cdata;

    /**
     * The Cdata page count.
     */
    private String cdataPageCount;

    /**
     * The Sql desc.
     */
    private String sqlDesc;

    /**
     * The no count.
     */
    private boolean noCount;

    /**
     * The Primitive params.
     */
    private Map<String, String> primitiveParams = Maps.newHashMap();

    /**
     * The Primitive foreach params.
     */
    private Map<String, String> primitiveForeachParams = Maps.newHashMap();

    /**
     * The operation method. see {@link OperationMethod}
     */
    private OperationMethod om;

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
     * Gets paging.
     *
     * @return the paging
     */
    public String getVo() {
        return vo;
    }

    /**
     * Sets paging.
     *
     * @param vo the vo
     */
    public void setVo(String vo) {
        this.vo = vo;
    }

    /**
     * Gets param type.
     *
     * @return the param type
     */
    public ParamTypeEnum getParamType() {
        return paramType;
    }

    /**
     * Sets param type.
     *
     * @param paramType the param type
     */
    public void setParamType(ParamTypeEnum paramType) {
        this.paramType = paramType;
    }

    /**
     * Gets multiplicity.
     *
     * @return the multiplicity
     */
    public MultiplicityEnum getMultiplicity() {
        return multiplicity;
    }

    /**
     * Sets multiplicity.
     *
     * @param multiplicity the multiplicity
     */
    public void setMultiplicity(MultiplicityEnum multiplicity) {
        this.multiplicity = multiplicity;
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

    /**
     * Gets resultType.
     *
     * @return the resultType
     */
    public String getResultType() {
        return resultType;
    }

    /**
     * Sets resultType.
     *
     * @param resultType the resultType
     */
    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    /**
     * Gets resultMap.
     *
     * @return the resultMap
     */
    public String getResultMap() {
        return resultMap;
    }

    /**
     * Sets resultMap.
     *
     * @param resultMap the resultMap
     */
    public void setResultMap(String resultMap) {
        this.resultMap = resultMap;
    }

    /**
     * Gets timeout.
     *
     * @return the timeout
     */
    public Long getTimeout() {
        return timeout;
    }

    /**
     * Sets timeout.
     *
     * @param timeout the timeout
     */
    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    /**
     * Gets cdata.
     *
     * @return the cdata
     */
    public String getCdata() {
        return cdata;
    }

    /**
     * Sets cdata.
     *
     * @param cdata the cdata
     */
    public void setCdata(String cdata) {
        this.cdata = cdata;
    }

    /**
     * Gets primitive params.
     *
     * @return the primitive params
     */
    public Map<String, String> getPrimitiveParams() {
        return primitiveParams;
    }

    /**
     * Add primitive param.
     *
     * @param attr the attr
     * @param type the type
     */
    public void addPrimitiveParam(String attr, String type) {
        this.primitiveParams.put(attr, type);
    }

    /**
     * Matcher primitive params and foreach params
     *
     * @param attr
     * @return
     */
    public boolean containsParam(String attr) {
        return this.primitiveParams.containsKey(attr) || this.primitiveForeachParams.containsValue(attr);
    }

    /**
     * Gets primitive foreach params.
     *
     * @return the primitive foreach params
     */
    public Map<String, String> getPrimitiveForeachParams() {
        return primitiveForeachParams;
    }

    /**
     * Add primitive foreach param.
     *
     * @param itemName the item name
     * @param collName the coll name
     */
    public void addPrimitiveForeachParam(String itemName, String collName) {
        if (this.primitiveForeachParams.containsKey(itemName)) {
            this.primitiveForeachParams.put(itemName + collName, collName);
        } else {
            this.primitiveForeachParams.put(itemName, collName);
        }
    }

    /**
     * Gets sql desc.
     *
     * @return the sql desc
     */
    public String getSqlDesc() {
        return sqlDesc;
    }

    /**
     * Sets sql desc.
     *
     * @param sqlDesc the sql desc
     */
    public void setSqlDesc(String sqlDesc) {
        this.sqlDesc = sqlDesc;
    }

    /**
     * Gets cdata page count.
     *
     * @return the cdata page count
     */
    public String getCdataPageCount() {
        return cdataPageCount;
    }

    /**
     * Sets cdata page count.
     *
     * @param cdataPageCount the cdata page count
     */
    public void setCdataPageCount(String cdataPageCount) {
        this.cdataPageCount = cdataPageCount;
    }

    public boolean isNoCount() {
        return noCount;
    }

    public void setNoCount(String noCount) {
        this.noCount = "yes".equals(noCount);
    }

    public OperationMethod getOm() {
        return om;
    }

    public void setOm(OperationMethod om) {
        this.om = om;
    }
}
