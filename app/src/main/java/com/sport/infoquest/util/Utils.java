package com.sport.infoquest.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sport.infoquest.R;
import com.sport.infoquest.activity.QR;
import com.sport.infoquest.activity.StartGame;
import com.sport.infoquest.entity.Game;
import com.sport.infoquest.entity.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

/**
 * Created by Ionut on 21/10/2016.
 */
public class Utils {

    public static boolean isEntryValid(String username, String password) {
        return !username.isEmpty() && !password.isEmpty();
    }

    public static boolean checkStatusResponse(JSONResponse response) {
        if (response.getStatusCode() == StatusCode.OK || response.getStatusCode() == StatusCode.GONE)
            return true;
        else
            return false;
    }

    public static Bitmap getRoundedShape(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static void showMessage(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static boolean checkIfAlreadyScan(final String questionId) {
        final String username = User.getInstance().getUsername();
        Log.w(QR.class.getName(), "username: " + username);
        try {

            JSONResponse response = RestService.postCheckAndSet(username, questionId);
            if (response.getResponseCode() == StatusCode.OK.getCode()) {
                return true;
            }
            if (response.getResponseCode() == StatusCode.ALREADY_EXISTS.getCode()) {
                return false;
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void showDialogStartGame(final Context context, String title, String message, boolean setCancel, String okButtonText) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(setCancel);
        builder.setPositiveButton(okButtonText, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                Intent i = new Intent(context, StartGame.class);
                context.startActivity(i);
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    public static void addFragment(Fragment fragment, String stackName, FragmentManager fragmentManager) {
        FragmentManager manager = fragmentManager;
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.container, fragment, stackName);
        transaction.addToBackStack(stackName);
        transaction.commit();
    }

    public static void removeFragment(String fragmentName, FragmentManager fragmentManager) {
        FragmentManager manager = fragmentManager;
        FragmentTransaction transaction = manager.beginTransaction();
        Fragment fragment = manager.findFragmentByTag(fragmentName);
        transaction.remove(fragment);
        transaction.commit();
        manager.popBackStack();
    }

    public static void replaceFragment(Fragment fragment, String stackName, FragmentManager fragmentManager) {
        FragmentManager manager = fragmentManager;
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, fragment, stackName);
        transaction.commit();
    }
}
