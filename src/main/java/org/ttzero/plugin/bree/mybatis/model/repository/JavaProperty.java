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

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;

/**
 * Create by guanquan.wang at 2019-05-24 09:55
 */
public class JavaProperty implements Serializable {
    /**
     * The class name
     */
    private String className;
    /**
     * The import
     */
    private String importPath;
    /**
     * Conditions
     */
    private Set<String> conditionSet = Sets.newHashSet();

    public JavaProperty(String className, String importPath) {
        this.className = className;
        this.importPath = importPath;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getImportPath() {
        return importPath;
    }

    public void setImportPath(String importPath) {
        this.importPath = importPath;
    }

    public void addCondition(String condition) {
        conditionSet.add(condition);
    }

    public boolean testCondition(String condition) {
        return conditionSet.contains(condition);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JavaProperty property = (JavaProperty) o;
        return Objects.equals(className, property.className) &&
            Objects.equals(importPath, property.importPath);
    }

    @Override
    public int hashCode() {

        return Objects.hash(className, importPath);
    }
}
