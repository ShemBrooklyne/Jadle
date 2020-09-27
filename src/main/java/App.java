import com.google.gson.Gson;
import dao.Sql2oFoodTypeDao;
import dao.Sql2oRestaurantDao;
import dao.Sql2oReviewDao;
import exceptions.ApiExceptions;
import model.Foodtype;
import model.Restaurant;
import model.Review;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.HashMap;
import java.util.Map;

import static spark.Spark.*;

public class App {

    public static void main(String[] args) {
        Sql2oFoodTypeDao foodtypeDao;
        Sql2oRestaurantDao restaurantDao;
        Sql2oReviewDao reviewDao;
        Connection conn;
        Gson gson = new Gson();

        String connectionString = "jdbc:h2:~/jadle.db;INIT=RUNSCRIPT from 'classpath:DB/create.sql'";
        Sql2o sql2o = new Sql2o(connectionString, "", "");

        restaurantDao = new Sql2oRestaurantDao(sql2o);
        foodtypeDao = new Sql2oFoodTypeDao(sql2o);
        reviewDao = new Sql2oReviewDao(sql2o);
        conn = sql2o.open();


        get("/restaurants", "application/json", (req, res) -> { //accept a request in format JSON from an app
            System.out.println(restaurantDao.getAll());
            return gson.toJson(restaurantDao.getAll());//send it back to be displayed
        });

        get("/restaurants/:id", "application/json", (req, res) -> { //accept a request in format JSON from an app
            res.type("application/json");
            int restaurantId = Integer.parseInt(req.params("id"));

            if (restaurantDao.findById(restaurantId) == null){
                throw new ApiExceptions(404, String.format("No restaurant with the id: \"%s\" exists", req.params("id")));
            }
            return gson.toJson(restaurantDao.findById(restaurantId));
        });

        get("/restaurants", "application/json", (req, res) -> {
            System.out.println(restaurantDao.getAll());

            if(restaurantDao.getAll().size() > 0){
                return gson.toJson(restaurantDao.getAll());
            }

            else {
                return "{\"message\":\"I'm sorry, but no restaurants are currently listed in the database.\"}";
            }

        });

        get("foodtypes/:id", "application/json", (request, response) -> {
            int target = Integer.parseInt(request.params("id"));
            Foodtype foodtype =  foodtypeDao.findFoodById(target);
            if(foodtype != null){
                return gson.toJson(foodtype);
            }else{
                throw new Error("There currently no restaurants with the aid ID");
            }
        });



        get("/restaurants/:id/foodtypes", "application/json", (req, res) -> {
            int restaurantId = Integer.parseInt(req.params("id"));
            Restaurant restaurantToFind = restaurantDao.findById(restaurantId);
            if (restaurantToFind == null){
                throw new Error(String.format("No restaurant with the id: \"%s\" exists", req.params("id")));
            }
            else if (restaurantDao.getAllFoodtypesByRestaurant(restaurantId).size()==0){
                return "{\"message\":\"I'm sorry, but no foodtypes are listed for this restaurant.\"}";
            }
            else {
                return gson.toJson(restaurantDao.getAllFoodtypesByRestaurant(restaurantId));
            }
        });



        get("/foodtypes/:id/restaurants", "application/json", (req, res) -> {
            int foodtypeId = Integer.parseInt(req.params("id"));
            Foodtype foodtypeToFind = foodtypeDao.findFoodById(foodtypeId);
            if (foodtypeToFind == null){
                throw new Error(String.format("No foodtype with the id: \"%s\" exists", req.params("id")));
            }
            else if (foodtypeDao.getAllRestaurantsForAFoodtype(foodtypeId).size()==0){
                return "{\"message\":\"I'm sorry, but no restaurants are listed for this foodtype.\"}";
            }
            else {
                return gson.toJson(foodtypeDao.getAllRestaurantsForAFoodtype(foodtypeId));
            }
        });

//        get("/restaurants/:id/reviews", "application/json", (req, res) -> {
//            int restaurantId = Integer.parseInt(req.params("id"));
//
//            Restaurant restaurantToFind = restaurantDao.findById(restaurantId);
//            List<Review> allReviews;
//
//            if (restaurantToFind == null){
//                throw new ApiException(404, String.format("No restaurant with the id: \"%s\" exists", req.params("id")));
//            }
//
//            allReviews = reviewDao.getAllReviewsByRestaurant(restaurantId);
//
//            return gson.toJson(allReviews);
//        });


        //POST REQUESTS

        post("/restaurants/new", "application/json", (req, res) -> {
            Restaurant restaurant = gson.fromJson(req.body(), Restaurant.class);
            restaurantDao.add(restaurant);
            res.status(201);
            return gson.toJson(restaurant);
        });

        post("/restaurants/:restaurantId/reviews/new", "application/json", (req, res) -> {
            int restaurantId = Integer.parseInt(req.params("restaurantId"));
            Review review = gson.fromJson(req.body(), Review.class);

            review.setRestaurantId(restaurantId); //we need to set this separately because it comes from our route, not our JSON input.
            reviewDao.add(review);
            res.status(201);
            return gson.toJson(review);
        });


        post("/foodtypes/new", "application/json", (req, res) -> {
            Foodtype foodtype = gson.fromJson(req.body(), Foodtype.class);
            foodtypeDao.add(foodtype);
            res.status(201);
            return gson.toJson(foodtype);
        });



        post("/restaurant/:id/update", "application/json", (request, response) -> {
            int restaurantId = Integer.parseInt(request.params("id"));
            Restaurant target = restaurantDao.findById(restaurantId);
            Restaurant update = gson.fromJson(request.body(), Restaurant.class);
            return null;
        });

        post("/restaurants/:restaurantId/foodtype/:foodtypeId", "application/json", (req, res) -> {
            int restaurantId = Integer.parseInt(req.params("restaurantId"));
            int foodtypeId = Integer.parseInt(req.params("foodtypeId"));
            Restaurant restaurant = restaurantDao.findById(restaurantId);
            Foodtype foodtype = foodtypeDao.findFoodById(foodtypeId);

            if (restaurant != null && foodtype != null){
                //both exist and can be associated - we should probably not connect things that are not here.
                foodtypeDao.addFoodtypeToRestaurant(foodtype, restaurant);
                res.status(201);
                return gson.toJson(String.format("Restaurant '%s' serves Foodtype '%s' which is a re-known Italian dish",restaurant.getName(), foodtype.getName()));
            }
            else {
                throw new Error("Restaurant or Foodtype does not exist");
            }
        });

        //FILTERS
        exception(ApiExceptions.class, (exc, req, res) -> {
            ApiExceptions err = exc;
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("status", err.getStatusCode());
            jsonMap.put("errorMessage", err.getMessage());
            res.type("application/json"); //after does not run in case of an exception.
            res.status(err.getStatusCode()); //set the status
            res.body(gson.toJson(jsonMap));  //set the output.
        });

        after((req, res) ->{
            res.type("application/json");
        });
    }
}