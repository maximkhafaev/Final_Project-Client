package com.maximkhafaev.pilotquiz.API_Data_Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User_Model {

    @SerializedName("FIO")
    @Expose
    public String user_fio;

    @SerializedName("Info")
    @Expose
    public String user_info;

    public String getUser_fio() {
        return user_fio;
    }

    public String getUser_info() {
        return user_info;
    }
}
