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

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;
import org.ttzero.plugin.bree.mybatis.enums.DatabaseTypeEnum;
import org.ttzero.plugin.bree.mybatis.model.repository.Reserved;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Create by guanquan.wang at 2019-07-30 20:53
 */
public class ReservedUtil {
    /**
     * The constant LOG.
     */
    private static final Log LOG = new SystemStreamLog();

    /**
     * Load reserved config
     *
     * @param reservedFile the reserved config path
     * @return the {@link Reserved}
     */
    public static Reserved load(Path reservedFile) {
        if (!Files.exists(reservedFile)) {
            LOG.info("No reserved config.");
            return null;
        }

        final Reserved reserved = new Reserved();
        try (Stream<String> stream = Files.lines(reservedFile)) {
            stream.map(String::trim).filter(s -> !s.isEmpty() && !s.startsWith("#")).forEach(s -> {
                if (s.charAt(0) == '[' && s.charAt(s.length() - 1) == ']') {
                    reserved.setCurrent(DatabaseTypeEnum.valueOf(s.substring(1, s.length() - 1)));
                }
                else reserved.put(s);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        LOG.info("Load " + reserved.size() + " reserved or keyword.");

        return reserved;
    }
}
