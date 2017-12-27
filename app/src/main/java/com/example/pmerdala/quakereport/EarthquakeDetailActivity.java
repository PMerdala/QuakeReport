package com.example.pmerdala.quakereport;

import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.net.URL;

public class EarthquakeDetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<EarthquakeData> {

    EarthquakeData earthquakeData;
    public static final int LOADER_ID = 1;
    public static final String Intent_Extra_url = "url";
    static final String Bundle_Url = "Url";
    static final String Save_Bundle_Data = "data";



    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = getIntent();
        String url = intent.getStringExtra(Intent_Extra_url);
        if (url != null) {
            Bundle bundle = new Bundle();
            bundle.putString(Bundle_Url, url);
            Loader<EarthquakeData> loader = getLoaderManager().initLoader(LOADER_ID, bundle, this);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_earthquake_detail);
    }

    public void changeToList(View view) {
        UtilIntent.executeIntent(this, EarthquakeActivity.class);
    }

    protected void refreshData() {
        if (earthquakeData != null) {
            TextView tv = findViewById(R.id.detail_mag_tv);
            tv.setText(QueryUtils.getFormatMagniture(earthquakeData.getMagnitude()));
            GradientDrawable magnitudeCircle = (GradientDrawable) tv.getBackground();
            int magnitudeColor = QueryUtils.getColorMagniture(earthquakeData.getMagnitude(), this);
            magnitudeCircle.setColor(magnitudeColor);
            tv = findViewById(R.id.detail_title_tv);
            tv.setText(earthquakeData.getTitle());
            tv = findViewById(R.id.detail_felt_tv);
            tv.setText(earthquakeData.getFelt() + " People felt it");
        }else{
            TextView tv = findViewById(R.id.detail_mag_tv);
            tv.setText("No");
            tv = findViewById(R.id.detail_title_tv);
            tv.setText("No data");
            tv = findViewById(R.id.detail_felt_tv);
            tv.setText("No data");
        }
    }

    @Override
    public Loader<EarthquakeData> onCreateLoader(int id, Bundle args) {
        String url = args.getString(Bundle_Url);
        return new EarthquakeDetailLoader(this, url);
    }

    @Override
    public void onLoadFinished(Loader<EarthquakeData> loader, EarthquakeData data) {
        earthquakeData = data;
        refreshData();
    }

    @Override
    public void onLoaderReset(Loader<EarthquakeData> loader) {
        earthquakeData  =null;
        refreshData();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Save_Bundle_Data,earthquakeData);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        earthquakeData = (EarthquakeData) savedInstanceState.getSerializable(Save_Bundle_Data);
        refreshData();
    }
}
