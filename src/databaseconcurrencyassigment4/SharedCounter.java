/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databaseconcurrencyassigment4;

import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author Cristi
 */
public class SharedCounter {

    private AtomicInteger startedThreads = new AtomicInteger();
    private AtomicInteger reservedSeats = new AtomicInteger();
    private AtomicInteger bookedSeats = new AtomicInteger();
    private AtomicInteger failedToReserveSeats = new AtomicInteger();
    private AtomicInteger reservedAndNotBookedSeats = new AtomicInteger();
    private AtomicInteger reservedAndTimedOutBookingSeats = new AtomicInteger();
    private AtomicInteger reservedAndOtherErrorBookingSeats = new AtomicInteger();

    public synchronized Integer incrementStartedThreads() {
        return startedThreads.incrementAndGet();
    }

    public synchronized Integer incrementReservedSeats() {
        return reservedSeats.incrementAndGet();
    }

    public synchronized Integer incrementBookedSeats() {
        return bookedSeats.incrementAndGet();
    }
    
    public synchronized Integer incrementFailedToReserveSeats() {
        return failedToReserveSeats.incrementAndGet();
    }

    public synchronized Integer incrementReservedAndNotBookedSeats() {
        return reservedAndNotBookedSeats.incrementAndGet();
    }

    public synchronized Integer incrementReservedAnTimedOutBookingSeats() {
        return reservedAndTimedOutBookingSeats.incrementAndGet();
    }

    public synchronized Integer incrementReservedAndOtherErrorBookingSeats() {
        return reservedAndOtherErrorBookingSeats.incrementAndGet();
    }

    public int getStartedThreadsCount() {
        return startedThreads.get();
    }

    public int getReservedSeatsCount() {
        return reservedSeats.get();
    }

    public int getBookedSeatsCount() {
        return bookedSeats.get();
    }
    
    public int getFailedToReserveSeatsCount() {
        return failedToReserveSeats.get();
    }

    public int getReservedAndNotBookedSeatsCount() {
        return reservedAndNotBookedSeats.get();
    }

    public int getReservedAndTimedOutBookingSeatsCount() {
        return reservedAndTimedOutBookingSeats.get();
    }

    public int getReservedAndOtherErrorBookingSeatsCount() {
        return reservedAndOtherErrorBookingSeats.get();
    }
}
