package com.example.tlds.testdrawerlayout;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Match_Register extends AppCompatActivity {

    private Spinner spinnerFieldName;
    private EditText editPrice, editMaxPlayes;
    private TextView txtFieldName;
    private Button btnMatchRegister;

    private ArrayList<String> listFieldName = new ArrayList<String>();
    private ArrayAdapter<String>adapter;

    private String Username, user_id;
    private String fieldName, price, maxPlayers;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match__register);

        context = this;

        connectToView();

        getUserFromCallerActivity();

        //get UserID and Field Name
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new getUserIDAndFieldName().execute("http://minhthangtkqn-001-site1.1tempurl.com/JSON_user_profiles.php");
                new getUserIDAndFieldName().execute("http://minhthangtkqn-001-site1.1tempurl.com/JSON_fields.php");
            }
        });

        arrayAdapterConnect();

    }

    private void arrayAdapterConnect(){
        adapter = new ArrayAdapter<String>(Match_Register.this, android.R.layout.simple_spinner_item, listFieldName);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerFieldName.setAdapter(adapter);
        spinnerFieldName.setSelection(0);
        spinnerFieldName.setOnItemSelectedListener(new MyProcessEvent());
    }

    class MyProcessEvent implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            txtFieldName.setText(listFieldName.get(position));
            Log.e("-----" + position + " Ten san: ", listFieldName.get(position).toString());
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            txtFieldName.setText("");
        }
    }

    private void connectToView() {
        spinnerFieldName = (Spinner)findViewById(R.id.spinnerFieldName);
        editPrice = (EditText)findViewById(R.id.editPrice);
        editMaxPlayes = (EditText)findViewById(R.id.editMaxPlayers);

        txtFieldName = (TextView)findViewById(R.id.txtFieldName);

        btnMatchRegister = (Button)findViewById(R.id.btnRegisterMatch);
    }


    private void getUserFromCallerActivity() {
        Intent destination = getIntent();
        Bundle pack = destination.getBundleExtra(Var.KEY_BUNDLE_USER);
        Username = pack.getString(Var.KEY_USER);
    }

    class getUserIDAndFieldName extends AsyncTask<String, Integer, String> {

        private int check;

        @Override
        protected String doInBackground(String... params) {
            if(params[0].equals("http://minhthangtkqn-001-site1.1tempurl.com/JSON_user_profiles.php"))
                check = 1;
            if(params[0].equals("http://minhthangtkqn-001-site1.1tempurl.com/JSON_fields.php"))
                check = 2;
            return LoginActivity.readContentFromURL(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray array = new JSONArray(s);
                switch (check){
                    case 1:{
                        for(int i=0; i<array.length(); i++){
                            JSONObject profile = array.getJSONObject(i);

                            if(Username.equals(profile.getString("username").toString())){
                                user_id = profile.getString("user_id").toString();
                                Log.e("-------------User ID: ", user_id);
                            }
                        }
                    }
                    case 2: {
                            for(int i=0; i<array.length(); i++){
                            JSONObject profile = array.getJSONObject(i);

                            listFieldName.add(profile.getString("field_name").toString());
                            Log.e("---ListFieldName--: ", listFieldName.get(i) + i);
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
