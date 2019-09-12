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

import com.google.common.collect.Lists;
import org.ttzero.plugin.bree.mybatis.model.repository.JavaProperty;

import java.util.List;

/**
 * Created by guanquan.wang at 2019-05-24 11:13
 */
public class Base implements Cloneable {
    /**
     * The Class name.
     */
    private String className;

    /**
     * The Desc.
     */
    private String desc;

    /**
     * The Package name.
     */
    private String packageName;

    /**
     * The Class path.
     */
    private String classPath;

    /**
     * The Table name.
     */
    private String tableName;

    /**
     * The Import lists.
     */
    private List<String> importLists = Lists.newArrayList();

    /**
     * The class extend from
     */
    private JavaProperty extend;

    /**
     * The entry name.
     */
    private String entryName;

    /**
     * The class implements
     */
    private List<JavaProperty> implementArray = Lists.newArrayList();
    /**
     * The class's annotation
     */
    private List<JavaProperty> annotationArray = Lists.newArrayList();

    /**
     * Gets class name.
     *
     * @return the class name
     */
    public String getClassName() {
        return className;
    }

    /**
     * Sets class name.
     *
     * @param className the class name
     */
    public void setClassName(String className) {
        this.className = className;
    }

    /**
     * Gets package name.
     *
     * @return the package name
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * Sets package name.
     *
     * @param packageName the package name
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    /**
     * Gets class path.
     *
     * @return the class path
     */
    public String getClassPath() {
        return classPath;
    }

    /**
     * Sets class path.
     *
     * @param classPath the class path
     */
    public void setClassPath(String classPath) {
        this.classPath = classPath;
    }

    /**
     * Gets import lists.
     *
     * @return the import lists
     */
    public List<String> getImportLists() {
        return importLists;
    }

    /**
     * Add import list.
     *
     * @param importClass the import class
     */
    public void addImport(String importClass) {
        if (!this.importLists.contains(importClass)) {
            this.importLists.add(importClass);
        }
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
     * Gets table name.
     *
     * @return the table name
     */
    public String getTableName() {
        return tableName;
    }

    /**
     * Sets table name.
     *
     * @param tableName the table name
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public JavaProperty getExtend() {
        return extend;
    }

    public void setExtend(JavaProperty extend) {
        this.extend = extend;
    }

    public List<JavaProperty> getImplementArray() {
        return implementArray;
    }

    public void setImplementArray(List<JavaProperty> implementArray) {
        this.implementArray = implementArray;
    }

    public List<JavaProperty> getAnnotationArray() {
        return annotationArray;
    }

    public void setAnnotationArray(List<JavaProperty> annotationArray) {
        this.annotationArray = annotationArray;
    }

    public String getEntryName() {
        return entryName;
    }

    public void setEntryName(String entryName) {
        this.entryName = entryName;
    }
}
