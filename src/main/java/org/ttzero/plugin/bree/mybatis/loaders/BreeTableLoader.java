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

package org.ttzero.plugin.bree.mybatis.loaders;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.ttzero.plugin.bree.mybatis.model.Gen;
import org.ttzero.plugin.bree.mybatis.utils.ConfigUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import org.ttzero.plugin.bree.mybatis.model.repository.TableRepository;
import com.google.common.collect.Lists;

/**
 * 初始化表用
 * Created by guanquan.wang at 2019-05-24 09:02
 */
public class BreeTableLoader extends AbstractLoader {
    /**
     * The constant LOG.
     */
    private static final Log LOG = new SystemStreamLog();

    /**
     * The Table repository.
     */
    private TableRepository tableRepository = new TableRepository();

    /**
     * Load.
     *
     * @param gen        the gen
     * @param connection the connection
     */
    @Override
    public void load(Gen gen, Connection connection, File tablesFile) throws SQLException {

        if (StringUtils.equals(StringUtils.trim(ConfigUtil.cmd), "*")) {
            return;
        }
        List<String> cmdTables = Lists.newArrayList(StringUtils.split(StringUtils.upperCase(ConfigUtil.cmd)));

        List<String> neadInitTables = Lists.newArrayList();

        List<String> existsTables = Lists.newArrayList();
        File[] files = tablesFile.listFiles((d, name) -> name.endsWith(".xml"));
        if (files != null) {
            for (File file : files) {
                existsTables.add(file2DbName(file));
            }
        }

        for (String cmdTable : cmdTables) {
            if (!existsTables.contains(cmdTable)) {
                neadInitTables.add(cmdTable);
            }
        }

        if (neadInitTables.isEmpty()) {
            return;
        }

        LOG.info("开始初始化表");
        for (String neadInitTable : neadInitTables) {
            LOG.info("初始化表:" + neadInitTable);
            gen.addTable(tableRepository.gainTable(connection, neadInitTable, null));
        }
    }
}
