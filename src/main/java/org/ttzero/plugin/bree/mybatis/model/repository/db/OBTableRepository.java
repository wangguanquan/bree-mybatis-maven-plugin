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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import com.google.common.collect.Maps;

import org.ttzero.plugin.bree.mybatis.enums.DatabaseTypeEnum;
import org.ttzero.plugin.bree.mybatis.enums.TypeMapEnum;
import org.ttzero.plugin.bree.mybatis.model.config.CfTable;
import org.ttzero.plugin.bree.mybatis.model.dbtable.Column;
import org.ttzero.plugin.bree.mybatis.model.dbtable.PrimaryKeys;
import org.ttzero.plugin.bree.mybatis.model.dbtable.Table;
import org.ttzero.plugin.bree.mybatis.utils.CamelCaseUtils;
import org.ttzero.plugin.bree.mybatis.utils.ConfigUtil;
import org.ttzero.plugin.bree.mybatis.utils.StringUtil;

/**
 * Created by guanquan.wang at 2019-05-24 09:02
 */
public class OBTableRepository implements ITableRepository {

    /**
     * The constant PRIMARY_KEY_PATTERN.
     */
    private static final Pattern PRIMARY_KEY_PATTERN = Pattern.compile("\\((.*)\\)");

    /**
     * Gain table table.
     *
     * @param connection the connection
     * @param tableName  the table name
     * @param cfTable    the cf table
     * @return the table
     * @throws SQLException the sql exception
     */
    @Override
    public Table gainTable(Connection connection, String tableName, CfTable cfTable)
            throws SQLException {

        Table table = generalParse(cfTable, tableName);

        List<Column> cfColumns = cfTable == null ? null : cfTable.getColumns();
        // 填充字段
        fillColumns(connection, table.getPhysicalName(), table, cfColumns);

        // 自动生成初始操作
        table.setCreateDefaultOperation(ConfigUtil.config.isCreateDefaultOperation());

        return table;
    }

    /**
     * Fill columns.
     *
     * @param connection the connection
     * @param tableName  the table name
     * @param table      the table
     * @param cfColumns  the cf columns
     * @throws SQLException the sql exception
     */
    private void fillColumns(Connection connection, String tableName, Table table,
                             List<Column> cfColumns) throws SQLException {
        try (Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SHOW CREATE TABLE " + tableName)) {

            // 指定表字段
            // 组装字段
            while (resultSet.next()) {
                // 得到建表语句
                String createTableSql = getCreateTableSql(resultSet);

                // 解析建表语句
                String[] createSqlLines = createTableSql.split("\n");

                // 准备字段
                Map<String, Column> columnMap = Maps.newHashMap();
                String primaryKeyLine = preColumns(table, columnMap, cfColumns, createSqlLines);

                // 最后一行解析表注释
                String lastLine = createSqlLines[createSqlLines.length - 1];
                for (String comments : StringUtils.split(lastLine)) {
                    if (comments.startsWith("COMMENT=")) {
                        table.setRemark(comments.split("=", 2)[1]);
                    }
                }

                // 设置主键
                if (primaryKeyLine != null) {
                    Matcher m = PRIMARY_KEY_PATTERN.matcher(primaryKeyLine);
                    while (m.find()) {
                        PrimaryKeys primaryKeys = new PrimaryKeys();
                        primaryKeys.setPkName("PrimaryKey");
                        String[] pks = StringUtils.split(m.group(1));
                        for (String pk : pks) {
                            pk = StringUtils.trim(pk);
                            if (pks.length == 1) {
                                primaryKeys.setPkName(CamelCaseUtils.toCapitalizeCamelCase(pk));
                                primaryKeys.addColumn(columnMap.get(pk));
                            } else {
                                primaryKeys.addColumn(columnMap.get(pk));
                            }
                        }
                        table.setPrimaryKeys(primaryKeys);
                    }
                }
            }
        }
    }

    /**
     * Gets create table sql.
     *
     * @param resultSet the result set
     * @return the create table sql
     * @throws SQLException the sql exception
     */
    private String getCreateTableSql(ResultSet resultSet) throws SQLException {
        String createTableSql = resultSet.getString(2).toUpperCase();
        createTableSql = createTableSql.replace("`", "");
        createTableSql = createTableSql.replaceAll("\\s{1,}=\\s{1,}", "=");
        createTableSql = createTableSql.replaceAll("\\(\\d*\\)", "");
        createTableSql = createTableSql.replaceAll("COMMENT\\s{1,}'", "COMMON='");
        createTableSql = createTableSql.replace(", ", " ");
        createTableSql = createTableSql.replace(",", "");
        createTableSql = createTableSql.replace("'", "");
        return createTableSql;
    }

    /**
     * Pre columns string.
     *
     * @param table          the table
     * @param columnMap      the column map
     * @param cfColumns      the cf columns
     * @param createSqlLines the create sql lines
     * @return the string
     */
    private String preColumns(Table table, Map<String, Column> columnMap, List<Column> cfColumns, String[] createSqlLines) {
        String primaryKeyLine = null;
        for (int i = 1, len = createSqlLines.length - 1; i < len; i++) {
            String createSqlLine = createSqlLines[i].trim();
            if (StringUtil.isEmpty(createSqlLine)) continue;

            if (createSqlLine.startsWith("PRIMARY KEY")) {
                primaryKeyLine = createSqlLine;
                continue;
            }
            if (createSqlLine.startsWith("KEY ")) {
                continue;
            }
            Column column = new Column();
            String[] columnArray = StringUtils.split(createSqlLine);
            column.setColumn(columnArray[0]);
            column.setJdbcType(TypeMapEnum.getByJdbcType(columnArray[1]).getJdbcType());
            column.setProperty(CamelCaseUtils.toCamelCase(columnArray[0]));
            column.setJavaType(getJavaType(column, cfColumns));
            if (columnArray[columnArray.length - 1].startsWith("COMMENT=")) {
                column.setRemark(columnArray[columnArray.length - 1].split("=", 2)[1]);
            }
            if (StringUtil.isEmpty(column.getRemark())) {
                column.setRemark(column.getColumn());
            }
            column.setReserved(isReserved(column.getColumn()));
            table.addColumn(column);
            columnMap.put(column.getColumn(), column);
        }
        return primaryKeyLine;
    }

    /**
     * Gets java type.
     *
     * @param column    the column
     * @param cfColumns the cf columns
     * @return the java type
     */
    private String getJavaType(Column column, List<Column> cfColumns) {
        if (cfColumns != null && !cfColumns.isEmpty()) {
            for (Column cfColumn : cfColumns) {
                if (StringUtils.endsWithIgnoreCase(column.getColumn(), cfColumn.getColumn())) {
                    return cfColumn.getJavaType();
                }
            }
        }
        String javaType = TypeMapEnum.getByJdbcType(column.getJdbcType()).getJavaType();
        String custJavaType = ConfigUtil.config.getTypeMap().get(javaType);
        return StringUtils.isBlank(custJavaType) ? javaType : custJavaType;
    }

    /**
     * Returns database type enum
     *
     * @return the {@link DatabaseTypeEnum}
     */
    @Override
    public DatabaseTypeEnum getType() {
        return DatabaseTypeEnum.ob;
    }
}
