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
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Day {

    private static Connection connection;
    private static PreparedStatement getDays;
    private static PreparedStatement insertNewDay;
    private static ArrayList<Date> days;

    public static ArrayList<Date> getDay() {

        ResultSet rs = null;
        try {
            connection = DBConnection.getConnection();
            days = new ArrayList();
            getDays = connection.prepareStatement("select date from day");
            rs = getDays.executeQuery();
            while (rs.next()) {
                days.add(rs.getDate(1));
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return days;
    }

    public static int addDay(Date day) {
        int result = 0;

        try {
            connection = DBConnection.getConnection();
            insertNewDay = connection.prepareStatement("insert into day (date) values (?)");
            insertNewDay.setDate(1, day);
            result = insertNewDay.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result;
    }
}
