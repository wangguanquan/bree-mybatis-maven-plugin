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

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by guanquan.wang at 2019-05-24 11:55
 */
public class IgnoreDTDEntityResolver implements EntityResolver {

    @Override
    public InputSource resolveEntity(String publicId, String systemId) {
        return new InputSource(new ByteArrayInputStream(
                "<?xml version='1.0' encoding='UTF-8'?>".getBytes(StandardCharsets.UTF_8)));
    }
}
