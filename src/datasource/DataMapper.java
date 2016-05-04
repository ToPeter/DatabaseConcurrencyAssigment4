/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package datasource;

import java.sql.SQLException;

/**
 *
 * @author redrose
 */
public class DataMapper {
    
    public boolean checkUserLogin(Long username, String password)
    {
//        userType = null;
//        String sqlString1 = "SELECT POSITION, PASSWORD FROM EMPLOYEES "
//                + "WHERE ID = ?";
//        String sqlString2 = "SELECT PASSWORD FROM CLIENTS "
//                + "WHERE ID = ?";
//        PreparedStatement statement;
//        try
//        {
//            statement = connection.prepareStatement(sqlString1);
//            statement.setLong(1, username);
//
//            ResultSet rs = statement.executeQuery();
//            if (rs.next())
//            {
//                userType = rs.getString(1);
//                if (rs.getString(2).equals(password))
//                {
//                    return true;
//                }
//            } else
//            {
//                statement = connection.prepareStatement(sqlString2);
//                statement.setLong(1, username);
//
//                rs = statement.executeQuery();
//                if (rs.next())
//                {
//                    userType = "Client";
//                    if (rs.getString(1).equals(password))
//                    {
//                        return true;
//                    }
//                }
//            }
//            statement.close();
//        } catch (SQLException ex)
//        {
//            System.out.println("Fail in DataMapper - checkUserLogin");
//            Logger.getLogger(DataMapper.class.getName()).log(Level.SEVERE, null, ex);
//        }
        return false;
    }



//    public ArrayList getAllRooms(Connection connection)
//    {
//        ArrayList result = new ArrayList<>();
//        PreparedStatement statement;
//        String sqlString = "SELECT R.ID, R.TYPE, RT.CAPACITY, RT.PRICE FROM ROOMS R "
//                + "JOIN ROOM_TYPES RT ON R.TYPE = RT.TYPE "
//                + "ORDER BY R.ID";
//
//        try
//        {
//            statement = connection.prepareStatement(sqlString);
//            ResultSet rs = statement.executeQuery();
//            while (rs.next())
//            {
//                result.add(new Room(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getDouble(4)));
//            }
//
//            statement.close();
//        } catch (SQLException ex)
//        {
//            System.out.println("Fail in DataMapper - getAllRooms");
//            Logger.getLogger(DataMapper.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        return result;
//    }
    
}
