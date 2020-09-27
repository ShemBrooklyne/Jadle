package model;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

public class Review implements Comparable<Review> {
    private String content;
    private String writtenBy;
    private int rating;
    private int id;
    private int restaurantId; //will be used to connect Restaurant to Review (one-to-many)
    private long createdat;
    private String formattedCreatedAt;

    public Review(String content, String writtenBy, int rating, int restaurantId) {
        this.content = content;
        this.writtenBy = writtenBy;
        this.rating = rating;
        this.restaurantId = restaurantId;
        this.createdat = System.currentTimeMillis();
        setFormattedCreatedAt(); //we'll make me in a minute
    }

    //Create comparison

    @Override
    public int compareTo(Review reviewObject) {
        if (this.createdat < reviewObject.createdat)
        {
            return -1; //this object was made earlier than the second object.
        }
        else if (this.createdat > reviewObject.createdat){ //this object was made later than the second object
            return 1;
        }
        else {
            return 0; //they were made at the same time, which is very unlikely, but mathematically not impossible.
        }
    }


    //Hashcode n equals override


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Review)) return false;
        Review review = (Review) o;
        return rating == review.rating &&
                id == review.id &&
                restaurantId == review.restaurantId &&
                Objects.equals(content, review.content) &&
                Objects.equals(writtenBy, review.writtenBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(content, writtenBy, rating, id, restaurantId);
    }

    //Getters
    public String getContent() {
        return content;
    }

    public String getWrittenBy() {
        return writtenBy;
    }

    public int getRating() {
        return rating;
    }

    public int getId() {
        return id;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public long getCreatedat() {
        return createdat;
    }

    public String getFormattedCreatedAt(){
        Date date = new Date(createdat);
        String datePatternToUse = "MM/dd/yyyy @ K:mm a"; //see https://docs.oracle.com/javase/7/docs/api/java/text/SimpleDateFormat.html
        SimpleDateFormat sdf = new SimpleDateFormat(datePatternToUse);
        return sdf.format(date);
    }


    //Setters

    public void setContent(String content) {
        this.content = content;
    }

    public void setWrittenBy(String writtenBy) {
        this.writtenBy = writtenBy;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public void setCreatedat() {
        this.createdat = System.currentTimeMillis(); // It'll become clear soon why we need this explicit setter
    }

    public void setFormattedCreatedAt(){
        Date date = new Date(this.createdat);
        String datePatternToUse = "MM/dd/yyyy @ K:mm a";
        SimpleDateFormat sdf = new SimpleDateFormat(datePatternToUse);
        this.formattedCreatedAt = sdf.format(date);
    }




}
