package edu.lawrence.daycareapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import edu.lawrence.daycareapp.R;
import edu.lawrence.daycareapp.URIHandler;

public class LoginActivity extends AppCompatActivity {
    public final static String USER_ID = "edu.lawrence.DaycareApp.USER_ID";
    private TextView error;
    EditText userInput;
    EditText passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        userInput = (EditText) findViewById(R.id.userNameInput);
        passwordInput = (EditText) findViewById(R.id.passwordInput);
        error = findViewById(R.id.error);
    }

    public void onLoginAttempt(View view)
    {

        String username = userInput.getText().toString();
        String password = passwordInput.getText().toString();

        new loginTask(username,password).execute();
    }
    private class loginTask extends AsyncTask<Void, Void, String> {

        private String uri = null;

        loginTask(String username, String password){
            uri = uri="http://"+ URIHandler.HOST_NAME+"/user?name="+username+"&password="+password;
        }

        @Override
        protected String doInBackground(Void... voids) {

            return URIHandler.doGet(uri);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                Log.d("Login", "No response from server");
            } else
                goToDashBoard(result);
        }
    }
    public void goToDashBoard(String ID){
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.putExtra(USER_ID, ID);
        startActivity(intent);
    }

    public void onNewUserRequest(View view){
        String username = userInput.getText().toString();
        String password = passwordInput.getText().toString();

        new queryLoginTask(username, password).execute();
    }
    private class queryLoginTask extends AsyncTask<Void, Void, String> {

        private String uri = null;
        private String mUsername;
        private String mPassword;
        queryLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
            uri = "http://" + URIHandler.HOST_NAME + "/user?name=" + username + "&password=" + password;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return URIHandler.doGet(uri);
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                Log.d("Login", "No response from server");
            } else if (result.equals("-1")) {
                new newUserTask(mUsername, mPassword).execute();
            }
            else error.setText("Username or Password already taken");
        }
    }
    private class newUserTask extends AsyncTask<Void, Void, String> {
        String uri = null;
        String json = null;
        public newUserTask(String username, String password){
            uri = "http://" + URIHandler.HOST_NAME + "/user";
            json ="{\"name\":\"" +username + "\",\"password\":\"" +password+ "\"}";
        }

        @Override
        protected String doInBackground(Void... voids) {

            return URIHandler.doPost(uri, json);
        }

        @Override
        protected void onPostExecute(String result) {
            goToParentDataActivity(result);
        }
    }
    public void goToParentDataActivity(String id){
        Intent intent = new Intent(this, ParentDataActivity.class);
        intent.putExtra("editing", false);
        intent.putExtra("ID", Integer.parseInt(id));
        startActivity(intent);
    }

}
