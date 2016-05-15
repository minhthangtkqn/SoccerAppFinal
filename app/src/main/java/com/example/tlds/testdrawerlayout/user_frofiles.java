package com.example.tlds.testdrawerlayout;

/**
 * Created by PC on 4/26/2016.
 */
public class user_frofiles {
    private String username, password, email, phone_number;

    public user_frofiles(){

    }

    public user_frofiles(String username, String password, String email, String phone_number){
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone_number = phone_number;
    }

    public String getUsername(){
        return username;
    }

    public String getEmail(){
        return email;
    }

    public String getPhone_number(){
        return phone_number;
    }

    public user_frofiles setUsername(String username){
        this.username = username;
        return this;
    }

    public user_frofiles setEmail(String email){
        this.email = email;
        return this;
    }

    public user_frofiles setPhone(String phone_number){
        this.phone_number = phone_number;
        return this;
    }


}
