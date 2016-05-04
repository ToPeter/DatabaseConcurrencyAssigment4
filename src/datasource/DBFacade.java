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
    private String dbUsername = "db_027";
    private String dbPassword = "db2016";

    public DBFacade()
    {
        dataMapper = new DataMapper();

    }

    public boolean createConnection()
    {
        try
        {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DriverManager.getConnection(dbHost, dbUsername, dbPassword);
        } catch (ClassNotFoundException e)
        {
            Logger.getLogger(DBFacade.class.getName()).log(Level.SEVERE, null, e);
            return false;
        } catch (SQLException ex) {
            Logger.getLogger(DBFacade.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }

        return true;
    }

    public boolean closeConnection()
    {
        try
        {
            connection.close();
        } catch (SQLException ex)
        {
            Logger.getLogger(DBFacade.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
        return true;
    }

    
    public boolean addNewClient()
    {
        return false;
    }
    
}
