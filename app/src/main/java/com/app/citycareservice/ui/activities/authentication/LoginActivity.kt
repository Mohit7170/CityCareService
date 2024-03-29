package com.app.citycareservice.ui.activities.authentication

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.util.Patterns
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.app.citycareservice.R
import com.app.citycareservice.databinding.ActivityLoginBinding
import com.app.citycareservice.interfaces.api.Authentication
import com.app.citycareservice.modals.authentication.allUserData.Result
import com.app.citycareservice.modals.authentication.allUserData.UserDetailResponse
import com.app.citycareservice.ui.activities.MainActivity
import com.app.citycareservice.utils.ApiClient
import com.app.citycareservice.utils.HelperClass
import com.app.citycareservice.utils.Params
import com.app.citycareservice.utils.SharedPrefHandler
import com.google.firebase.FirebaseException
import com.google.firebase.auth.*
import com.google.firebase.auth.PhoneAuthProvider.ForceResendingToken
import com.google.firebase.auth.PhoneAuthProvider.OnVerificationStateChangedCallbacks
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.HttpURLConnection
import java.util.concurrent.TimeUnit

class LoginActivity : AppCompatActivity(), Params {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var activity: Activity
    private var countDownTimer: CountDownTimer? = null
    private var phoneNum = ""
    private lateinit var verificationId: String
    private lateinit var mAuth: FirebaseAuth
    var click = 3

    private val mCallBack: OnVerificationStateChangedCallbacks =
        object : OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(s: String, forceResendingToken: ForceResendingToken) {
                super.onCodeSent(s, forceResendingToken)
                HelperClass.hideLoader()
                with(binding) {
                    verificationId = s
                    nextBtn.visibility = View.GONE
                    codeEt.visibility = View.VISIBLE
                    resendTv.visibility = View.VISIBLE
                    view1.visibility = View.VISIBLE
                    topTv.text = "We have Sent you Otp"
                    phoneEt.isFocusable = false
                    phoneEt.isFocusableInTouchMode = false
                    codeEt.requestFocus()
                    startResendOtpTimer()
                    phoneEt.setOnClickListener {
                        HelperClass.showSimpleAlert(
                            activity,
                            "",
                            "Do You want to Change Number?",
                            "Yes",
                            "No",
                            true
                        ) {
                            countDownTimer?.cancel()
                            nextBtn.visibility = View.VISIBLE
                            codeEt.setText("")
                            codeEt.visibility = View.GONE
                            resendTv.visibility = View.GONE
                            view1.visibility = View.GONE
                            topTv.text = "Join us via Phone Number"
                            phoneEt.isFocusableInTouchMode = true
                            phoneEt.isFocusable = true
                            countDownTimer?.cancel()
                            phoneEt.setOnClickListener(null)
                        }
                    }
                }
            }

            override fun onVerificationCompleted(phoneAuthCredential: PhoneAuthCredential) {
                val code = phoneAuthCredential.smsCode
                if (code != null) {
                    binding.codeEt.setText(code)
                    verifyCode(code)
                }
            }

