package com.example.tlds.testdrawerlayout;

/**
 * Created by PC on 4/26/2016.
 */
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.Toast;

public class Var {


    // const for load and put data with server
    public static final String KEY_METHOD = "method";

    public static final String KEY_BUNDLE_USER = "user package";

    public static final String KEY_USERNAME = "username";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_PASS = "pass";


    public static final String KEY_EMAIL = "email";
    public static final String KEY_PHONE = "phone_number";

    public static final String KEY_REMEMBER = "remember";

    public static final String KEY_LOGIN = "login";
    public static final String KEY_REGISTER = "register";
    public static final String KEY_ADD_MATCH = "add";
    public static final String KEY_UPDATE_INFO = "update";

    public static final int METHOD_LOGIN = 1;
    public static final int METHOD_REGISTER = 2;
    public static final int METHOD_GET_MATCH = 3;
    public static final int METHOD_ADD_MATCH = 4;
    public static final int METHOD_JOIN_MATCH = 5;
    public static final int METHOD_UPDATE_INFO = 6;

    public static final String KEY_MATCH_ID = "match_id";
    public static final String KEY_DATE = "date";
    public static final String KEY_FIELD_NAME = "fieldName";
    public static final String KEY_MAX_PLAYERS = "maxPlayers";
    public static final String KEY_NUM_PLAYERS = "numberPlayers";
    public static final String KEY_PRICE = "price";


    public static void showToast(Context context, String sms) {
        Toast.makeText(context, sms, Toast.LENGTH_SHORT).show();
    }

    // method for save and get nick and pass user

    public static void save(Context context, String key, String value) {
        SharedPreferences.Editor editor = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext())
                .edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String get(Context context, String key) {
        SharedPreferences settings = PreferenceManager
                .getDefaultSharedPreferences(context.getApplicationContext());
        return settings.getString(key, null);
    }
}
