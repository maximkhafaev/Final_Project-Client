package com.maximkhafaev.pilotquiz.API_Data_Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Login_Data {

    @SerializedName("username")
    @Expose
    public String username;

    @SerializedName("password")
    @Expose
    public String password;

    public Login_Data(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
