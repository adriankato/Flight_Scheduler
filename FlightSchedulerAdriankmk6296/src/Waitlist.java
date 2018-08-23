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
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;

public class Waitlist {

    private static Connection connection;
    private static PreparedStatement insertWaitlist;
    private static PreparedStatement statusByDay;
    private static PreparedStatement statusByFlight;
    private static PreparedStatement deleteWaitlistFlight;
    private static PreparedStatement deleteWaitlistCustomerDay;
    private static PreparedStatement retrieveWaitlistByDay;
    private static PreparedStatement retrieveWaitlistByFlight;

    public Waitlist() {
        try {
            connection = DBConnection.getConnection();
            insertWaitlist = connection.prepareStatement(
                    "insert into waitlist (Customer, Flight, Day, Timestamp) values(?,?,?,?)"
            );
            statusByDay = connection.prepareStatement(
                    "SELECT * FROM waitlist WHERE day = ? ORDER BY Timestamp ASC"
            );
            statusByFlight = connection.prepareStatement(
                    "SELECT * FROM waitlist WHERE flight = ? ORDER BY Timestamp ASC"
            );

            deleteWaitlistFlight = connection.prepareStatement(
                    "DELETE FROM waitlist WHERE customer = ? AND flight = ?"
            );
            deleteWaitlistCustomerDay = connection.prepareStatement(
                    "DELETE FROM waitlist WHERE customer = ? AND day = ?"
            );
            retrieveWaitlistByDay = connection.prepareStatement(
                    "SELECT * FROM waitlist WHERE day = ? ORDER BY Timestamp ASC FETCH FIRST 1 ROW ONLY"
            );
            retrieveWaitlistByFlight = connection.prepareStatement(
                    "SELECT * FROM waitlist WHERE flight = ? ORDER BY Timestamp ASC FETCH FIRST 1 ROW ONLY"
            );

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public String statusByDay(Date Day) {
        ResultSet rs = null;
        String bookingsRow = "";
        String flightStatus = "Name" + "\t" + "Flight" + "\t\t" + "Timestamp" + "\n";

        try {
            statusByDay.setDate(1, Day);
            rs = statusByDay.executeQuery();
            while (rs.next()) {
                bookingsRow = rs.getObject(1).toString()
                        + "\t" + rs.getObject(2).toString()
                        + "\t\t" + rs.getObject(4).toString() + "\n";
                flightStatus += bookingsRow;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                close();
            }
        }

        return flightStatus;
    }

    public String statusByFlight(String Flight) {
        ResultSet rs = null;
        String bookingsRow = "";
        String flightStatus = "Name" + "\t" + "Flight" + "\t\t" + "Timestamp" + "\n";

        try {
            statusByFlight.setString(1, Flight);
            rs = statusByFlight.executeQuery();
            while (rs.next()) {
                bookingsRow = rs.getObject(1).toString()
                        + "\t" + rs.getObject(2).toString()
                        + "\t\t" + rs.getObject(4).toString() + "\n";
                flightStatus += bookingsRow;
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                rs.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                close();
            }
        }

        return flightStatus;
    }

    public int addWaitlist(BookingEntry bookingEntry, Timestamp Timestamp) {

        int result = 0;

        try {
            insertWaitlist.setString(1, bookingEntry.getCustomer());
            insertWaitlist.setString(2, bookingEntry.getFlight());
            insertWaitlist.setDate(3, bookingEntry.getDay());
            insertWaitlist.setTimestamp(4, Timestamp);

            result = insertWaitlist.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            close();
        }
        return result;
    }

    public int cancelWaitlistFlight(BookingEntry bookingEntry) {
        int result = 0;

        try {
            deleteWaitlistFlight.setString(1, bookingEntry.getCustomer());
            deleteWaitlistFlight.setString(2, bookingEntry.getFlight());
            result = deleteWaitlistFlight.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            close();
        }
        return result;
    }

    public int cancelWaitlistCustomerDay(BookingEntry bookingEntry) {
        int result = 0;
        try {
            deleteWaitlistCustomerDay.setString(1, bookingEntry.getCustomer());
            deleteWaitlistCustomerDay.setDate(2, bookingEntry.getDay());
            result = deleteWaitlistCustomerDay.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            close();
        }
        return result;
    }

    public BookingEntry getWaitlist(Date Day) {
        ResultSet rs = null;
        BookingEntry bookingEntry = null;
        try {
            retrieveWaitlistByDay.setDate(1, Day);
            rs = retrieveWaitlistByDay.executeQuery();
            rs.next();
            bookingEntry = new BookingEntry(rs.getObject(1).toString(),
                    rs.getObject(2).toString(), (Date) rs.getObject(3), (Timestamp) rs.getObject(4));
        } catch (SQLException ex) {
            ex.printStackTrace();
            close();
        }
        return bookingEntry;
    }

    public BookingEntry getWaitlistbyFlight(String Flight) {
        ResultSet rs = null;
        BookingEntry bookingEntry = null;
        try {
            retrieveWaitlistByFlight.setString(1, Flight);
            rs = retrieveWaitlistByFlight.executeQuery();
            rs.next();
            bookingEntry = new BookingEntry(rs.getObject(1).toString(),
                    rs.getObject(2).toString(), (Date) rs.getObject(3), (Timestamp) rs.getObject(4));
        } catch (SQLException ex) {
            ex.printStackTrace();
            close();
        }
        return bookingEntry;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
