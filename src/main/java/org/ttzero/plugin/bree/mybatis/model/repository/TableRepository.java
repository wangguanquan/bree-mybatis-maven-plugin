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
import org.ttzero.plugin.bree.mybatis.model.config.CfTable;
import org.ttzero.plugin.bree.mybatis.model.dbtable.Table;
import org.ttzero.plugin.bree.mybatis.model.repository.db.ITableRepository;
import org.ttzero.plugin.bree.mybatis.model.repository.db.MySQLTableRepository;
import org.ttzero.plugin.bree.mybatis.model.repository.db.OBTableRepository;
import org.ttzero.plugin.bree.mybatis.utils.ConfigUtil;
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
        String type = ConfigUtil.getCurrentDb().getType().toLowerCase();
        ITableRepository tableRepository = null;
        switch (type) {
            case "mysql":
            case "sqlite":
                tableRepository = new MySQLTableRepository(DatabaseTypeEnum.valueOf(type));
                break;
            case "ob":
                tableRepository = new OBTableRepository();
                break;
                default:
                    System.out.println("===== 目前支持 mysql/sqlite/ob 请正确选择 =====");
        }

        return tableRepository != null ? tableRepository.gainTable(connection,tableName,cfTable) : null;
    }
}
