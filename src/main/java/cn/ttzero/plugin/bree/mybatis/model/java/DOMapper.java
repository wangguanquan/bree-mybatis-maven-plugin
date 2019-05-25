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

import cn.ttzero.plugin.bree.mybatis.model.java.domapper.DOMapperMethod;
import cn.ttzero.plugin.bree.mybatis.model.java.domapper.DOMapperMethodParam;
import com.google.common.collect.Lists;

import java.util.List;

/**
 * Created by guanquan.wang at 2019-05-24 17:16
 */
public class DOMapper extends Base {

    /**
     * The HasParam
     */
    private boolean hasParam;

    /**
     * The Methods.
     */
    private List<DOMapperMethod> methods = Lists.newArrayList();

    /**
     * Gets methods.
     *
     * @return the motheds
     */
    public List<DOMapperMethod> getMethods() {
        return methods;
    }

    /**
     * Add method.
     *
     * @param method the method
     */
    public void addMethod(DOMapperMethod method) {
        this.methods.add(method);
    }

    /**
     * has param
     *
     * @return
     */
    public boolean isHasParam() {
        for (DOMapperMethod method : methods) {
            List<DOMapperMethodParam> params = method.getParams();
            if (params.isEmpty()) continue;
            hasParam = params.size() > 1 || params.get(0).ispAnnot();
            if (hasParam) break;
        }
        return hasParam;
    }

}
