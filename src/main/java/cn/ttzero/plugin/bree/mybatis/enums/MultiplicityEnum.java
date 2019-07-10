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

package cn.ttzero.plugin.bree.mybatis.enums;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by guanquan.wang at 2019-05-24 21:50
 */
public enum MultiplicityEnum {
    /**
     * One multiplicity enum.
     */
    one("one"),
    /**
     * Many multiplicity enum.
     */
    many("many"),
    /**
     * Paging multiplicity enum.
     */
    paging("paging");

    /**
     * The Code.
     */
    private String code;

    /**
     * Instantiates a new Multiplicity enum.
     *
     * @param code the code
     */
    MultiplicityEnum(String code) {
        this.code = code;
    }

    /**
     * Gets by code.
     *
     * @param code the code
     * @return the by code
     */
    public static MultiplicityEnum getByCode(String code) {
        for (MultiplicityEnum multiplicityEnum : MultiplicityEnum.values()) {
            if (StringUtils.equals(code, multiplicityEnum.code)) {
                return multiplicityEnum;
            }
        }
        return MultiplicityEnum.one;
    }

    /**
     * Gets code.
     *
     * @return the code
     */
    public String getCode() {
        return code;
    }
}
