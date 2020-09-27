package dao;

import model.Foodtype;
import model.Restaurant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class Sql2oFoodTypeDaoTest{
    private Connection conn;
    private Sql2oFoodTypeDao foodTypeDao;
    private Sql2oRestaurantDao restaurantDao;

    private static  Sql2o sql2o;

    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:DB/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");
        foodTypeDao = new Sql2oFoodTypeDao(sql2o);
        restaurantDao = new Sql2oRestaurantDao(sql2o);
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        foodTypeDao.clearAll();
        conn.close();
    }


    @Test
    public void addingFoodSetsId() throws Exception {
        Foodtype testFoodtype = setupNewFoodtype();
        int originalFoodtypeId = testFoodtype.getId();
        foodTypeDao.add(testFoodtype);
        assertNotEquals(originalFoodtypeId,testFoodtype.getId());
    }

    @Test
    public void addedFoodtypesAreReturnedFromGetAll() throws Exception {
        Foodtype testfoodtype = setupNewFoodtype();
        foodTypeDao.add(testfoodtype);
        assertEquals(1, foodTypeDao.getAll().size());
    }

    @Test
    public void noFoodtypesReturnsEmptyList() throws Exception {
        assertEquals(0, foodTypeDao.getAll().size());
    }

    @Test
    public void deleteByIdDeletesCorrectFoodtype() throws Exception {
        Foodtype foodtype = setupNewFoodtype();
        foodTypeDao.add(foodtype);
        foodTypeDao.deleteById(foodtype.getId());
        assertEquals(0, foodTypeDao.getAll().size());
    }

    @Test
    public void clearAll() throws Exception {
        Foodtype testFoodtype = setupNewFoodtype();
        Foodtype otherFoodtype = setupNewFoodtype();
        foodTypeDao.clearAll();
        assertEquals(0, foodTypeDao.getAll().size());
    }

    @Test
    public void addFoodTypeToRestaurantAddsTypeCorrectly() throws Exception {

        Restaurant testRestaurant = setupRestaurant();
        Restaurant altRestaurant = setupAltRestaurant();

        restaurantDao.add(testRestaurant);
        restaurantDao.add(altRestaurant);

        Foodtype testFoodtype = setupNewFoodtype();

        foodTypeDao.add(testFoodtype);

        foodTypeDao.addFoodtypeToRestaurant(testFoodtype, testRestaurant);
        foodTypeDao.addFoodtypeToRestaurant(testFoodtype, altRestaurant);

        assertEquals(2, foodTypeDao.getAllRestaurantsForAFoodtype(testFoodtype.getId()).size());
    }

    @Test
    public void deleteingRestaurantAlsoUpdatesJoinTable() throws Exception {
        Foodtype testFoodtype  = new Foodtype("Seafood");
        foodTypeDao.add(testFoodtype);

        Restaurant testRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);

        Restaurant altRestaurant = setupAltRestaurant();
        restaurantDao.add(altRestaurant);

        restaurantDao.addRestaurantToFoodType(testRestaurant,testFoodtype);
        restaurantDao.addRestaurantToFoodType(altRestaurant, testFoodtype);

        restaurantDao.deleteById(testRestaurant.getId());
        assertEquals(0, restaurantDao.getAllFoodtypesByRestaurant(testRestaurant.getId()).size());
    }

    // helpers

    public Foodtype setupNewFoodtype(){
        return new Foodtype("Sushi");
    }

    public Restaurant setupRestaurant (){
        Restaurant restaurant = new Restaurant("Fish Omena", "214 NE Safaricom", "97232", "254-402-9874", "http://fishwitch.com", "hellofishy@fishwitch.com");
        restaurantDao.add(restaurant);
        return restaurant;
    }

    public Restaurant setupAltRestaurant (){
        Restaurant restaurant = new Restaurant("Fish Omena", "214 NE Safaricom", "97232", "254-402-9874");
        restaurantDao.add(restaurant);
        return restaurant;
    }
}