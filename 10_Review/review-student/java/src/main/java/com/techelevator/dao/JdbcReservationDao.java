package com.techelevator.dao;

import com.techelevator.model.Reservation;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlRowSetResultSetExtractor;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class JdbcReservationDao implements ReservationDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcReservationDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public int createReservation(int siteId, String name, LocalDate fromDate, LocalDate toDate) {

        String sql = "INSERT INTO reservation (site_id, name, from_date, " +
                "to_date) " +
                "VALUES(?,?,?,?) " +
                "RETURNING reservation_id;";

        long newId = jdbcTemplate.queryForObject(sql, Long.class, siteId, name, fromDate, toDate);

        return (int) newId;
    }

    public List<Reservation> upcomingReservations(int parkId) {
        List<Reservation> reservations = new ArrayList<>();
        String sql = "SELECT reservation_id, reservation.site_id, reservation.name, from_date, to_date, create_date \n" +
                "FROM reservation  \n" +
                "INNER JOIN site ON reservation.site_id=site.site_id \n" +
                "INNER JOIN campground ON site.campground_id=campground.campground_id \n" +
                "INNER JOIN park ON campground.park_id=park.park_id \n" +
                "WHERE from_date > CURRENT_DATE AND from_date < CURRENT_DATE + 30 AND park.park_id=?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, parkId);
        while (results.next()) {
            reservations.add(mapRowToReservation(results));
        }
        return reservations;
    }


    private Reservation mapRowToReservation(SqlRowSet results) {
        Reservation r = new Reservation();
        r.setReservationId(results.getInt("reservation_id"));
        r.setSiteId(results.getInt("site_id"));
        r.setName(results.getString("name"));
        r.setFromDate(results.getDate("from_date").toLocalDate());
        r.setToDate(results.getDate("to_date").toLocalDate());
        r.setCreateDate(results.getDate("create_date").toLocalDate());
        return r;
    }


}
