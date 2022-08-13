package com.app.citycareservice.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.app.citycareservice.R;
import com.app.citycareservice.utils.Params;
import com.app.citycareservice.utils.SharedPrefHandler;

public class SplashActivity extends AppCompatActivity {


    private Activity activity = SplashActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPrefHandler prefHandler = new SharedPrefHandler(activity);
                if (prefHandler.hasKey(Params.SP_KEY_USER_ID)) {
                    Intent i = new Intent(activity, MainActivity.class);
                    startActivity(i);
                    finish();
                    return;
                }

                Intent i = new Intent(activity, LoginActivity.class);
                startActivity(i);
                finish();
            }
        }, 2000);


    }
}