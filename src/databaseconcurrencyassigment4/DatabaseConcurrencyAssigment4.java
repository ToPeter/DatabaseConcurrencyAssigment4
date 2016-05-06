/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseconcurrencyassigment4;

import datasource.DBFacade;
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

        SharedCounter sc = new SharedCounter();
        ArrayList<Thread> tenThreads = new ArrayList();
        UserThread userThread;
        DBFacade dbf = DBFacade.getInstance();
        System.out.println(dbf.clearAllBookings("CR9"));

        for (int i = 0; i < 10; i++) {
            userThread = new UserThread(sc);
            Thread t = new Thread(userThread);
            t.start();
            tenThreads.add(t);
        }

        while (sc.getBookedSeatsCount() < 96) {
            for (int i = 0; i < 10; i++) {
                if (!tenThreads.get(i).isAlive()) {
                    tenThreads.remove(i);
                    userThread = new UserThread(sc);
                    Thread t = new Thread(userThread);
                    t.start();
                    tenThreads.add(t);
                    break;
                }
            }
        }
        
        for (int i = 0; i < 10; i++) {
            tenThreads.get(i).interrupt();
        }

        dbf.closeConnection();
        System.out.println("total reserved seats: " + sc.getReservedSeatsCount());
        System.out.println("total book seats: " + sc.getBookedSeatsCount());
        System.out.println("failed to reserve seats: " + sc.getFailedToReserveSeatsCount());
        System.out.println("total reserved and not booked (~25% of total reserved): " + sc.getReservedAndNotBookedSeatsCount());
        System.out.println("total reserved and timed out for booking: " + sc.getReservedAndTimedOutBookingSeatsCount());
        System.out.println("total reserved and not booked - other errors: " + sc.getReservedAndOtherErrorBookingSeatsCount());
    }
}
