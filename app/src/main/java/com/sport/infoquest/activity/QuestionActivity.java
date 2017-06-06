package com.sport.infoquest.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.sport.infoquest.R;
import com.sport.infoquest.entity.CurrentScore;
import com.sport.infoquest.entity.LeaderBoard;
import com.sport.infoquest.entity.Question;
import com.sport.infoquest.entity.Quests;
import com.sport.infoquest.entity.User;
import com.sport.infoquest.entity.UserInfo;
import com.sport.infoquest.util.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.sport.infoquest.enums.Drawer.SCAN_QR;

public class QuestionActivity extends BaseActivity {
    public static final String TAG = "QuestionActivity";
    User user;
    private DatabaseReference scoreReference;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_redesign);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        Intent intent = getIntent();

        scoreReference = FirebaseDatabase.getInstance().getReference().child("scores").child("currentScore").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
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

        String[] wrongAnswer = question.getOtherAnswer().split(",");
        questionText.setText(question.getText());
        questionPoint.setText(questionPoint.getText().toString().replace("x", question.getPoint()));

        radioList.get(0).setText(question.getCorrectAnswer());
        radioList.get(1).setText(wrongAnswer[0]);
        radioList.get(2).setText(wrongAnswer[1]);
        radioList.get(3).setText(wrongAnswer[2]);

        radioList.get(0).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showProgressDialog();
                updateCorrectAnswer(question);
            }
        });

        radioList.get(1).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showProgressDialog();
                updateWrongAnswer(question);
            }
        });

        radioList.get(2).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showProgressDialog();
                updateWrongAnswer(question);
            }
        });

        radioList.get(3).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                showProgressDialog();
                updateWrongAnswer(question);
            }
        });

    }

    private void updateCorrectAnswer(final Question question) {
        FirebaseDatabase.getInstance().getReference()
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.d(TAG, " -> " + FirebaseAuth.getInstance().getCurrentUser().getUid() + " responded correct!");

                                DataSnapshot currentScoreSnapshot = dataSnapshot.child("scores").child("currentScore").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                GenericTypeIndicator<CurrentScore> genericTypeScore = new GenericTypeIndicator<CurrentScore>() {};
                                final CurrentScore currentScore  = currentScoreSnapshot.getValue(genericTypeScore);
                                int newCorrectAnswer = currentScore.getCorrectAnswer() + 1;
                                int newCurrentScore = Integer.valueOf(question.getPoint()) + currentScore.getCurrentScore();
                                ArrayList<Quests> inProgress = currentScore.getInProgress();
                                if (inProgress.size() > 0) {
                                    Quests questToRemove = new Quests();
                                    for (Quests quest : inProgress) {
                                        if (quest.getId().equals(question.getId())) {
                                            questToRemove = quest;
                                        }
                                    }
                                    inProgress.remove(questToRemove);
                                }
                                int newRemainingQuests = currentScore.getRemainingQuests() - 1;
                                scoreReference.child("correctAnswer").setValue(newCorrectAnswer);
                                currentScore.setCorrectAnswer(newCorrectAnswer);
                                scoreReference.child("currentScore").setValue(newCurrentScore);
                                currentScore.setCurrentScore(newCurrentScore);
                                scoreReference.child("remainingQuests").setValue(newRemainingQuests);
                                scoreReference.child("inProgress").setValue(inProgress);


                                if (inProgress.size() == 0){
                                    scoreReference.child("status").setValue("Complete");
                                    DataSnapshot leaderBoardSnapshot =  dataSnapshot.child("scores").child("leaderBoard").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    GenericTypeIndicator<LeaderBoard> genericTypeLeaderBoard = new GenericTypeIndicator<LeaderBoard>() {};
                                    LeaderBoard leaderBoard = leaderBoardSnapshot.getValue(genericTypeLeaderBoard);
                                    DataSnapshot userSnapshot =  dataSnapshot.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                    if (leaderBoard == null){
                                        GenericTypeIndicator<UserInfo> genericTypeUser = new GenericTypeIndicator<UserInfo>() {};
                                        UserInfo userInfo = userSnapshot.getValue(genericTypeUser);

                                        leaderBoard = new LeaderBoard();
                                        leaderBoard.setDisplayName(userInfo.getUsername());
                                        leaderBoard.setEmail(userInfo.getEmail());
                                        leaderBoard.setTotalScore(currentScore.getCurrentScore());
                                        leaderBoard.setUid(userInfo.getUid());
                                        leaderBoard.getFinishedGames().add(currentScore);
                                    } else {
                                        leaderBoard.setTotalScore(leaderBoard.getTotalScore() + currentScore.getCurrentScore());
                                        leaderBoard.getFinishedGames().add(currentScore);
                                    }
                                    FirebaseDatabase.getInstance().getReference().child("users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("onTrack").setValue(false);
                                    FirebaseDatabase.getInstance().getReference().child("scores").child("leaderBoard")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(leaderBoard);
                                    User.getInstance().setOnTrack(false);
                                    updateUiFinishGame();
                                } else {
                                    updateUiCorrent(question);
                                }
                                stopProgressDialog();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e(TAG, "onCancelled", databaseError.toException());
                                stopProgressDialog();
                            }
                        });
    }

    private void updateWrongAnswer(final Question question) {
        FirebaseDatabase.getInstance().getReference().addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                Log.d(TAG, " -> " + FirebaseAuth.getInstance().getCurrentUser().getUid() + " responded wrong!");

                                DataSnapshot currentScoreSnapshot = dataSnapshot.child("scores").child("currentScore").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                GenericTypeIndicator<CurrentScore> genericTypeScore = new GenericTypeIndicator<CurrentScore>() {};
                                final CurrentScore currentScore  = currentScoreSnapshot.getValue(genericTypeScore);
                                int newWrongAnswer = currentScore.getWrongAnswer() + 1;
                                ArrayList<Quests> inProgress = currentScore.getInProgress();
                                if (inProgress.size() > 0) {
                                    Quests questToRemove = new Quests();
                                    for (Quests quest : inProgress) {
                                        if (quest.getId().equals(question.getId())) {
                                            questToRemove = quest;
                                        }
                                    }
                                    inProgress.remove(questToRemove);
                                }
                                int newRemainingQuests = currentScore.getRemainingQuests() - 1;
                                scoreReference.child("wrongAnswer").setValue(newWrongAnswer);
                                currentScore.setWrongAnswer(newWrongAnswer);
                                scoreReference.child("remainingQuests").setValue(newRemainingQuests);
                                currentScore.setRemainingQuests(newRemainingQuests);
                                scoreReference.child("inProgress").setValue(inProgress);

                                if (inProgress.size() == 0){
                                    scoreReference.child("status").setValue("Complete");
                                    DataSnapshot leaderBoardSnapshot =  dataSnapshot.child("scores").child("leaderBoard").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                    GenericTypeIndicator<LeaderBoard> genericTypeLeaderBoard = new GenericTypeIndicator<LeaderBoard>() {};
                                    LeaderBoard leaderBoard = leaderBoardSnapshot.getValue(genericTypeLeaderBoard);

                                    if (leaderBoard == null){
                                        DataSnapshot userSnapshot =  dataSnapshot.child("users").child(FirebaseAuth.getInstance().getCurrentUser().getUid());
                                        GenericTypeIndicator<UserInfo> genericTypeUser = new GenericTypeIndicator<UserInfo>() {};
                                        UserInfo userInfo = userSnapshot.getValue(genericTypeUser);

                                        leaderBoard = new LeaderBoard();
                                        leaderBoard.setDisplayName(userInfo.getUsername());
                                        leaderBoard.setEmail(userInfo.getEmail());
                                        leaderBoard.setTotalScore(currentScore.getCurrentScore());
                                        leaderBoard.setUid(userInfo.getUid());
                                        leaderBoard.getFinishedGames().add(currentScore);
                                    } else {
                                        leaderBoard.setTotalScore(leaderBoard.getTotalScore() + currentScore.getCurrentScore());
                                        leaderBoard.getFinishedGames().add(currentScore);
                                    }

                                    FirebaseDatabase.getInstance().getReference().child("users")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .child("onTrack").setValue(false);
                                    FirebaseDatabase.getInstance().getReference().child("scores").child("leaderBoard")
                                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                            .setValue(leaderBoard);
                                    User.getInstance().setOnTrack(false);
                                    updateUiFinishGame();
                                } else {
                                    updateUiWrong();
                                }
                                stopProgressDialog();
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.e(TAG, "onCancelled", databaseError.toException());
                                stopProgressDialog();
                            }
                        });
    }

    private void updateUiCorrent(final Question question) {
            AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this);
            builder.setTitle("Raspuns Corect");
            builder.setMessage("Felicitari. Ai castigat " + question.getPoint() + " punct!");
            builder.setCancelable(false);
            builder.setPositiveButton("Scaneaza urmatorul cod", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
    }

    private void updateUiWrong() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this);
        builder.setTitle("Raspuns Gresit");
        builder.setMessage("0 puncte primite");
        builder.setCancelable(false);
        builder.setPositiveButton("Scaneaza urmatorul cod", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void updateUiFinishGame() {
        AlertDialog.Builder builder = new AlertDialog.Builder(QuestionActivity.this);

        builder.setTitle("Felicitari");
        builder.setMessage("Ati terminat aventura !");
        builder.setCancelable(false);
        builder.setPositiveButton("Finalizeaza", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
//                Fragment fragment = getFragmentManager().findFragmentByTag(SCAN_QR.getName());
//
//                getFragmentManager().beginTransaction().remove(fragment).commit();
//                getFragmentManager().popBackStack();

                finish();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
