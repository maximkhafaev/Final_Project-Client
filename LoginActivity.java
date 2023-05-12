package com.maximkhafaev.pilotquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.maximkhafaev.pilotquiz.API.ServerAPI_Client;
import com.maximkhafaev.pilotquiz.API_Data_Models.API_Error;
import com.maximkhafaev.pilotquiz.API_Data_Models.Login_Data;
import com.maximkhafaev.pilotquiz.API_Data_Models.Login_Response;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText inputUsername, inputPassword;
    private Button bttnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // Init
        inputUsername = findViewById(R.id.usernameInput);
        inputPassword = findViewById(R.id.passwordInput);
        bttnLogin = findViewById(R.id.loginBttn);

        bttnLogin.setOnClickListener(v -> validateData());
    }

    private void validateData() {
        String username = inputUsername.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        if (username.isEmpty()){
            inputUsername.setError("Введите Ваш логин!");
            inputUsername.requestFocus();
            return;
        }
        if (password.isEmpty()){
            inputPassword.setError("Введите Ваш пароль!");
            inputUsername.requestFocus();
        } else {
            doLogin(username, password);
        }
    }

    private void doLogin(String username, String password) {
        SharedPreferences preferences = getSharedPreferences("com.maximkhafaev.pilotquiz_SHPR", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Login_Data login_data = new Login_Data(username, password);
        Call<Login_Response> call = ServerAPI_Client.getInstance().getAPI().login(login_data);
        call.enqueue(new Callback<Login_Response>() {
            @Override
            public void onResponse(Call<Login_Response> call, Response<Login_Response> response) {
                if (response.isSuccessful()) {
                    Login_Response resp = response.body();
                    Toast.makeText(LoginActivity.this, "Вход выполнен!", Toast.LENGTH_SHORT).show();
                    editor.putString("USER_TOKEN", String.valueOf(resp.getUser_token()));
                    editor.apply();
                    sendToTestsActivity();
                } else {
                    API_Error message = new Gson().fromJson(response.errorBody().charStream(), API_Error.class);
                    Toast.makeText(LoginActivity.this, String.valueOf(message.getError_message()), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Login_Response> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void sendToTestsActivity() {
        Intent intent = new Intent(getApplicationContext(), TestsList.class);
        startActivity(intent);
    }
}