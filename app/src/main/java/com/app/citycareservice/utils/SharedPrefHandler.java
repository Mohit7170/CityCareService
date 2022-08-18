package com.app.citycareservice.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SharedPrefHandler implements Params {

    private SharedPreferences sharedPreferences;

    public SharedPrefHandler(Context context) {
       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                MasterKey masterKey = new MasterKey.Builder(context)
                        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                        .build();

                sharedPreferences = EncryptedSharedPreferences.create(
                        context,
                        Params.SHARED_PREF_NAME,
                        masterKey,
                        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
                );
            } catch (GeneralSecurityException | IOException e) {
                e.printStackTrace();
            }
        } else*/
        sharedPreferences = context.getSharedPreferences(Params.SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public void setStringValue(String key, String value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public void setLongValue(String key, long value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public void setIntValue(String key, int value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public void setBooleanValue(String key, boolean value) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public long getLongFromSharedPref(String key) {
        return sharedPreferences.getLong(key, -1);
    }

    public boolean getBoolean(String key) {
        return sharedPreferences.getBoolean(key, false);
    }

    public String getString(String key) {
        return sharedPreferences.getString(key, DEFAULT_EMPTY_STRING);
    }

    public int getIntFromSharedPref(String key) {
        return sharedPreferences.getInt(key, -1);
    }

    public boolean hasKey(String key) {
        return sharedPreferences.contains(key);
    }

    public void setUserData(Bundle loginDataBundle) {

        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (loginDataBundle.containsKey(BUNDLE_KEY_USER_FULL_NAME))
            editor.putString(SP_KEY_USER_NAME, loginDataBundle.getString(BUNDLE_KEY_USER_FULL_NAME));
        if (loginDataBundle.containsKey(BUNDLE_KEY_USER_EMAIL))
            editor.putString(SP_KEY_USER_EMAIL, loginDataBundle.getString(BUNDLE_KEY_USER_EMAIL));
        if (loginDataBundle.containsKey(BUNDLE_KEY_PROFILE_PIC_URL))
            editor.putString(SP_KEY_PROFILE_IMAGE, loginDataBundle.getString(BUNDLE_KEY_PROFILE_PIC_URL));
        if (loginDataBundle.containsKey(BUNDLE_KEY_USER_ID))
            editor.putString(SP_KEY_USER_ID, loginDataBundle.getString(BUNDLE_KEY_USER_ID));
        if (loginDataBundle.containsKey(BUNDLE_KEY_USER_PHONE))
            editor.putString(SP_KEY_USER_PHONE, loginDataBundle.getString(BUNDLE_KEY_USER_PHONE));
        if (loginDataBundle.containsKey(BUNDLE_KEY_USER_AUTH_TOKEN))
            editor.putString(SP_KEY_AUTH_TOKEN, loginDataBundle.getString(BUNDLE_KEY_USER_AUTH_TOKEN));

        editor.apply();
    }

    public void clearAllData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}
