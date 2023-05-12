package com.maximkhafaev.pilotquiz.API_Data_Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Login_Response {

    @SerializedName("token")
    @Expose
    public String user_token;

    public String getUser_token() {
        return user_token;
    }
}
