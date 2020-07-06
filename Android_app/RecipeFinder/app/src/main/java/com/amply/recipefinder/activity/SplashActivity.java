package com.amply.recipefinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.amply.recipefinder.R;
import com.amply.recipefinder.utils.SharedPreferencesManager;

public class SplashActivity extends AppCompatActivity {

    private static int SPLASH_SCREEN_TIME_OUT=2000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                boolean userLoggedIn = SharedPreferencesManager.getUserCheck(SplashActivity.this);

                if (!userLoggedIn){
                    Intent i=new Intent(SplashActivity.this,
                            SignInActivity.class);
                    //Intent is used to switch from one activity to another.

                    startActivity(i);
                    //invoke the SecondActivity.

                    finish();
                }else{
                    Intent i=new Intent(SplashActivity.this,
                            IngredientsActivity.class);
                    //Intent is used to switch from one activity to another.

                    startActivity(i);
                    //invoke the SecondActivity.

                    finish();
                }


                //the current activity will get finished.
            }
        }, SPLASH_SCREEN_TIME_OUT);




    }
}