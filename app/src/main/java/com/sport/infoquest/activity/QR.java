package com.sport.infoquest.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.zxing.Result;
import com.sport.infoquest.R;
import com.sport.infoquest.entity.Question;
import com.sport.infoquest.entity.User;
import com.sport.infoquest.util.Factory;
import com.sport.infoquest.util.JSONResponse;
import com.sport.infoquest.util.RestService;
import com.sport.infoquest.util.StatusCode;
import com.sport.infoquest.util.Utils;

import org.json.JSONException;

import java.io.IOException;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class QR extends Activity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView mScannerView;
    private RestService restService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        restService = new RestService();
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
    public void handleResult(Result rawResult) {
        // Do something with the result here
        try {
            JSONResponse response = RestService.postQuestionById(User.getInstance().getUsername(), rawResult.getText());
            if (response.getStatusCode().equals(StatusCode.NOT_FOUND)) {
                Toast.makeText(this.getApplicationContext(), "Cod invalid !",
                        Toast.LENGTH_LONG).show();
                mScannerView.resumeCameraPreview(this);
            } else {
                JSONResponse responseQ = RestService.getQuestionById(rawResult.getText());
                Question question = Factory.createQuestionObject(responseQ);
                Log.w(QR.class.getName(), "checking question id " + String.valueOf(question.getId()));
                if (!Utils.checkIfAlreadyScan(String.valueOf(question.getId()))) {
                    Toast.makeText(this.getApplicationContext(), "Aceasta intrebare a mai fost scanata !",
                            Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), ScanQR.class);
                    startActivity(i);
                } else {
                    Log.e("handler", rawResult.getText()); // Prints scan results
                    Log.e("handler", rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode)
                    Intent i = new Intent(this, QuestionActivity.class);
                    i.putExtra("questionObject", question);
                    startActivity(i);
                    finish();
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        // code here to show dialog
        super.onBackPressed();  // optional depending on your needs
    }
}
