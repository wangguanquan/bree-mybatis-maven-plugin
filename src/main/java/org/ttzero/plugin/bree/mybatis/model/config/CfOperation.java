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

import org.ttzero.plugin.bree.mybatis.enums.MultiplicityEnum;
import org.ttzero.plugin.bree.mybatis.enums.ParamTypeEnum;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Created by guanquan.wang at 2019-05-24 15:58
 */
public class CfOperation {

    /**
     * The id.
     */
    private String id;
    /**
     * Paging param name
     */
    private String vo;
    /**
     * The Param type.
     */
    private ParamTypeEnum paramType = ParamTypeEnum.primitive;
    /**
     * The Multiplicity.
     */
    private MultiplicityEnum multiplicity = MultiplicityEnum.one;
    /**
     * The Remark.
     */
    private String remark;
    /**
     * The result type.
     */
    private String resultType;
    /**
     * The result map.
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
     * Other properties
     */
    private String others;

    /**
     * The Primitive params.
     */
    private Map<String, String> primitiveParams = Maps.newHashMap();

    /**
     * The Primitive foreach params.
     */
    private Map<String, String> primitiveForeachParams = Maps.newHashMap();

    /**
     * If exist customize count operation
     */
    private boolean customizeCount;

    /**
     * The operation method. see {@link OperationMethod}
     */
    private OperationMethod operation;

    /**
     * Returns operation id.
     *
     * @return the operation id
     */
    public String getId() {
        return id;
    }

    /**
     * Setting the operation id.
     *
     * @param id the operation id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns paging.
     *
     * @return the paging
     */
    public String getVo() {
        return vo;
    }

    /**
     * Setting paging.
     *
     * @param vo the vo
     */
    public void setVo(String vo) {
        this.vo = vo;
    }

    /**
     * Returns param type.
     *
     * @return the param type
     */
    public ParamTypeEnum getParamType() {
        return paramType;
    }

    /**
     * Setting param type.
     *
     * @param paramType the param type
     */
    public void setParamType(ParamTypeEnum paramType) {
        this.paramType = paramType;
    }

    /**
     * Returns multiplicity.
     *
     * @return the multiplicity
     */
    public MultiplicityEnum getMultiplicity() {
        return multiplicity;
    }

    /**
     * Setting multiplicity.
     *
     * @param multiplicity the multiplicity
     */
    public void setMultiplicity(MultiplicityEnum multiplicity) {
        this.multiplicity = multiplicity;
    }

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
     * Returns resultType.
     *
     * @return the resultType
     */
    public String getResultType() {
        return resultType;
    }

    /**
     * Setting resultType.
     *
     * @param resultType the resultType
     */
    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    /**
     * Returns resultMap.
     *
     * @return the resultMap
     */
    public String getResultMap() {
        return resultMap;
    }

    /**
     * Setting resultMap.
     *
     * @param resultMap the resultMap
     */
    public void setResultMap(String resultMap) {
        this.resultMap = resultMap;
    }

    /**
     * Returns timeout.
     *
     * @return the timeout
     */
    public Long getTimeout() {
        return timeout;
    }

    /**
     * Setting timeout.
     *
     * @param timeout the timeout
     */
    public void setTimeout(Long timeout) {
        this.timeout = timeout;
    }

    /**
     * Returns cdata.
     *
     * @return the cdata
     */
    public String getCdata() {
        return cdata;
    }

    /**
     * Setting cdata.
     *
     * @param cdata the cdata
     */
    public void setCdata(String cdata) {
        this.cdata = cdata;
    }

    /**
     * Returns primitive params.
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
     * Returns primitive foreach params.
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
     * Returns sql desc.
     *
     * @return the sql desc
     */
    public String getSqlDesc() {
        return sqlDesc;
    }

    /**
     * Setting sql desc.
     *
     * @param sqlDesc the sql desc
     */
    public void setSqlDesc(String sqlDesc) {
        this.sqlDesc = sqlDesc;
    }

    /**
     * Returns cdata page count.
     *
     * @return the cdata page count
     */
    public String getCdataPageCount() {
        return cdataPageCount;
    }

    /**
     * Setting cdata page count.
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

    /**
     * Returns true if has customize count
     *
     * @return true if has customize count
     */
    public boolean isCustomizeCount() {
        return customizeCount;
    }

    /**
     * Setting the customize flag
     *
     * @param customizeCount the customize count flag
     */
    public void setCustomizeCount(boolean customizeCount) {
        this.customizeCount = customizeCount;
    }

    public OperationMethod getOperation() {
        return operation;
    }

    public void setOperation(OperationMethod operation) {
        this.operation = operation;
    }

    /**
     * Returns other properties, join on space
     *
     * @return the joined result
     */
    public String getOthers() {
        return others;
    }

    /**
     * Setting the joined properties string
     *
     * @param others the joined string
     */
    public void setOthers(String others) {
        this.others = others;
    }
}
