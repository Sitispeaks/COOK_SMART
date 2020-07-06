package com.amply.recipefinder.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.amply.recipefinder.R;
import com.amply.recipefinder.utils.SharedPreferencesManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileActivity extends AppCompatActivity {

    @BindView(R.id.img_profile)
    ImageView imgProfile;
    @BindView(R.id.card_view_recipe_image)
    CardView cardViewRecipeImage;
    @BindView(R.id.tv_profile_name)
    TextView tvProfileName;
    @BindView(R.id.tv_profile_email)
    TextView tvProfileEmail;
    @BindView(R.id.tv_profile_contact)
    TextView tvProfileContact;
    @BindView(R.id.tv_profile_recipe_count)
    TextView tvProfileRecipeCount;
    @BindView(R.id.tv_profile_ingr_count)
    TextView tvProfileIngrCount;
    @BindView(R.id.ll_camera_open)
    LinearLayout llCameraOpen;
    @BindView(R.id.card_back)
    CardView cardBack;
    @BindView(R.id.tv_update_profile)
    TextView tvUpdateProfile;

    private FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        ButterKnife.bind(this);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        int[] images = {R.drawable.pro_1, R.drawable.pro_2,
                R.drawable.pro_3, R.drawable.pro_4,
                R.drawable.pro_5, R.drawable.pro_6,
                R.drawable.pro_7, R.drawable.pro_8};
        Random rand = new Random();

        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int randomImageCnt = rand.nextInt(images.length);
                imgProfile.setImageResource(images[randomImageCnt]);
                SharedPreferencesManager.setUserProfileImage(ProfileActivity.this, randomImageCnt);
//                Toast.makeText(ProfileActivity.this, "I set image to "+randomImageCnt, Toast.LENGTH_SHORT).show();
            }
        });



        if (SharedPreferencesManager.getUserProfileImage(ProfileActivity.this) != 0) {
            int imageCount = SharedPreferencesManager.getUserProfileImage(ProfileActivity.this);
//            Toast.makeText(this, "profile Image at: "+imageCount, Toast.LENGTH_SHORT).show();
            imgProfile.setImageResource(images[imageCount]);
        } else {
            imgProfile.setImageResource(images[rand.nextInt(images.length)]);
        }

        tvUpdateProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LayoutInflater inflater = LayoutInflater.from(ProfileActivity.this);
                final View vv = inflater.inflate(R.layout.alert_update_profile, null);
                final AlertDialog.Builder alert = new AlertDialog.Builder(
                        ProfileActivity.this);

                alert.setView(vv);

                final AlertDialog dialog = alert.create();
                dialog.show();


                TextView tvUpdate = vv.findViewById(R.id.tv_alert_update_profile);
                EditText etName = vv.findViewById(R.id.et_update_profile_name);
                EditText etEmail = vv.findViewById(R.id.et_update_profile_email);
                TextView tvCancel = vv.findViewById(R.id.tv_alert_update_profile_cancel);

                etEmail.setClickable(false);
                etEmail.setText("" + firebaseUser.getEmail());

                tvUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName("" + etName.getText().toString().trim()).build();
                        firebaseUser.updateProfile(profileUpdates);
                        tvProfileName.setText(firebaseUser.getDisplayName());

                        dialog.cancel();
                    }
                });


                tvCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.cancel();
                    }
                });


            }


        });


        tvProfileName.setText(firebaseUser.getDisplayName());

        if (TextUtils.isEmpty(firebaseUser.getDisplayName())) {
            Toast.makeText(this, "Please Update Profile", Toast.LENGTH_SHORT).show();
        }

        tvProfileContact.setText(firebaseUser.getPhoneNumber());
        tvProfileEmail.setText(firebaseUser.getEmail());
        tvProfileIngrCount.setText("0 favorite Ingredients");
        if (SharedPreferencesManager.getAttempedRecipes(ProfileActivity.this) != null) {
            tvProfileRecipeCount.setText(String.format("%d Attempted Recipes", SharedPreferencesManager.getAttempedRecipes(ProfileActivity.this).size()));
        } else {
            tvProfileRecipeCount.setText("0 Attempted Recipes");
        }

//        imgProfile

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder().setDisplayName("John Smith").build();
//
//        user.updateProfile(profileUpdates);


//        Glide.with(this)
//                .load("" + firebaseUser.getPhotoUrl())
//                .placeholder(R.drawable.splash_screen)
//                .into(imgProfile);

        cardBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}