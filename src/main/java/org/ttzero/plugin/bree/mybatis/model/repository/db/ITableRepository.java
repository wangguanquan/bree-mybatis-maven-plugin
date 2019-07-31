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

package org.ttzero.plugin.bree.mybatis.model.repository.db;

import org.ttzero.plugin.bree.mybatis.enums.DatabaseTypeEnum;
import org.ttzero.plugin.bree.mybatis.model.config.CfTable;
import org.ttzero.plugin.bree.mybatis.model.dbtable.Database;
import org.ttzero.plugin.bree.mybatis.model.dbtable.Table;
import org.ttzero.plugin.bree.mybatis.utils.CamelCaseUtils;
import org.ttzero.plugin.bree.mybatis.utils.ConfigUtil;
import org.ttzero.plugin.bree.mybatis.utils.StringUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Create by guanquan.wang at 2019-07-13 11:27
 */
public interface ITableRepository {
    /**
     * Gain table.
     *
     * @param connection the connection
     * @param tableName the table name
     * @param cfTable the cf table
     * @return the table
     * @throws SQLException the sql exception
     */
    Table gainTable(Connection connection, String tableName, CfTable cfTable) throws SQLException;

    /**
     * Returns database type enum
     *
     * @return the {@link DatabaseTypeEnum}
     */
    DatabaseTypeEnum getType();

    /**
     * Test the column is reserved word
     *
     * @param column the column name
     * @return true if the name is reserved
     */
    default boolean isReserved(String column) {
        return ConfigUtil.reserved != null && ConfigUtil.reserved.isReserved(getType(), column);
    }

    /**
     * Parse general information from {@link CfTable}
     *
     * @param cfTable the table defined
     * @param tableName the table name
     * @return {@link Table}
     */
    default Table generalParse(CfTable cfTable, String tableName) {
        String logicName = tableName.toUpperCase();
        Database database = ConfigUtil.getCurrentDb();
        for (String splitTableSuffix : database.getSplitSuffixs()) {
            if (tableName.endsWith(splitTableSuffix.toUpperCase())) {
                logicName = logicName.substring(0, logicName.length() - splitTableSuffix.length());
                break;
            }
        }

        Table table = new Table();
        table.setName(logicName);
        for (String pre : database.getPrefixs()) {
            if (!pre.endsWith("_")) {
                pre = pre + "_";
            }

            pre = pre.toUpperCase();
            if (logicName.startsWith(pre)) {
                table.setJavaName(CamelCaseUtils.toCapitalizeCamelCase(logicName.substring(pre.length())));
                break;
            }
        }
        if (StringUtil.isEmpty(table.getJavaName())) {
            table.setJavaName(CamelCaseUtils.toCapitalizeCamelCase(logicName));
        }

        String physicalName = cfTable == null ? tableName : cfTable.getPhysicalName();
        table.setPhysicalName(physicalName);
        table.setRemark(logicName);

        return table;
    }

    /**
     * Getting value from {@link ResultSet} and set a default value if it empty
     *
     * @param resultSet the {@link ResultSet}
     * @param key the column name
     * @param defaultValue the default value
     * @return value of result set
     * @throws SQLException if sql exception occur
     */
    default String getOrElse(ResultSet resultSet, String key, String defaultValue) throws SQLException {
        String value = resultSet.getString(key);
        return StringUtil.isEmpty(key) ? defaultValue : value;
    }
}
