package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static final String URL = "jdbc:derby:db/SABSDB;create=true"; // db folder under project root
    private static final String USER = ""; // embedded Derby doesn't need username/password
    private static final String PASS = "";

    static {
        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver"); // load embedded driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}
