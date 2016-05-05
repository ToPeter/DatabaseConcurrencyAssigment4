/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import datasource.DBFacade;

/**
 *
 * @author redrose
 */
public class Reservation {

    private DBFacade dbFacade;
    private String user, pw;

    public Reservation(String user, String pw) {
        this.user = user;
        this.pw = pw;

        dbFacade = DBFacade.getInstance();
    }

    public String reserve(String plane_no, long id) {
        return dbFacade.reserve(plane_no, id);
    }

    public Integer book(String plane_no, String seat_no, long id) {
        return dbFacade.book(plane_no, seat_no, id);
    }

    public boolean bookAll(String plane_no) {
        return false;
    }

    public boolean clearAllBookings(String plane_no) {
        return false;
    }

    public boolean isAllBooked(String plane_no) {

        return false;
    }

    public boolean isAllReserved(String plane_no) {

        return false;
    }

}
