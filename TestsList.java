package com.maximkhafaev.pilotquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.maximkhafaev.pilotquiz.API.ServerAPI_Client;
import com.maximkhafaev.pilotquiz.API_Data_Models.API_Error;
import com.maximkhafaev.pilotquiz.API_Data_Models.Test_Info;
import com.maximkhafaev.pilotquiz.API_Data_Models.User_Model;
import com.maximkhafaev.pilotquiz.Recycle_Views.AdapterFin;
import com.maximkhafaev.pilotquiz.Recycle_Views.AdapterUnfin;
import com.maximkhafaev.pilotquiz.Recycle_Views.Interface_RV;
import com.maximkhafaev.pilotquiz.Recycle_Views.ItemDecorations;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestsList extends AppCompatActivity implements Interface_RV {
    private RecyclerView recyclerView_fin, recyclerView_unfin;
    private AdapterUnfin adapter_unfin;
    private AdapterFin adapter_fin;
    private ItemDecorations ItemDecorator;
    private Button logoutBttn;
    private TextView userFIO, userINFO, available_text, finished_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tests_lists);

        // Init
        logoutBttn = findViewById(R.id.logoutBttn);
        userFIO = findViewById(R.id.userFIO);
        userINFO = findViewById(R.id.userInfo);
        available_text = findViewById(R.id.textAvailable);
        finished_text = findViewById(R.id.textFinished);

        logoutBttn.setOnClickListener(v -> logout());

        inputUserInfo();

        Resources resources = getResources();
        ItemDecorator = new ItemDecorations(resources.getDimensionPixelSize(R.dimen.margins));

        recyclerView_unfin = findViewById(R.id.unfinished_tests);
        recyclerView_unfin.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_unfin.addItemDecoration(ItemDecorator);

        recyclerView_fin = findViewById(R.id.finished_tests);
        recyclerView_fin.setLayoutManager(new LinearLayoutManager(this));
        recyclerView_fin.addItemDecoration(ItemDecorator);

        reqTestsLists(this);
    }

    private String getAuthHeader() {
        SharedPreferences preferences = getSharedPreferences("com.maximkhafaev.pilotquiz_SHPR", Context.MODE_PRIVATE);
        return "Bearer " + preferences.getString("USER_TOKEN", "");
    }

    private void inputUserInfo() {
        Call<User_Model> call = ServerAPI_Client.getInstance().getAPI().user(getAuthHeader());
        call.enqueue(new Callback<User_Model>() {
            @Override
            public void onResponse(Call<User_Model> call, Response<User_Model> response) {
                if (response.isSuccessful()) {
                    User_Model resp = response.body();
                    userFIO.setText(resp.user_fio);
                    userINFO.setText(resp.user_info);
                } else {
                    API_Error message = new Gson().fromJson(response.errorBody().charStream(), API_Error.class);
                    Toast.makeText(TestsList.this, String.valueOf(message.getError_message()), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<User_Model> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void reqTestsLists(Interface_RV interface_rv) {
        List<Test_Info> finished = new ArrayList<Test_Info>();
        List<Test_Info> unfinished = new ArrayList<Test_Info>();
        Call<List<Test_Info>> call = ServerAPI_Client.getInstance().getAPI().getAvailableTests(getAuthHeader());
        call.enqueue(new Callback<List<Test_Info>>() {
            @Override
            public void onResponse(Call<List<Test_Info>> call, Response<List<Test_Info>> response) {
                if (response.isSuccessful()) {
                    List<Test_Info> list = response.body();
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getIsFinished() == 0) {
                            unfinished.add(list.get(i));
                        } else {
                            finished.add(list.get(i));
                        }
                    }
                    if (unfinished.size() != 0) {
                        adapter_unfin = new AdapterUnfin(interface_rv, unfinished);
                        recyclerView_unfin.setAdapter(adapter_unfin);
                    } else {
                        available_text.setText("Доступно (0)");
                    }
                    if (finished.size() != 0) {
                        adapter_fin = new AdapterFin(interface_rv, finished);
                        recyclerView_fin.setAdapter(adapter_fin);
                    } else {
                        finished_text.setText("Завершены (0)");
                    }
               } else {
                    API_Error message = new Gson().fromJson(response.errorBody().charStream(), API_Error.class);
                    Toast.makeText(TestsList.this, String.valueOf(message.getError_message()), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Test_Info>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }


    private void logout() {
        SharedPreferences preferences = getSharedPreferences("com.maximkhafaev.pilotquiz_SHPR", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("USER_TOKEN", "");
        editor.apply();
        sendToLoginActivity();
    }

    private void sendToLoginActivity() {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(intent);
    }

    private void showBottomSheetUNFIN(Test_Info test_info) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(TestsList.this);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.test_start_bottomsheet, (LinearLayout) findViewById(R.id.BSH_LL));
        TextView testname = bottomSheetView.findViewById(R.id.BSH_testname);
        TextView max_res = bottomSheetView.findViewById(R.id.BSH_max_res);
        TextView req_res = bottomSheetView.findViewById(R.id.BSH_required_result);
        Button start_test = bottomSheetView.findViewById(R.id.BSH_start_testBttn);
        testname.setText(test_info.getName());
        max_res.setText(String.valueOf(test_info.getMaxRes()));
        req_res.setText(String.valueOf(test_info.getReqRes()));
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
        start_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestsList.this, Test.class);
                intent.putExtra("testid", test_info.getIdTest());
                intent.putExtra("max_res", test_info.getMaxRes());
                intent.putExtra("testname", test_info.getName());
                intent.putExtra("required_res", test_info.getReqRes());
                startActivity(intent);
            }
        });
    }
    private void showBottomSheetFIN(Test_Info test_info) {
        BottomSheetDialog bottomSheetDialog2 = new BottomSheetDialog(TestsList.this);
        View bottomSheetView2 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.test_fin_bottomsheet, (LinearLayout) findViewById(R.id.BSH_fin_LL));
        TextView testname = bottomSheetView2.findViewById(R.id.BSH_fin_testname);
        TextView isSuccessful = bottomSheetView2.findViewById(R.id.BSH_fin_isSuccessful);
        TextView user_res = bottomSheetView2.findViewById(R.id.BSH_fin_user_res);
        TextView max_res = bottomSheetView2.findViewById(R.id.BSH_fin_max_res);
        TextView req_res = bottomSheetView2.findViewById(R.id.BSH_fin_required_result);
        testname.setText(test_info.getName());
        user_res.setText(String.valueOf(test_info.getResult()));
        max_res.setText(String.valueOf(test_info.getMaxRes()));
        req_res.setText(String.valueOf(test_info.getReqRes()));
        if (test_info.getResult() >= test_info.getReqRes()) {
            isSuccessful.setText("Успешно");
        } else {
            isSuccessful.setText("Неуспешно");
        }
        bottomSheetDialog2.setCancelable(true);
        bottomSheetDialog2.setContentView(bottomSheetView2);
        bottomSheetDialog2.show();
    }

    @Override
    public void onItemClick_UNFINISHED(List<Test_Info> data, int position) {
        showBottomSheetUNFIN(data.get(position));
    }

    @Override
    public void onItemClickFINISHED(List<Test_Info> data, int position) {
        showBottomSheetFIN(data.get(position));
    }
}
