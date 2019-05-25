package cn.ttzero.plugin.bree.mybatis;

import java.io.File;
import java.io.IOException;

import cn.ttzero.plugin.bree.mybatis.utils.CmdUtil;
import cn.ttzero.plugin.bree.mybatis.utils.ConfigUtil;
import junit.framework.TestCase;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

import org.dom4j.DocumentException;

/**
 * Created by guanquan.wang at 2019-05-24 11:11
 */
public class BaseTest extends TestCase {
    /**
     * The constant LOG.
     */
    private static final Log LOG = new SystemStreamLog();

    /**
     * The constant BASE_PATH.
     */
    public static final String BASE_PATH = CmdUtil.class
        .getResource("")
        .getPath()
        .replace(CmdUtil.class.getPackage().getName().replace(".", "/") + "/", "");

    /**
     * The constant outputDirectory.
     */
    public static File outputDirectory = new File(BASE_PATH + "out/");

    /**
     * The constant templateDirectory.
     */
    public static File templateDirectory = new File(BASE_PATH + "bree/templates/");

    /**
     * The constant config.
     */
    public static File config = new File(BASE_PATH + "bree/config/config.xml");

    static {

        try {
            ConfigUtil.readConfig(config);
            ConfigUtil.setCmd("sdm_rewrite");
            //ConfigUtil.setCmd("dc_per_account_dept_role");
            // ConfigUtil.setCmd("fp_oac_ast_attr_000");
            // ConfigUtil.setCmd("dc_hello");
            //ConfigUtil.setCmd("*");
        } catch (IOException | DocumentException e) {
            LOG.error(e);
        }
    }

}
