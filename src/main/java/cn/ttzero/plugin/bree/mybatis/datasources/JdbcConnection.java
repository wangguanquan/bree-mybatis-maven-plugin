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

package cn.ttzero.plugin.bree.mybatis.datasources;

import cn.ttzero.plugin.bree.mybatis.model.db.Database;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import java.sql.*;

/**
 * Created by guanquan.wang at 2019-05-24 21:50
 */
public class JdbcConnection {

    /**
     * The constant LOG.
     */
    private static final Log LOG = new SystemStreamLog();

    /**
     * Gets connection.
     *
     * @param dataBase the data base
     * @return the connection
     */
    public static Connection getConnection(Database dataBase) {
        try {

            Class.forName(dataBase.getDriverClass());
        } catch (ClassNotFoundException e) {
           LOG.info("驱动加载错误");
        }
        try {

            return DriverManager.getConnection(dataBase.getPropertyMapVal("url"),
                    dataBase.getPropertyMapVal("user"), dataBase.getPropertyMapVal("password"));
        } catch (SQLException e) {
            LOG.error("获取连接失败", e);
        }
        return null;
    }
}
