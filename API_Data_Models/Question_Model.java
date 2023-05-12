package com.maximkhafaev.pilotquiz.API_Data_Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Question_Model {

    @SerializedName("id")
    @Expose
    public Integer id;

    @SerializedName("Question")
    @Expose
    public String question;

    @SerializedName("Option_a")
    @Expose
    public String optionA;

    @SerializedName("Option_b")
    @Expose
    public String optionB;

    @SerializedName("Option_c")
    @Expose
    public String optionC;

    public Integer getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

}
