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

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

/**
 * Desc 控制台输入
 * Created by guanquan.wang at 2019-05-24 09:00
 */
public class CmdUtil {

    /**
     * 获取控制台输入
     *
     * @return 控制台命令
     */
    public String consoleInput() {

        Scanner cmdIn = new Scanner(System.in, StandardCharsets.UTF_8.name());
        // 只有一个DB时
        if (ConfigUtil.config.getDataSourceMap().size() == 1) {
            ConfigUtil.setCmd(chooseTableCmd(cmdIn));
            return ConfigUtil.cmd;
        }

        System.out.println("==============选择需要从哪个库中生成===============");
        int i = 0;
        for (String dbStr : ConfigUtil.config.getDataSourceMap().keySet()) {
            i++;
            System.out.println(i + " : " + dbStr);
        }
        System.out.println("==============选择需要从哪个库中生成===============");

        int dbInt = cmdIn.nextInt();
        if (dbInt > i && dbInt < 1) {

            System.out.println("输入有误,自动退出[后续改进...]");
            return "q";
        } else {
            i = 0;
            for (String dbStr : ConfigUtil.config.getDataSourceMap().keySet()) {
                i++;
                if (i == dbInt) {
                    ConfigUtil.setCurrentDb(dbStr);
                }
            }
        }

        ConfigUtil.setCmd(chooseTableCmd(cmdIn));
        return ConfigUtil.cmd;
    }

    private String chooseTableCmd(Scanner cmdIn) {
        System.out.println("输入需要生成的表:");
        System.out.println("===========输入需要生成的表==============");
        System.out.println("-- * 标示所有 (暂未开放)");
        System.out.println("-- 多表用逗号分隔");
        System.out.println("-- 新表会先生成默认配置,已有表不会影响修改后的SQL");
        System.out.println("-- q 退出");
        System.out.println("===========输入需要生成的表==============");
        String _cmd = cmdIn.next();
        if (StringUtils.isBlank(_cmd)) {
            System.out.println("空输入默认为 * ");
            _cmd = "*";
        }
        return _cmd;
    }

}
