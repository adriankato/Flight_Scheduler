/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author adriankato
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static Connection connection;
    private static final String URL = "jdbc:derby://localhost:1527/FlightSchedulerDBAdriankmk6296";
    private static final String username = "java";
    private static final String password = "java";

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, username, password);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return connection;

    }

}
