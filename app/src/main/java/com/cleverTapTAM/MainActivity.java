package com.cleverTapTAM;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.clevertap.android.sdk.CleverTapAPI;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
/*
    Please Note :
    Extra code has been added for added support of Telephony API in ABD setup. Dynamic explicit permission is required for READ_PHONE_STATE.
    CleverTAP Direct initialization has been removed to avoid getting initial pop-up dialog box and load simple activity.
 */
    //Permission State Callback value
    private static final int PERMISSION_READ_STATE = 0;
    private Button processBtn;
    //Clever SDK Object to be initialized after Permission Check
    CleverTapAPI cleverTapAPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission Not available for PHONE STATE.
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Permission Uavailable",
                    Toast.LENGTH_SHORT);
            toast.show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, PERMISSION_READ_STATE);
        } else {
            // Permission available for PHONE STATE.
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Permission Available",
                    Toast.LENGTH_SHORT);
            toast.show();
            //initiate explicit - CleverTap SDK
            initiaLizeCleverTap();
        }
    }

    //Callback for handling dynamic permissions addition
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_READ_STATE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission granted!
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Permission Granted",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    initiaLizeCleverTap();
                } else {
                    // permission denied
                    Toast toast = Toast.makeText(getApplicationContext(),
                            "Permission Denied",
                            Toast.LENGTH_SHORT);
                    toast.show();
                }
                return;
            }
        }
    }

    //initialize CleverTab SDK with default instance
    public void initiaLizeCleverTap() {
        cleverTapAPI = CleverTapAPI.getDefaultInstance(getApplicationContext());
        //adding notification channel
        cleverTapAPI.createNotificationChannel(getApplicationContext(),"1","1","Test Description", NotificationManager.IMPORTANCE_MAX,true);
        processBtn = findViewById(R.id.processBtn);
        processBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //push profile information
                pushProfileInfo();
                //push Product Viewed Event
                pushEventInfo();
            }
        });
    }

    public void pushProfileInfo() {
        HashMap<String, Object> profileUpdate = new HashMap<String, Object>();
        //building required email as specified
        String email = "dk+" + "arghajyoti" + "@clevertap.com";
        profileUpdate.put("Email", email);
        profileUpdate.put("Name", "Arghajyoti");

        //updating profile
        cleverTapAPI.pushProfile(profileUpdate);
        Toast toast = Toast.makeText(getApplicationContext(),
                "Profile Updated",
                Toast.LENGTH_SHORT);
        toast.show();
    }

    public void pushEventInfo() {
        HashMap<String, Object> prodViewedAction = new HashMap<String, Object>();
        prodViewedAction.put("Product Name", "CleverTap");
        prodViewedAction.put("Product ID", 1);
        prodViewedAction.put("Product Image", "https://d35fo82fjcw0y8.cloudfront.net/2018/07/26020307/customer-success-clevertap.jpg");

        //update event info
        cleverTapAPI.pushEvent("Product viewed", prodViewedAction);

        Toast toast = Toast.makeText(getApplicationContext(),
                "Event View Updated",
                Toast.LENGTH_SHORT);
        toast.show();
    }

}