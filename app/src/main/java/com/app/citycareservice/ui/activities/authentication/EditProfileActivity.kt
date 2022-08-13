package com.app.citycareservice.ui.activities.authentication

import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.content.Intent
import android.util.Patterns
import com.app.citycareservice.databinding.ActivityEditProfileBinding
import com.app.citycareservice.service.UpdateProfileService
import com.app.citycareservice.utils.HelperClass
import com.app.citycareservice.utils.Params
import com.app.citycareservice.utils.SharedPrefHandler

class EditProfileActivity : AppCompatActivity(), Params {
    private val activity: Activity = this@EditProfileActivity

    private lateinit var binding: ActivityEditProfileBinding
/*
    private var back_iv: ImageView? = null
    private var name_tif: TextInputLayout? = null
    private var email_tif: TextInputLayout? = null
    private var phone_tif: TextInputLayout? = null
    private var update_btn: MaterialButton? = null*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setData()

        binding.backIv.setOnClickListener { onBackPressed() }
        binding.updateBtn.setOnClickListener { validateForm() }
    }

    private fun setData() {
        val prefHandler = SharedPrefHandler(activity)
        with(binding) {
            nameTef.setText(prefHandler.getString(Params.SP_KEY_USER_NAME))
            emailTef.setText(prefHandler.getString(Params.SP_KEY_USER_EMAIL))
            phoneTef.setText(prefHandler.getString(Params.SP_KEY_USER_PHONE))
        }
    }

    private fun validateForm() {

        with(binding) {
            nameTif.isErrorEnabled = false
            emailTif.isErrorEnabled = false
            phoneTif.isErrorEnabled = false
            val nameRegex = Regex("^[A-Za-z]+$")
            val name =
                nameTef.text.toString().trim()
            val lastName = ""
            val email =
                emailTef.text.toString().trim()
            val phone =
                phoneTef.text.toString().trim()
            if (TextUtils.isEmpty(name)) {
                nameTif.error = "Name Cannot be Empty"
                nameTif.isFocusable = true
                return
            }
            if (!name.matches(nameRegex)) {
                nameTif.error = "Invalid Name"
                nameTif.isFocusable = true
                return
            }
            if (TextUtils.isEmpty(email)) {
                emailTif.error = "Email Cannot be Empty"
                emailTif.isFocusable = true
                return
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                emailTif.error = "Invalid Email Id"
                emailTif.isFocusable = true
                return
            }
            if (TextUtils.isEmpty(phone)) {
                phoneTif.error = "Phone Cannot be Empty"
                return
            }
            if (!Patterns.PHONE.matcher(phone).matches()) {
                phoneTif.error = "Invalid Phone NUmber"
                return
            }
            val bundle = Bundle()
            bundle.putString(Params.BUNDLE_KEY_USER_FIRST_NAME, name)
            bundle.putString(Params.BUNDLE_KEY_USER_EMAIL, email)

//        if (isProfileImgSelected)
//            bundle.putString(BUNDLE_KEY_PROFILE_PIC_URI, profile_pic_uri.toString());
            if (!TextUtils.isEmpty(lastName)) bundle.putString(
                Params.BUNDLE_KEY_USER_LAST_NAME,
                lastName
            )
            startService(
                Intent(
                    activity,
                    UpdateProfileService::class.java
                ).putExtra(Params.BUNDLE_NAME_PROFILE_DATA, bundle)
            )
            HelperClass.showToast(activity, "Profile Update Successfully")
            finish()
        }
    }

    companion object {
        private const val TAG = "EditProfileActivity"
    }
}