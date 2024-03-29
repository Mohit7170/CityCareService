package com.app.citycareservice.ui.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.app.citycareservice.R
import com.app.citycareservice.databinding.FragmentSettingsBinding
import com.app.citycareservice.ui.activities.authentication.EditProfileActivity
import com.app.citycareservice.ui.activities.settings.FeedbackActivity
import com.app.citycareservice.utils.HelperClass
import com.app.citycareservice.utils.Params
import com.app.citycareservice.utils.SharedPrefHandler

class SettingsFragment : Fragment(), Params {

    private lateinit var binding: FragmentSettingsBinding
    private lateinit var activity: Activity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        activity = binding.root.context as Activity
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        HelperClass.changeStatusBarColor(activity, R.color.black, false)

        with(binding) {


            partnerTv.setOnClickListener {
                HelperClass.openUrl(
                    activity,
                    activity.getString(R.string.play_console_link) + activity.getString(R.string.partner_apk_package)
                )
            }
            addressTv.setOnClickListener {
                startActivity(
                    Intent(
                        activity,
                        com.app.citycareservice.ui.activities.settings.ManageAddressActivity::class.java
                    )
                )
            }
            editIv.setOnClickListener {
                startActivity(
                    Intent(
                        activity,
                        EditProfileActivity::class.java
                    )
                )
            }
            bookingTv.setOnClickListener {
                startActivity(
                    Intent(
                        activity,
                        com.app.citycareservice.ui.activities.settings.ScheduledBookingsActivity::class.java
                    )
                )
            }
            walletTv.setOnClickListener {
                HelperClass.showToast(
                    activity,
                    "Upcoming"
                )
            }
            logoutTv.setOnClickListener {
                HelperClass.logout(
                    activity,
                    true
                )
            }
            shareTv.setOnClickListener {
                val shareText =
                    "I like the Service offered by City Care Service \nPLease Download The CIty CAre Service Service app here --- " + getString(
                        R.string.play_console_link
                    ) + getString(R.string.partner_apk_package)
                HelperClass.shareContent(activity, shareText)
            }
            rateTv.setOnClickListener { HelperClass.rateUs(activity) }
            feedbackTv.setOnClickListener {
                activity.startActivity(
                    Intent(
                        activity,
                        FeedbackActivity::class.java
                    )
                )
            }
            supportTv.setOnClickListener {
                activity.startActivity(
                    Intent(
                        activity,
                        com.app.citycareservice.ui.activities.settings.SupportActivity::class.java
                    )
                )
            }
            faqTv.setOnClickListener {
                HelperClass.showToast(
                    activity,
                    "Upcoming"
                )
            }
        }
    }

    override fun onResume() {
        super.onResume()
        with(binding) {

            val prefHandler = SharedPrefHandler(activity)
            if (prefHandler.hasKey(Params.SP_KEY_USER_NAME)) userNameTv.text =
                prefHandler.getString(
                    Params.SP_KEY_USER_NAME
                )
            if (prefHandler.hasKey(Params.SP_KEY_USER_PHONE)) userPhoneTv.text =
                prefHandler.getString(
                    Params.SP_KEY_USER_PHONE
                )
        }
    }

    companion object {
        private const val TAG = "SettingsFragment"
    }
}