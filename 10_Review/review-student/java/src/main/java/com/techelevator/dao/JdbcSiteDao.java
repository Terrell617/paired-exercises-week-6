package com.techelevator.dao;

import com.techelevator.model.Site;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

public class JdbcSiteDao implements SiteDao {

    private JdbcTemplate jdbcTemplate;

    public JdbcSiteDao(DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Site> getSitesThatAllowRVs(int parkId) {
        List<Site> sites =new ArrayList<>();
        String sql = "SELECT site_id, site.campground_id, site_number, max_occupancy, "+"" +
                "accessible, max_rv_length, utilities "+
                "FROM site " +
                "INNER JOIN campground ON site.campground_id = campground.campground_id "+
                "WHERE campground.park_id = ? AND max_rv_length > 0;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, parkId);
        while(results.next()){
            sites.add(mapRowToSite(results));
        }

        return sites;
    }

    public List<Site> availableSites(int parkId){
        List<Site> sites = new ArrayList<>();
        String sql ="Select  site_id, site.campground_id, site_number, max_occupancy, accessible, max_rv_length, utilities "+
                "FROM site  " +
                "INNER JOIN campground ON site.campground_id = campground.campground_id " +
                "INNER JOIN park ON campground.park_id = park.park_id " +
                "where site_id NOT IN  " +
                "(SELECT site_id  " +
                "FROM reservation " +
                "where CURRENT_DATE BETWEEN From_date AND To_date) " +
                "AND park.park_id = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, parkId);
        while(results.next()){
            sites.add(mapRowToSite(results));
        }return sites;
    }

    private Site mapRowToSite(SqlRowSet results) {
        Site site = new Site();
        site.setSiteId(results.getInt("site_id"));
        site.setCampgroundId(results.getInt("campground_id"));
        site.setSiteNumber(results.getInt("site_number"));
        site.setMaxOccupancy(results.getInt("max_occupancy"));
        site.setAccessible(results.getBoolean("accessible"));
        site.setMaxRvLength(results.getInt("max_rv_length"));
        site.setUtilities(results.getBoolean("utilities"));
        return site;
    }
}
