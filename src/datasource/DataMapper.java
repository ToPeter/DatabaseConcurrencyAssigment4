/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author redrose
 */
public class DataMapper {

    public String reserve(String plane_no, long id, Connection connection) {
        PreparedStatement statement = null;
        ResultSet rs;
        String empty_seat = null;

        String sqlString0 = "select SEAT_NO from SEAT "
                + "where PLANE_NO = ? "
                + "and (RESERVED is NULL "
                + "  or (BOOKED is NULL and BOOKING_TIME < ?)) "
                + "and ROWNUM <= 1 "
                + "for update ";

        String sqlString1 = "update SEAT "
                + "set RESERVED = ?, BOOKING_TIME = ? "
                + "where PLANE_NO = ? "
                + "and SEAT_NO = ?";

        try {
            Long time_now_in_sec = System.currentTimeMillis() / 1000;

            statement = connection.prepareStatement(sqlString0);
            statement.setString(1, plane_no);
            statement.setLong(2, time_now_in_sec - 5);
            rs = statement.executeQuery();
            if (rs.next()) {
                empty_seat = rs.getString(1);
                statement.close();

                statement = connection.prepareStatement(sqlString1);
                statement.setLong(1, id);
                statement.setLong(2, time_now_in_sec);
                statement.setString(3, plane_no);
                statement.setString(4, empty_seat);

                int count = statement.executeUpdate();
                statement.close();
                connection.commit();
                if (count == 1) {
                    return empty_seat;
                } else {
                    return null;
                }
            } else {
                statement.close();
                connection.rollback();
                return null;
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataMapper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Integer book(String plane_no, String seat_no, long id, Connection connection) {
        PreparedStatement statement = null;
        ResultSet rs;

        String sqlString0 = "update SEAT "
                + "set BOOKED = ?, BOOKING_TIME = ? "
                + "where PLANE_NO = ? "
                + "and SEAT_NO = ? "
                + "and RESERVED = ? "
                + "and BOOKED is NULL "
                + "and BOOKING_TIME >= ?";

        String sqlString1 = "select * from SEAT "
                + "where PLANE_NO = ? "
                + "and SEAT_NO = ? ";

        try {
            Long time_now_in_sec = System.currentTimeMillis() / 1000;

            statement = connection.prepareStatement(sqlString0);
            statement.setLong(1, id);
            statement.setLong(2, time_now_in_sec);
            statement.setString(3, plane_no);
            statement.setString(4, seat_no);
            statement.setLong(5, id);
            statement.setLong(6, time_now_in_sec - 5);

            int count = statement.executeUpdate();
            statement.close();
            if (count == 1) {
                connection.commit();
                return 0;
            } else {
                statement = connection.prepareStatement(sqlString1);
                statement.setString(1, plane_no);
                statement.setString(2, seat_no);
                rs = statement.executeQuery();
                if (rs.next()) {
                    String actual_seat = rs.getString("SEAT_NO");
                    Long actual_reserved = rs.getLong("RESERVED");
                    if (rs.getObject("RESERVED") == null) {
                        actual_reserved = null;
                    }
                    Long actual_booked = rs.getLong("BOOKED");
                    if (rs.getObject("BOOKED") == null) {
                        actual_booked = null;
                    }
                    Long actual_booking_time = rs.getLong("BOOKING_TIME");
                    if (rs.getObject("BOOKING_TIME") == null) {
                        actual_booking_time = null;
                    }
                    statement.close();
                    if (actual_reserved == null) {
                        return -1;
                    } else if (!actual_seat.equals(seat_no)) {
                        return -2;
                    } else if (time_now_in_sec - actual_booking_time > 5) {
                        return -3;
                    } else if (actual_booked != null) {
                        return -4;
                    } else {
                        return -5;
                    }
                } else {
                    statement.close();
                    return -5;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataMapper.class.getName()).log(Level.SEVERE, null, ex);
            return -5;
        }
    }

    public Integer clearAllBookings(String plane_no, Connection connection) {
        PreparedStatement statement = null;
        ResultSet rs;

        String sqlString0 = "update SEAT "
                + "set RESERVED = null, BOOKED = null, BOOKING_TIME = null "
                + "where PLANE_NO = ? ";

        try {
            statement = connection.prepareStatement(sqlString0);
            statement.setString(1, plane_no);
            int count = statement.executeUpdate();
            statement.close();
            connection.commit();
            return count;
        } catch (SQLException ex) {
            Logger.getLogger(DataMapper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
