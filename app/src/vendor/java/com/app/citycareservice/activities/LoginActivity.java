package com.app.citycareservice.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.app.citycareservice.R;
import com.app.citycareservice.interfaces.ButtonPressListener;
import com.app.citycareservice.interfaces.api.Authentication;
import com.app.citycareservice.modals.authentication.Register.RegisterResponse;
import com.app.citycareservice.modals.authentication.Register.Result;
import com.app.citycareservice.utils.ApiClient;
import com.app.citycareservice.utils.HelperClass;
import com.app.citycareservice.utils.Params;
import com.app.citycareservice.utils.SharedPrefHandler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.net.HttpURLConnection;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements Params {

    private static final String TAG = "LoginActivity";

    private final Activity activity = LoginActivity.this;

    private EditText phone_et;
    private EditText code_et;
    private TextView join_us;
    private TextView resend_tv;
    private TextView terms_tv;
    private View view1;
    private MaterialButton next_btn;

    private CountDownTimer countDownTimer;

    private String phone_num;
    private String verificationId;

    private FirebaseAuth mAuth;

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks
            mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            HelperClass.hideLoader();

            verificationId = s;

            next_btn.setVisibility(View.GONE);
            code_et.setVisibility(View.VISIBLE);
            resend_tv.setVisibility(View.VISIBLE);
            view1.setVisibility(View.VISIBLE);
            join_us.setText("We have Sent you Otp");
            phone_et.setFocusable(false);
            phone_et.setFocusableInTouchMode(false);

            code_et.requestFocus();

            startResendOtpTimer();

            phone_et.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    HelperClass.showSimpleAlert(activity, "", "Do You want to Change Number?", "Yes", "No", true, new ButtonPressListener() {
                        @Override
                        public void onButtonPressed(boolean pressed) {

                            countDownTimer.cancel();
                            next_btn.setVisibility(View.VISIBLE);
                            code_et.setText("");
                            code_et.setVisibility(View.GONE);
                            resend_tv.setVisibility(View.GONE);
                            view1.setVisibility(View.GONE);
                            join_us.setText("Join us via Phone Number");
                            phone_et.setFocusableInTouchMode(true);
                            phone_et.setFocusable(true);
                            countDownTimer.cancel();

                            phone_et.setOnClickListener(null);
                        }
                    });

                }
            });

        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            final String code = phoneAuthCredential.getSmsCode();
            if (code != null) {
                code_et.setText(code);
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            HelperClass.hideLoader();
            Log.d(TAG, "onVerificationFailed: Error  -- " + e);
            HelperClass.showToast(activity, e.getMessage());
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);

        setContentView(R.layout.activity_login);

        join_us = findViewById(R.id.top_tv);
        phone_et = findViewById(R.id.phone_et);
        code_et = findViewById(R.id.code_et);
        resend_tv = findViewById(R.id.resend_tv);
        terms_tv = findViewById(R.id.terms_tv);
        next_btn = findViewById(R.id.next_btn);
        view1 = findViewById(R.id.view1);

        mAuth = FirebaseAuth.getInstance();

        setTermsandCondition();

        next_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                phone_num = phone_et.getText().toString();

                if (TextUtils.isEmpty(phone_num)) {
                    HelperClass.showToast(activity, "Please Enter Phone Number");
                    return;
                }
                if (!Patterns.PHONE.matcher(phone_num).matches() || phone_num.length() != MAX_PHONE_NUM_LENGTH) {
                    HelperClass.showToast(activity, "Enter Correct Phone Number");
                    return;
                }

                phone_num = "+91" + phone_num;

                sendVerificationCode();
            }
        });

        resend_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOtp();
            }
        });

        code_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String otp = code_et.toString();
                if (code_et.length() == OTP_LENGTH)
                    verifyCode(otp);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void setTermsandCondition() {

//        String termsText = "By tapping Next you agree to Terms and Conditions and Privacy Policy";
        String termsText = getString(R.string.terms_text);
        final SpannableString spannableString = new SpannableString(termsText);
        ClickableSpan clickableSpan1 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
/*
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(TERMS_OF_SERVICE));
                startActivity(intent);*/

                HelperClass.showToast(activity, "Terms");

            }
        };

        ClickableSpan clickableSpan2 = new ClickableSpan() {
            @Override
            public void onClick(@NonNull View widget) {
               /* Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(PRIVACY_POLICY));
                startActivity(intent);*/

                HelperClass.showToast(activity, "Privacy");

            }
        };

        spannableString.setSpan(clickableSpan1, 31, 51, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(clickableSpan2, 56, 70, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        terms_tv.setText(spannableString);
        terms_tv.setMovementMethod(LinkMovementMethod.getInstance());
        terms_tv.setHighlightColor(getResources().getColor(android.R.color.transparent));

    }

    private void sendVerificationCode() {
        HelperClass.showLoader(activity);

        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phone_num)            // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(activity)                 // Activity (for callback binding)
                        .setCallbacks(mCallBack)           // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void verifyLoginApi() {

        if (HelperClass.getNetworkInfo(activity)) {
            HelperClass.showLoader(activity);

            Authentication api = ApiClient.apiService(activity).create(Authentication.class);
            Call<RegisterResponse> call = api.checkRegister(phone_num);
            call.enqueue(new Callback<RegisterResponse>() {
                @Override
                public void onResponse(@NonNull Call<RegisterResponse> call, @NonNull Response<RegisterResponse> response) {

                    if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        //Token Expired
                        HelperClass.logout(activity, true);
                        return;
                    }
                    RegisterResponse apiResponse = response.body();

                    if (response.code() == HttpURLConnection.HTTP_OK && apiResponse != null) {

                        if (apiResponse.getStatusCode() == HttpURLConnection.HTTP_OK) {
//                            Account Already Exist
//                            Login User

                            Result result = apiResponse.getResults().get(0);
                            setDataToPref(result);

                            startActivity(new Intent(activity, MainActivity.class));
                            finish();

                        } else if (apiResponse.getStatusCode() == HttpURLConnection.HTTP_CREATED) {
//                            Account Created
//                            Go to Add Details

                            Result result = apiResponse.getResults().get(0);
                            setDataToPref(result);

                            startActivity(new Intent(activity, AddDetailActivity.class));
                            finish();

                        } else
                            HelperClass.showToast(activity, apiResponse.getMessage());
                    } else
                        HelperClass.showToast(activity, activity.getString(R.string.something_went_wrong));

                    HelperClass.hideLoader();
                }

                @Override
                public void onFailure
                        (@NonNull Call<RegisterResponse> call, @NonNull Throwable t) {
                    Log.d(TAG, "onFailure: Error is -- " + t);
                    HelperClass.hideLoader();
                    HelperClass.showToast(activity, activity.getString(R.string.something_went_wrong));
                }
            });

        } else {
            HelperClass.hideLoader();
            HelperClass.showToast(activity, activity.getString(R.string.check_internet_connection));
        }
    }

    private void setDataToPref(Result result) {
        Bundle bundle = new Bundle();

        if (!TextUtils.isEmpty(result.getId()))
            bundle.putString(BUNDLE_KEY_USER_ID, result.getId());
        if (!TextUtils.isEmpty(result.getAuthToken()))
            bundle.putString(BUNDLE_KEY_USER_AUTH_TOKEN, result.getAuthToken());
        if (!TextUtils.isEmpty(result.getEmail()))
            bundle.putString(BUNDLE_KEY_USER_EMAIL, result.getEmail());
        if (!TextUtils.isEmpty(result.getFirstName()))
            bundle.putString(BUNDLE_KEY_USER_FIRST_NAME, result.getFirstName());
        if (!TextUtils.isEmpty(result.getLastName()))
            bundle.putString(BUNDLE_KEY_USER_LAST_NAME, result.getLastName());
        if (!TextUtils.isEmpty(result.getPhone()))
            bundle.putString(BUNDLE_KEY_USER_PHONE, result.getPhone());
        if (!TextUtils.isEmpty(result.getProfilePic()))
            bundle.putString(BUNDLE_KEY_PROFILE_PIC_URL, result.getProfilePic());

        SharedPrefHandler prefHandler = new SharedPrefHandler(activity);
        prefHandler.setUserData(bundle);

    }

    private void verifyCode(String code) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        signInWithCredential(credential);
    }

    int click = 3;

    private void resendOtp() {
        if (click-- > 0) {
            startResendOtpTimer();
            resend_tv.setClickable(false);
            resend_tv.setText("Resend Otp in 30 seconds");
            resend_tv.setTextColor(getResources().getColor(R.color.font_grey));
            sendVerificationCode();
        } else
            HelperClass.showToast(activity, "OTP limit reached");
    }

    private void signInWithCredential(PhoneAuthCredential credential) {

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "signInWithCredential:success");

                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                            verifyLoginApi();

                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                HelperClass.showToast(activity, "Verification Failed");

                            }
                        }
                    }
                });

    }

    private void startResendOtpTimer() {

        countDownTimer = new CountDownTimer(RESEND_OTP_TIME * ONE_SEC_IN_MILLS, ONE_SEC_IN_MILLS) {

            public void onTick(long millisUntilFinished) {

                long seconds = millisUntilFinished / ONE_SEC_IN_MILLS;

                if (seconds < 1) {
                    onFinish();
                    return;
                }

                resend_tv.setText("Resend Otp in " + seconds + " seconds");

            }

            public void onFinish() {
                enableResendOtp();
            }

        }.start();

    }

    public void enableResendOtp() {
        resend_tv.setClickable(true);
        resend_tv.setText("Resend Otp");
        resend_tv.setTextColor(getResources().getColor(R.color.call_green));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        countDownTimer.cancel();
    }
}