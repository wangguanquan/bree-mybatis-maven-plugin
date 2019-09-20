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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by guanquan.wang at 2019-05-24 23:54
 */
public class Dao extends DoMapper {
    /**
     * The Do mapper.
     */
    private DoMapper doMapper;

    private boolean hasImpl;

    /**
     * Gets do mapper.
     *
     * @return the do mapper
     */
    public DoMapper getDoMapper() {
        return doMapper;
    }

    /**
     * Sets do mapper.
     *
     * @param doMapper the do mapper
     */
    public void setDoMapper(DoMapper doMapper) {
        this.doMapper = doMapper;
    }

    public boolean isHasImpl() {
        return hasImpl;
    }

    public void setHasImpl(boolean hasImpl) {
        this.hasImpl = hasImpl;
    }

    public Dao deepClone() {
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            oos = new ObjectOutputStream(bos);
            oos.writeObject(this);

            ois = new ObjectInputStream(new ByteArrayInputStream(bos.toByteArray()));
            return (Dao) ois.readObject();
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
