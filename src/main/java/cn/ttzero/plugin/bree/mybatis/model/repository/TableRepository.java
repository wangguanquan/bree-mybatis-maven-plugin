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

package cn.ttzero.plugin.bree.mybatis.model.repository;

import cn.ttzero.plugin.bree.mybatis.model.config.CfTable;
import cn.ttzero.plugin.bree.mybatis.model.dbtable.Table;
import cn.ttzero.plugin.bree.mybatis.model.repository.db.MySQLTableRepository;
import cn.ttzero.plugin.bree.mybatis.model.repository.db.OBTableRepository;
import cn.ttzero.plugin.bree.mybatis.utils.ConfigUtil;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.Validate;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by guanquan.wang at 2019-05-24 23:55
 */
public class TableRepository {
    /**
     * Gain table table.
     *
     * @param connection the connection
     * @param tableName the table name
     * @param cfTable the cf table
     * @return the table
     * @throws SQLException the sql exception
     */
    public Table gainTable(Connection connection, String tableName, CfTable cfTable)
            throws SQLException {
        if (StringUtils.equalsIgnoreCase(ConfigUtil.getCurrentDb().getType(), "mysql")) {
            MySQLTableRepository tableRepository = new MySQLTableRepository();
            return tableRepository.gainTable(connection,tableName,cfTable);
        }
        if (StringUtils.equalsIgnoreCase(ConfigUtil.getCurrentDb().getType(), "ob")) {
            OBTableRepository tableRepository = new OBTableRepository();
            return tableRepository.gainTable(connection,tableName,cfTable);
        }

        Validate.notNull(null,"===== config.xml 目前仅支持 mysql ob 请正确选择 =====");
        return null;
    }
}