            override fun onVerificationFailed(e: FirebaseException) {
                HelperClass.hideLoader()
                Log.d(TAG, "onVerificationFailed: Error  -- $e")
                HelperClass.showToast(activity, e.message)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //        SplashScreen splashScreen = SplashScreen.installSplashScreen(this);
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        activity = this@LoginActivity

        mAuth = FirebaseAuth.getInstance()

        setTermsAndCondition()

        with(binding) {
            nextBtn.setOnClickListener(View.OnClickListener {
                phoneNum = phoneEt.text.toString()
                if (TextUtils.isEmpty(phoneNum)) {
                    HelperClass.showToast(activity, "Please Enter Phone Number")
                    return@OnClickListener
                }
                if (!Patterns.PHONE.matcher(phoneNum)
                        .matches() || phoneNum.length != Params.MAX_PHONE_NUM_LENGTH
                ) {
                    HelperClass.showToast(activity, "Enter Correct Phone Number")
                    return@OnClickListener
                }
                phoneNum = "+91$phoneNum"
                verifyLoginApi()
//                sendVerificationCode()
            })
            resendTv.setOnClickListener(View.OnClickListener { resendOtp() })
            codeEt.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    val otp = codeEt.toString()
                    if (codeEt.length() == Params.OTP_LENGTH) verifyCode(otp)
                }

                override fun afterTextChanged(s: Editable) {}
            })
        }

    }

    private fun setTermsAndCondition() {

        val termsText = getString(R.string.terms_text)
        val spannableString = SpannableString(termsText)
        val clickableSpan1: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                HelperClass.openUrl(activity, "https://www.citycareservice.com/terms-conditions/")
            }
        }
        val clickableSpan2: ClickableSpan = object : ClickableSpan() {
            override fun onClick(widget: View) {
                HelperClass.openUrl(activity, "https://www.citycareservice.com/terms-conditions/")
            }
        }
        spannableString.setSpan(clickableSpan1, 31, 51, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(clickableSpan2, 56, 70, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        with(binding) {
            termsTv.text = spannableString
            termsTv.movementMethod = LinkMovementMethod.getInstance()
            termsTv.highlightColor = resources.getColor(android.R.color.transparent)
        }
    }


    private fun sendVerificationCode() {
        HelperClass.showLoader(activity)
        val options = PhoneAuthOptions.newBuilder(mAuth)
            .setPhoneNumber(phoneNum) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(activity) // Activity (for callback binding)
            .setCallbacks(mCallBack) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun verifyLoginApi() {
        if (HelperClass.getNetworkInfo(activity)) {
            HelperClass.showLoader(activity)
            val api = ApiClient.apiService(activity).create(Authentication::class.java)
            val call = api.checkRegister(phoneNum)
            call.enqueue(object : Callback<UserDetailResponse> {
                override fun onResponse(
                    call: Call<UserDetailResponse>,
                    response: Response<UserDetailResponse>
                ) {
                    if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                        //Token Expired
                        HelperClass.logout(activity, true)
                        return
                    }
                    val apiResponse = response.body()
                    if (response.code() == HttpURLConnection.HTTP_OK && apiResponse != null) {
                        if (apiResponse.status_code == HttpURLConnection.HTTP_OK) {
//                            Account Already Exist
//                            Login User
                            val result = apiResponse.results[0]
                            setDataToPref(result)
                            startActivity(Intent(activity, MainActivity::class.java))
                            finish()
                        } else if (apiResponse.status_code == HttpURLConnection.HTTP_CREATED) {
//                            Account Created
//                            Go to Add Details
                            val result = apiResponse.results[0]
                            setDataToPref(result)
                            startActivity(Intent(activity, AddDetailActivity::class.java))
                            finish()
                        } else HelperClass.showToast(activity, apiResponse.message)
                    } else HelperClass.showToast(
                        activity,
                        activity.getString(R.string.something_went_wrong)
                    )
                    HelperClass.hideLoader()
                }

                override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                    Log.d(TAG, "onFailure: Error is -- $t")
                    HelperClass.hideLoader()
                    HelperClass.showToast(
                        activity,
                        activity.getString(R.string.something_went_wrong)
                    )
                }
            })
        } else {
            HelperClass.hideLoader()
            HelperClass.showToast(activity, activity.getString(R.string.check_internet_connection))
        }
    }

    private fun setDataToPref(result: Result) {
        val bundle = Bundle()
        if (!TextUtils.isEmpty(result.id)) bundle.putString(Params.BUNDLE_KEY_USER_ID, result.id)
        if (!TextUtils.isEmpty(result.auth_token)) bundle.putString(
            Params.BUNDLE_KEY_USER_AUTH_TOKEN,
            result.auth_token
        )
        if (!TextUtils.isEmpty(result.email)) bundle.putString(
            Params.BUNDLE_KEY_USER_EMAIL,
            result.email
        )
        if (!TextUtils.isEmpty(result.name)) bundle.putString(
            Params.BUNDLE_KEY_USER_FULL_NAME,
            result.name
        )
        if (!TextUtils.isEmpty(result.phone)) bundle.putString(
            Params.BUNDLE_KEY_USER_PHONE,
            result.phone
        )
        if (!TextUtils.isEmpty(result.profile_pic)) bundle.putString(
            Params.BUNDLE_KEY_PROFILE_PIC_URL,
            result.profile_pic
        )
        val prefHandler = SharedPrefHandler(activity)
        prefHandler.setUserData(bundle)
    }

    private fun verifyCode(code: String) {
        val credential = PhoneAuthProvider.getCredential(verificationId, code)
        signInWithCredential(credential)
    }

    private fun resendOtp() {
        if (click-- > 0) {
            startResendOtpTimer()
            with(binding) {
                resendTv.isClickable = false
                resendTv.text = "Resend Otp in 30 seconds"
                resendTv.setTextColor(resources.getColor(R.color.font_grey))
                sendVerificationCode()
            }
        } else HelperClass.showToast(activity, "OTP limit reached")
    }

    private fun signInWithCredential(credential: PhoneAuthCredential) {
        mAuth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithCredential:success")
                    val user = task.result.user
                    // Update UI
                    verifyLoginApi()
                } else {
                    // Sign in failed, display a message and update the UI
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    if (task.exception is FirebaseAuthInvalidCredentialsException) {
                        // The verification code entered was invalid
                        HelperClass.showToast(activity, "Verification Failed")
                    }
                }
            }
    }

    private fun startResendOtpTimer() {
        countDownTimer?.cancel()

        countDownTimer = object : CountDownTimer(
            Params.RESEND_OTP_TIME * Params.ONE_SEC_IN_MILLS,
            Params.ONE_SEC_IN_MILLS
        ) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / Params.ONE_SEC_IN_MILLS
                if (seconds < 1) {
                    onFinish()
                    return
                }
                binding.resendTv.text = "Resend Otp in $seconds seconds"
            }

            override fun onFinish() {
                enableResendOtp()
            }
        }.start()
    }

    fun enableResendOtp() {
        with(binding) {
            resendTv.isClickable = true
            resendTv.text = "Resend Otp"
            resendTv.setTextColor(resources.getColor(R.color.call_green))
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }

    companion object {
        private const val TAG = "LoginActivity"
    }
}