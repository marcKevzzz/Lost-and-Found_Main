/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author QCU
 */
public class DataBase {

    private static final String URL = "jdbc:mysql://localhost:3306/lostandfound_db";
    private static final String USER = "root";
    private static final String PASS = "";

    // Logger for logging errors
    private static final Logger LOGGER = Logger.getLogger(DataBase.class.getName());

    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(URL, USER, PASS);
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "Database driver class not found", e);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database connection failed", e);
        }
        return conn;
    }

    // Utility method to close the connection safely
    public static void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                LOGGER.log(Level.WARNING, "Failed to close the database connection", e);
            }
        }
    }
}

