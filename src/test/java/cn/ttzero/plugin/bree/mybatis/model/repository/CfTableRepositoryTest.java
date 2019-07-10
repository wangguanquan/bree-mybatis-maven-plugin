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

//import cn.ttzero.plugin.bree.mybatis.model.config.OperationMethod;
//import org.dom4j.Element;
//import org.junit.Test;
//
//import static cn.ttzero.plugin.bree.mybatis.model.repository.CfTableRepository.stringToXml;
//import static cn.ttzero.plugin.bree.mybatis.model.repository.CfTableRepository.testMethod;
//import static cn.ttzero.plugin.bree.mybatis.utils.ConfigUtil.getAttr;

/**
 * Create by guanquan.wang at 2019-06-03 22:12
 */
public class CfTableRepositoryTest {
//    @Test public void testStringToXml() {
//        String xmlString = "<operation name=\"getByPrimary\" multiplicity=\"one\" remark=\"根据主键获取数据:SDM_REWRITE\">\n" +
//            "        SELECT * FROM SDM_REWRITE\n" +
//            "        <where>\n" +
//            "            ID = #{id, jdbcType=INTEGER}\n" +
//            "        </where>\n" +
//            "    </operation>";
//
//        Element element = stringToXml(xmlString);
//        assert element.getName().equals("operation");
//        assert getAttr(element, "name").equals("getByPrimary");
//
//        System.out.println(element.asXML());
//    }
//
//    @Test public void testGetMethod() {
//        String xmlString = "<operation name=\"getByPrimary\" multiplicity=\"one\" remark=\"根据主键获取数据:SDM_REWRITE\">\n" +
//            "        SELECT * FROM SDM_REWRITE\n" +
//            "        <where>\n" +
//            "            ID = #{id, jdbcType=INTEGER}\n" +
//            "        </where>\n" +
//            "    </operation>";
//
//        Element element = stringToXml(xmlString);
//
//        OperationMethod method = testMethod(element);
//        assert method == OperationMethod.select;
//
//
//        String insertSQL = "<operation name=\"\" paramType=\"object\" remark=\"插入表:SDM_REWRITE\">\n" +
//            "        <selectKey resultType=\"java.lang.Long\" keyProperty=\"id\" order=\"AFTER\">\n" +
//            "            SELECT LAST_INSERT_ID()\n" +
//            "        </selectKey>\n" +
//            "        INSERT INTO SDM_REWRITE(\n" +
//            "            \n" +
//            "            KEYWORD\n" +
//            "            , REWORD\n" +
//            "            , TYPE\n" +
//            "            , SYNC_STATUS\n" +
//            "            , COUNT\n" +
//            "            , STATUS\n" +
//            "            , CREATE_BY\n" +
//            "            , CREATE_BY_NAME\n" +
//            "            , CREATE_AT\n" +
//            "            , UPDATE_BY\n" +
//            "            , UPDATE_BY_NAME\n" +
//            "            , UPDATE_AT\n" +
//            "        ) VALUES (\n" +
//            "            \n" +
//            "            #{keyword, jdbcType=VARCHAR}\n" +
//            "            , #{reword, jdbcType=VARCHAR}\n" +
//            "            , #{type, jdbcType=TINYINT}\n" +
//            "            , #{syncStatus, jdbcType=TINYINT}\n" +
//            "            , #{count, jdbcType=INTEGER}\n" +
//            "            , #{status, jdbcType=TINYINT}\n" +
//            "            , #{createBy, jdbcType=BIGINT}\n" +
//            "            , #{createByName, jdbcType=VARCHAR}\n" +
//            "            , #{createAt, jdbcType=TIMESTAMP}\n" +
//            "            , #{updateBy, jdbcType=BIGINT}\n" +
//            "            , #{updateByName, jdbcType=VARCHAR}\n" +
//            "            , #{updateAt, jdbcType=TIMESTAMP}\n" +
//            "        )\n" +
//            "    </operation>";
//        element = stringToXml(insertSQL);
//        method = testMethod(element);
//        assert method == OperationMethod.insert;
//
//        String deleteSQL = "<operation name=\"deleteByPrimary\" multiplicity=\"one\" remark=\"根据主键删除数据:SDM_REWRITE\">\n" +
//            "        DELETE FROM SDM_REWRITE\n" +
//            "        <where>\n" +
//            "            ID = #{id, jdbcType=INTEGER}\n" +
//            "        </where>" +
//            "    </operation>";
//        element = stringToXml(deleteSQL);
//        method = testMethod(element);
//        assert method == OperationMethod.delete;
//
//        String updateSQL = "<operation>update t set a -1 where select a  from t</operation>";
//        element = stringToXml(updateSQL);
//        method = testMethod(element);
//        assert method == OperationMethod.update;
//    }
}
