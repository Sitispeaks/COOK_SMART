package com.amply.recipefinder.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.amply.recipefinder.R;
import com.amply.recipefinder.utils.SharedPreferencesManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RegisterActivity extends AppCompatActivity {

    @BindView(R.id.et_signup_name)
    EditText etSignupName;
    @BindView(R.id.et_signup_email)
    EditText etSignupEmail;
    @BindView(R.id.et_signup_pass)
    EditText etSignupPass;
    @BindView(R.id.et_signup_cnf_pass)
    EditText etSignupCnfPass;
    @BindView(R.id.card_btn_signup)
    CardView cardBtnSignup;
    @BindView(R.id.tv_sign_in)
    TextView tvSignIn;


    FirebaseAuth firebaseAuth;
    GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);

        firebaseAuth = FirebaseAuth.getInstance();

        etSignupPass.setTransformationMethod(new PasswordTransformationMethod());
        etSignupCnfPass.setTransformationMethod(new PasswordTransformationMethod());

        cardBtnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etSignupEmail.getText().toString();
                String password = etSignupPass.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Please fill in the required fields", Toast.LENGTH_SHORT).show();
                    etSignupEmail.setError("Empty Field");
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Please fill in the required fields", Toast.LENGTH_SHORT).show();
                    etSignupPass.setError("Empty Field");
                    return;
                }

                if (etSignupCnfPass.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password must be at least 6 characters", Toast.LENGTH_SHORT).show();
                    etSignupCnfPass.setError("Empty Field");
                    return;
                }

                if (!etSignupPass.getText().toString().trim().equals(etSignupCnfPass.getText().toString().trim())) {
                    Toast.makeText(RegisterActivity.this, "Password doesn't Match", Toast.LENGTH_SHORT).show();
                    return;
                }

                firebaseAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    SharedPreferencesManager.saveUserCheck(RegisterActivity.this, true);
                                    SharedPreferencesManager.saveFirebaseUser(RegisterActivity.this, "" + firebaseAuth.getCurrentUser().getEmail());

                                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                            .setDisplayName("" + etSignupName.getText().toString().trim()).build();
                                    firebaseAuth.getCurrentUser().updateProfile(profileUpdates);
                                    startActivity(new Intent(getApplicationContext(), IngredientsActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "E-mail or password is wrong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });


    }


}