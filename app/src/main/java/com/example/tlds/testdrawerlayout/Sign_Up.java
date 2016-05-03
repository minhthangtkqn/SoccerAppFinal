package com.example.tlds.testdrawerlayout;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Sign_Up extends AppCompatActivity implements View.OnClickListener, LoadJson.OnFinishLoadJSonListener {

    private Button btnRegister;
    private EditText editUser, editEmail, editPass, editCFPass;
    private Context context;
    private LoadJson loadJson;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign__up);

        context = this;

        setupToolbar();

        connectView();

        loadJson = new LoadJson();
        loadJson.setOnFinishLoadJSonListener(this);
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(R.string.wait));

    }

    private void connectView() {
        editUser = (EditText)findViewById(R.id.editUser);
        editCFPass = (EditText)findViewById(R.id.editCfpass);
        editPass = (EditText)findViewById(R.id.editPass);
//        editEmail = (EditText)findViewById(R.id.editEmail);

        findViewById(R.id.btnRegister).setOnClickListener(this);
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


    @Override
    public void onClick(View v) {
        switch (v.getId()){
//            case R.id.btn_reset:
//                reset();
//                break;
            case R.id.btnRegister:
                register();
                break;
            default:
                break;
        }
    }

    private void register() {
        String nick = editUser.getText().toString().trim();
        String pass = editPass.getText().toString().trim();
        String repass = editCFPass.getText().toString().trim();

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

        HashMap<String, String> map = new HashMap<>();
        map.put(Var.KEY_USER, nick);
        map.put(Var.KEY_PASS, pass);

        loadJson.sendDataToServer(Var.METHOD_REGISTER, map);
        progressDialog.show();
    }

    private void reset() {
        editUser.setText("");
//        editEmail.setText("");
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
}
