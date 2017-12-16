package com.example.pmerdala.quakereport;

import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.net.URL;

public class EarthquakeDetailActivity extends AppCompatActivity {

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        if (url!=null) {
            EarthquakeItemNetwork task = new EarthquakeItemNetwork();
            task.execute(url);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_detail);
    }

    public void changeToList(View view) {
        UtilIntent.executeIntent(this,EarthquakeActivity.class);
    }

    protected void refreshData(EarthquakeData earthquakeData){
        if (earthquakeData!=null) {
            TextView tv = findViewById(R.id.detail_mag_tv);
            tv.setText(QueryUtils.getFormatMagniture(earthquakeData.getMagnitude()));
            GradientDrawable magnitudeCircle = (GradientDrawable) tv.getBackground();
            int magnitudeColor = QueryUtils.getColorMagniture(earthquakeData.getMagnitude(),this);
            magnitudeCircle.setColor(magnitudeColor);
            tv = findViewById(R.id.detail_title_tv);
            tv.setText(earthquakeData.getTitle());
            tv = findViewById(R.id.detail_felt_tv);
            tv.setText(earthquakeData.getFelt() + " People felt it");
        }
    }

    private class EarthquakeItemNetwork extends AsyncTask<String,Void,EarthquakeData>{

        @Override
        protected EarthquakeData doInBackground(String... urls) {
            if (urls == null || urls.length == 0) {
                return null;
            }
            String urlString = urls[0];
            URL url = QueryUtils.createURL(urlString);
            if (url==null){
                return null;
            }
            String jsonResponse = QueryUtils.makeHttpRequests(url);
            EarthquakeData earthquake = QueryUtils.extractEarthquake(jsonResponse,urlString);
            return earthquake;
        }

        @Override
        protected void onPostExecute(EarthquakeData earthquakeData) {
            refreshData(earthquakeData);
        }
    }

}
