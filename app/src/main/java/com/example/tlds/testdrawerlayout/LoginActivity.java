package com.example.tlds.testdrawerlayout;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin;
    ImageView btnShowPassword;
    EditText inputPassword, inputUserName;
    TextView signUp;
    Toast successLogin, failLogin;
    CheckBox rememberMe;

    String userID;

    private final String DefaultUnameValue = "";
    private String UnameValue;

    private final String DefaultPasswordValue = "";
    private String PasswordValue;

    private ProgressDialog progressDialog;

    Context context;

//    public static final String KEY_USER = "user";
//    public static final String KEY_PASSWORD = "password";
//    public static final String KEY_REMEMBER_ME = "remember me";

    public void login(){
        progressDialog.show();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new getJSONLogin().execute("http://minhthangtkqn-001-site1.1tempurl.com/JSON_user_profiles.php");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        context = this;

        //create Wait progress Dialog
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage(context.getResources().getString(R.string.wait));

        connectView();

        //set up action when click buttons
        ClickEvents();
    }

    //Prevent back button form go into app
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }

    private void ClickEvents(){
        //Nhấn giữ để hiển thị password
        btnShowPassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        inputPassword.setInputType(inputPassword.getInputType() ^ InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return true;
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signUpActivity = new Intent(getApplicationContext(), Sign_Up.class);
                startActivity(signUpActivity);
            }
        });
    }

    private void connectView() {
        btnLogin = (Button)findViewById(R.id.btnLogin);
        btnShowPassword = (ImageView)findViewById(R.id.btnShowPassword);
        inputPassword = (EditText)findViewById(R.id.editPassword);
        inputUserName = (EditText)findViewById(R.id.editUserName);
        signUp = (TextView)findViewById(R.id.signUp);
        rememberMe = (CheckBox)findViewById(R.id.rememberMe);

        successLogin = Toast.makeText(LoginActivity.this, "Login Success !!!", Toast.LENGTH_SHORT);
        failLogin = Toast.makeText(LoginActivity.this, "Username or Password is incorrect !!!", Toast.LENGTH_SHORT);
    }


    public static String readContentFromURL(String theUrl)
    {
        StringBuilder content = new StringBuilder();

        // many of these calls can throw exceptions, so i've just
        // wrapped them all in one try/catch statement.
        try
        {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null)
            {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return content.toString();
    }


    class getJSONLogin extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... params) {
            return readContentFromURL(params[0]);
        }


        @Override
        protected void onPostExecute(String s) {
            try {
                if(!validate())
                    return;
                JSONArray array = new JSONArray(s);
                for(int i=0; i<array.length(); i++){

                    //Mỗi profile là một tài khoản
                    JSONObject profile = array.getJSONObject(i);

                    //So sánh với CSDL để đăng nhập
                    if(inputUserName.getText().toString().equals(profile.getString("username").toString() )
                            &&  inputPassword.getText().toString().equals(profile.getString("password").toString() ) )
                    {
                        if(progressDialog.isShowing())
                            progressDialog.hide();
                        userID = profile.getString("user_id").toString();
                        successLogin.show();
                        savingPreferences();
                        openProfileActivity();
                        break;
                    }
                    if(i == array.length() - 1)
                    {
                        if(progressDialog.isShowing())
                            progressDialog.hide();
                        failLogin.show();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void openProfileActivity(){
            Intent matches = new Intent(LoginActivity.this, MainActivity.class);
            Bundle userPack = new Bundle();
            userPack.putString(Var.KEY_USER, inputUserName.getText().toString());
            userPack.putString(Var.KEY_USER_ID, userID);
            matches.putExtra(Var.KEY_BUNDLE_USER, userPack);
            startActivity(matches);
        }

        private boolean validate(){                 //kiem tra dieu kien cua ten tai khoan va mat khau
            boolean value = true;
            if(inputUserName.getText().toString().trim().isEmpty()){
                inputUserName.setError("Enter UserName");
                value = false;
            }
            else
            if(inputPassword.getText().toString().isEmpty() || inputPassword.getText().length() <5){
                inputPassword.setError("Password too short");
                value = false;
            }
            return value;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        savingPreferences();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPreferences();
    }

    public void savingPreferences(){
        SharedPreferences preferences = getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

//        String user = inputUserName.getText().toString();
//        String pass = inputPassword.getText().toString();

        UnameValue = inputUserName.getText().toString();
        PasswordValue = inputPassword.getText().toString();

        System.out.println("onPause save name: " + UnameValue);
        System.out.println("onPause save password: " + PasswordValue);

        boolean bChk = rememberMe.isChecked();

        if(!bChk){
            editor.clear();
        }
        else
        {
            editor.putString(Var.KEY_USER, UnameValue);
            editor.putString(Var.KEY_PASS, PasswordValue);
            editor.putBoolean(Var.KEY_REMEMBER, bChk);
        }
        editor.commit();
    }

    private void loadPreferences() {

        SharedPreferences settings = getSharedPreferences("data", Context.MODE_PRIVATE);

        // Get value

        UnameValue = settings.getString(Var.KEY_USER, DefaultUnameValue);
        PasswordValue = settings.getString(Var.KEY_PASS, DefaultPasswordValue);
        inputUserName.setText(UnameValue);
        inputPassword.setText(PasswordValue);

        if(inputUserName.getText().toString().equals("")){
            rememberMe.setChecked(false);
        }
        else
        {
            rememberMe.setChecked(true);
        }

//        inputUserName.setText("");
//        inputPassword.setText("");
//        inputUserName.requestFocus();

        System.out.println("onResume load name: " + UnameValue);
        System.out.println("onResume load password: " + PasswordValue);
    }
}
