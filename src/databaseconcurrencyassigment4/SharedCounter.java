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

    private AtomicInteger reservedSeats = new AtomicInteger();
    private AtomicInteger bookedSeats = new AtomicInteger();
    private AtomicInteger reservedAndNotBookedSeats = new AtomicInteger();

    public synchronized void incrementReservedSeats() {
        reservedSeats.incrementAndGet();
    }

    public synchronized void incrementBookedSeats() {
        bookedSeats.incrementAndGet();
    }

    public synchronized void incrementReservedAndNotBookedSeats() {
        reservedAndNotBookedSeats.incrementAndGet();
    }
    
    public int getReservedSeatsCount() {
        return reservedSeats.get();
    }

    public int getBookedSeatsCount() {
        return bookedSeats.get();
    }
    
    public int getReservedAndNotBookedSeatsCount() {
        return reservedAndNotBookedSeats.get();
    }
}
