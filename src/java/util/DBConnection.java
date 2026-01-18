/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    private static final String URL
            = "jdbc:derby://localhost:1527/SABSdb";
    private static final String USER = "app";
    private static final String PASSWORD = "app";

    public static Connection getConnection() {
        try {
            // Load Derby driver
            Class.forName("org.apache.derby.jdbc.ClientDriver");

            // Return connection
            return DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
