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

import com.google.common.collect.Lists;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

/**
 * Java类基本通用
 * Create by guanquan.wang at 2019-05-24 09:51
 */
public class JavaConfig implements Serializable {
    /**
     * The directory name
     */
    private String namespace;
    /**
     * The class prefix
     */
    private String prefix;
    /**
     * The class suffix
     */
    private String suffix;
    /**
     * The class extend from
     */
    private JavaProperty extend;
    /**
     * The class implements
     */
    private List<JavaProperty> implementArray = Lists.newArrayList();
    /**
     * The class's annotation
     */
    private List<JavaProperty> annotationArray = Lists.newArrayList();
    /**
     * Impl config
     */
    private JavaConfig impl;

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
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

    public void addImplement(String _class, String _import) {
        this.implementArray.add(new JavaProperty(_class, _import));
    }

    public void addImplement(JavaProperty jp) {
        this.implementArray.add(jp);
    }

    public List<JavaProperty> getAnnotationArray() {
        return annotationArray;
    }

    public void addAnnotation(String _class, String _import) {
        this.annotationArray.add(new JavaProperty(_class, _import));
    }

    public void addAnnotation(JavaProperty jp) {
        this.annotationArray.add(jp);
    }

    public JavaConfig getImpl() {
        return impl;
    }

    public void setImpl(JavaConfig impl) {
        this.impl = impl;
    }

    public JavaConfig deepClone() {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(this);

            ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
            return (JavaConfig) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (oos != null) {
                try {
                    oos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (ois != null) {
                try {
                    ois.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
