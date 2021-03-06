/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author redrose
 */
public class DBFacade {

    private DataMapper dataMapper;
    private Connection connection;

    private String dbHost = "jdbc:oracle:thin:@datdb.cphbusiness.dk:1521:dat";
    private String dbUsername, dbPassword;

    public DBFacade(String user, String pw) {
        this.dataMapper = new DataMapper();
        this.dbUsername = user;
        this.dbPassword = pw;
    }

    public boolean createConnection() {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(dbHost, dbUsername, dbPassword);
            connection.setAutoCommit(false);
        } catch (ClassNotFoundException | SQLException e) {
            Logger.getLogger(DBFacade.class.getName()).log(Level.SEVERE, null, e);
            return false;
        }

        return true;
    }

    public boolean closeConnection() {
        try {
            connection.close();
        } catch (SQLException ex) {
            Logger.getLogger(DBFacade.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    public String reserve(String plane_no, long id) {
        return dataMapper.reserve(plane_no, id, connection);
    }

    public Integer book(String plane_no, String seat_no, long id) {
        return dataMapper.book(plane_no, seat_no, id, connection);
    }

    public Integer bookAll(String plane_no, long id) {
        return dataMapper.bookAll(plane_no, id, connection);
    }

    public Integer clearAllBookings(String plane_no) {
        return dataMapper.clearAllBookings(plane_no, connection);
    }

    public Integer notReservedCount(String plane_no) {
        return dataMapper.notReservedCount(plane_no, connection);
    }

    public Boolean isAllReserved(String plane_no) {
        return dataMapper.notReservedCount(plane_no, connection) == 0;
    }

    public Integer notBookedCount(String plane_no) {
        return dataMapper.notBookedCount(plane_no, connection);
    }

    public Boolean isAllBooked(String plane_no) {
        return dataMapper.notBookedCount(plane_no, connection) == 0;
    }
}
