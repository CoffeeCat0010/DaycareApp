package edu.lawrence.daycareapp.Activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import edu.lawrence.daycareapp.R;
import edu.lawrence.daycareapp.URIHandler;
import edu.lawrence.daycareapp.data.Registration;

public class ConfirmRegistrationActivity extends AppCompatActivity {

    private int parent;
    private int childId;
    private int providerId;
    private Date start;
    private Date end;
    private Registration registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_registration);

        Intent intent = getIntent();
        parent = intent.getIntExtra("parent", 0);
        providerId = intent.getIntExtra("provider", 0);
        childId = intent.getIntExtra("child", 0);
        start = (Date)intent.getSerializableExtra("start");
        end = (Date)intent.getSerializableExtra("end");
    }

    public void onConfirm(View view) {
        registration = new Registration();
        registration.setChild(childId);
        registration.setProvider(providerId);
        registration.setStart(start);
        registration.setEnd(end);
        registration.setStatus(1);
        new registrationAddTask(registration).execute();
    }

    private class registrationAddTask extends AsyncTask<Void, Void, String> {
        private String uri = null;
        private String data = null;

        private GsonBuilder gsonBuilder;

        public registrationAddTask(Registration input){
            uri = "http://" + URIHandler.HOST_NAME + "/registration";
            gsonBuilder = new GsonBuilder().setDateFormat("yyyy-MM-dd");
            Gson gson = gsonBuilder.create();
            data = gson.toJson(input);
        }

        @Override
        protected String doInBackground(Void... voids) {
            return URIHandler.doPost(uri, data);
        }

        @Override
        protected void onPostExecute(String s) {
            goToChildren();
        }

    }
    private void goToChildren(){
        Intent intent = new Intent(this, ChildrenActivity.class);
        intent.putExtra("parent", parent);
        startActivity(intent);
    }
}
