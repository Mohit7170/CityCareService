package com.app.citycareservice.ui.activities.authentication

import androidx.appcompat.app.AppCompatActivity
import android.app.Activity
import android.os.Bundle
import com.github.dhaval2404.imagepicker.ImagePicker
import android.text.TextUtils
import com.app.citycareservice.utils.HelperClass
import android.content.Intent
import android.net.Uri
import com.app.citycareservice.databinding.ActivityAddDetailBinding
import com.app.citycareservice.service.UpdateProfileService
import com.app.citycareservice.ui.activities.MainActivity
import com.app.citycareservice.utils.Params

class AddDetailActivity : AppCompatActivity(), Params {
    private val activity: Activity = this@AddDetailActivity

    private lateinit var binding: ActivityAddDetailBinding

    private var profilePicUri: Uri? = null
    private var isProfileImgSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.nextBtn.setOnClickListener { validateForm() }
        binding.userIv.setOnClickListener {
            ImagePicker.with(activity)
                .cameraOnly()
                .cropSquare()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start(Params.REQ_CODE_SELECT_IMAGE)
        }
    }

    private fun validateForm() {
        val firstName = binding.firstNameEt.text.toString()
        val lastName = binding.lastNameEt.text.toString()
        if (TextUtils.isEmpty(firstName)) {
            HelperClass.showToast(activity, "Please Enter Name")
            return
        }
        val fullName = "$firstName $lastName"
        val bundle = Bundle()
        bundle.putString(Params.BUNDLE_KEY_USER_FULL_NAME, fullName)
        if (isProfileImgSelected) bundle.putString(
            Params.BUNDLE_KEY_PROFILE_PIC_URI,
            profilePicUri.toString()
        )
        startService(
            Intent(
                activity,
                UpdateProfileService::class.java
            ).putExtra(Params.BUNDLE_NAME_PROFILE_DATA, bundle)
        )
        startActivity(Intent(activity, MainActivity::class.java))
        finish()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Params.REQ_CODE_SELECT_IMAGE) {
            if (resultCode == RESULT_OK && data != null) {
                isProfileImgSelected = true
                val uri = data.data
                profilePicUri = uri
                binding.userIv.setImageURI(uri)
            } else if (resultCode == ImagePicker.RESULT_ERROR) HelperClass.showToast(
                activity,
                ImagePicker.getError(data)
            )
        }
    }

    companion object {
        private const val TAG = "AddDetailActivity"
    }
}