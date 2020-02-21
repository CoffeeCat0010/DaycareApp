package edu.lawrence.daycareapp.Activities;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import edu.lawrence.daycareapp.R;
import edu.lawrence.daycareapp.URIHandler;
import edu.lawrence.daycareapp.data.Parent;
import edu.lawrence.daycareapp.data.Provider;

public class Registration_Activity extends AppCompatActivity {
    private RecyclerView providerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager manager;

    private Gson gson;

    private int childid;
    private int parentid;

    private Parent childParent;
    private String startDate;
    private String endDate;


    private ArrayList<Provider> mDataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_);
        mDataset = new ArrayList<>();
        gson = new Gson();
        Intent intent = getIntent();
        childid = intent.getIntExtra("child", 0);
        parentid = intent.getIntExtra("parent", 0);
        new getParentTask(parentid).execute();
        DialogFragment fragment2 = new StartDatePicker(R.string.End_day, s -> endDate = s);
        fragment2.show(getSupportFragmentManager(), "Start Date");
        DialogFragment fragment = new StartDatePicker(R.string.Start_day, s -> {startDate = s; new getProvidersTask(childParent).execute();});
        fragment.show(getSupportFragmentManager(), "Start Date");
        providerView = findViewById(R.id.providerView);
        manager = new LinearLayoutManager(this);
        providerView.setLayoutManager(manager);
        adapter = new ProviderAdapter();
        providerView.setAdapter(adapter);

    }

    public void goToRegConfirm(Provider provider){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date start = null;
        Date end = null;
        try {
            start = simpleDateFormat.parse(startDate);
            end = simpleDateFormat.parse(endDate);
        } catch (ParseException e) {
            Log.d("Error", "Could not parse date");
        }
        Intent intent = new Intent(this, ConfirmRegistrationActivity.class);
        intent.putExtra("parent", parentid);
        intent.putExtra("provider", provider.getId());
        intent.putExtra("child", childid);
        intent.putExtra("start", start );
        intent.putExtra("end", end);
        startActivity(intent);
    }

    private class ProviderAdapter extends RecyclerView.Adapter<ProviderAdapter.ProviderViewHolder> {
        private int selected;
        private class ProviderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
            private TextView name;
            private TextView distance;
            public ProviderViewHolder(View v) {
                super(v);
                name = v.findViewById(R.id.ProviderNameText);
                distance = v.findViewById(R.id.ProviderDistanceText);
                v.setOnClickListener(this::onClick);
                selected = -1;
            }

            @Override
            public void onClick(View v) {
                if(getAdapterPosition() == RecyclerView.NO_POSITION) return;
                notifyItemChanged(selected);
                selected = getAdapterPosition();
                notifyItemChanged(selected);
                goToRegConfirm(mDataset.get(selected));
            }
        }


        @Override
        public ProviderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = (View) LayoutInflater.from(parent.getContext())
                                        .inflate(R.layout.provider_view_holder, parent, false);
            ProviderViewHolder viewHolder = new ProviderViewHolder(view);
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(ProviderViewHolder holder, int position) {
            holder.name.setText(mDataset.get(position).getName());
            holder.distance.setText(mDataset.get(position).getAddress() + ", " + mDataset.get(position).getCity());
            holder.itemView.setBackgroundColor(selected == position ? Color.DKGRAY : android.R.style.Theme_Black_NoTitleBar);
        }

        @Override
        public int getItemCount() {
            return mDataset.size();
        }

        public void setDataSet(Provider[] list){
            mDataset.clear();
            mDataset.addAll(Arrays.asList(list));
            notifyDataSetChanged();
        }


    }
    private class getParentTask extends  AsyncTask<Void, Void, Parent>{
        String uri = null;
        public getParentTask(int parentId) {
            uri = "http://" + URIHandler.HOST_NAME +"/parent?id=" + parentId;
        }

        @Override
        protected Parent doInBackground(Void... voids) {
            String result = URIHandler.doGet(uri);
            return gson.fromJson(result, Parent.class);
        }

        @Override
        protected void onPostExecute(Parent parent) {
            childParent = parent;
        }
    }
    private class getProvidersTask extends AsyncTask <Void, Void, Provider[]> {
        String uri = null;

        public getProvidersTask(Parent parent){
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                java.util.Date utilDate = dateFormat.parse(startDate);
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
            String address = childParent.getAddress().replace( " ", "%20");
            uri = "http://" + URIHandler.HOST_NAME + "/providers?start=" + sqlDate + "&id=" + childid +
                    "&address=" + address + "&city=" + childParent.getCity();
            } catch (ParseException e) {
                Log.d("Date", e.getMessage());
            }
        }

        @Override
        protected Provider[] doInBackground(Void... voids) {
            Provider[] result = gson.fromJson(URIHandler.doGet(uri), Provider[].class);
            return result;
        }

        @Override
        protected void onPostExecute(Provider[] providers) {
            ProviderAdapter providerAdapter = (ProviderAdapter) providerView.getAdapter();
            providerAdapter.setDataSet(providers);
        }
    }
}
