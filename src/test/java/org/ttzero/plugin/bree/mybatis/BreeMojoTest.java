package org.ttzero.plugin.bree.mybatis;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Test;
import org.ttzero.plugin.bree.mybatis.utils.ConfigUtil;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;

/**
 * Created by guanquan.wang at 2019-05-24 23:35
 */
public class BreeMojoTest {

    /**
     * The default output path
     */
    private static Path defaultTestPath = Paths.get("target/out/");

    /**
     * Returns the path of test dest path
     *
     * @return path of test dest
     * @throws IOException if I/O error occur
     */
    public static Path getOutputTestPath() throws IOException {
        if (!Files.exists(defaultTestPath)) {
            mkdir(defaultTestPath);
        }
        return defaultTestPath;
    }

    /**
     * Create a directory
     *
     * @param destPath the destination directory path
     * @return the temp directory path
     * @throws IOException if I/O error occur
     */
    public static Path mkdir(Path destPath) throws IOException {
        Path path;
        if (isWindows()) {
            path = Files.createDirectories(destPath);
        } else {
            path = Files.createDirectories(destPath
                , PosixFilePermissions.asFileAttribute(PosixFilePermissions.fromString("rwxr-x---")));
        }
        return path;
    }

    /**
     * Returns the path of 'test/resources'
     *
     * @return the test/resources root path
     */
    public static Path testResourceRoot() {
        URL url = BreeMojoTest.class.getClassLoader().getResource(".");
        if (url == null) {
            throw new RuntimeException("Load test resources error.");
        }
        return isWindows()
            ? Paths.get(url.getFile().substring(1))
            : Paths.get(url.getFile());
    }



    /**
     * Test current OS system is windows family
     *
     * @return true if OS is windows family
     */
    public static boolean isWindows() {
        return System.getProperty("os.name").toUpperCase().startsWith("WINDOWS");
    }

    @Test
    public void testExecute() throws IOException, MojoFailureException, MojoExecutionException {
        Path resourceRoot = testResourceRoot();

        File config = resourceRoot.resolve("bree/config/config.xml").toFile();
        BreeMojo mojo = new BreeMojo(getOutputTestPath().toFile()
            , resourceRoot.resolve("bree/templates/").toFile(), config, true);

        ConfigUtil.setCmd("sdm_rewrite");
        mojo.execute();
    }

}