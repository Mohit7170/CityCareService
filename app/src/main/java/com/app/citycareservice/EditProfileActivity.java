package com.app.citycareservice;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.citycareservice.service.UpdateProfileService;
import com.app.citycareservice.utils.HelperClass;
import com.app.citycareservice.utils.Params;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Objects;

public class EditProfileActivity extends AppCompatActivity implements Params {

    private static final String TAG = "EditProfileActivity";

    private Activity activity = EditProfileActivity.this;

    private ImageView back_iv;
    private TextInputLayout name_tif;
    private TextInputLayout email_tif;
    private TextInputLayout phone_tif;

    private MaterialButton update_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        back_iv = findViewById(R.id.back_iv);
        name_tif = findViewById(R.id.name_tif);
        email_tif = findViewById(R.id.email_tif);
        phone_tif = findViewById(R.id.phone_tif);
        update_btn = findViewById(R.id.update_btn);

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });

    }

    private void validateForm() {

        name_tif.setErrorEnabled(false);
        email_tif.setErrorEnabled(false);
        phone_tif.setErrorEnabled(false);

        String nameRegex = "^[A-Za-z]+$";

        String name = Objects.requireNonNull(name_tif.getEditText()).getText().toString().trim();
        String firstName = name;
        String lastName = "";
        String email = Objects.requireNonNull(email_tif.getEditText()).getText().toString().trim();
        String phone = Objects.requireNonNull(phone_tif.getEditText()).getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            name_tif.setError("Name Cannot be Empty");
            name_tif.setFocusable(true);
            return;
        }
        if (!name.matches(nameRegex)) {
            name_tif.setError("Invalid Name");
            name_tif.setFocusable(true);
            return;
        }

        if (TextUtils.isEmpty(email)) {
            email_tif.setError("Email Cannot be Empty");
            email_tif.setFocusable(true);
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            email_tif.setError("Invalid Email Id");
            email_tif.setFocusable(true);
            return;
        }

        if (TextUtils.isEmpty(phone)) {
            phone_tif.setError("Phone Cannot be Empty");
            return;
        }
        if (Patterns.PHONE.matcher(phone).matches()) {
            phone_tif.setError("Invalid Phone NUmber");
            return;
        }

        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_USER_FIRST_NAME, firstName);
        bundle.putString(BUNDLE_KEY_USER_EMAIL, email);

//        if (isProfileImgSelected)
//            bundle.putString(BUNDLE_KEY_PROFILE_PIC_URI, profile_pic_uri.toString());
        if (!TextUtils.isEmpty(lastName))
            bundle.putString(BUNDLE_KEY_USER_LAST_NAME, lastName);

        startService(new Intent(activity, UpdateProfileService.class).putExtra(BUNDLE_NAME_PROFILE_DATA, bundle));
        HelperClass.showToast(activity, "Profile Update Successfully");
        finish();
    }
}