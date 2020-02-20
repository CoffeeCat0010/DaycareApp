package edu.lawrence.daycareapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import com.google.gson.*;

import edu.lawrence.daycareapp.R;
import edu.lawrence.daycareapp.URIHandler;
import edu.lawrence.daycareapp.data.Parent;

public class DashboardActivity extends AppCompatActivity {

    private Gson gson;
    private Parent shownParent;
    private TextView nameView;
    private TextView phoneView;
    private TextView addressView;
    private TextView cityView;
    private TextView emailView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        setContentView(R.layout.activity_dashboard);
        Intent intent = getIntent();
        String idExtra = intent.getStringExtra(LoginActivity.USER_ID);
        nameView = findViewById(R.id.NameView);
        phoneView= findViewById(R.id.PhoneView);
        addressView = findViewById(R.id.AddressView);
        cityView = findViewById(R.id.CityView);
        emailView = findViewById(R.id.EmailView);
        int userID = Integer.parseInt(idExtra);
        new ParentLookupTask(userID).execute();
    }
    private class ParentLookupTask extends AsyncTask<Void, Void, String> {
        int userId;
        String uri = null;
        public ParentLookupTask(int id){
            userId = id;
            uri = "http://" + URIHandler.HOST_NAME + "/parent?user=" + userId;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return URIHandler.doGet(uri);
        }

        @Override
        protected void onPostExecute(String json) {
            shownParent = gson.fromJson(json, Parent.class);
            nameView.setText("Name: " + shownParent.getName());
            phoneView.setText("Phone: " + shownParent.getPhone());
            addressView.setText("Address: " + shownParent.getAddress());
            cityView.setText("City: " + shownParent.getCity());
            emailView.setText("Phone: " + shownParent.getEmail());
        }
    }
    public void onEditRequest(View view){
        goToDataInput(shownParent);

    }
    private void goToDataInput(Parent parent){
        Intent intent = new Intent(this, ParentDataActivity.class);
        intent.putExtra("editing", true);
        intent.putExtra("ID", parent.getUser());
        startActivity(intent);
    }

    public void goToChildList(View view){
        Intent intent = new Intent(this, ChildrenActivity.class);
        intent.putExtra("parent", shownParent.getId());
        startActivity(intent);

    }
}
