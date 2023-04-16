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
import com.app.citycareservice.utils.Params.BUNDLE_KEY_USER_EMAIL
import com.app.citycareservice.utils.Params.BUNDLE_KEY_USER_FULL_NAME
import com.app.citycareservice.utils.SharedPrefHandler

class EditProfileActivity : AppCompatActivity(), Params {
    private val activity: Activity = this@EditProfileActivity
    private lateinit var binding: ActivityEditProfileBinding

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
            val fullName =
                nameTef.text.toString().trim()
            val email =
                emailTef.text.toString().trim()

            val bundle = Bundle()

            if (!TextUtils.isEmpty(fullName))
                bundle.putString(BUNDLE_KEY_USER_FULL_NAME, fullName)

            if (!TextUtils.isEmpty(email)) {
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    emailTif.error = "Invalid Email Id"
                    emailTif.isFocusable = true
                } else
                    bundle.putString(BUNDLE_KEY_USER_EMAIL, email)
            }

            if (bundle.containsKey(BUNDLE_KEY_USER_FULL_NAME)
                || bundle.containsKey(BUNDLE_KEY_USER_EMAIL)
            ) {



                startService(
                    Intent(
                        activity, UpdateProfileService::class.java
                    ).putExtra(Params.BUNDLE_NAME_PROFILE_DATA, bundle)
                )
//                HelperClass.showToast(activity, "Profile Update Successfully")
                finish()
            } else {
                HelperClass.showToast(activity, "Please enter name or email to be updated")
            }
        }
    }

    companion object {
        private const val TAG = "EditProfileActivity"
    }
}