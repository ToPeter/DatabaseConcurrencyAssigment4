/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseconcurrencyassigment4;

import datasource.DBFacade;
import static java.lang.Integer.min;
import java.util.ArrayList;

/**
 *
 * @author redrose
 */
public class DatabaseConcurrencyAssigment4 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        SharedCounter sc;
        ArrayList<Thread> tenThreads = new ArrayList();
        UserThread userThread;
        DBFacade dbf = new DBFacade("db_027", "db2016");
        String plane_no = "CR9";

        dbf.createConnection();
        System.out.println(dbf.clearAllBookings(plane_no));
        sc = new SharedCounter(dbf.notBookedCount(plane_no));
//        System.out.println(dbf.bookAll(plane_no, 113));
//        System.out.println(dbf.notReservedCount(plane_no));
//        System.out.println(dbf.isAllReserved(plane_no));
//        System.out.println(dbf.notBookedCount(plane_no));
//        System.out.println(dbf.isAllBooked(plane_no));

        int max_threads = min(10, dbf.notBookedCount(plane_no));
        for (int i = 0; i < max_threads; i++) {
            userThread = new UserThread(sc);
            Thread t = new Thread(userThread);
            t.start();
            tenThreads.add(t);
        }

        while (!dbf.isAllBooked(plane_no)) {
            for (int i = 0; i < max_threads; i++) {
                if (!tenThreads.get(i).isAlive()) {
                    tenThreads.remove(i);
                    userThread = new UserThread(sc);
                    Thread t = new Thread(userThread);
                    t.start();
                    tenThreads.add(t);
                }
            }
        }

        dbf.closeConnection();

        System.out.println("total reserved seats: " + sc.getReservedSeatsCount());
        System.out.println("total book seats: " + sc.getBookedSeatsCount());
        System.out.println("failed to reserve seats: " + sc.getFailedToReserveSeatsCount());
        System.out.println("total reserved and not booked (~25% of total reserved): " + sc.getReservedAndNotBookedSeatsCount());
        System.out.println("total failed to book because of overbooking(because of the delay > 5s): " + sc.getOverReservedBecauseOfDelaySeatsCount());
        System.out.println("total reserved and timed out for booking: " + sc.getReservedAndTimedOutBookingSeatsCount());
        System.out.println("total reserved and not booked - other errors: " + sc.getReservedAndOtherErrorBookingSeatsCount());
    }
}
