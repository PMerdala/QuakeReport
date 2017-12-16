package com.example.pmerdala.quakereport;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class EarthquakeActivity extends AppCompatActivity {

    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2014-12-01&minmagnitude=5";
    public static final int PLEASE_WAIT_DIALOG = 1;
    ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        //ArrayList<EarthquakeData> earthquakes = getData();
        listView = findViewById(R.id.quake_list_view_id);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EarthquakeData data = (EarthquakeData) parent.getAdapter().getItem(position);
                openDetail(data.getUrlDetail());
            }
        });
        QuakeReportAsyncTask task = new QuakeReportAsyncTask(this);
        task.execute(USGS_REQUEST_URL);
    }

    @Override
    public Dialog onCreateDialog(int dialogId) {

        switch (dialogId) {
            case PLEASE_WAIT_DIALOG:
                ProgressDialog dialog = new ProgressDialog(this);
                dialog.setTitle("Pobieranie danych");
                dialog.setMessage("Proszę czekać....");
                dialog.setCancelable(true);
                return dialog;

            default:
                break;
        }

        return null;
    }

//    protected ArrayList<EarthquakeData> getData() {
//        return QueryUtils.extractEarthquakes();
//    }

    private void openDetail(String url){
        Intent intent = new Intent(this,EarthquakeDetailActivity.class);
        intent.putExtra("url",url);
        startActivity(intent);
    }

    private void openBrowsel(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Nie można otworzyć strony", Toast.LENGTH_SHORT).show();
        }
    }

    public void changeToDetail(View view) {
        UtilIntent.executeIntent(this,EarthquakeDetailActivity.class);
    }

    private class QuakeReportAsyncTask extends AsyncTask<String, Void, ArrayList<EarthquakeData>> {

        final Activity activity;

        private QuakeReportAsyncTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected ArrayList<EarthquakeData> doInBackground(String... urls) {
            if (urls == null || urls.length == 0) {
                return new ArrayList<>();
            }
            URL url = QueryUtils.createURL(urls[0]);
            if (url==null){
                return new ArrayList<>();
            }
            String jsonResponse =QueryUtils.makeHttpRequests(url);
            ArrayList<EarthquakeData> earthquakes = QueryUtils.extractEarthquakes(jsonResponse);
            return earthquakes;
        }

        @Override
        protected void onPreExecute() {
            activity.showDialog(PLEASE_WAIT_DIALOG);
        }

        @Override
        protected void onPostExecute(ArrayList<EarthquakeData> earthquakes) {
            listView.setAdapter(new EarthquakeListAdaper(activity, earthquakes));
            activity.removeDialog(PLEASE_WAIT_DIALOG);
        }




    }
}
