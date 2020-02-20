package edu.lawrence.daycareapp.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;

import edu.lawrence.daycareapp.R;
import edu.lawrence.daycareapp.URIHandler;
import edu.lawrence.daycareapp.data.Child;

public class ChildrenActivity extends AppCompatActivity {

    private RecyclerView mChildren;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayout;

    private Toolbar toolbar;

    private AppCompatImageButton addButton;
    private AppCompatImageButton editButton;
    int parent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_children);
        Intent intent = getIntent();
        parent = intent.getIntExtra("parent", 0);
        mChildren = findViewById(R.id.child_view);
        toolbar = findViewById(R.id.tool_bar);
        addButton = findViewById(R.id.AddEntry);
        addButton.setOnClickListener(v -> onAddButtonPressed(v));
        editButton = findViewById(R.id.EditSelcted);
        editButton.setOnClickListener(v -> onEditButtonPressed(v));

        mLayout = new LinearLayoutManager(this);
        mChildren.setLayoutManager(mLayout);

        mAdapter = new ChildAdapter();
        mChildren.setAdapter(mAdapter);

        new childLookupTask(parent).execute();

    }
    private void onEditButtonPressed(View view){
        ChildAdapter adapter = (ChildAdapter) mChildren.getAdapter();
        int childId = adapter.getChildFromDataSet(adapter.getSelected()).getId();
        toChildDataEntry(parent, childId);
    }
    private void onAddButtonPressed(View view){
        toChildDataEntry(parent);
    }
    private void toChildDataEntry(int parentId) {
        Intent intent = new Intent(this, ChildDataActivity.class);
        intent.putExtra("parent", parentId);
        startActivity(intent);
    }
    private void toChildDataEntry(int parentId, int childId) {
        Intent intent = new Intent(this, ChildDataActivity.class);
        intent.putExtra("parent", parentId);
        intent.putExtra("child", childId);
        startActivity(intent);
    }
    private void toRegistion(int childId, int parentId){
        Intent intent = new Intent(this, Registration_Activity.class);
        intent.putExtra("child", childId);
        intent.putExtra("parent", parentId);
        startActivity(intent);
    }
    private class childLookupTask extends AsyncTask<Void, Void, Child[]> {
        int mParentID;
        String uri = null;
        Gson gson;
        public childLookupTask(int parentID){
            mParentID = parentID;
            uri = "http://" + URIHandler.HOST_NAME + "/child?parent=" +parentID;
            gson = new Gson();
        }
        @Override
        protected Child[] doInBackground(Void... voids) {
            String response = URIHandler.doGet(uri);
            Child[] result = gson.fromJson(response, Child[].class);
            Log.d("Children",response);
            return result;
        }

        @Override
        protected void onPostExecute(Child[] result){
            ChildAdapter childAdapter = (ChildAdapter) mChildren.getAdapter();
            childAdapter.setDataSet(result);
        }
    }
    protected class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ChildViewHolder>{
        private ArrayList<Child> mDataSet;
        private int selected;
        class ChildViewHolder extends RecyclerView.ViewHolder implements TextView.OnClickListener{
            protected TextView textView;
            public ChildViewHolder(TextView view){
                super(view);
                textView = view;
                textView.setOnClickListener(this);
            }

            @Override
            public void onClick(View v) {
                if(getAdapterPosition() == RecyclerView.NO_POSITION) return;
                notifyItemChanged(selected);
                selected = getAdapterPosition();
                notifyItemChanged(selected);
                toRegistion(mDataSet.get(selected).getId(), mDataSet.get(selected).getParent());
            }
        }
        public ChildAdapter(){
            mDataSet = new ArrayList<Child>();
        }
        public void setDataSet(Child dataSet[]) {
            mDataSet.clear();
            mDataSet.addAll(Arrays.asList(dataSet));
            notifyDataSetChanged();
        }
        public int getSelected(){
            return selected;
        }
        public Child getChildFromDataSet(int position){
            return mDataSet.get(position);
        }
        @Override
        public ChildAdapter.ChildViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            TextView view = (TextView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.text_view, parent, false);
            ChildViewHolder viewHolder = new ChildViewHolder(view);
            return viewHolder;
        }
        @Override
        public void onBindViewHolder(ChildViewHolder viewHolder, int position){
            viewHolder.textView.setText(mDataSet.get(viewHolder.getAdapterPosition()).getName());
            viewHolder.textView.setBackgroundColor(selected == position ? Color.CYAN : Color.TRANSPARENT);
        }

        @Override
        public int getItemCount(){
            return mDataSet.size();
        }
    }
}
