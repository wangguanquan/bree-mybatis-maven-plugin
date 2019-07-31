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

import com.google.common.collect.Lists;
import org.ttzero.plugin.bree.mybatis.BreeException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Created by guanquan.wang at 2019-05-24 23:53
 */
public class DoMapperMethod implements Serializable {
    /**
     * The Return class.
     */
    private String returnClass;
    /**
     * The Name.
     */
    private String name;

    /**
     * The Paging name.
     */
    private String pagingName;

    /**
     * The Desc.
     */
    private String desc;
    /**
     * The Sql.
     */
    private String sql;

    /**
     * The Is paging.
     */
    private String pagingFlag = "false";

    /**
     * The No count.
     */
    private String noCount = "false";

    /**
     * The Params.
     */
    private List<DoMapperMethodParam> params = Lists.newArrayList();

    /**
     * Gets return class.
     *
     * @return the return class
     */
    public String getReturnClass() {
        return returnClass;
    }

    /**
     * Sets return class.
     *
     * @param returnClass the return class
     */
    public void setReturnClass(String returnClass) {
        this.returnClass = returnClass;
    }

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
     * Gets params.
     *
     * @return the params
     */
    public List<DoMapperMethodParam> getParams() {
        // 对params进行排序
//        Ordering<DOMapperMethodParam> byLengthOrdering = new Ordering<DOMapperMethodParam>() {
//            private static final long serialVersionUID = 2293951554121638998L;
//
//            public int compare(DOMapperMethodParam left, DOMapperMethodParam right) {
//                int cr = compare(left.getParamType(), right.getParamType());
//                return cr == 0 ? compare(left.getParam(), right.getParam()) : cr;
//            }
//
//            private int compare(String left, String right) {
//                int cr = Ints.compare(left.length(), right.length());
//                return cr == 0 ? left.compareTo(right) : cr;
//            }
//
//        };
//        return byLengthOrdering.sortedCopy(params);
        return params;
    }

    /**
     * Add param.
     *
     * @param param the param
     */
    public void addParam(DoMapperMethodParam param) {
        if (!this.params.contains(param)) {
            this.params.add(param);
        }
    }

    /**
     * Sets params.
     *
     * @param params the params
     */
    public void setParams(List<DoMapperMethodParam> params) {
        this.params = params;
    }

    /**
     * Gets desc.
     *
     * @return the desc
     */
    public String getDesc() {
        return desc;
    }

    /**
     * Sets desc.
     *
     * @param desc the desc
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }

    /**
     * Gets sql.
     *
     * @return the sql
     */
    public String getSql() {
        return sql;
    }

    /**
     * Sets sql.
     *
     * @param sql the sql
     */
    public void setSql(String sql) {
        this.sql = sql;
    }

    /**
     * Gets paging flag.
     *
     * @return the paging flag
     */
    public String getPagingFlag() {
        return pagingFlag;
    }

    /**
     * Sets paging flag.
     *
     * @param pagingFlag the paging flag
     */
    public void setPagingFlag(String pagingFlag) {
        this.pagingFlag = pagingFlag;
    }

    /**
     * Gets paging name.
     *
     * @return the paging name
     */
    public String getPagingName() {
        return pagingName;
    }

    /**
     * Sets paging name.
     *
     * @param pagingName the paging name
     */
    public void setPagingName(String pagingName) {
        this.pagingName = pagingName;
    }

    public String getNoCount() {
        return noCount;
    }

    public void makeNoCount() {
        this.noCount = "true";
    }

    public DoMapperMethod deepClone() {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(this);

            ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
            return  getClass().cast(ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new BreeException("", e);
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    ;
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    ;
                }
            }
        }
    }
}
