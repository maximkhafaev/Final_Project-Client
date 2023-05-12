package com.maximkhafaev.pilotquiz.API_Data_Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class User_Choice {
    @SerializedName("questionID")
    @Expose
    public int questionid;

    @SerializedName("user_choice")
    @Expose
    public String userChoice;

    public User_Choice(int questionid, String userChoice) {
        this.questionid = questionid;
        this.userChoice = userChoice;
    }
}
