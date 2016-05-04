package com.example.tlds.testdrawerlayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private Context context;

    private String Username, userID;

    private TextView txtUsername, txtEmail, txtPhone;
    private ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        context = this;
        connectToView();

        getUserFromCallerActivity();

        //show profile information
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new getJSon_Info().execute("http://minhthangtkqn-001-site1.1tempurl.com/JSON_user_profiles.php");
            }
        });

        //set up action when click button
//        ClickEvents();

        //set up drawer layout
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Soccer Social");

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);
    }

    private void getUserFromCallerActivity() {
        Intent destination = getIntent();
        Bundle pack = destination.getBundleExtra(Var.KEY_BUNDLE_USER);
        Username = pack.getString(Var.KEY_USER);
        userID = pack.getString(Var.KEY_USER_ID);
    }

    private void ClickEvents(){
        //set up action when click button

    }

    public void openProfile() {
        Intent intent = new Intent(context, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Var.KEY_USER, txtUsername.getText().toString());
        bundle.putString(Var.KEY_USER_ID, userID);
        intent.putExtra(Var.KEY_BUNDLE_USER, bundle);
        startActivity(intent);
    }

    public void openMatchsList(){
        Intent intent = new Intent(context, Matchs_View.class);
        Bundle bundle = new Bundle();
        bundle.putString(Var.KEY_USER, txtUsername.getText().toString());
        bundle.putString(Var.KEY_USER_ID, userID);
        intent.putExtra(Var.KEY_BUNDLE_USER, bundle);
        startActivity(intent);
    }


    public void connectToView(){
        txtUsername = (TextView)findViewById(R.id.txtUsername);
        txtEmail = (TextView)findViewById(R.id.txtEmail);
        txtPhone = (TextView)findViewById(R.id.txtPhone);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Intent intent;
        switch (position){
            case 0: //Matchs List - Home
                openMatchsList();
                break;
            case 1: //User Profile
                openProfile();
                break;
            case 2: //Logout
                intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }


    class getJSon_Info extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return LoginActivity.readContentFromURL(params[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray array = new JSONArray(s);
                for(int i=0; i<array.length(); i++){
                    JSONObject profile = array.getJSONObject(i);

                    if(Username.equals(profile.getString("username").toString())){
                        txtPhone.setText(profile.getString("phone_number").toString());
                        txtEmail.setText(profile.getString("email").toString());
                        txtUsername.setText(profile.getString("username").toString());
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}