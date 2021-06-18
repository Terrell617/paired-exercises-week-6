package com.techelevator.dao;

import com.techelevator.model.Reservation;
import com.techelevator.model.Site;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class JdbcSiteDaoTests extends BaseDaoTests {

    private SiteDao dao;
    private JdbcSiteDao sut;

    @Before
    public void setup() {
        dao = new JdbcSiteDao(dataSource);
        sut = new JdbcSiteDao(dataSource);
    }

    @Test
    public void getSitesThatAllowRVs_Should_ReturnSites() {
        List<Site> sites = dao.getSitesThatAllowRVs(99);

        assertEquals(2,sites.size());
    }

    @Test
    public void getAvailableSites_Should_ReturnSites() {
        List<Site> reservations=sut.availableSites(99);
        Assert.assertEquals(2,reservations.size());

    }

    public void getAvailableSitesDateRange_Should_ReturnSites() {

    }
}
