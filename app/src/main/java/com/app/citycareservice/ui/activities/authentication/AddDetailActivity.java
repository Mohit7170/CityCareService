package com.app.citycareservice.ui.activities.authentication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.citycareservice.R;
import com.app.citycareservice.ui.activities.MainActivity;
import com.app.citycareservice.service.UpdateProfileService;
import com.app.citycareservice.utils.HelperClass;
import com.app.citycareservice.utils.Params;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;

public class AddDetailActivity extends AppCompatActivity implements Params {

    private static final String TAG = "AddDetailActivity";

    private Activity activity = AddDetailActivity.this;

    //    private ImageView back_iv;
    private ShapeableImageView user_iv;
    private EditText first_name_et;
    private EditText last_name_et;
    private MaterialButton next_btn;

    private Uri profile_pic_uri;

    private boolean isProfileImgSelected = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_detail);

//        back_iv = findViewById(R.id.back_iv);
        user_iv = findViewById(R.id.user_iv);
        first_name_et = findViewById(R.id.first_name_et);
        last_name_et = findViewById(R.id.last_name_et);
        next_btn = findViewById(R.id.next_btn);

       /* back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });*/

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateForm();
            }
        });

        user_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.with(activity)
                        .cameraOnly()
                        .cropSquare()
                        .compress(1024)
                        .maxResultSize(1080, 1080)
                        .start(REQ_CODE_SELECT_IMAGE);
            }
        });

    }

    private void validateForm() {

        String firstName = first_name_et.getText().toString();
        String lastName = last_name_et.getText().toString();

        if (TextUtils.isEmpty(firstName)) {
            HelperClass.showToast(activity, "Please Enter Name");
            return;
        }

        String fullName = firstName + " " + lastName;

        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_KEY_USER_FULL_NAME, fullName);

        if (isProfileImgSelected)
            bundle.putString(BUNDLE_KEY_PROFILE_PIC_URI, profile_pic_uri.toString());

        startService(new Intent(activity, UpdateProfileService.class).putExtra(BUNDLE_NAME_PROFILE_DATA, bundle));
        startActivity(new Intent(activity, MainActivity.class));
        finish();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                isProfileImgSelected = true;

                Uri uri = data.getData();
                profile_pic_uri = uri;
                user_iv.setImageURI(uri);
            } else if (resultCode == ImagePicker.RESULT_ERROR)
                HelperClass.showToast(activity, ImagePicker.getError(data));
        }

    }

}