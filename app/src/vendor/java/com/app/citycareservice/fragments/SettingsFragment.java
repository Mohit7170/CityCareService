package com.app.citycareservice.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.app.citycareservice.EditProfileActivity;
import com.app.citycareservice.ManageAddressActivity;
import com.app.citycareservice.R;
import com.app.citycareservice.ScheduledBookingsActivity;
import com.app.citycareservice.SupportActivity;
import com.app.citycareservice.ui.activities.FeedbackActivity;
import com.app.citycareservice.utils.HelperClass;
import com.app.citycareservice.utils.Params;
import com.app.citycareservice.utils.SharedPrefHandler;
import com.google.android.material.imageview.ShapeableImageView;

public class SettingsFragment extends Fragment implements Params {

    private static final String TAG = "SettingsFragment";

    private Activity activity;

    private TextView user_name_tv;
    private TextView user_phone_tv;
    private TextView profile_tv;
    private TextView partner_tv;
    private TextView address_tv;
    private TextView booking_tv;
    private TextView wallet_tv;
    private TextView faq_tv;
    private TextView feedback_tv;
    private TextView support_tv;
    private TextView rate_tv;
    private TextView share_tv;
    private TextView logout_tv;

    private ShapeableImageView edit_iv;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        activity = (Activity) view.getContext();

//        activity.setTheme(R.style.blackActionBar);
        HelperClass.changeStatusBarColor(activity, R.color.black, false);

        user_name_tv = view.findViewById(R.id.user_name_tv);
        user_phone_tv = view.findViewById(R.id.user_phone_tv);
        profile_tv = view.findViewById(R.id.profile_tv);
        partner_tv = view.findViewById(R.id.partner_tv);
        address_tv = view.findViewById(R.id.address_tv);
        booking_tv = view.findViewById(R.id.booking_tv);
        wallet_tv = view.findViewById(R.id.wallet_tv);
        faq_tv = view.findViewById(R.id.faq_tv);
        share_tv = view.findViewById(R.id.share_tv);
        feedback_tv = view.findViewById(R.id.feedback_tv);
        support_tv = view.findViewById(R.id.support_tv);
        rate_tv = view.findViewById(R.id.rate_tv);
        edit_iv = view.findViewById(R.id.edit_iv);
        logout_tv = view.findViewById(R.id.logout_tv);

//        Objects.requireNonNull(rate_tva.getEditText()).getText();


        SharedPrefHandler prefHandler = new SharedPrefHandler(activity);

        if (prefHandler.hasKey(SP_KEY_USER_NAME))
            user_name_tv.setText(prefHandler.getString(SP_KEY_USER_NAME));

        if (prefHandler.hasKey(SP_KEY_USER_PHONE))
            user_phone_tv.setText(prefHandler.getString(SP_KEY_USER_PHONE));

        partner_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperClass.openUrl(activity, activity.getString(R.string.play_console_link) + activity.getString(R.string.partner_apk_package));
            }
        });

        address_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, ManageAddressActivity.class));
            }
        });

        edit_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, EditProfileActivity.class));
            }
        });

        booking_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(activity, ScheduledBookingsActivity.class));
            }
        });

        wallet_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperClass.showToast(activity, "Upcomming");
            }
        });

        logout_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperClass.logout(activity, true);
            }
        });

        share_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String shareText = "I like the Service offered by City Care Service \nPLease Downlaod The CIty CAre Service Service app here --- "
                        .concat(getString(R.string.play_console_link)
                                .concat(getString(R.string.partner_apk_package)));
                HelperClass.shareContent(activity, shareText);

            }
        });

        rate_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperClass.rateUs(activity);
            }
        });

        feedback_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, FeedbackActivity.class));
            }
        });

        support_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.startActivity(new Intent(activity, SupportActivity.class));
            }
        });

        faq_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HelperClass.showToast(activity, "Upcoming");
            }
        });

    }
}