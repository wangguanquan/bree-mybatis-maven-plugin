package org.ttzero.plugin.bree.mybatis;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.junit.Test;
import org.ttzero.plugin.bree.mybatis.utils.ConfigUtil;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Random;

/**
 * Created by guanquan.wang at 2019-05-24 23:35
 */
public class BreeMojoTest {

    /**
     * The default output path
     */
    private static Path defaultTestPath = Paths.get("target/out/");

    /**
     * The root path of test resources
     */
    private static Path resourcePath;

    static Random random = new Random();
    static char[] charArray = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".toCharArray();
    private static char[] cache = new char[32];

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
        if (resourcePath != null) return resourcePath;
        URL url = BreeMojoTest.class.getClassLoader().getResource(".");
        if (url == null) {
            throw new RuntimeException("Load test resources error.");
        }
        return resourcePath = isWindows()
            ? Paths.get(url.getFile().substring(1))
            : Paths.get(url.getFile());
    }

    /**
     * Returns path of config
     *
     * @return the config path
     */
    public static Path getConfigPath() {
        return testResourceRoot().resolve("bree/config/mysql/config.xml");
    }

    /**
     * Test current OS system is windows family
     *
     * @return true if OS is windows family
     */
    public static boolean isWindows() {
        return System.getProperty("os.name").toUpperCase().startsWith("WINDOWS");
    }

    public static String getRandomString() {
        int n = random.nextInt(cache.length) + 1, size = charArray.length;
        for (int i = 0; i < n; i++) {
            cache[i] = charArray[random.nextInt(size)];
        }
        return new String(cache, 0, n);
    }

    @Test
    public void testExecute() throws IOException, MojoFailureException, MojoExecutionException {
        Path resourceRoot = testResourceRoot();

        BreeMojo mojo = new BreeMojo(getOutputTestPath().toFile()
            , resourceRoot.resolve("bree/templates/").toFile(), getConfigPath().toFile(), true);

        ConfigUtil.setCmd("sdm_rewrite");
        // FIXME Modify a exists table name before test.
        //  then delete '//'
//        mojo.execute();
    }

}