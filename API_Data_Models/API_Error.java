package com.maximkhafaev.pilotquiz.API_Data_Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class API_Error {

    @SerializedName("error")
    @Expose
    public String error_message;

    public String getError_message() {
        return error_message;
    }
}
