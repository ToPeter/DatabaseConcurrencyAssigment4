/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseconcurrencyassigment4;

import domain.Reservation;

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
        r.reserve("CR9", 13);
        r.closeConnection();
    }
    
}
