package dao;

import model.Foodtype;
import model.Restaurant;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;

public class Sql2oRestaurantDaoTest {
    private Sql2oRestaurantDao restaurantDao;
    private Sql2oFoodTypeDao foodTypeDao;
    private Connection conn;


    @Before
    public void setUp() throws Exception {
        String connectionString = "jdbc:h2:mem:testing;INIT=RUNSCRIPT from 'classpath:DB/create.sql';";
        Sql2o sql2o = new Sql2o(connectionString,"","");
        restaurantDao = new Sql2oRestaurantDao(sql2o);
        foodTypeDao = new Sql2oFoodTypeDao(sql2o);
        conn = sql2o.open();
    }

    @After
    public void tearDown() throws Exception {
        restaurantDao.clearAll();
        conn.close();
    }


    @Test
    public void  testSaveWorks(){
        Restaurant restaurant = new Restaurant("kims","072672", "72627662", "089278828");
        restaurantDao.add(restaurant);
        assertEquals(1, restaurantDao.getAll().size());
    }

    @Test
    public void RestaurantReturnsFoodtypesCorrectly() throws Exception {
        Foodtype testFoodtype  = setupNewFoodtype();
        foodTypeDao.add(testFoodtype);

        Foodtype otherFoodtype  = setupNewFoodtype();
        foodTypeDao.add(otherFoodtype);

        Restaurant testRestaurant = setupRestaurant();
        restaurantDao.add(testRestaurant);
        restaurantDao.addRestaurantToFoodType(testRestaurant,testFoodtype);
        restaurantDao.addRestaurantToFoodType(testRestaurant,otherFoodtype);

        Foodtype[] foodtypes = {testFoodtype, otherFoodtype}; //oh hi what is this? Observe how we use its assertion below.
        System.out.println(Arrays.toString(foodtypes));
        assertEquals(Arrays.asList(foodtypes), restaurantDao.getAllFoodtypesByRestaurant(testRestaurant.getId()));
    }

    //helper functions
    public Restaurant setupRestaurant() {
        Restaurant restaurant = new Restaurant("Mama Kiwinya", "878272672", "0100", "0782874673", "https://mamakiwinya.github.oi", "kenhyoiitr@gmail.com");
        restaurantDao.add(restaurant);
        return restaurant;
    }

    public Restaurant setupAltRestaurant(){
        Restaurant restaurant1 = new Restaurant("WINDERGUARDLTD","034500","02100","06094512");
        restaurantDao.add(restaurant1);
        return restaurant1;

    }

    public Foodtype setupNewFoodtype(){
        Foodtype foodtype = new Foodtype("Samchi-gui");
        return foodtype;
    }
}

