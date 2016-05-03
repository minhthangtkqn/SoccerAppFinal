package com.example.tlds.testdrawerlayout;

/**
 * Created by PC on 4/27/2016.
 */
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class LoadJson {

    public static final String LINK = "http://minhthangtkqn-001-site1.1tempurl.com/user_profiles.php";

    public void sendDataToServer(int method, HashMap<String, String> map) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();

        // put data to server
        params.put(Var.KEY_METHOD, method);

        if (map != null) {
            for (String key : map.keySet()) {
                params.put(key, map.get(key));
            }
        }

        System.out.println("Post...");

        client.post(LINK, params, new AsyncHttpResponseHandler() {

            @SuppressWarnings("deprecation")
            @Override
            public void onSuccess(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody) {
                String json = new String(responseBody);
                System.out.println("onSuccess:" + json);
                onFinishLoadJSonListener.finishLoadJSon(null, json);
            }

            @SuppressWarnings("deprecation")
            @Override
            public void onFailure(int statusCode, cz.msebera.android.httpclient.Header[] headers, byte[] responseBody, Throwable error) {
                System.out.println("onFailure:" + statusCode);

                String e;

                if (statusCode == 404) {
                    e = "Requested resource not found";
                } else if (statusCode == 500) {
                    e = "Something went wrong at server end";
                } else {
                    e = "Device might not be connected to Internet";
                }
                onFinishLoadJSonListener.finishLoadJSon(e, null);
            }
        });
    }

    public static user_frofiles jsonToUser(JSONObject jsonObject) {
        try {
            String username = jsonObject.getString(Var.KEY_USER);
            String password = jsonObject.getString(Var.KEY_PASS);
            String email = jsonObject.getString(Var.KEY_EMAIL);
            String phone_number = jsonObject.getString(Var.KEY_PHONE);

            return new user_frofiles(username, password, email, phone_number);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static ArrayList<user_frofiles> jsonToListUser(String json) {
        ArrayList<user_frofiles> list = new ArrayList<>();

        try {
            JSONArray arraySMSJson = new JSONArray(json);
            for (int i = 0; i < arraySMSJson.length(); i++) {
                JSONObject jsonObject = arraySMSJson.getJSONObject(i);
                user_frofiles user = jsonToUser(jsonObject);
                if (user != null) {
                    list.add(user);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    public interface OnFinishLoadJSonListener {
        void finishLoadJSon(String error, String json);
    }

    public OnFinishLoadJSonListener onFinishLoadJSonListener;

    public void setOnFinishLoadJSonListener(OnFinishLoadJSonListener onFinishLoadJSonListener) {
        this.onFinishLoadJSonListener = onFinishLoadJSonListener;
    }
}
