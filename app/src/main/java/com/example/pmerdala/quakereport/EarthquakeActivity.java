package com.example.pmerdala.quakereport;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.net.URL;
import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        ArrayList<EarthquakeData> earthquakes  = getData();
        ListView listView = findViewById(R.id.quake_list_view_id);
        listView.setAdapter(new EarthquakeListAdaper(this,earthquakes ));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EarthquakeData data =(EarthquakeData) parent.getAdapter().getItem(position);
                openBrowsel(data.getUrl());
            }
        });
    }

    protected ArrayList<EarthquakeData> getData(){
        return QueryUtils.extractEarthquakes();
    }

    private void openBrowsel(String url){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        if (intent.resolveActivity(getPackageManager())!=null){
            startActivity(intent);
        }else{
            Toast.makeText(this,"Nie można otworzyć strony",Toast.LENGTH_SHORT).show();
        }
    }
}
