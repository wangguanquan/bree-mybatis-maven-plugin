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

package org.ttzero.plugin.bree.mybatis.model;

import com.google.common.collect.Maps;
import org.ttzero.plugin.bree.mybatis.model.dbtable.Database;
import org.ttzero.plugin.bree.mybatis.model.repository.DataObjectConfig;
import org.ttzero.plugin.bree.mybatis.model.repository.JavaConfig;
import org.ttzero.plugin.bree.mybatis.model.repository.VoConfig;

import java.io.File;
import java.util.Map;

/**
 * 数据源相关配置
 * Created by guanquan.wang at 2019-05-24 11:11
 */
public class Config {
    /**
     * The Data source map.
     */
    private Map<String, Database> dataSourceMap = Maps.newHashMap();
    /**
     * The Type map.
     */
    private Map<String, String> typeMap = Maps.newHashMap();

    /**
     * The Data object map.
     */
    private DataObjectConfig doConfig;

    /**
     * The XMLMapper.
     */
    private String xmlMapperPath;

    /**
     * Ignore the dao config
     */
    private boolean ignoreDao;

    /**
     * Ignore the service config
     */
    private boolean ignoreService;

    /**
     * Ignore the controller config
     */
    private boolean ignoreController;

    /**
     * The do-mapper config
     */
    private JavaConfig doMapperConfig;

    /**
     * The vo config
     */
    private VoConfig voConfig;

    /**
     * The dao config
     */
    private JavaConfig daoConfig;

    /**
     * The service config
     */
    private JavaConfig serviceConfig;

    /**
     * The controller config
     */
    private JavaConfig controllerConfig;

    /**
     * Create default operation
     */
    private boolean createDefaultOperation = true;

    /**
     * The config path in disk
     */
    private File configPath;

    /**
     * Add data source.
     *
     * @param dataBase the data base
     */
    public void addDataSource(Database dataBase) {
        this.dataSourceMap.put(dataBase.getName(), dataBase);
    }

    /**
     * Gets data source map.
     *
     * @return the data source map
     */
    public Map<String, Database> getDataSourceMap() {
        return dataSourceMap;
    }

    /**
     * Gets type map.
     *
     * @return the type map
     */
    public Map<String, String> getTypeMap() {
        return typeMap;
    }

    /**
     * Add type map.
     *
     * @param type the type
     * @param to   the to
     */
    public void addTypeMap(String type, String to) {
        this.typeMap.put(type, to);
    }


    /**
     * Returns the data-object config
     *
     * @return the data-object config
     */
    public DataObjectConfig getDoConfig() {
        return doConfig;
    }

    /**
     * Setting the data-object config
     *
     * @param doConfig the data-object config
     */
    public void setDoConfig(DataObjectConfig doConfig) {
        this.doConfig = doConfig;
    }

    public String getXmlMapperPath() {
        return xmlMapperPath;
    }

    public void setXmlMapperPath(String xmlMapperPath) {
        this.xmlMapperPath = xmlMapperPath;
    }

    public void ignoreDao() {
        this.ignoreDao = true;
    }

    public void ignoreService() {
        this.ignoreService = true;
    }

    public void ignoreController() {
        this.ignoreController = true;
    }

    public boolean isIgnoreDao() {
        return ignoreDao;
    }

    public boolean isIgnoreService() {
        return ignoreService;
    }

    public boolean isIgnoreController() {
        return ignoreController;
    }

    public JavaConfig getDoMapperConfig() {
        return doMapperConfig;
    }

    public void setDoMapperConfig(JavaConfig doMapperConfig) {
        this.doMapperConfig = doMapperConfig;
    }

    public JavaConfig getDaoConfig() {
        return daoConfig;
    }

    public void setDaoConfig(JavaConfig daoConfig) {
        this.daoConfig = daoConfig;
    }

    public JavaConfig getServiceConfig() {
        return serviceConfig;
    }

    public void setServiceConfig(JavaConfig serviceConfig) {
        this.serviceConfig = serviceConfig;
    }

    public JavaConfig getControllerConfig() {
        return controllerConfig;
    }

    public void setControllerConfig(JavaConfig controllerConfig) {
        this.controllerConfig = controllerConfig;
    }

    public VoConfig getVoConfig() {
        return voConfig;
    }

    public void setVoConfig(VoConfig voConfig) {
        this.voConfig = voConfig;
    }

    /**
     * Returns the createDefaultOperation
     *
     * @return true if create default operation
     */
    public boolean isCreateDefaultOperation() {
        return createDefaultOperation;
    }

    /**
     * Setting the createDefaultOperation
     *
     * @param createDefaultOperation a flag
     */
    public void setCreateDefaultOperation(boolean createDefaultOperation) {
        this.createDefaultOperation = createDefaultOperation;
    }

    /**
     * Returns the config path
     *
     * @return config path
     */
    public File getConfigPath() {
        return configPath;
    }

    /**
     * Setting the path of config
     *
     * @param configPath the path
     */
    public void setConfigPath(File configPath) {
        this.configPath = configPath;
    }
}
