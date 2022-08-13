package com.app.citycareservice.ui.activities;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.app.citycareservice.R;
import com.app.citycareservice.utils.HelperClass;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputLayout;

public class FeedbackActivity extends AppCompatActivity {

    private static final String TAG = "FeedbackActivity";

    private Activity activity = FeedbackActivity.this;

    private ImageView back_iv;
    private TextInputLayout feedback_tif;
    private MaterialButton submit_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        feedback_tif = findViewById(R.id.feedback_tif);
        submit_btn = findViewById(R.id.submit_btn);
        back_iv = findViewById(R.id.back_iv);

        EditText editText = feedback_tif.getEditText();

        assert editText != null;
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                submit_btn.setEnabled(!TextUtils.isEmpty(editText.getText()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        back_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedbackApi();
            }
        });
    }

    private void feedbackApi() {
        HelperClass.showToast(activity, "Feedback Submitted");
        finish();
    }
}