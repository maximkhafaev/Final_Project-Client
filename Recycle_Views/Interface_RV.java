package com.maximkhafaev.pilotquiz.Recycle_Views;

import com.maximkhafaev.pilotquiz.API_Data_Models.Test_Info;

import java.util.List;

public interface Interface_RV {
    void onItemClick_UNFINISHED(List<Test_Info> data, int position);
    void onItemClickFINISHED(List<Test_Info> data, int position);
}
