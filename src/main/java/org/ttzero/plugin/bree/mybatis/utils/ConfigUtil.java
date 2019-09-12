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

import java.io.File;
import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import org.ttzero.plugin.bree.mybatis.model.dbtable.Database;
import org.ttzero.plugin.bree.mybatis.model.repository.DataObjectConfig;
import org.ttzero.plugin.bree.mybatis.model.repository.JavaConfig;
import org.ttzero.plugin.bree.mybatis.model.repository.JavaProperty;
import org.ttzero.plugin.bree.mybatis.BreeException;
import org.ttzero.plugin.bree.mybatis.model.repository.Reserved;
import org.ttzero.plugin.bree.mybatis.model.repository.VoConfig;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;


import org.ttzero.plugin.bree.mybatis.model.Config;

/**
 * Created by guanquan.wang at 2019-05-24 11:11
 */
public class ConfigUtil {
    /**
     * The constant config.
     */
    public static Config config = null;

    /**
     * The constant currentDb.
     */
    private static String currentDb = null;

    /**
     * The constant cmd.
     */
    public static String cmd = null;

    public static String breePath;

    /**
     * Reserved config
     */
    public static Reserved reserved;

    /**
     * Read config config.
     *
     * @param configFile the config file
     * @throws IOException the io exception
     */
    @SuppressWarnings({"unchecked", "retype"})
    public static void readConfig(File configFile) throws IOException, DocumentException {
        if (config == null || !config.getConfigPath().equals(configFile)) {
            SAXReader reader = new SAXReader();
            Document document = reader.read(Files.newInputStream(
                Paths.get(URLDecoder.decode(configFile.toPath().toString(), StandardCharsets.UTF_8.name()))));
            config = new Config();
            config.setConfigPath(configFile);

            Element root = document.getRootElement();
            // type-maps
            Element typeMaps = root.element("type-maps");
            if (typeMaps != null) {
                List<Element> typeMapList = typeMaps.elements("type-map");
                for (Element e : typeMapList) {
                    config.addTypeMap(getAttr(e, "from"), getAttr(e, "to"));
                }
            }

            String cdo = getValue(root.element("create-default-operation"));
            boolean bcdo = "false".equalsIgnoreCase(cdo) || "0".equals(cdo);
            config.setCreateDefaultOperation(!bcdo);

            // database
            databaseConfig(root);

            // DO config
            dataObjectConfig(root);

            // xml-Mapper
            xmlMapperConfig(root);

            // do-mapper
            doMapperConfig(root);

            // vo
            voConfig(root);

            // dao
            daoConfig(root);

            // service
            serviceConfig(root);

            // controller
            controllerConfig(root);
        }
    }

    /**
     * Parse database config
     *
     * @param root the root element
     */
    @SuppressWarnings({"unchecked", "retype"})
    private static void databaseConfig(Element root) {
        Element element;
        String defaultPackage;
        if ((element = root.element("package")) != null) {
            defaultPackage = getValue(element);
        } else {
            throw new BreeException("config.xml文件中必须配置package参数.");
        }
        List<Element> databases = root.elements("database");
        if (databases.isEmpty()) {
            throw new BreeException("config.xml文件中至少配置一个数据源.");
        }
        for (Element e : databases) {
            Database dataBase = new Database();
            dataBase.setName(getAttr(e, "name"));
            dataBase.setDriverClass(getAttr(e, "class"));
            dataBase.setType(getAttr(e, "type"));
            List<Element> properties = e.elements("property");
            for (Element property : properties) {
                dataBase.addProperty(getAttr(property, "name"), getValue(property));
            }
            String genPackage = StringUtils.replace(defaultPackage, "${database.name}",
                dataBase.getName());
            dataBase.setGenPackage(genPackage);
            dataBase.setGenPackagePath(StringUtils.replace(genPackage, ".", "/"));
            String genCommonPackage = StringUtils.replace(defaultPackage, "${database.name}",
                "common");
            dataBase.setGenDalCommonPackage(genCommonPackage);
            dataBase.setGenDalCommonPackagePath(StringUtils.replace(genCommonPackage, ".", "/"));

            List<Element> prefixs = e.elements("prefix");
            for (Element prefix : prefixs) {
                dataBase.addTablePrefix(getValue(prefix));
            }

            List<Element> splitSuffixs = e.elements("split-suffix");
            for (Element splitSuffix : splitSuffixs) {
                dataBase.addSplitSuffix(getValue(splitSuffix));
            }

            if (StringUtil.isEmpty(dataBase.getType())) {
                dataBase.setType(getType(dataBase.getPropertyMapVal("url")));
            }
            config.addDataSource(dataBase);
        }
    }

