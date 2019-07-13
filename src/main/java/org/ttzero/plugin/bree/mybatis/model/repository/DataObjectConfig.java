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

import com.google.common.collect.Sets;

import java.util.Set;

/**
 * Create by guanquan.wang at 2019-05-24 10:02
 */
public class DataObjectConfig extends JavaConfig {
    /**
     * Ignore fields in do
     */
    private Set<String> ignoreFields = Sets.newHashSet();

    public static DataObjectConfig of(JavaConfig config) {
        if (config.getClass() == DataObjectConfig.class) {
            return DataObjectConfig.class.cast(config);
        }
        DataObjectConfig _config = new DataObjectConfig();
        _config.setNamespace(config.getNamespace());
        _config.setPrefix(config.getPrefix());
        _config.setSuffix(config.getSuffix());
        _config.setExtend(config.getExtend());
        for (JavaProperty jp : config.getAnnotationArray()) {
            _config.addAnnotation(jp);
        }
        for (JavaProperty jp : config.getImplementArray()) {
            _config.addImplement(jp);
        }
        return _config;
    }

    /**
     * Add the ignore filed
     *
     * @param fieldName the field name
     */
    public void addIgnoreField(String fieldName) {
        ignoreFields.add(fieldName);
    }

    /**
     * Returns the ignore fields name in do
     *
     * @return the ignore fields
     */
    public Set<String> getIgnoreFields() {
        return ignoreFields;
    }

    /**
     * Testing the ignore field
     *
     * @param field to be test field name
     * @return true if ignore
     */
    public boolean isIgnore(String field) {
        return ignoreFields.contains(field);
    }
}
