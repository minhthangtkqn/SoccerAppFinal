package com.example.tlds.testdrawerlayout;

/**
 * Created by PC on 4/27/2016.
 */
public class Match {
    private String field_name;
    private String maximum_players, price, date, match_id, number_players, user_id;

    public Match(String field_name, String maximum_players, String price, String date, String match_id, String number_players, String user_id){
        this.field_name = field_name;
        this.maximum_players = maximum_players;
        this.price = price;
        this.date = date;
        this.match_id = match_id;
        this.number_players = number_players;
        this.user_id = user_id;
    }

    public String getMaximum_players() {
        return maximum_players;
    }

    public void setMaximum_players(String maximum_players) {
        this.maximum_players = maximum_players;
    }

    public String getField_name() {
        return field_name;
    }

    public void setField_name(String field_name) {
        this.field_name = field_name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMatch_id() {
        return match_id;
    }

    public void setMatch_id(String match_id) {
        this.match_id = match_id;
    }

    public String getNumber_players() {
        return number_players;
    }

    public void setNumber_players(String number_players) {
        this.number_players = number_players;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