    /**
     * Parse data-object config
     *
     * @param root the root element
     */
    private static void dataObjectConfig(Element root) {
        Element element = root.element("data-object");
        if (element != null) {
            DataObjectConfig doConfig = DataObjectConfig.of(parseJavaConfig(element));
            config.setDoConfig(doConfig);

            element = element.element("ignore");
            if (element != null) {
                @SuppressWarnings({"unchecked", "retype"})
                List<Element> ignoreFields = element.elements();
                for (Element e : ignoreFields) {
                    doConfig.addIgnoreField(getAttr(e, "name"));
                }
            }
        } else config.setDoConfig(new DataObjectConfig());
    }

    /**
     * Parse xml-mapper config
     *
     * @param root the root element
     */
    private static void xmlMapperConfig(Element root) {
        Element element = root.element("xml-mapper");
        if (element != null) {
            config.setXmlMapperPath(getAttr(element, "output"));
        }
    }

    /**
     * Parse do-mapper config
     *
     * @param root the root element
     */
    private static void doMapperConfig(Element root) {
        Element element = root.element("do-mapper");
        if (!isIgnore(element)) {
            config.setDoMapperConfig(parseJavaConfig(element));
        } else config.setDoMapperConfig(new JavaConfig());
    }

    /**
     * Parse vo config
     *
     * @param root the root element
     */
    private static void voConfig(Element root) {
        Element element = root.element("vo");
        if (!isIgnore(element)) {
            VoConfig voConfig = VoConfig.of(parseJavaConfig(element));
            voConfig.setUseBasePageVo("true".equalsIgnoreCase(getValue(element.element("useBasePageVo"))));
            config.setVoConfig(voConfig);
        } else config.setVoConfig(new VoConfig());
    }

    /**
     * Parse dao config
     *
     * @param root the root element
     */
    private static void daoConfig(Element root) {
        Element element = root.element("dao");
        if (isIgnore(element)) {
            config.ignoreDao();
        } else {
            JavaConfig daoConfig = parseJavaConfig(element);
            config.setDaoConfig(daoConfig);
            Element impl = element.element("impl");
            if (!isIgnore(impl)) {
                daoConfig.setImpl(parseJavaConfig(impl));
            }
        }
    }

    /**
     * Parse service config
     *
     * @param root the root element
     */
    private static void serviceConfig(Element root) {
        Element element = root.element("service");
        if (isIgnore(element)) {
            config.ignoreService();
        } else {
            JavaConfig serviceConfig = parseJavaConfig(element);
            config.setServiceConfig(serviceConfig);
            Element impl = element.element("impl");
            if (!isIgnore(impl)) {
                serviceConfig.setImpl(parseJavaConfig(impl));
            }
        }
    }

    /**
     * Parse controller config
     *
     * @param root the root element
     */
    private static void controllerConfig(Element root) {
        Element element = root.element("controller");
        if (isIgnore(element)) {
            config.ignoreController();
        } else {
            config.setControllerConfig(parseJavaConfig(element));
        }
    }

