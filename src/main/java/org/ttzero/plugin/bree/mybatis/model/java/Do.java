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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by guanquan.wang at 2019-05-24 11:13
 */
public class Do extends Base {

    /**
     * The do field array
     */
    private List<Field> fieldList = new ArrayList<>();

    /**
     * Gets the do field array.
     *
     * @return the do field array
     */
    public List<Field> getFieldList() {
        return fieldList;
    }

    /**
     * Add field.
     *
     * @param field the field
     */
    public void addField(Field field) {
        if (!fieldList.contains(field)) {
            this.fieldList.add(field);
        }
    }


}
