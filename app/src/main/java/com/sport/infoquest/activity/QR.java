package com.sport.infoquest.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.Result;
import com.sport.infoquest.R;
import com.sport.infoquest.entity.Question;
import java.util.ArrayList;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QR extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private static final String TAG = "QR";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_qr);
        mScannerView = (ZXingScannerView) findViewById(R.id.scanner_view);
        mScannerView.startCamera(); //error on this line
        mScannerView.setResultHandler(this);

    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();   // Stop camera on pause
    }

//    @Override
//    public void onResume() {
//        super.onResume();
//        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
//        mScannerView.startCamera();          // Start camera on resume
//    }

    @Override
    public void handleResult(final Result rawResult) {
            final String questId = rawResult.getText();
            FirebaseDatabase.getInstance().getReference().child("questions")
                    .addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    GenericTypeIndicator<ArrayList<Question>> genericType = new GenericTypeIndicator<ArrayList<Question>>() {};
                                    ArrayList<Question> questionList = dataSnapshot.getValue(genericType);
                                    boolean isCorrectScanned = false;
                                    Question scannedQuestion = new Question();
                                    for (Question question:questionList){
                                        if (question.getId().equals(questId)){
                                            isCorrectScanned = true;
                                            scannedQuestion = question;
                                        }
                                    }
                                    if (isCorrectScanned){
                                        goToQuestionActivity(rawResult, scannedQuestion);
                                    } else {
//                                        scannedQuestion
//                                        JSONResponse responseQ = RestService.getQuestionById(rawResult.getText());
//                                        Question question = Factory.createQuestionObject(responseQ);
//                                        Log.w(QR.class.getName(), "checking question id " + String.valueOf(question.getId()));
//                                        if (!Utils.checkIfAlreadyScan(String.valueOf(question.getId()))) {
//                                            Toast.makeText(this.getApplicationContext(), "Aceasta intrebare a mai fost scanata !",
//                                                    Toast.LENGTH_LONG).show();
//                                            //   Intent i = new Intent(getApplicationContext(), ScanQR.class);
//                                            //  startActivity(i);
//                                        } else {

                                        Toast.makeText(getApplicationContext(), "Cod invalid !",
                                                Toast.LENGTH_LONG).show();
                                        resumeCamera();
                                        }
                                    }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Log.e(TAG, "onCancelled", databaseError.toException());
                                    //stopProgressDialog();
                                }
    });
        }
    private void goToQuestionActivity(Result rawResult, Question scannedQuestion) {
        Log.e("handler", rawResult.getText()); // Prints scan results
        Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)
        Intent i = new Intent(this, QuestionActivity.class);
        i.putExtra("questionObject", scannedQuestion);
        startActivity(i);
        finish();
        }

    private void resumeCamera() {
        mScannerView.resumeCameraPreview(this);
    }


    @Override
    public void onBackPressed() {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }
}
