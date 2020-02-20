package edu.lawrence.daycareapp.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import edu.lawrence.daycareapp.R;
import edu.lawrence.daycareapp.URIHandler;
import edu.lawrence.daycareapp.data.Child;

public class ChildDataActivity extends AppCompatActivity {
    private int parent;
    private int childId;
    private Child child;
    private TextView nameField;
    private TextView dateField;

    private CalendarView datePicker;

    private Button confirm;

    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_data);

        Intent intent = getIntent();
        parent = intent.getIntExtra("parent", 0);
        childId = intent.getIntExtra("child", 0);

        nameField = findViewById(R.id.childNameField);
        dateField = findViewById(R.id.dateText);
        datePicker = findViewById(R.id.datePicker);
        confirm = findViewById(R.id.ChildDataConfirm);
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");

        dateField.setOnFocusChangeListener((v, hasFocus) -> {if(!hasFocus)onDateFieldModified(v);});
        dateField.setOnKeyListener((v, keyCode, event) -> {
            Log.d("key",String.valueOf(keyCode));
            if(keyCode == 66) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                dateField.clearFocus();
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                return true;
            }
            return false;
        });
        datePicker.setOnDateChangeListener((v, year, month, day) -> onDateChange(v, year, month, day));
        if(childId != 0 ) new childLookupTask(childId).execute();
        else child = new Child();
    }
    private class childLookupTask extends AsyncTask<Void, Void, Child> {
        int mChildID;
        String uri = null;
        Gson gson;
        public childLookupTask(int childID){
            mChildID = childID;
            uri = "http://" + URIHandler.HOST_NAME + "/child?id=" + mChildID;
            gson = new Gson();
        }
        @Override
        protected Child doInBackground(Void... voids) {
            String response = URIHandler.doGet(uri);
            Child result = gson.fromJson(response, Child.class);
            Log.d("Children",response);
            return result;
        }

        @Override
        protected void onPostExecute(Child result){
            child = result;
            dateField.setText(dateFormat.format(result.getBirthday()));
            onDateFieldModified();
            nameField.setText(result.getName());
        }
    }

    private void onDateChange(View view, int year, int month, int day){
        String dateSelected = (month + 1) + "/" + day + "/" + year;
        dateField.setText(dateSelected);
        //dateFormat.format(dateSelected);
    }
    private void onDateFieldModified(View view){
        Log.d("onDateFieldModified", "Date changed!");
        String date = dateField.getText().toString();
        try {
           Date newDate =  dateFormat.parse(date);
           datePicker.setDate(newDate.getTime());
        } catch (ParseException e) {
            Log.e("Error", e.getMessage());
        }
    }
    private void onDateFieldModified(){
        Log.d("onDateFieldModified", "Date changed!");
        String date = dateField.getText().toString();
        try {
            Date newDate =  dateFormat.parse(date);
            datePicker.setDate(newDate.getTime());
        } catch (ParseException e) {
            Log.e("Error", e.getMessage());
        }
    }

    public void onConfirm(View view){
        child.setParent(parent);
        child.setName(nameField.getText().toString());
        try {
            Date currentDate = dateFormat.parse(dateField.getText().toString());
            child.setBirthday(currentDate);
        } catch (ParseException e) {
            Log.e("Invalid Input!", e.getMessage());
            return;
        }
        new childAddTask(child).execute();
    }

    private void goToChildren(){
        Intent intent = new Intent(this, ChildrenActivity.class);
        intent.putExtra("parent", parent);
        startActivity(intent);
    }

    private class childAddTask extends AsyncTask<Void, Void, String> {
        private String uri = null;
        private String data = null;

        private GsonBuilder gsonBuilder;

        public childAddTask(Child input){
            uri = "http://" + URIHandler.HOST_NAME + "/child";
            gsonBuilder = new GsonBuilder().setDateFormat("yyyy-MM-dd");
            Gson gson = gsonBuilder.create();
            data = gson.toJson(input);
        }

        @Override
        protected String doInBackground(Void... voids) {
            return childId != 0 ? URIHandler.doPut(uri, data) :URIHandler.doPost(uri, data);
        }

        @Override
        protected void onPostExecute(String s) {
            goToChildren();
        }
    }
}
