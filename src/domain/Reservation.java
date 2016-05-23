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

    public Reservation(String user, String pw) {
        dbFacade = new DBFacade(user, pw);
    }

    public String reserve(String plane_no, long id) {
        dbFacade.createConnection();
        String result = dbFacade.reserve(plane_no, id);
        dbFacade.closeConnection();
        return result;
    }

    public Integer book(String plane_no, String seat_no, long id) {
        dbFacade.createConnection();
        Integer result = dbFacade.book(plane_no, seat_no, id);
        dbFacade.closeConnection();
        return result;
    }

    public boolean isAllBooked(String plane_no) {
        dbFacade.createConnection();
        Boolean result = dbFacade.isAllBooked(plane_no);
        dbFacade.closeConnection();
        return result;
    }

    public boolean isAllReserved(String plane_no) {
        dbFacade.createConnection();
        Boolean result = dbFacade.isAllReserved(plane_no);
        dbFacade.closeConnection();
        return result;
    }

}
