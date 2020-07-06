package com.amply.recipefinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.amply.recipefinder.R;
import com.amply.recipefinder.utils.Util;
import com.google.firebase.auth.FirebaseAuth;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SignInActivity extends AppCompatActivity {


    @BindView(R.id.ll_camera_open)
    LinearLayout llCameraOpen;
    @BindView(R.id.card_sign_up)
    CardView cardSignUp;
    @BindView(R.id.tv_sign_in)
    TextView tvSignIn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        ButterKnife.bind(this);


        cardSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.checkConnection(SignInActivity.this)) {
                    startActivity(new Intent(SignInActivity.this, RegisterActivity.class));
                }else{
                    Toast.makeText(SignInActivity.this, "Please check your internet connections", Toast.LENGTH_SHORT).show();
                }
            }
        });


        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Util.checkConnection(SignInActivity.this)) {
                    startActivity(new Intent(SignInActivity.this, LoginActivity.class));
                }else {
                    Toast.makeText(SignInActivity.this, "Please check your internet connections", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}