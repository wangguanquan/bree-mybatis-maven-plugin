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

package org.ttzero.plugin.bree.mybatis.model.repository;

import org.ttzero.plugin.bree.mybatis.enums.DatabaseTypeEnum;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringJoiner;

/**
 * Create by guanquan.wang at 2019-07-30 20:55
 */
public class Reserved {
    /**
     * Common config
     */
    private Set<String> common;
    /**
     * Sub reserved config
     */
    private Map<DatabaseTypeEnum, Set<String>> sub;
    /**
     * The database type enum
     */
    private DatabaseTypeEnum current;

    public Reserved() {
        this.common = new HashSet<>();
        this.sub = new HashMap<>();
    }

    /**
     * Setting current database type
     *
     * @param type type of database
     */
    public void setCurrent(DatabaseTypeEnum type) {
        this.current = type;
        this.sub.put(type, new HashSet<>());
    }

    /**
     * Put into reserved config
     *
     * @param reserved the config value
     */
    public void put(String reserved) {
        if (current != null) this.sub.get(current).add(reserved);
        else this.common.add(reserved);
    }

    /**
     * Test the column name is reserved word.
     *
     * @param name the column name
     * @return true if it reserved
     */
    public boolean isReserved(DatabaseTypeEnum type, String name) {
        boolean h = common.contains(name);
        if (!h) {
            Set<String> set = sub.get(type);
            h = set != null && set.contains(name);
        }
        return h;
    }

    /**
     * Returns size of reserved word
     *
     * @return the config size
     */
    public int size() {
        int size = common.size();
        for (Map.Entry<DatabaseTypeEnum, Set<String>> it : sub.entrySet()) {
            size += it.getValue().size();
        }
        return size;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(System.lineSeparator());
        joiner.add("[common]");
        for (String name : common) {
            joiner.add(name);
        }

        for (Map.Entry<DatabaseTypeEnum, Set<String>> it : sub.entrySet()) {
            joiner.add("[" + it.getKey().name() + "]");
            for (String name : it.getValue()) {
                joiner.add(name);
            }
        }
        return joiner.toString();
    }
}
