package dao;

import model.Foodtype;
import model.Restaurant;

import java.util.List;

public interface FoodTypeDao {

    //create
    void add(Foodtype foodtype);
    void addFoodtypeToRestaurant(Foodtype foodtype, Restaurant restaurant);


    //read
    List<Foodtype> getAll();
    Foodtype findFoodById(int id);
    List<Restaurant> getAllRestaurantsForAFoodtype(int id);

    //update
    //omit for now

    //delete
    void deleteById(int id);

    void clearAll();
}
