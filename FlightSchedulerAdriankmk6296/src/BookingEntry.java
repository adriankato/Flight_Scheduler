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
import java.sql.Timestamp;
import java.util.ArrayList;

public class BookingEntry {

    private String customer;
    private String flight;
    private Date day;
    private Timestamp timestamp;

    public BookingEntry() {
    }

    public BookingEntry(String customer, String flight, Date day, Timestamp timestamp) {
        setCustomer(customer);
        setFlight(flight);
        setDay(day);
        setTimestamp(timestamp);
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public void setFlight(String flight) {
        this.flight = flight;
    }

    public void setDay(Date day) {
        this.day = day;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public String getCustomer() {
        return customer;
    }

    public String getFlight() {
        return flight;
    }

    public Date getDay() {
        return day;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
