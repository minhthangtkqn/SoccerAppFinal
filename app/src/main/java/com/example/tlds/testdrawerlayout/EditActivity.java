package com.example.tlds.testdrawerlayout;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class EditActivity extends AppCompatActivity {

    private EditText editEmail, editPhone;
    private Button btnUpdate;

    private  String Username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        connectToView();

        getInfoFromMainActivity();

    }

    private void getInfoFromMainActivity() {
        Intent intent = getIntent();
        Bundle pack = intent.getBundleExtra(Var.KEY_BUNDLE_USER);

        Username = pack.getString(Var.KEY_USERNAME);
        editPhone.setText(pack.getString(Var.KEY_PHONE));
        editEmail.setText(pack.getString(Var.KEY_EMAIL));

    }

    private void connectToView() {
        editEmail = (EditText)findViewById(R.id.editEmailUpdate);
        editPhone = (EditText)findViewById(R.id.editPhoneUpdate);

        btnUpdate = (Button)findViewById(R.id.btnUpdate);

    }
}
