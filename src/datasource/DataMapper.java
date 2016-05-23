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
        PreparedStatement statement;
        ResultSet rs;
        String empty_seat;

        String sqlString0 = "select SEAT_NO from SEAT "
                + "where PLANE_NO = ? "
                + "and (RESERVED is NULL "
                + "  or (BOOKED is NULL and (select date_to_unix_ts(systimestamp) from DUAL) - BOOKING_TIME > 5)) "
                + "and ROWNUM <= 1 "
                + "for update ";

        String sqlString1 = "update SEAT "
                + "set RESERVED = ?, BOOKING_TIME = (select date_to_unix_ts(systimestamp) from DUAL) "
                + "where PLANE_NO = ? "
                + "and SEAT_NO = ?";

        try {
            statement = connection.prepareStatement(sqlString0);
            statement.setString(1, plane_no);
            rs = statement.executeQuery();
            if (rs.next()) {
                empty_seat = rs.getString(1);
                statement.close();

                statement = connection.prepareStatement(sqlString1);
                statement.setLong(1, id);
                statement.setString(2, plane_no);
                statement.setString(3, empty_seat);

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
        PreparedStatement statement;
        ResultSet rs;

        String sqlString0 = "update SEAT "
                + "set BOOKED = ?, BOOKING_TIME = (select date_to_unix_ts(systimestamp) from DUAL) "
                + "where PLANE_NO = ? "
                + "and SEAT_NO = ? "
                + "and RESERVED = ? "
                + "and BOOKED is NULL "
                + "and (select date_to_unix_ts(systimestamp) from DUAL) - BOOKING_TIME <= 5";

        String sqlString1 = "select * from SEAT "
                + "where PLANE_NO = ? "
                + "and SEAT_NO = ? ";

        String sqlString2 = "select date_to_unix_ts(systimestamp) from DUAL";

        try {
            statement = connection.prepareStatement(sqlString0);
            statement.setLong(1, id);
            statement.setString(2, plane_no);
            statement.setString(3, seat_no);
            statement.setLong(4, id);

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

                    statement = connection.prepareStatement(sqlString2);
                    rs = statement.executeQuery();
                    rs.next();
                    long time_now_in_sec = rs.getLong(1);
                    statement.close();

                    if (actual_reserved == null) {
                        return -1;
                    } else if (actual_reserved != id) {
                        return -2;
                    } else if (time_now_in_sec - actual_booking_time > 5) {
                        return -3;
                    } else if (actual_booked != null) {
                        return -4;
                    } else {
                        System.out.println("-5 for what reason? " + actual_seat + " vs " + seat_no + " | " + actual_reserved + " vs " + id + " | " + actual_booked + " vs " + id + " | " + actual_booking_time + " vs " + time_now_in_sec);
                        return -5;
                    }
                } else {
                    statement.close();
                    System.out.println("-5 because the select didn't find anything, weiiird!");
                    return -5;
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(DataMapper.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("-5 because of SQLException");
            return -5;
        }
    }

    public Integer bookAll(String plane_no, long id, Connection connection) {
        PreparedStatement statement;

        String sqlString0 = "update SEAT "
                + "set RESERVED = ?, BOOKED = ?, BOOKING_TIME = (select date_to_unix_ts(systimestamp) from DUAL) "
                + "where PLANE_NO = ? ";

        try {
            statement = connection.prepareStatement(sqlString0);
            statement.setLong(1, id);
            statement.setLong(2, id);
            statement.setString(3, plane_no);
            int count = statement.executeUpdate();
            statement.close();
            connection.commit();
            return count;
        } catch (SQLException ex) {
            Logger.getLogger(DataMapper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Integer clearAllBookings(String plane_no, Connection connection) {
        PreparedStatement statement;

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

    public Integer notReservedCount(String plane_no, Connection connection) {
        PreparedStatement statement;
        ResultSet rs;

        String sqlString0 = "select count(*) from SEAT "
                + "where PLANE_NO = ? "
                + "and (RESERVED is null or (BOOKED is NULL and (select date_to_unix_ts(systimestamp) from DUAL) - BOOKING_TIME > 5))";

        try {
            statement = connection.prepareStatement(sqlString0);
            statement.setString(1, plane_no);
            rs = statement.executeQuery();
            rs.next();
            int notReserved = rs.getInt(1);
            statement.close();
            return notReserved;

        } catch (SQLException ex) {
            Logger.getLogger(DataMapper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public Integer notBookedCount(String plane_no, Connection connection) {
        PreparedStatement statement;
        ResultSet rs;

        String sqlString0 = "select count(*) from SEAT "
                + "where PLANE_NO = ? "
                + "and BOOKED is null";

        try {
            statement = connection.prepareStatement(sqlString0);
            statement.setString(1, plane_no);
            rs = statement.executeQuery();
            rs.next();
            int notBooked = rs.getInt(1);
            statement.close();
            return notBooked;

        } catch (SQLException ex) {
            Logger.getLogger(DataMapper.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }
}
