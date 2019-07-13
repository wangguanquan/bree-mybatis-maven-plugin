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

import org.ttzero.plugin.bree.mybatis.BreeException;
import org.ttzero.plugin.bree.mybatis.model.Gen;
import org.ttzero.plugin.bree.mybatis.utils.ConfigUtil;
import fmpp.Engine;
import fmpp.tdd.DataLoader;
import org.ttzero.plugin.bree.mybatis.others.DBConnectionFactory;

import java.io.File;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by guanquan.wang at 2019-05-24 21:50
 */
public abstract class AbstractLoader implements DataLoader {

    /**
     * Load object.
     *
     * @param e the e
     * @param args Arguments that the caller specifies for this directive call.
     *            Not null. The implementation should check if it understands
     *            all arguments, and it should throw
     *            <code>java.lang.IllegalArgumentException</code> if it doesn't.
     * @return The object that will be accessed in FreeMarker templates. The
     *         object can be of any type. FreeMarker will wrap the object so
     *         that it is visible as an FTL variable. However, if the object
     *         implements <code>freemarker.template.TemplateModel</code>, then
     *         it will not be wrapped, as it is already an FTL variable.
     * @throws Exception the exception
     */
    public Object load(Engine e, List args) throws Exception {
        Gen gen = new Gen();

        gen.setOutRoot(e.getOutputRoot().getAbsolutePath());
        gen.setBreeRoot(ConfigUtil.breePath);
        gen.setDataBaseName(ConfigUtil.getCurrentDb().getName());
        gen.setTablesPath(ConfigUtil.getCurrentDb().getName() + "Tables");

        File tablesFile = new File(
                (gen.getBreeRoot() + File.separator + gen.getTablesPath() + File.separator));
        if (!tablesFile.exists()) {
            if (!tablesFile.mkdir()) {
                throw new BreeException("Create " + tablesFile + " failed.");
            }
        }

        Connection connection = null;
        try {
            connection = DBConnectionFactory.getConnection();
            gen.setDbType(connection.getMetaData().getDatabaseProductName());
            load(gen, connection, tablesFile);

        } finally {
            if (connection != null) {
                connection.close();
            }
        }

        return gen;
    }

    /**
     * Load.
     *
     * @param gen the gen
     * @param connection the connection
     * @throws SQLException the sql exception
     */
    public abstract void load(Gen gen, Connection connection, File tablesFile) throws Exception;

    /**
     * File 2 db name string.
     *
     * @param tableFile the table file
     * @return the string
     */
    protected String file2DbName(File tableFile) {
        return tableFile.getName().substring(0, tableFile.getName().length() - 4).toUpperCase();
    }
}
