package com.app.citycareservice.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.app.citycareservice.BuildConfig;
import com.app.citycareservice.R;
import com.app.citycareservice.ui.activities.authentication.LoginActivity;
import com.app.citycareservice.ui.dialogs.CustomLoader;
import com.app.citycareservice.interfaces.ButtonPressListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class HelperClass implements Params {

    private static final String TAG = "HelperClass";

    static CustomLoader customLoader;

    public static boolean isGpsEnabled(Context activity) {
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public static void turnOnGps(Activity activity, LocationRequest locationRequest) {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(activity)
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    HelperClass.showToast(activity, "GPS is already turned ON");
                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                resolvableApiException.startResolutionForResult(activity, REQ_CODE_ENABLE_GPS);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    public static void rateUs(Activity activity) {
//TODO Pending
        openUrl(activity, activity.getString(R.string.play_console_link).concat(BuildConfig.APPLICATION_ID));
    }

    public static void changeStatusBarColor(Activity activity, int color, boolean isLight) {
//     TODO Edit this
//     TODO Pending
        Window window = activity.getWindow();
        View view = window.getDecorView();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(activity, color));
        if (isLight)
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        else
            window.getDecorView().setSystemUiVisibility(view.getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }

    public static void makeStatusBarWhite(Activity activity) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(activity, R.color.white));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }
   /* public static void makeStatusBarRed(Activity activity) {
        Window window = activity.getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(activity, R.color.main_bg));
        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    }*/

    public static void showLoader(Context context) {
        try {
            hideLoader();
            customLoader = new CustomLoader(context);
            customLoader.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            customLoader.show();
            Log.d(TAG, "showLoader: From Activity -- " + context.getClass().getSimpleName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void hideLoader() {
        try {
            if ((customLoader != null) && customLoader.isShowing()) {
                customLoader.dismiss();
                customLoader.cancel();
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    public static void shareContent(Context activity, String shareText) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, activity.getString(R.string.app_name));
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            activity.startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
        }
    }

    public static void openUrl(Activity activity, String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        activity.startActivity(intent);
    }

    public static boolean getNetworkInfo(Context activity) {
        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected() == true;
    }


    public static void showToast(Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    public static void showSimpleAlert(Context context, String title, String message, String positiveText, String negativeText, boolean cancelable, final ButtonPressListener listener) {

        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(positiveText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onButtonPressed(true);
                    }
                }).setNegativeButton(negativeText, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                }).setCancelable(cancelable)
                .show();
    }

    public static void logout(Activity activity, boolean finishActivity) {
//        logoutUser_API(activity);
        SharedPrefHandler sharedPrefHandler = new SharedPrefHandler(activity);

        //Firebase SignOut
        FirebaseAuth.getInstance().signOut();

        //Clear Shared Pref Data
        sharedPrefHandler.clearAllData();

        showToast(activity, "Logout Successfully");

        //Destroy Socket
        if (finishActivity) {
            Intent intent = new Intent(activity, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            activity.startActivity(intent);
            activity.finish();
        }
    }

    public static String getRealPathFromURI(Context activity, Uri contentURI) {
        String filePath;
        Cursor cursor = activity.getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) {
            filePath = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            filePath = cursor.getString(idx);
            cursor.close();
        }
        return filePath;
    }


    public static void setImage(Activity activity, String imgUrl, ImageView imageView, int placeholder) {
        if (TextUtils.isEmpty(imgUrl) || TextUtils.equals(imgUrl, DEFAULT_EMPTY_STRING)) {
            imageView.setImageDrawable(activity.getDrawable(R.drawable.ic_placeholder));
//            Glide.with(activity).load(placeholder).into(imageView);
            return;
        }
        Glide.with(activity)
                .load(imgUrl)
                .thumbnail(0.01f)
                .placeholder(activity.getDrawable(R.drawable.ic_placeholder))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

/*
    public static void showSimpleAlertDialog(final Activity activity, String message, String buttonText, boolean cancelable, final ButtonPressListener listener) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(activity);
        builder1.setMessage(message);
        builder1.setCancelable(cancelable);
        builder1.setPositiveButton(
                buttonText,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                        listener.onButtonPressed(true);
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
*/


}
