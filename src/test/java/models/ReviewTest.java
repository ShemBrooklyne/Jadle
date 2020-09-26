package models;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ReviewTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void getContent() {
        Review testReview = setupReview();
        assertEquals("Great service", testReview.getContent());
    }

    @Test
    public void setContent() {
        Review testReview = setupReview();
        testReview.setContent("No free dessert :(");
        assertNotEquals("Great service", testReview.getContent());
    }

    @Test
    public void getWrittenBy() {
        Review testReview = setupReview();
        assertEquals("Kim", testReview.getWrittenBy());
    }

    @Test
    public void setWrittenBy() {
        Review testReview = setupReview();
        testReview.setWrittenBy("Mike");
        assertNotEquals("Kim", testReview.getWrittenBy());
    }

    @Test
    public void getRating() {
        Review testReview = setupReview();
        assertEquals(4, testReview.getRating());
    }

    @Test
    public void setRating() {
        Review testReview = setupReview();
        testReview.setRating(1);
        assertNotEquals(4, testReview.getRating());
    }

    @Test
    public void getRestaurantId() {
        Review testReview = setupReview();
        assertEquals(1, testReview.getRestaurantId());
    }

    @Test
    public void setRestaurantId() {
        Review testReview = setupReview();
        testReview.setRestaurantId(10);
        assertNotEquals(1, testReview.getRestaurantId());
    }

    @Test
    public void setId() {
        Review testReview = setupReview();
        testReview.setId(5);
        assertEquals(5, testReview.getId());
    }

    // helper
    public Review setupReview (){
        return new Review("Great service", "Kim", 4, 1);
    }

}