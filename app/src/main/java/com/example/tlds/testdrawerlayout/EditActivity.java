package com.example.tlds.testdrawerlayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class EditActivity extends AppCompatActivity implements LoadJson.OnFinishLoadJSonListener {

    private EditText editEmail, editPhone;
    private Button btnUpdate;

    private  String Username, userID;

    private LoadJson loadJson;

    private ProgressDialog progressDialog;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        context = this;

        loadJson = new LoadJson();
        loadJson.setOnFinishLoadJSonListener(this);

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(R.string.wait));

        connectToView();

        getInfoFromMainActivity();

        clickEvents();
    }

    private void clickEvents() {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateInfo();
            }
        });
    }

    private void updateInfo() {

        HashMap<String, String> map = new HashMap<>();

        map.put(Var.KEY_USERNAME, Username);
        Log.e("Username: ", Username);

        map.put(Var.KEY_EMAIL, editEmail.getText().toString().trim());
        Log.e("Email: ", editEmail.getText().toString().trim());

        map.put(Var.KEY_PHONE, editPhone.getText().toString().trim());
        Log.e("Phone: ", editPhone.getText().toString().trim());

        loadJson.sendDataToServer(Var.METHOD_UPDATE_INFO, map);
        progressDialog.show();
    }

    private void getInfoFromMainActivity() {
        Intent intent = getIntent();
        Bundle pack = intent.getBundleExtra(Var.KEY_BUNDLE_USER);

        Username = pack.getString(Var.KEY_USERNAME);
        userID = pack.getString(Var.KEY_USER_ID);

        editPhone.setText(pack.getString(Var.KEY_PHONE));
        editEmail.setText(pack.getString(Var.KEY_EMAIL));
    }

    private void connectToView() {
        editEmail = (EditText)findViewById(R.id.editEmailUpdate);
        editPhone = (EditText)findViewById(R.id.editPhoneUpdate);

        btnUpdate = (Button)findViewById(R.id.btnUpdate);

    }

    @Override
    public void finishLoadJSon(String error, String json) {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                if (jsonObject.getBoolean(Var.KEY_UPDATE_INFO)) {
                    Var.showToast(context, context.getResources().getString(R.string.update_success));
                    openMainActivity();
                    finish();
                } else {
                    Var.showToast(context, context.getResources().getString(R.string.update_fail));
                    finish();
                }
            } else {
                Var.showToast(context, error);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void openMainActivity() {
        Intent main = new Intent(EditActivity.this, MainActivity.class);
        Bundle pack = new Bundle();

        pack.putString(Var.KEY_USERNAME, Username);
        pack.putString(Var.KEY_USER_ID, userID);
        main.putExtra(Var.KEY_BUNDLE_USER, pack);

        startActivity(main);
    }
}
