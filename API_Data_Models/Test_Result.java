package com.maximkhafaev.pilotquiz.API_Data_Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Test_Result {

    @SerializedName("user_result")
    @Expose
    public Integer userResult;

    @SerializedName("max_result")
    @Expose
    public Integer maxResult;

    public Integer getUserResult() {
        return userResult;
    }

    public Integer getMaxResult() {
        return maxResult;
    }
}
