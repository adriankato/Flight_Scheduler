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

public class BookingQueries {

    private static Connection connection;
    private static PreparedStatement insertBooking;
    private static PreparedStatement statusByFlightDay;
    private static PreparedStatement deleteBooking;
    private static PreparedStatement deleteBookingByEntry;
    private static PreparedStatement retrieveBookingByFlight;
    private static PreparedStatement statusByFlight;
    private static PreparedStatement retrieveVacantBooking;

    public BookingQueries() {
        try {
            connection = DBConnection.getConnection();
            insertBooking = connection.prepareStatement(
                    "insert into bookings (Customer, Flight, Day, Timestamp) values (?, ?, ?, ?)"
            );

            statusByFlightDay = connection.prepareStatement(
                    "SELECT * FROM bookings WHERE flight = ? AND day = ? ORDER BY Timestamp ASC"
            );
            statusByFlight = connection.prepareStatement(
                    "SELECT * FROM bookings WHERE flight = ? ORDER BY Timestamp ASC"
            );
            deleteBooking = connection.prepareStatement(
                    "DELETE FROM bookings WHERE customer = ? AND day = ?"
            );
            deleteBookingByEntry = connection.prepareStatement(
                    "DELETE FROM bookings WHERE customer = ? AND flight = ? AND day = ?"
            );
            retrieveBookingByFlight = connection.prepareStatement(
                    "SELECT * FROM bookings WHERE flight = ? ORDER BY Timestamp ASC FETCH FIRST 1 ROW ONLY"
            );
            retrieveVacantBooking = connection.prepareStatement(
                    "SELECT * FROM bookings WHERE day = ? AND NOT flight = ?"
            );

        } catch (SQLException ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public String statusByFlightDay(String Flight, Date Day) {
        ResultSet rs = null;
        String bookingsRow = "";
        String flightStatus = "Name" + "\t\t" + "Timestamp" + "\n";
        try {

            statusByFlightDay.setString(1, Flight);
            statusByFlightDay.setDate(2, Day);
            rs = statusByFlightDay.executeQuery();
            while (rs.next()) {
                bookingsRow = rs.getObject(1).toString()
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

    // function for dropFlight
    public String statusByFlight(String Flight) {
        ResultSet rs = null;
        String bookingsRow = "";
        String flightStatus = "Name" + "\t\t" + "Timestamp" + "\n";
        try {

            statusByFlight.setString(1, Flight);
            rs = statusByFlight.executeQuery();
            while (rs.next()) {
                bookingsRow = rs.getObject(1).toString()
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

    public int addBooking(BookingEntry bookingEntry, Timestamp Timestamp) {

        int result = 0;

        try {
            insertBooking.setString(1, bookingEntry.getCustomer());
            insertBooking.setString(2, bookingEntry.getFlight());
            insertBooking.setDate(3, bookingEntry.getDay());
            insertBooking.setTimestamp(4, Timestamp);

            result = insertBooking.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            close();
        }
        return result;
    }

    // function for dropFlight
    public int addRebooking(BookingEntry oldEntry, BookingEntry vacantEntry, Timestamp Timestamp) {
        int result = 0;

        try {
            insertBooking.setString(1, oldEntry.getCustomer());
            insertBooking.setString(2, vacantEntry.getFlight());
            insertBooking.setDate(3, vacantEntry.getDay());
            insertBooking.setTimestamp(4, Timestamp);

            result = insertBooking.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            close();
        }
        return result;
    }

    public int cancelBooking(String Customer, Date Day) {

        int result = 0;
        try {

            deleteBooking.setString(1, Customer);
            deleteBooking.setDate(2, Day);
            result = deleteBooking.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            close();
        }

        return result;
    }

    // function for dropFlight
    public int cancelBookingByEntry(BookingEntry bookingEntry) {
        int result = 0;
        try {

            deleteBookingByEntry.setString(1, bookingEntry.getCustomer());
            deleteBookingByEntry.setString(2, bookingEntry.getFlight());
            deleteBookingByEntry.setDate(3, bookingEntry.getDay());
            result = deleteBookingByEntry.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
            close();
        }

        return result;
    }

    // function for dropFlight
    public BookingEntry getBookingByFlight(String Flight) {
        ResultSet rs = null;
        BookingEntry bookingEntry = null;
        try {
            retrieveBookingByFlight.setString(1, Flight);
            rs = retrieveBookingByFlight.executeQuery();
            rs.next();
            bookingEntry = new BookingEntry(rs.getObject(1).toString(),
                    rs.getObject(2).toString(), (Date) rs.getObject(3), (Timestamp) rs.getObject(4));

        } catch (SQLException ex) {
            ex.printStackTrace();
            close();
        }
        return bookingEntry;
    }

    // function for dropFlight
    public ArrayList<BookingEntry> getVacantBooking(BookingEntry bookingEntry) {
        ResultSet rs = null;
        ArrayList<BookingEntry> vacantFlights = null;

        try {
            vacantFlights = new ArrayList<BookingEntry>();

            retrieveVacantBooking.setDate(1, bookingEntry.getDay());
            retrieveVacantBooking.setString(2, bookingEntry.getFlight());
            rs = retrieveVacantBooking.executeQuery();
            while (rs.next()) {

                if (Flight.getFlightSeats((String) rs.getObject(2), (Date) rs.getObject(3)) < Flight.getMaxSeatCount((String) rs.getObject(2))) {
                    vacantFlights.add(new BookingEntry((String) rs.getObject(1),
                            (String) rs.getObject(2), (Date) rs.getObject(3), (Timestamp) rs.getObject(4)));
                }
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            close();
        }
        return vacantFlights;
    }

    public ArrayList<String> getEmptyFlights(String flight, Date day) {
        ArrayList<String> tempList = new ArrayList(Flight.getFlightNames());
        ArrayList<String> emptyFlightNames = new ArrayList();

        for (int i = 0; i < tempList.size(); i++) {
            String tempString = tempList.get(i);
            if (tempString != flight && (Flight.getFlightSeats(tempString, day) == 0)) {
                emptyFlightNames.add(tempString);
            }
        }

        return emptyFlightNames;
    }

    public int bookEmptyFlights(BookingEntry oldEntry, String EmptyFlight, Timestamp timestamp) {
        int result = 0;

        try {
            insertBooking.setString(1, oldEntry.getCustomer());
            insertBooking.setString(2, EmptyFlight);
            insertBooking.setDate(3, oldEntry.getDay());
            insertBooking.setTimestamp(4, timestamp);

            result = insertBooking.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
            close();
        }
        return result;
    }

    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
