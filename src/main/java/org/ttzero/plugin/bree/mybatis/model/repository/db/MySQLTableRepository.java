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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.ttzero.plugin.bree.mybatis.model.config.CfTable;
import org.ttzero.plugin.bree.mybatis.model.dbtable.Database;
import org.ttzero.plugin.bree.mybatis.model.dbtable.Table;
import org.ttzero.plugin.bree.mybatis.utils.ConfigUtil;
import org.ttzero.plugin.bree.mybatis.utils.StringUtil;
import org.apache.ibatis.type.JdbcType;

import org.ttzero.plugin.bree.mybatis.enums.TypeMapEnum;
import org.ttzero.plugin.bree.mybatis.model.dbtable.Column;
import org.ttzero.plugin.bree.mybatis.model.dbtable.PrimaryKeys;
import org.ttzero.plugin.bree.mybatis.utils.CamelCaseUtils;

/**
 * Created by guanquan.wang at 2019-05-24 09:02
 */
public class MySQLTableRepository implements ITableRepository {

    /**
     * Gain table table.
     *
     * @param connection the connection
     * @param tableName the table name
     * @param cfTable the cf table
     * @return the table
     * @throws SQLException the sql exception
     */
    @Override
    public Table gainTable(Connection connection, String tableName, CfTable cfTable)
            throws SQLException {
        String physicalName = cfTable == null ? tableName : cfTable.getPhysicalName();
        String logicName = tableName.toUpperCase();
        Database database = ConfigUtil.getCurrentDb();
        for (String splitTableSuffix : database.getSplitSuffixs()) {
            if (tableName.endsWith(splitTableSuffix.toUpperCase())) {
                logicName = logicName.substring(0, logicName.length() - splitTableSuffix.length());
                break;
            }
        }

        List<Column> cfColumns = cfTable == null ? null : cfTable.getColumns();
        DatabaseMetaData databaseMetaData = connection.getMetaData();


        // 生成table
        Table table = new Table();
        table.setName(logicName);
        for (String pre : database.getPrefixs()) {
            if (!pre.endsWith("_")) {
                pre = pre + "_";
            }

            pre = pre.toUpperCase();
            if (logicName.startsWith(pre)) {
                table.setJavaName(CamelCaseUtils.toCapitalizeCamelCase(logicName.substring(pre.length())));
                break;/* 取第一个匹配的 */
            }
        }
        if (StringUtil.isEmpty(table.getJavaName())) {
            table.setJavaName(CamelCaseUtils.toCapitalizeCamelCase(logicName));
        }
        table.setPhysicalName(physicalName);
        table.setRemark(logicName);


        // 填充字段
        fillColumns(connection, physicalName, databaseMetaData, table, cfColumns);

        // 主键
        fillPrimaryKeys(connection, physicalName, databaseMetaData, table);

        // 自动生成初始操作
        table.setCreateDefaultOperation(ConfigUtil.config.isCreateDefaultOperation());

        return table;
    }

    /**
     * Fill primary keys.
     *
     * @param connection the connection
     * @param tableName the table name
     * @param databaseMetaData the database meta data
     * @param table the table
     * @throws SQLException the sql exception
     */
    private void fillPrimaryKeys(Connection connection, String tableName,
                                 DatabaseMetaData databaseMetaData, Table table)
            throws SQLException {
        PrimaryKeys primaryKeys = null;

        ResultSet resultSet = databaseMetaData.getPrimaryKeys(connection.getCatalog(),
                connection.getSchema(), tableName);

        while (resultSet.next()) {
            String name = resultSet.getString("COLUMN_NAME")
                , pkName = resultSet.getString("PK_NAME");
            for (Column column : table.getColumnList()) {
                if (column.getColumn().equalsIgnoreCase(name)) {
                    primaryKeys = primaryKeys == null ? new PrimaryKeys() : primaryKeys;
                    primaryKeys.addColumn(column);
                    column.setPrimaryKey(true);
                    pkName = StringUtil.isEmpty(pkName) ? column.getColumn() : pkName;
                    primaryKeys.setPkName(CamelCaseUtils.toCapitalizeCamelCase(pkName));
                }
            }
        }
        table.setPrimaryKeys(primaryKeys);
    }

    /**
     * Fill columns.
     *
     * @param connection the connection
     * @param tableName the table name
     * @param databaseMetaData the database meta data
     * @param table the table
     * @param cfColumns the cf columns
     * @throws SQLException the sql exception
     */
    private void fillColumns(Connection connection, String tableName,
                             DatabaseMetaData databaseMetaData, Table table,
                             List<Column> cfColumns) throws SQLException {
        // 指定表字段
        ResultSet resultSet = databaseMetaData.getColumns(connection.getCatalog(), null, tableName, null);

        // 组装字段
        while (resultSet.next()) {
            Column column = new Column();
            column.setColumn(resultSet.getString("COLUMN_NAME").toUpperCase());
            column.setJdbcType(JdbcType.forCode(resultSet.getInt("DATA_TYPE")).name());
            column.setDefaultValue(resultSet.getString("COLUMN_DEF"));
            column.setProperty(CamelCaseUtils.toCamelCase(column.getColumn()));
            column.setJavaType(getJavaType(column, cfColumns));
            column.setRemark(str(resultSet, "REMARKS", column.getColumn()));
            table.addColumn(column);
        }
    }

    /**
     * Gets java type.
     *
     * @param column the column
     * @param cfColumns the cf columns
     * @return the java type
     */
    private String getJavaType(Column column, List<Column> cfColumns) {
//        if (cfColumns != null && cfColumns.size() > 0) {
//            for (Column cfColumn : cfColumns) {
//                if (StringUtils.endsWithIgnoreCase(column.getColumn(), cfColumn.getColumn())) {
//                    return cfColumn.getJavaType();
//                }
//            }
//        }
        String javaType = TypeMapEnum.getByJdbcType(column.getJdbcType()).getJavaType();
        String customizeJavaType = ConfigUtil.config.getTypeMap().get(javaType);
        return StringUtil.isEmpty(customizeJavaType) ? javaType : customizeJavaType;
    }


    /**
     * Str string.
     *
     * @param resultSet the result set
     * @param column the column
     * @param defaultVal the default val
     * @return the string
     * @throws SQLException the sql exception
     */
    private String str(ResultSet resultSet, String column, String defaultVal) throws SQLException {
        String val = resultSet.getString(column);
        return StringUtil.isEmpty(val) ? defaultVal : val;
    }
}
