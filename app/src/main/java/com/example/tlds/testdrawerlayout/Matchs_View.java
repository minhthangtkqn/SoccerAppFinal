package com.example.tlds.testdrawerlayout;

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
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Matchs_View extends AppCompatActivity implements FragmentDrawer.FragmentDrawerListener {

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private Context context;

    private String Username;
    private ListView listView;
    private List<Match> matches = new ArrayList<Match>();
    private Button btnCreateMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matchs__view);

        context = this;
        connectToView();

        Intent caller = getIntent();
        Bundle pack = caller.getBundleExtra(Var.KEY_BUNDLE_USER);
        Username = pack.getString(Var.KEY_USER);

        //show matchs list
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new ViewMatches().execute("http://minhthangtkqn-001-site1.1tempurl.com/matches.php");
            }
        });

        //set up drawer layout
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Soccer Social");

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout), mToolbar);
        drawerFragment.setDrawerListener(this);

        ClickEvents();
    }

    private void ClickEvents() {

        btnCreateMatch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Match_Register.class);
                Bundle bundle = new Bundle();
                bundle.putString(Var.KEY_USER, Username);
                intent.putExtra(Var.KEY_BUNDLE_USER, bundle);
                startActivity(intent);
            }
        });
    }

    private void connectToView() {
        listView = (ListView)findViewById(R.id.listMatch);
        btnCreateMatch = (Button)findViewById(R.id.btnCreateMatch);
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

    public void openProfile() {
        Intent intent = new Intent(context, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString(Var.KEY_USER, Username);
        intent.putExtra(Var.KEY_BUNDLE_USER, bundle);
        startActivity(intent);
    }

    public void openMatchsList(){
        Intent intent = new Intent(context, Matchs_View.class);
        Bundle bundle = new Bundle();
        bundle.putString(Var.KEY_USER, Username);
        intent.putExtra(Var.KEY_BUNDLE_USER, bundle);
        startActivity(intent);
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

    class ViewMatches extends AsyncTask<String, Integer, String> {

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

                    Match tran = new Match(match.getString("field_name").toString(),
                            match.getString("maximum_players").toString(),
                            match.getString("price").toString(),
                            match.getString("start_time").toString(),
                            match.getString("match_id").toString());
                    matches.add(tran);
                    listView.setAdapter(new CustomListAdapter(Matchs_View.this, matches));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
