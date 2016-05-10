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
        System.out.println("thread#" + threadNo + " started");

//      try to reserve a seat
        r = new Reservation("db_027", "db2016");

        if (sc.getBookedSeatsCount() < sc.getMaxToBook()) {
            String seatNb = r.reserve("CR9", threadNo);
            try {
                Thread.sleep(new Random().nextInt(11) * 1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(DatabaseConcurrencyAssigment4.class.getName()).log(Level.SEVERE, null, ex);
            }

            if (seatNb != null) {
                sc.incrementReservedSeats();
                System.out.println("\u001B[36mthread#" + threadNo + " reserved " + seatNb);

//              now try to book the reserved seat in 75% of the cases
                if (new Random().nextInt(4) != 0) {
                    if (sc.getBookedSeatsCount() < 96) {
                        int status = r.book("CR9", seatNb, threadNo);
                        switch (status) {
                            case 0:
                                sc.incrementBookedSeats();
                                System.out.println("\u001B[32mthread#" + threadNo + " succeeded to book " + seatNb + ": status " + status);
                                break;
                            case -2:
                                sc.incrementOverReservedBecauseOfDelaySeats();
                                System.out.println("\u001B[31mthread#" + threadNo + " failed to book " + seatNb + ": status " + status);
                                break;
                            case -3:
                                sc.incrementReservedAndTimedOutBookingSeats();
                                System.out.println("\u001B[31mthread#" + threadNo + " failed to book " + seatNb + ": status " + status);
                                break;
                            default:
                                sc.incrementReservedAndOtherErrorBookingSeats();
                                System.out.println("\u001B[31mthread#" + threadNo + " failed to book " + seatNb + ": status " + status);
                                break;
                        }
                    }
                } else {
                    sc.incrementReservedAndNotBookedSeats();
                    System.out.println("\u001B[34mthread#" + threadNo + " gave up after reserving " + seatNb);
                }
            } else {
                sc.incrementFailedToReserveSeats();
                System.out.println("\u001B[31mthread#" + threadNo + " failed to reserve");
            }
        }
    }
}
