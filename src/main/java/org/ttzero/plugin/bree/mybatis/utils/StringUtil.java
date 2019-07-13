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

package org.ttzero.plugin.bree.mybatis.utils;

/**
 * Created by guanquan.wang at 2019-05-24 123:57
 */
public class StringUtil {
    /**
     * Join string with space character.
     *
     * @param p1 the p 1
     * @param p2 the p 2
     * @return the string
     */
    public static String join(String p1, String p2) {
        if (p1 == null && p2 == null) {
            return "";
        }
        String o1 = p1 == null ? "" : p1, o2 = p2 == null ? "" : p2;
        return o1 + " " + o2;
    }

    /**
     * Upper first string.
     *
     * @param str the str
     * @return the string
     */
    public static String upperFirst(String str) {
        return CamelCaseUtils.toCapitalizeCamelCase(CamelCaseUtils.toUnderlineName(str));
    }

    /**
     * Lower first string.
     *
     * @param str the str
     * @return the string
     */
    public static String lowerFirst(String str) {
        return CamelCaseUtils.toCamelCase(CamelCaseUtils.toUnderlineName(str));
    }

    /**
     * Testing null or empty value
     *
     * @param value to be test string
     * @return true if null or empty
     */
    public static boolean isEmpty(String value) {
        return value == null || value.isEmpty();
    }

    /**
     * Testing not null and not empty
     *
     * @param value to be test string
     * @return true if not null and not empty
     */
    public static boolean isNotEmpty(String value) {
        return value != null && !value.isEmpty();
    }
}
