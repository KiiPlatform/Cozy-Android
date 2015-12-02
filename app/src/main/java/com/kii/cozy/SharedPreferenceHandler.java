package com.kii.cozy;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yue on 15-12-01.
 */
public class SharedPreferenceHandler {

    private Context context;

    private SharedPreferences sharedPreferences;

    public SharedPreferenceHandler(Context context) {
        this.context = context;
    }

    public String getWifiPassword() {
        sharedPreferences = context.getSharedPreferences("wifi", Context.MODE_PRIVATE);
        String accessToken = sharedPreferences.getString("password", null);
        return accessToken;
    }

    public void setWifiPassword(String password) {
        sharedPreferences = context.getSharedPreferences("wifi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password", password);
        editor.commit();
    }

    public void clearPassword() {
        sharedPreferences = context.getSharedPreferences("wifi", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }


}
