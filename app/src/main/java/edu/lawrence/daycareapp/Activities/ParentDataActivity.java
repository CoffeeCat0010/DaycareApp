package edu.lawrence.daycareapp.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;

import edu.lawrence.daycareapp.R;
import edu.lawrence.daycareapp.URIHandler;
import edu.lawrence.daycareapp.data.Parent;

public class ParentDataActivity extends AppCompatActivity {
    Gson gson;
    private int userID;
    private Boolean isEditing;
    private Parent editedParent;
    private EditText name;
    private EditText phone;
    private EditText address;
    private EditText city;
    private EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new Gson();
        setContentView(R.layout.activity_parent_data);
        Intent intent = getIntent();
        isEditing = intent.getBooleanExtra("editing", false);
        userID = intent.getIntExtra("ID", 0);

        name = findViewById(R.id.editName);
        phone = findViewById(R.id.editPhone);
        address = findViewById(R.id.editAddress);
        city = findViewById(R.id.editCity);
        email = findViewById(R.id.editEmail);

        new ParentLookupTask(userID).execute();

    }

    public void onConfirm(View view){
        editedParent.setName(name.getText().toString());
        editedParent.setPhone(phone.getText().toString());
        editedParent.setAddress(address.getText().toString());
        editedParent.setCity(city.getText().toString());
        editedParent.setEmail(email.getText().toString());
        new ParentCommitTask(editedParent).execute();
    }
    private class ParentLookupTask extends AsyncTask<Void, Void, String> {
        int queriedUserId;
        String uri = null;
        public ParentLookupTask(int id){
            queriedUserId = id;
            uri = "http://" + URIHandler.HOST_NAME + "/parent?user=" + queriedUserId;
        }

        @Override
        protected String doInBackground(Void... voids) {
            return URIHandler.doGet(uri);
        }

        @Override
        protected void onPostExecute(String json) {
            editedParent = gson.fromJson(json, Parent.class);
            if (editedParent != null) {
                name.setText(editedParent.getName());
                phone.setText(editedParent.getPhone());
                address.setText(editedParent.getAddress());
                city.setText(editedParent.getCity());
                email.setText(editedParent.getEmail());
            }else{
                editedParent = new Parent();
                editedParent.setUser(userID);
            }
        }
    }
    private class ParentCommitTask extends AsyncTask<Void, Void, String> {
        String uri = null;
        String data = null;
        public ParentCommitTask(Parent parent){

            uri = "http://" + URIHandler.HOST_NAME + "/parent";
            data = gson.toJson(parent);
        }

        @Override
        protected String doInBackground(Void... voids) {
            if(isEditing)
                return URIHandler.doPut(uri, data);
            else return URIHandler.doPost(uri,data);
        }

        @Override
        protected void onPostExecute(String id) {
            goToDashBoard();
        }
    }

    private void goToDashBoard(){
        Intent intent = new Intent(this, DashboardActivity.class);
        intent.putExtra(LoginActivity.USER_ID, String.valueOf(editedParent.getUser()));
        startActivity(intent);
    }

}
