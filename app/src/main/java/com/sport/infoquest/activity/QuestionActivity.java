package com.sport.infoquest.activity;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.sport.infoquest.R;
import com.sport.infoquest.entity.Question;
import com.sport.infoquest.entity.User;
import com.sport.infoquest.util.JSONResponse;
import com.sport.infoquest.util.RestService;
import com.sport.infoquest.util.Status;
import com.sport.infoquest.util.StatusCode;
import com.sport.infoquest.util.Utils;

import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class QuestionActivity extends Activity {
    JSONResponse response;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_redesign);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent intent = getIntent();

        user = User.getInstance();
        List<RadioButton> radioList = new ArrayList<>();
        final Question question = (Question) intent.getSerializableExtra("questionObject");
        TextView questionText = (TextView) findViewById(R.id.questionText);
        TextView questionPoint = (TextView) findViewById(R.id.questionPointText);
        RadioButton Rb1 = (RadioButton) findViewById(R.id.radioButton1);
        RadioButton Rb2 = (RadioButton) findViewById(R.id.radioButton2);
        RadioButton Rb3 = (RadioButton) findViewById(R.id.radioButton3);
        RadioButton Rb4 = (RadioButton) findViewById(R.id.radioButton4);
        radioList.add(Rb1);
        radioList.add(Rb2);
        radioList.add(Rb3);
        radioList.add(Rb4);
        Collections.shuffle(radioList);

        String[] wrongAnswer = question.getWrongAnswer().split(",");
        questionText.setText(question.getText());
        questionPoint.setText(questionPoint.getText().toString().replace("x", question.getPoint()));

        radioList.get(0).setText(question.getCorrectAnswer());
        radioList.get(1).setText(wrongAnswer[0]);
        radioList.get(2).setText(wrongAnswer[1]);
        radioList.get(3).setText(wrongAnswer[2]);

        radioList.get(0).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    response = RestService.setCorrectAnswer(user.getUsername(), String.valueOf(question.getId()));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (Utils.checkStatusResponse(response)) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this);
                    builder.setTitle("Raspuns Corect");
                    builder.setMessage("Felicitari. Ai castigat " + question.getPoint() + " punct!");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Scaneaza urmatorul cod", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(getApplicationContext(), ScanQR.class);
                            i.putExtra("questionId", String.valueOf(question.getId()));
                            if (response.getResponseCode() == StatusCode.GONE.getCode()) {
                                i.putExtra("finishGame", "true");
                            }
                            startActivity(i);
                            finish();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();

                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this);
                    builder.setTitle("Eroare");
                    builder.setMessage("Ceva a mers gresit in validarea raspunsului!");
                    builder.setCancelable(false);
                    builder.setPositiveButton("Scaneaza urmatorul cod", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            Intent i = new Intent(getApplicationContext(), ScanQR.class);
                            startActivity(i);
                            finish();
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

        radioList.get(1).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    response = RestService.setWrongAnswer(user.getUsername(), String.valueOf(question.getId()));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this);
                builder.setTitle("Raspuns Gresit");
                builder.setMessage("0 puncte primite");
                builder.setCancelable(false);
                builder.setPositiveButton("Scaneaza urmatorul cod", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(getApplicationContext(), ScanQR.class);
                        i.putExtra("questionId", String.valueOf(question.getId()));
                        if (response.getResponseCode() == StatusCode.GONE.getCode()) {
                            i.putExtra("finishGame", "true");
                        }
                        startActivity(i);
                        finish();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        radioList.get(2).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    response = RestService.setWrongAnswer(user.getUsername(), String.valueOf(question.getId()));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this);
                builder.setTitle("Raspuns Gresit");
                builder.setMessage("0 puncte primite");
                builder.setCancelable(false);
                builder.setPositiveButton("Scaneaza urmatorul cod", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(getApplicationContext(), ScanQR.class);
                        i.putExtra("questionId", String.valueOf(question.getId()));
                        if (response.getResponseCode() == StatusCode.GONE.getCode()) {
                            i.putExtra("finishGame", "true");
                        }
                        startActivity(i);
                        finish();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });

        radioList.get(3).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                try {
                    response = RestService.setWrongAnswer(user.getUsername(), String.valueOf(question.getId()));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this);
                builder.setTitle("Raspuns Gresit");
                builder.setMessage("0 puncte primite");
                builder.setCancelable(false);
                builder.setPositiveButton("Scaneaza urmatorul cod", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent i = new Intent(getApplicationContext(), ScanQR.class);
                        i.putExtra("questionId", String.valueOf(question.getId()));
                        if (response.getResponseCode() == StatusCode.GONE.getCode()) {
                            i.putExtra("finishGame", "true");
                        }
                        startActivity(i);
                        finish();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();

            }
        });


    }


}
