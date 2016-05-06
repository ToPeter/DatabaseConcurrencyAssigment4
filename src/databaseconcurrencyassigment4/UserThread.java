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
    Reservation r;

    public UserThread(SharedCounter sc) {
        this.sc = sc;
    }

    @Override
    public void run() {
        int threadNo = sc.incrementStartedThreads();
        System.out.println("threadNo:" + threadNo);

//      try to reserve a seat
        r = new Reservation("db_027", "db2016");

        if (sc.getBookedSeatsCount() < sc.getMaxToBook()) {
            String result = r.reserve("CR9", threadNo);
            System.out.println(result);
            try {
                Thread.sleep(new Random().nextInt(11) * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(DatabaseConcurrencyAssigment4.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (result != null) {
                sc.incrementReservedSeats();

//              now try to book the reserved seat in 75% of the cases
                if (new Random().nextInt(4) != 0) {
                    if (sc.getBookedSeatsCount() < 96) {
                        int status = r.book("CR9", result, threadNo);
                        System.out.println(result + ": " + status);
                        switch (status) {
                            case 0:
                                sc.incrementBookedSeats();
                                break;
                            case -2:
                                sc.incrementOverReservedBecauseOfDelaySeats();
                                break;
                            case -3:
                                sc.incrementReservedAndTimedOutBookingSeats();
                                break;
                            default:
                                sc.incrementReservedAndOtherErrorBookingSeats();
                                break;
                        }
                    }
                } else {
                    sc.incrementReservedAndNotBookedSeats();
                }
            } else {
                sc.incrementFailedToReserveSeats();
            }
        }
    }
}
