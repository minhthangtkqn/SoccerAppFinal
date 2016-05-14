package com.example.tlds.testdrawerlayout;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Sign_Up extends AppCompatActivity implements LoadJson.OnFinishLoadJSonListener {

    private Button btnRegister;
    private EditText editUser, editPass, editCFPass, editEmail, editPhoneNumber;
    private Context context;
    private LoadJson loadJson;
    private ProgressDialog progressDialog;

    private ImageView showPass, showCFPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up);

        context = this;

        setupToolbar();

        connectView();

        clickEvents();

        loadJson = new LoadJson();
        loadJson.setOnFinishLoadJSonListener(this);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(R.string.wait));
    }

    private void clickEvents() {
        showPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        editPass.setInputType(editPass.getInputType() ^ InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return true;
            }
        });

        showCFPass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        editCFPass.setInputType(editCFPass.getInputType() ^ InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return true;
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void connectView() {
        editUser = (EditText)findViewById(R.id.editUser);
        editCFPass = (EditText)findViewById(R.id.editCfpass);
        editPass = (EditText)findViewById(R.id.editPass);
        editEmail = (EditText)findViewById(R.id.editEmail);
        editPhoneNumber = (EditText)findViewById(R.id.editPhoneNumber);

        editPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        editCFPass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        showPass = (ImageView)findViewById(R.id.showPass);
        showCFPass = (ImageView)findViewById(R.id.showCfPass);

        btnRegister = (Button)findViewById(R.id.btnRegister);
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle(R.string.register);
            }
        }
    }

    private void register() {
        String nick = editUser.getText().toString().trim();
        String pass = editPass.getText().toString().trim();
        String repass = editCFPass.getText().toString().trim();
        String email = editEmail.getText().toString().trim();
        String phoneNumber = editPhoneNumber.getText().toString().trim();


        // not enter nick name
        if (nick.length() == 0) {
            editUser.requestFocus();
            Var.showToast(context, context.getResources().getString(R.string.enter_nick));
            return;
        }

        // not enter pass
        if (pass.length() == 0) {
            editPass.requestFocus();
            Var.showToast(context, context.getResources().getString(R.string.enter_pass));
            return;
        }

        // not enter pass confirm
        if (repass.length() == 0) {
            editCFPass.requestFocus();
            Var.showToast(context, context.getResources().getString(R.string.enter_repass));
            return;
        }

        if (!repass.equals(pass)) {
            editPass.requestFocus();
            Var.showToast(context, context.getResources().getString(R.string.pass_not_match));
            return;
        }

        if(email.isEmpty()){
            editEmail.requestFocus();
            Var.showToast(context, context.getResources().getString(R.string.enter_email));
            return;
        }

        if(phoneNumber.isEmpty()){
            editEmail.requestFocus();
            Var.showToast(context, context.getResources().getString(R.string.enter_phone_number));
            return;
        }

        HashMap<String, String> map = new HashMap<>();
        map.put(Var.KEY_USERNAME, nick);
        map.put(Var.KEY_PASS, pass);
        map.put(Var.KEY_EMAIL, email);
        map.put(Var.KEY_PHONE, phoneNumber);

        loadJson.sendDataToServer(Var.METHOD_REGISTER, map);
        progressDialog.show();
    }

    private void reset() {
        editUser.setText("");
        editPass.setText("");
        editCFPass.setText("");
        editUser.requestFocus();
    }

    @Override
    public void finishLoadJSon(String error, String json) {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                if (jsonObject.getBoolean(Var.KEY_REGISTER)) {
                    Var.showToast(context, context.getResources().getString(R.string.register_success));
                    openLoginActivity();
                    finish();
                } else {
                    Var.showToast(context, context.getResources().getString(R.string.register_fail));
                }
            } else {
                Var.showToast(context, error);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void openLoginActivity() {
        Intent login = new Intent(Sign_Up.this, LoginActivity.class);
        Bundle userPack = new Bundle();

        userPack.putString(Var.KEY_USERNAME, editUser.getText().toString().trim());
        userPack.putString(Var.KEY_PASS, editPass.getText().toString().trim());
        login.putExtra(Var.KEY_BUNDLE_USER, userPack);

        startActivity(login);
    }
}
