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

package org.ttzero.plugin.bree.mybatis.model.repository;

/**
 * Create by guanquan.wang at 2019-05-24 17:30
 */
public class VoConfig extends JavaConfig {
    private boolean useBasePageVo = true;

    public static VoConfig of(JavaConfig config) {
        if (config.getClass() == VoConfig.class) {
            return VoConfig.class.cast(config);
        }
        VoConfig _config = new VoConfig();
        _config.setNamespace(config.getNamespace());
        _config.setPrefix(config.getPrefix());
        _config.setSuffix(config.getSuffix());
        _config.setExtend(config.getExtend());
        for (JavaProperty jp : config.getAnnotationArray()) {
            _config.addAnnotation(jp);
        }
        for (JavaProperty jp : config.getImplementArray()) {
            _config.addImplement(jp);
        }
        return _config;
    }

    public boolean isUseBasePageVo() {
        return useBasePageVo;
    }

    public void setUseBasePageVo(boolean useBasePageVo) {
        this.useBasePageVo = useBasePageVo;
    }
}
