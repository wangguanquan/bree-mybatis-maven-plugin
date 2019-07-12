package org.ttzero.plugin.bree.mybatis;

import org.ttzero.plugin.bree.mybatis.utils.CmdUtil;
import org.ttzero.plugin.bree.mybatis.utils.ConfigUtil;
import org.mockito.Mockito;

/**
 * Created by guanquan.wang at 2019-05-24 23:35
 */
public class BreeMojoTest extends BaseTest {

    public void testExecute() throws Exception {
        BreeMojo mojo = new BreeMojo(outputDirectory, templateDirectory, config, true);
        CmdUtil cmdUtil = Mockito.mock(CmdUtil.class);
        Mockito.when(cmdUtil.consoleInput()).thenReturn(ConfigUtil.cmd);
        mojo.setCmdUtil(cmdUtil);
        mojo.execute();
    }
}