/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseconcurrencyassigment4;

import domain.Reservation;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Cristi
 */
public class UserThread implements Runnable {

    SharedCounter sc;

    public UserThread(SharedCounter sc) {
        this.sc = sc;
    }

    @Override
    public void run() {
//        try to reserve a seat
        Reservation r = new Reservation("db_027", "db2016");
        String result = r.reserve("CR9", 13);
//        System.out.println(result);
        if (result != null) {
            sc.incrementReservedSeats();

//            now try to book the reserved seat in 75% of the cases
            if (new Random().nextInt(4) < 3) {
                try {
                    Thread.sleep(new Random().nextInt(10000));
                } catch (InterruptedException ex) {
                    Logger.getLogger(DatabaseConcurrencyAssigment4.class.getName()).log(Level.SEVERE, null, ex);
                }
                int status = r.book("CR9", result, 13);
//                System.out.println(status);
                if (status == 0) {
                    sc.incrementBookedSeats();
                }
            } else {
                sc.incrementReservedAndNotBookedSeats();
            }
        }
    }
}
