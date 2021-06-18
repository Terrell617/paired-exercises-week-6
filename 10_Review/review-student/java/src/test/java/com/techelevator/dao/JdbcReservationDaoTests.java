package com.techelevator.dao;

import com.techelevator.model.Park;
import com.techelevator.model.Reservation;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JdbcReservationDaoTests extends BaseDaoTests {

    private ReservationDao dao;
    private JdbcReservationDao sut;

    @Before
    public void setup() {
        dao = new JdbcReservationDao(dataSource);
        sut = new JdbcReservationDao(dataSource);
    }

    @Test
    public void createReservation_Should_ReturnNewReservationId() {
        int reservationCreated = dao.createReservation(9999,
                "TEST NAME",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(3));

        assertEquals(reservationCreated, 1);
    }
    @Test
    public void upcomingReservations_Should_Return_All_Upcoming_Reservations(){
        List<Reservation> reservations=sut.upcomingReservations(99);
        Assert.assertEquals(2,reservations.size());
    }


}
