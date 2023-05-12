package com.maximkhafaev.pilotquiz;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.gson.Gson;
import com.maximkhafaev.pilotquiz.API.ServerAPI_Client;
import com.maximkhafaev.pilotquiz.API_Data_Models.API_Error;
import com.maximkhafaev.pilotquiz.API_Data_Models.Question_Model;
import com.maximkhafaev.pilotquiz.API_Data_Models.Test_Result;
import com.maximkhafaev.pilotquiz.API_Data_Models.User_Choice;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Test extends AppCompatActivity {
    private ProgressBar test_progress;
    private TextView done_questions_num, total_questions, timer, curr_question_number, question;
    private RadioGroup answers_group;
    private RadioButton optA, optB, optC;
    private Button send_answer_bttn;
    private int question_counter = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question_window);
        int test_id = getIntent().getIntExtra("testid", 0);
        int max_res = getIntent().getIntExtra("max_res", 0);
        int req_res = getIntent().getIntExtra("required_res", 0);
        String testname = getIntent().getStringExtra("testname");

        //Init
        test_progress = findViewById(R.id.test_progressBar);
        done_questions_num = findViewById(R.id.done_question_num);
        total_questions = findViewById(R.id.total_question_num);
        timer = findViewById(R.id.timer);
        curr_question_number = findViewById(R.id.question_number);
        question = findViewById(R.id.question);
        answers_group = findViewById(R.id.answers);
        optA = findViewById(R.id.option_a);
        optB = findViewById(R.id.option_b);
        optC = findViewById(R.id.option_c);
        send_answer_bttn = findViewById(R.id.send_answer_bttn);

        total_questions.setText(String.valueOf(max_res));
        test_progress.setMax(max_res);

        Call<List<Question_Model>> call = ServerAPI_Client.getInstance().getAPI().getQuestions(getAuthHeader(), test_id);
        call.enqueue(new Callback<List<Question_Model>>() {
            @Override
            public void onResponse(Call<List<Question_Model>> call, Response<List<Question_Model>> response) {
                if (response.isSuccessful()) {
                    List<Question_Model> questionsList = response.body();
                    testing(questionsList);
                } else {
                    API_Error message = new Gson().fromJson(response.errorBody().charStream(), API_Error.class);
                    Toast.makeText(Test.this, String.valueOf(message.getError_message()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Question_Model>> call, Throwable t) {
                t.printStackTrace();
            }

            private void testing(List<Question_Model> questionsList) {
                answers_group.clearCheck();
                int question_id = questionsList.get(question_counter).getId();
                test_progress.setProgress(question_counter);
                done_questions_num.setText(String.valueOf(question_counter));
                curr_question_number.setText("Вопрос №\u0020" + (question_counter + 1));
                question.setText(questionsList.get(question_counter).getQuestion());
                optA.setText(questionsList.get(question_counter).getOptionA());
                optB.setText(questionsList.get(question_counter).getOptionB());
                optC.setText(questionsList.get(question_counter).getOptionC());
                if ((question_counter + 1) == questionsList.size()) {
                    send_answer_bttn.setText("Завершить тестирование");
                    send_answer_bttn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (optA.isChecked() || optB.isChecked() || optC.isChecked()) {
                                send_answer(question_id);
                                showBottomSheetRESULT(testname, req_res, max_res, test_id);
                            } else {
                                Toast.makeText(Test.this, "Выберите ответ!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }); } else {
                    send_answer_bttn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (optA.isChecked() || optB.isChecked() || optC.isChecked()) {
                                send_answer(question_id);
                                question_counter++;
                                testing(questionsList);
                            } else {
                                Toast.makeText(Test.this, "Выберите ответ!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
            private void send_answer(int question_id) {
                RadioButton opt_selected = findViewById(answers_group.getCheckedRadioButtonId());
                User_Choice user_choice = new User_Choice(question_id, String.valueOf(opt_selected.getText()));
                Call<Void> call1 = ServerAPI_Client.getInstance().getAPI().sendAnswer(getAuthHeader(), user_choice);
                call1.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (!response.isSuccessful()) {
                            API_Error message = new Gson().fromJson(response.errorBody().charStream(), API_Error.class);
                            Toast.makeText(Test.this, String.valueOf(message.getError_message()), Toast.LENGTH_SHORT).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });
    }

    private void showBottomSheetRESULT(String testname, int required, int max_res, int test_id) {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(Test.this);
        View bottomSheetView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.test_result_bottomsheet, (LinearLayout) findViewById(R.id.BSH_result_LL));
        LinearLayout resultLL = bottomSheetView.findViewById(R.id.BSH_res_LL_questions);
        resultLL.setVisibility(View.GONE);
        LinearLayout requiredLL = bottomSheetView.findViewById(R.id.BSH_res_LL_need_correct);
        requiredLL.setVisibility(View.GONE);
        TextView testnameBSH = bottomSheetView.findViewById(R.id.BSH_res_testname);
        TextView isSuccessful = bottomSheetView.findViewById(R.id.BSH_res_isSuccessful);
        TextView user_res = bottomSheetView.findViewById(R.id.BSH_res_user_res);
        TextView max_resBSH = bottomSheetView.findViewById(R.id.BSH_res_max_res);
        TextView req_resBSH = bottomSheetView.findViewById(R.id.BSH_res_required_result);
        Button back_to_tests_bttn = bottomSheetView.findViewById(R.id.BSH_start_testBttn);
        back_to_tests_bttn.setVisibility(View.INVISIBLE);
        testnameBSH.setVisibility(View.GONE);
        ProgressBar load = bottomSheetView.findViewById(R.id.loading);
        load.setVisibility(View.VISIBLE);
        Call<Test_Result> call = ServerAPI_Client.getInstance().getAPI().getTestResult(getAuthHeader(), test_id);
        call.enqueue(new Callback<Test_Result>() {
            @Override
            public void onResponse(Call<Test_Result> call, Response<Test_Result> response) {
                if (response.isSuccessful()) {
                    Test_Result test_result = response.body();
                    if (test_result.userResult >= required) {
                        load.setVisibility(View.GONE);
                        resultLL.setVisibility(View.VISIBLE);
                        requiredLL.setVisibility(View.VISIBLE);
                        back_to_tests_bttn.setVisibility(View.VISIBLE);
                        testnameBSH.setVisibility(View.VISIBLE);
                        isSuccessful.setText("Пройден успешно");
                        user_res.setText(String.valueOf(test_result.userResult));
                    } else {
                        load.setVisibility(View.GONE);
                        resultLL.setVisibility(View.VISIBLE);
                        requiredLL.setVisibility(View.VISIBLE);
                        back_to_tests_bttn.setVisibility(View.VISIBLE);
                        testnameBSH.setVisibility(View.VISIBLE);
                        isSuccessful.setText("Не пройден");
                        user_res.setText(String.valueOf(test_result.userResult));
                    }
                } else {
                    API_Error message = new Gson().fromJson(response.errorBody().charStream(), API_Error.class);
                    Toast.makeText(Test.this, String.valueOf(message.getError_message()), Toast.LENGTH_SHORT).show();
                }
            };
            @Override
            public void onFailure(Call<Test_Result> call, Throwable t) {
                t.printStackTrace();
            }
        });
        testnameBSH.setText(testname);
        max_resBSH.setText(String.valueOf(max_res));
        req_resBSH.setText(String.valueOf(required));
        back_to_tests_bttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Test.this, TestsList.class);
                startActivity(intent);
            }
        });
        bottomSheetDialog.setCancelable(false);
        bottomSheetDialog.setContentView(bottomSheetView);
        bottomSheetDialog.show();
    }

    private String getAuthHeader() {
        SharedPreferences preferences = getSharedPreferences("com.maximkhafaev.pilotquiz_SHPR", Context.MODE_PRIVATE);
        return "Bearer " + preferences.getString("USER_TOKEN", "");
    }
}
