package com.example.pmerdala.quakereport;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        ArrayList<EarthquakeData> earthquakes  = getData();
        ListView listView = findViewById(R.id.quake_list_view_id);
        listView.setAdapter(new EarthquakeListAdaper(this,earthquakes ));
    }

    protected ArrayList<EarthquakeData> getData(){

        return QueryUtils.extractEarthquakes();
    }
}
