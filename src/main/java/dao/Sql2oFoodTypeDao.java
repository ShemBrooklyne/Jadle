package dao;

import model.Foodtype;
import model.Restaurant;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.ArrayList;
import java.util.List;

public class Sql2oFoodTypeDao implements FoodTypeDao {
    private final Sql2o sql2o;


    public Sql2oFoodTypeDao(Sql2o sql2o) { this.sql2o = sql2o; }

    @Override
    public void add(Foodtype foodtype) {
        String sql = "INSERT INTO foodtypes (name) VALUES (:name);"; //if you change your model, be sure to update here as well!
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql, true)
                    .bind(foodtype)
                    .executeUpdate()
                    .getKey();
            foodtype.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }


    @Override
    public List<Foodtype> getAll() {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM foodtypes")
                    .executeAndFetch(Foodtype.class);

        }
    }

    @Override
    public Foodtype findFoodById(int id){
        String sql = "SELECT * FROM foodtypes WHERE id = :id";
        try(Connection conn = sql2o.open()){
            return conn.createQuery(sql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Foodtype.class);
        }
    }

    @Override
    public void addFoodtypeToRestaurant(Foodtype foodtype, Restaurant restaurant){
        String sql = "INSERT INTO restaurants_foodtypes (restaurantid, foodtypeid) VALUES (:restaurantId, :foodtypeId)";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("restaurantId", restaurant.getId())
                    .addParameter("foodtypeId", foodtype.getId())
                    .executeUpdate();
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public List<Restaurant> getAllRestaurantsForAFoodtype(int foodtypeId) {
        ArrayList<Restaurant> restaurants = new ArrayList<>();

        String joinQuery = "SELECT restaurantid FROM restaurants_foodtypes WHERE foodtypeid = :foodtypeId";

        try (Connection con = sql2o.open()) {
            List<Integer> allRestaurantIds = con.createQuery(joinQuery)
                    .addParameter("foodtypeId", foodtypeId)
                    .executeAndFetch(Integer.class); //what is happening in the lines above?
            for (Integer restaurantId : allRestaurantIds){
                String restaurantQuery = "SELECT * FROM restaurants WHERE id = :restaurantId";
                restaurants.add(
                        con.createQuery(restaurantQuery)
                                .addParameter("restaurantId", restaurantId)
                                .executeAndFetchFirst(Restaurant.class));
            } //why are we doing a second sql query - set?
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
        return restaurants;
    }


    @Override
    public void deleteById(int id) {
        String sql = "DELETE from foodtypes WHERE id = :id";
        String deleteJoin = "DELETE from restaurants_foodtypes WHERE restaurantid = :restaurantId";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
            con.createQuery(deleteJoin)
                    .addParameter("restaurantId", id)
                    .executeUpdate();

        } catch (Sql2oException ex){
            System.out.println(ex);
        }
    }

    @Override
    public void clearAll() {
        String sql = "DELETE FROM foodtypes";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql).executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }
}
