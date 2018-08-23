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
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Customer {

    private static Connection connection;
    private static PreparedStatement getAllCustomers;
    private static PreparedStatement insertNewCustomer;
    private static PreparedStatement customerStatusBooking;
    private static PreparedStatement customerStatusWaitlist;
    private static ArrayList<String> customers;

    public static ArrayList<String> getAllCustomers() {

        ResultSet rs = null;
        try {
            connection = DBConnection.getConnection();
            customers = new ArrayList();
            getAllCustomers = connection.prepareStatement("select name from customer");
            rs = getAllCustomers.executeQuery();
            while (rs.next()) {
                customers.add(rs.getString(1));
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return customers;
    }

    public static void addNewCustomer(String customer) {
        try {
            connection = DBConnection.getConnection();
            insertNewCustomer = connection.prepareStatement("insert into customer (name) values (?)");
            insertNewCustomer.setString(1, customer);
            insertNewCustomer.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static String getCustomerStatus(String customer) {

        ResultSet rs1 = null;
        ResultSet rs2 = null;
        String statusRow = "";
        String customerStatusStringBooking = "\nBooking: \nFlight" + "\t\t" + "Day" + "\t" + "Timestamp" + "\n";
        String customerStatusStringWaitlist = "\nWaitlist: \nFlight" + "\t\t" + "Day" + "\t" + "Timestamp" + "\n";
        String customerStatusString = "";
        try {
            connection = DBConnection.getConnection();
            customerStatusBooking = connection.prepareStatement("SELECT * FROM bookings WHERE customer = ?");
            customerStatusWaitlist = connection.prepareStatement("SELECT * FROM waitlist WHERE customer = ?");
            customerStatusBooking.setString(1, customer);
            customerStatusWaitlist.setString(1, customer);
            rs1 = customerStatusBooking.executeQuery();
            while (rs1.next()) {
                statusRow = rs1.getObject(2).toString() + "\t\t" + rs1.getObject(3).toString()
                        + "\t" + rs1.getObject(4).toString() + "\n";
                customerStatusStringBooking += statusRow;
            }
            customerStatusString += customerStatusStringBooking;

            rs2 = customerStatusWaitlist.executeQuery();
            while (rs2.next()) {
                statusRow = rs2.getObject(2).toString() + "\t\t" + rs2.getObject(3).toString()
                        + "\t" + rs2.getObject(4).toString() + "\n";
                customerStatusStringWaitlist += statusRow;
            }
            customerStatusString += customerStatusStringWaitlist;
        } catch (SQLException ex) {
            ex.printStackTrace();
        } finally {
            try {
                rs1.close();
                rs2.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                close();
            }
        }

        return customerStatusString;
    }

    public static void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
