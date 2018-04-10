package com.example.nurdan.lavaproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ApplicationLogic.ProgramSingletonController;

public class StoreActivity extends AppCompatActivity {
    private List<StoreItem> myItems = new ArrayList<>();
    private String purchaseItem;
    ProgramSingletonController currInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainMenuActivity.setPrefTheme(StoreActivity.this);
        setContentView(R.layout.activity_store);

        setupToolbar();
        populateStoreList();
        populateStoreListView();
    }
    private void setupToolbar(){
        Toolbar inboxToolBar = (Toolbar) findViewById(R.id.storeToolbar);
        setSupportActionBar(inboxToolBar);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.store_info_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.store_back_button:
                finish();
                break;
        }
        return true;
    }

    private void populateStoreList() {
        myItems.add(new StoreItem("Dark Blue Theme", 1, R.drawable.dummyscreenshot));
        myItems.add(new StoreItem("Icon 1", 1, R.drawable.markericon1));
        myItems.add(new StoreItem("Icon 2", 1, R.drawable.markericon2));
        myItems.add(new StoreItem("Icon 3", 1, R.drawable.markericon3));
        myItems.add(new StoreItem("Icon 4", 1, R.drawable.markericon4));
        myItems.add(new StoreItem("Icon 5", 1, R.drawable.markericon5));
        myItems.add(new StoreItem("Icon 6", 1, R.drawable.markericon6));
        myItems.add(new StoreItem("Icon 7", 1, R.drawable.markericon7));
    }

    private void populateStoreListView() {
        ArrayAdapter<StoreItem> adapter = new MyArrayAdapter();
        ListView list = findViewById(R.id.storeListView);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                StoreItem currItem = myItems.get(position);
                purchaseItem = currItem.getTitle();
                buyWithXPAsync buy = new buyWithXPAsync();
                buy.execute(currItem.getCost());
            }
        });
    }

    private class MyArrayAdapter extends ArrayAdapter<StoreItem> {
        public MyArrayAdapter() {
            super(StoreActivity.this, R.layout.store_item_view, myItems);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.store_item_view, parent, false);
            }

            StoreItem currItem = getItem(position);

            Log.d("STOREITEM", currItem.getTitle() + " " + currItem.getCost());

            ImageView imageView = (ImageView) convertView.findViewById(R.id.store_item_icon);
            imageView.setImageResource(currItem.getIconId());

            TextView makeTitle = (TextView) convertView.findViewById(R.id.store_item_title);
            makeTitle.setText(currItem.getTitle());

            TextView makeCost = (TextView) convertView.findViewById(R.id.store_item_price);
            makeCost.setText("Price: " + currItem.getCost());

            return convertView;
        }
    }

    private class StoreItem {
        private String title;
        private int cost;
        private int iconId;

        public StoreItem(String title, int cost, int iconId) {
            this.title = title;
            this.cost = cost;
            this.iconId = iconId;
        }
        public String getTitle() { return title; }
        public int getCost() { return cost; }
        public int getIconId() { return iconId; }
    }

    private class buyWithXPAsync extends AsyncTask<Integer,Void,Boolean> {
        String copyItem = "";
        @Override
        protected Boolean doInBackground(Integer... ints) {
            int cost = ints[0];
            copyItem = purchaseItem;
            currInstance = ProgramSingletonController.getCurrInstance();
            return currInstance.purchaseWithXP(copyItem, cost, getApplicationContext());
        }

        @Override
        protected void onPostExecute(Boolean purchased) {
            if(purchased) {
                if(copyItem.equals("Dark Blue Theme")) {
                    SharedPreferences prefs = getApplicationContext().getSharedPreferences("appPrefs", Context.MODE_PRIVATE);
                    prefs.edit()
                            .putString("currentTheme", copyItem)
                            .apply();
                }
                Toast.makeText(StoreActivity.this, "Purchased " + copyItem, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(StoreActivity.this, "Failed to get " + copyItem, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
