/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseconcurrencyassigment4;

import domain.Reservation;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author redrose
 */
public class DatabaseConcurrencyAssigment4 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
//       class used only to test small stuff
        Reservation r = new Reservation("db_027", "db2016");
        String result = r.reserve("CR9", 13);
        System.out.println(result);
        
        try {
            Thread.sleep(6000);
        } catch (InterruptedException ex) {
            Logger.getLogger(DatabaseConcurrencyAssigment4.class.getName()).log(Level.SEVERE, null, ex);
        }
        int status = r.book("CR9", result, 13);
        System.out.println(status);
        
        r.closeConnection();
    }
}
