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
                + "and ROWNUM <= 1 ";
//                + "for update ";

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

}
