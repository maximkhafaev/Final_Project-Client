package com.maximkhafaev.pilotquiz.API_Data_Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Test_Info {

    @SerializedName("id_test")
    @Expose
    public Integer idTest;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("is_finished")
    @Expose
    public Integer isFinished;

    @SerializedName("result")
    @Expose
    public Integer result;

    @SerializedName("max_res")
    @Expose
    public Integer maxRes;

    @SerializedName("req_res")
    @Expose
    public Integer reqRes;

    public Integer getIdTest() {
        return idTest;
    }

    public String getName() {
        return name;
    }

    public Integer getIsFinished() {
        return isFinished;
    }

    public Integer getResult() {
        return result;
    }

    public Integer getMaxRes() {
        return maxRes;
    }

    public Integer getReqRes() {
        return reqRes;
    }
}