    /**
     * Parse the general java config
     *
     * @param e current element
     * @return a {@link JavaConfig}
     */
    private static JavaConfig parseJavaConfig(Element e) {
        JavaConfig javaConfig = new JavaConfig();
        Element element;
        // namespace
        element = e.element("namespace");
        javaConfig.setNamespace(getValue(element));
        // prefix
        element = e.element("prefix");
        javaConfig.setPrefix(getValue(element));
        // suffix
        element = e.element("suffix");
        javaConfig.setSuffix(getValue(element));
        // extend
        element = e.element("extend");
        JavaProperty property;
        if (element != null) {
            String _class = getAttr(element, "class");
            if (StringUtil.isNotEmpty(_class)) {
                property = new JavaProperty(_class, getAttr(element, "import"));
                addIfTest(property, getAttr(element, "if"));
                javaConfig.setExtend(property);

            }
        }
        // implements
        element = e.element("implements");
        if (element != null) {
            @SuppressWarnings({"unchecked", "retype"})
            List<Element> _implements = element.elements();
            for (Element implement : _implements) {
                String _class = getAttr(implement, "class");
                if (StringUtil.isNotEmpty(_class)) {
                    property = new JavaProperty(_class, getAttr(implement, "import"));
                    addIfTest(property, getAttr(implement, "if"));
                    javaConfig.addImplement(property);
                }
            }
        }
        // annotations
        element = e.element("annotations");
        if (element != null) {
            @SuppressWarnings({"unchecked", "retype"})
            List<Element> annotations = element.elements();
            for (Element annotation : annotations) {
                String _class = getAttr(annotation, "class");
                if (StringUtil.isNotEmpty(_class)) {
                    property = new JavaProperty(_class, getAttr(annotation, "import"));
                    addIfTest(property, getAttr(annotation, "if"));
                    javaConfig.addAnnotation(property);
                }
            }
        }
        return javaConfig;
    }

    private static void addIfTest(JavaProperty property, String _if) {
        if (StringUtil.isNotEmpty(_if)) {
            String[] ifs = _if.toLowerCase().split("and");
            for (String s : ifs) {
                property.addCondition(s);
            }
        }
    }

    /**
     * The the config will be ignore
     *
     * @param element current element
     * @return true if ignore config is "true"
     */
    private static boolean isIgnore(Element element) {
        return element == null || "true".equals(getAttr(element, "ignore"));
    }

    /**
     * Returns the "value" attribute value from Element
     *
     * @param element current element
     * @return the attr value
     */
    public static String getValue(Element element) {
        return getAttr(element, "value");
    }

    /**
     * Returns the attribute value from Element
     *
     * @param element current element
     * @param attr the attr name
     * @return the attr value
     */
    public static String getAttr(Element element, String attr) {
        return element != null ? element.attributeValue(attr) : null;
    }

    /**
     * Sets current db.
     *
     * @param currentDb the current db
     */
    public static void setCurrentDb(String currentDb) {
        ConfigUtil.currentDb = currentDb;
    }

    /**
     * Sets cmd.
     *
     * @param cmd the cmd
     */
    public static void setCmd(String cmd) {
        ConfigUtil.cmd = cmd;
    }

    /**
     * Gets current db.
     *
     * @return the current db
     */
    public static Database getCurrentDb() {
        if (config.getDataSourceMap().size() == 1) {
            return config.getDataSourceMap().values().iterator().next();
        }
        return config.getDataSourceMap().get(currentDb);
    }

    /**
     * database type: mysql oracle sqlite
     * @return the database type name
     */
    public static String getType(String url) {
        if (url != null) {
            int index = url.indexOf(':'), next = index > 0 ? url.indexOf(':', ++index) : -1;
            if (next < 0) return null;
            String type = url.substring(index, next);
            // special case type
            if ("microsoft".equals(type)) {
                index = ++next;
                next = url.indexOf(':', index);
                if (next > index) {
                    type = url.substring(next, index);
                }
            }
            return type;
        }
        return null;
    }
}
