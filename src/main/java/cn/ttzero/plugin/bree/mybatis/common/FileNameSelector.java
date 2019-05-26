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

package cn.ttzero.plugin.bree.mybatis.common;

import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * Created by guanquan.wang at 2019-05-24 21:50
 */
public class FileNameSelector implements FilenameFilter {
    private String extension;

    public FileNameSelector(String fileExtension) {
        if (StringUtils.startsWith(fileExtension, ".")) {
            extension += fileExtension;
        } else {
            extension = fileExtension;
        }
    }

    /**
     * Tests if a specified file should be included in a file list.
     *
     * @param dir the directory in which the file was found.
     * @param name the name of the file.
     * @return <code>true</code> if and only if the name should be included in
     *         the file list; <code>false</code> otherwise.
     */
    @Override
    public boolean accept(File dir, String name) {
        return name.endsWith(extension);
    }
}
