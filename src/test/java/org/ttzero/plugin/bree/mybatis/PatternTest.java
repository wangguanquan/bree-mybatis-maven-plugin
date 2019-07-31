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

package org.ttzero.plugin.bree.mybatis;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create by guanquan.wang at 2019-05-24 11:54
 */
public class PatternTest {

    @Test public void testFind() {
        Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
        String string = "TableName(\"${DO.tableName}\")";
        Matcher matcher = pattern.matcher(string);
        System.out.println(matcher.find());
        System.out.println(matcher.group(1));

        string = "net.vipmro.${project.moduleName}.${database.name}";
        matcher = pattern.matcher(string);
        while (matcher.find()) {
            System.out.println(matcher.group(1));
        }
    }

    @Test public void testReplace() {
        Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
        String string = "TableName(\"${DO.tableName}\")";
        Matcher matcher = pattern.matcher(string);
        StringBuffer buffer = new StringBuffer();
        if (matcher.find()) {
            matcher.appendReplacement(buffer, value(matcher.group(1)));
        }
        matcher.appendTail(buffer);
        System.out.println(buffer);


        string = "net.vipmro.${project.moduleName}.${database.name}";
        matcher = pattern.matcher(string);
        buffer.delete(0, buffer.length());

        while (matcher.find()) {
            matcher.appendReplacement(buffer, value(matcher.group(1)));
        }
        matcher.appendTail(buffer);
        System.out.println(buffer);
    }

    @Test public void testReplaceAll() {
        String c = "/|\\\\",  p = "resources";
        String p1 = "/" + p;
        String r = p1.replaceAll(c, "");

        assert p.equals(r);

        String p2 = p + "/";
        r = p2.replaceAll(c, "");
        assert p.equals(r);

        String p3 = "/" + p + "/";
        r = p3.replaceAll(c, "");
        assert p.equals(r);
    }

    private String value(String key) {
        String value;
        switch (key) {
            case "DO.tableName": value = "sdm_rewrite"; break;
            case "project.moduleName": value = "sdm"; break;
            case "database.name": value = "sdm"; break;
            default:value = key;
        }
        return value;
    }
}
