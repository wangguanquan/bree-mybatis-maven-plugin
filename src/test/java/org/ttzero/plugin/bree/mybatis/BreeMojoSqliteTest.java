package org.ttzero.plugin.bree.mybatis;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.junit.Before;
import org.junit.Test;
import org.ttzero.plugin.bree.mybatis.utils.ConfigUtil;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static org.ttzero.plugin.bree.mybatis.BreeMojoTest.getOutputTestPath;
import static org.ttzero.plugin.bree.mybatis.BreeMojoTest.getRandomString;
import static org.ttzero.plugin.bree.mybatis.BreeMojoTest.random;
import static org.ttzero.plugin.bree.mybatis.BreeMojoTest.testResourceRoot;
import static org.ttzero.plugin.bree.mybatis.utils.ConfigUtil.getAttr;

public class BreeMojoSqliteTest {

    /**
     * Returns path of config
     *
     * @return the config path
     */
    public static Path getConfigPath() {
        return testResourceRoot().resolve("bree/config/sqlite/config.xml");
    }

    private static String url;

    static {
        System.out.println(getConfigPath().toString());
        String driver = null;
        SAXReader reader = new SAXReader();
        try {
            Document document = reader.read(Files.newInputStream(
                Paths.get(URLDecoder.decode(getConfigPath().toString(), StandardCharsets.UTF_8.name()))));

            Element root = document.getRootElement();
            Element database = root.element("database");
            if (database == null) {
                System.out.println("Miss database config.");
                System.exit(-1);
            }
            driver = getAttr(database, "class");
            assert driver != null;

            @SuppressWarnings({"unchecked", "retype"})
            List<Element> properties = database.elements();
            assert properties != null && !properties.isEmpty();

            for (Element property : properties) {
                if ("url".equalsIgnoreCase(getAttr(property, "name"))) {
                    url = getAttr(property, "value");
                    assert url != null;
                }
            }
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            System.exit(-1);
        }

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    protected Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    /**
     * Install test data
     */
    @Before
    public void init() {
        Connection con = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            con = getConnection();
            String student = "create table if not exists student(id integer primary key, name text, age integer)";
            ps = con.prepareStatement(student);
            ps.executeUpdate();
            ps.close();

            ps = con.prepareStatement("select id from student limit 1");
            rs = ps.executeQuery();
            // No data in database
            if (!rs.next()) {
                ps.close();
                con.setAutoCommit(false);
                ps = con.prepareStatement("insert into student(name, age) values (?,?)");
                int size = 10_000;
                for (int i = 0; i < size; i++) {
                    ps.setString(1, getRandomString());
                    ps.setInt(2, random.nextInt(15) + 5);
                    ps.addBatch();
                }
                ps.executeBatch();
                con.commit();
            }

            // Create teacher
            String teacher = "create table if not exists bl_teacher(id integer primary key, name text, bl integer)";
            ps = con.prepareStatement(teacher);
            ps.executeUpdate();
            ps.close();
            ps = con.prepareStatement("select id from bl_teacher limit 1");
            rs = ps.executeQuery();
            // No data in database
            if (!rs.next()) {
                ps.close();
                con.setAutoCommit(false);
                ps = con.prepareStatement("insert into bl_teacher(name, bl) values (?,?)");
                int size = 1000;
                for (int i = 0; i < size; i++) {
                    ps.setString(1, getRandomString());
                    ps.setInt(2, random.nextInt(10) >= 8 ? 1 : 0);
                    ps.addBatch();
                }
                ps.executeBatch();
                con.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
            if (con != null) {
                try {
                    con.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        // Add Database

    }

    @Test public void test() throws IOException, MojoFailureException, MojoExecutionException {

        BreeMojo mojo = new BreeMojo(getOutputTestPath().toFile()
            , testResourceRoot().resolve("bree/templates/").toFile(), getConfigPath().toFile(), true);

        ConfigUtil.setCmd("student,bl_teacher");
        mojo.execute();
    }
}
