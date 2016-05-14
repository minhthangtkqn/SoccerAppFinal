package com.example.tlds.testdrawerlayout;

import android.app.ProgressDialog;
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
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class Match_Register extends AppCompatActivity implements LoadJson.OnFinishLoadJSonListener, DatePickerDialog.OnDateSetListener {

    private LoadJson loadJson;

    private Spinner spinnerFieldName;
    private EditText editPrice, editMaxPlayes;
    private TextView txtFieldName;
    private Button btnMatchRegister, btnDate;

    private HashMap<String, String> map = new HashMap<>();

    private ArrayList<String> listFieldName = new ArrayList<String>();
    private ArrayAdapter<String>adapter;

    private String Username, userId, match_id;
    private String fieldID, price, maxPlayers;
    private String Date = "";
    private Context context;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_match__register);

        context = this;

        connectToView();

        loadJson = new LoadJson();
        loadJson.setOnFinishLoadJSonListener(this);

        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(R.string.wait));

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
        clickEvents();
    }

    private void clickEvents() {
        btnMatchRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if( !validate())
                    return;

                fieldID = map.get(txtFieldName.getText().toString());
                price = editPrice.getText().toString().trim();
                maxPlayers = editMaxPlayes.getText().toString().trim();

                HashMap<String, String>data = new HashMap<String, String>();
                data.put("host_id", userId);
                data.put("field_id", fieldID);
                data.put("price", price);
                data.put("maximum_players", maxPlayers);
                data.put("start_time", Date);

                loadJson.sendDataToServer(Var.METHOD_ADD_MATCH, data);
                progressDialog.show();
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
    }

    private boolean validate() {
        boolean value = true;

        if(editPrice.getText().toString().trim().isEmpty())
        {
            editPrice.setError("Enter Price");
            value = false;
        }
        if(editMaxPlayes.getText().toString().trim().isEmpty())
        {
            editMaxPlayes.setError("Enter number of Players");
            value = false;
        }
        if (Date.equals(""))
        {
            value = false;
            Toast.makeText(context, "Choose Date, please", Toast.LENGTH_LONG).show();
        }
        return value;
    }

    private void showDatePicker() {
        Calendar now = Calendar.getInstance();
        DatePickerDialog dpd = DatePickerDialog.newInstance(
                this,
                now.get(Calendar.YEAR),
                now.get(Calendar.MONTH),
                now.get(Calendar.DAY_OF_MONTH)
        );

        String title;
        title = context.getResources().getString(R.string.date);
        dpd.show(getFragmentManager(), title);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = dayOfMonth + "/" + monthOfYear + "/" + year;
        Date = year + "-" + monthOfYear + "-" + dayOfMonth;
        btnDate.setText(date);
    }

    private void arrayAdapterConnect(){
        adapter = new ArrayAdapter<String>(Match_Register.this, android.R.layout.simple_spinner_item, listFieldName);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinnerFieldName.setAdapter(adapter);
        spinnerFieldName.setSelection(0);
        spinnerFieldName.setOnItemSelectedListener(new MyProcessEvent());
    }

    private void openMatchsList(){
        Intent intent = new Intent(context, Matchs_View.class);
        Bundle bundle = new Bundle();
        bundle.putString(Var.KEY_USERNAME, Username);
        bundle.putString(Var.KEY_USER_ID, userId);
        intent.putExtra(Var.KEY_BUNDLE_USER, bundle);
        startActivity(intent);
    }

    @Override
    public void finishLoadJSon(String error, String json) {
        if (progressDialog.isShowing()) {
            progressDialog.hide();
        }
        try {
            if (json != null) {
                JSONObject jsonObject = new JSONObject(json);
                if (jsonObject.getBoolean(Var.KEY_ADD_MATCH)) {
                    //tao tran dau thanh cong
                    Var.showToast(context, context.getResources().getString(R.string.add_match_success));

                    new getMatchID().execute("http://minhthangtkqn-001-site1.1tempurl.com/matches.php");

                    openMatchsList();

                    finish();
                } else {
                    Var.showToast(context, context.getResources().getString(R.string.add_match_fail));
                }
            } else {
                Var.showToast(context, error);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
        editPrice = (EditText)findViewById(R.id.editPrice);
        editMaxPlayes = (EditText)findViewById(R.id.editMaxPlayers);

        btnMatchRegister = (Button)findViewById(R.id.btnRegisterMatch);
        btnDate = (Button)findViewById(R.id.btnDate);
    }


    private void getUserFromCallerActivity() {
        Intent destination = getIntent();
        Bundle pack = destination.getBundleExtra(Var.KEY_BUNDLE_USER);
        Username = pack.getString(Var.KEY_USERNAME);
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
                                userId = profile.getString("userId").toString();
                                Log.e("-------------User ID: ", userId);
                            }
                        }
                    }
                    case 2: {
                            for(int i=0; i<array.length(); i++){
                                JSONObject profile = array.getJSONObject(i);
                                map.put(profile.getString("field_name"), profile.getString("field_id"));
                                listFieldName.add(profile.getString("field_name").toString());
                                Log.e("---ListFieldName--: ", listFieldName.get(i) + i);
                            }
                        adapter.notifyDataSetChanged();
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class getMatchID extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return LoginActivity.readContentFromURL(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray array = new JSONArray(s);
                for(int i=0; i<array.length(); i++){
                    JSONObject match = array.getJSONObject(i);

                    match_id = match.getString("match_id");

                    Log.e("++++ Match ID: ", match_id);
                    Log.e("++++ User ID: ", userId);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            new SendJoin(userId, match_id);
        }
    }

    class SendJoin implements LoadJson.OnFinishLoadJSonListener{

        public SendJoin(String user, String match){

            LoadJson loadJsonJoin = new LoadJson();
            loadJsonJoin.setOnFinishLoadJSonListener(this);

            HashMap<String, String> dataJoin = new HashMap<>();
            dataJoin.put("userId", user);
            dataJoin.put("match_id", match);

            loadJsonJoin.sendDataToServer(Var.METHOD_JOIN_MATCH, dataJoin);
            progressDialog.show();
        }

        @Override
        public void finishLoadJSon(String error, String json) {
            if (progressDialog.isShowing()) {
                progressDialog.hide();
            }
            try {
                if (json != null) {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.getBoolean(Var.KEY_ADD_MATCH)) {
                        //tao join thanh cong
                        Var.showToast(context, context.getResources().getString(R.string.add_join_success));

                        finish();
                    } else {
                        Var.showToast(context, context.getResources().getString(R.string.add_join_fail));
                    }
                } else {
                    Var.showToast(context, error);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}
