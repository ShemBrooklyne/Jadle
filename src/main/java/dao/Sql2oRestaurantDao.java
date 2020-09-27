package dao;

import model.Foodtype;
import model.Restaurant;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import java.util.ArrayList;
import java.util.List;

public class Sql2oRestaurantDao implements RestaurantDao {


    private final Sql2o sql2o;
    public Sql2oRestaurantDao(Sql2o sql2o) { this.sql2o = sql2o; }

    @Override
    public void add(Restaurant restaurant) {
        String sql = "INSERT INTO restaurants (name, address, zipcode, phone, website, email) VALUES (:name, :address, :zipcode, :phone, :website, :email)";
        try (Connection con = sql2o.open()) {
            int id = (int) con.createQuery(sql, true)
                    .bind(restaurant)
                    .executeUpdate()
                    .getKey();
            restaurant.setId(id);
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }
    @Override
    public List<Restaurant> getAll() {
        try (Connection con = sql2o.open()) {
            System.out.println(con.createQuery("SELECT * FROM restaurants")
                    .executeAndFetch(Restaurant.class));
            return con.createQuery("SELECT * FROM restaurants")
                    .executeAndFetch(Restaurant.class);
        }
    }

    @Override
    public Restaurant findById(int restaurantId) {
        try (Connection con = sql2o.open()) {
            return con.createQuery("SELECT * FROM restaurants WHERE id = :id")
                    .addParameter("id", restaurantId)
                    .executeAndFetchFirst(Restaurant.class);
        }
    }

    @Override
    public void addRestaurantToFoodType(Restaurant restaurant, Foodtype foodtype){
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
    public List<Foodtype> getAllFoodtypesByRestaurant(int restaurantId){
        ArrayList<Foodtype> foodtypes = new ArrayList<>();

        String joinQuery = "SELECT foodtypeid FROM restaurants_foodtypes WHERE restaurantid = :restaurantid";

        try (Connection con = sql2o.open()) {
            List<Integer> allFoodtypesIds = con.createQuery(joinQuery)
                    .addParameter("restaurantid", restaurantId)
                    .executeAndFetch(Integer.class);
            for (Integer foodId : allFoodtypesIds){
                String foodtypeQuery = "SELECT * FROM foodtypes WHERE id = :foodtypeId";
                foodtypes.add(
                        con.createQuery(foodtypeQuery)
                                .addParameter("foodtypeId", foodId)
                                .executeAndFetchFirst(Foodtype.class));
            }
        } catch (Sql2oException ex){
            System.out.println(ex);
        }
        return foodtypes;
    }


    @Override
    public void update(int id, String name, String address, String zipcode, String phone, String website, String email){
        String sql = "UPDATE restaurants SET (id,name, address, zipcode, phone, website, email)=(:id, :name, :address, :zipcode, :phone, :website, :email) WHERE id=:id";
        try(Connection con = sql2o.open()){
            con.createQuery(sql)
                    .addParameter("name", name)
                    .addParameter("address", address)
                    .addParameter("zipcode", zipcode)
                    .addParameter("phone", phone)
                    .addParameter("website", website)
                    .addParameter("email", email)
                    .executeUpdate();
        }
    }


    @Override
    public void deleteById(int id) {
        String sql = "DELETE from restaurants WHERE id=:id";
        String deleteJoin = "DELETE from restaurants_foodtypes WHERE restaurantid = :restaurantid";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql)
                    .addParameter("id", id)
                    .executeUpdate();
            con.createQuery(deleteJoin)
                    .addParameter("restaurantid", id)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }

    @Override
    public void clearAll() {
        String sql = "DELETE from restaurants";
        try (Connection con = sql2o.open()) {
            con.createQuery(sql).executeUpdate();
        } catch (Sql2oException ex) {
            System.out.println(ex);
        }
    }
}
