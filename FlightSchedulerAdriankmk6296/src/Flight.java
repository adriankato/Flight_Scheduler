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

public class Flight {

    private static Connection connection;
    private static PreparedStatement getFlightNames;
    private static PreparedStatement getFlightSeats;
    private static PreparedStatement insertNewFlight;
    private static PreparedStatement deleteFlight;
    private static PreparedStatement checkSeatCount;

    private static ArrayList<String> flightNames;

    private static int flightSeats;

    public static ArrayList<String> getFlightNames() {

        ResultSet rs = null;
        try {
            connection = DBConnection.getConnection();
            flightNames = new ArrayList();
            getFlightNames = connection.prepareStatement("select name from flight");
            rs = getFlightNames.executeQuery();
            while (rs.next()) {
                flightNames.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return flightNames;
    }

    public static int getFlightSeats(String flight, Date day) {

        ResultSet rs = null;
        try {
            connection = DBConnection.getConnection();
            getFlightSeats = connection.prepareStatement("select count(flight) from bookings where flight = ? and day = ?");
            getFlightSeats.setString(1, flight);
            getFlightSeats.setDate(2, day);
            rs = getFlightSeats.executeQuery();
            rs.next();
            flightSeats = rs.getInt(1);
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return flightSeats;
    }

    public static int addFlight(String flight, int seats) {
        int result = 0;
        try {
            connection = DBConnection.getConnection();
            insertNewFlight = connection.prepareStatement("insert into flight (name, seats) values (?,?)");
            insertNewFlight.setString(1, flight);
            insertNewFlight.setInt(2, seats);
            result = insertNewFlight.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return result;
    }

    public static int getMaxSeatCount(String flight) {
        ResultSet rs = null;
        int maxSeats = 0;
        try {
            connection = DBConnection.getConnection();
            checkSeatCount = connection.prepareStatement("SELECT seats from flight WHERE Name = ?");
            checkSeatCount.setString(1, flight);
            rs = checkSeatCount.executeQuery();
            rs.next();
            maxSeats = rs.getInt(1);

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return maxSeats;
    }

    public static int cancelFlight(String flight) {
        int result = 0;
        try {
            connection = DBConnection.getConnection();
            deleteFlight = connection.prepareStatement("DELETE from FLIGHT where Name = ?");
            deleteFlight.setString(1, flight);
            result = deleteFlight.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return result;
    }

}
