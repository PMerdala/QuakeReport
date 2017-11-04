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
                openBrowsel(data.getUrl());
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

    private void openBrowsel(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Nie można otworzyć strony", Toast.LENGTH_SHORT).show();
        }
    }

    private class QuakeReportAsyncTask extends AsyncTask<String, Void, ArrayList<EarthquakeData>> {

        final static String HTTP_REQUEST_METHOD = "GET";
        final Activity activity;

        private QuakeReportAsyncTask(Activity activity) {
            this.activity = activity;
        }

        @Override
        protected ArrayList<EarthquakeData> doInBackground(String... urls) {
            if (urls == null || urls.length == 0) {
                return new ArrayList<>();
            }
            URL url = createURL(urls[0]);
            if (url==null){
                return new ArrayList<>();
            }
            String jsonResponse = makeHttpRequests(url);
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


        private URL createURL(String requestUrl){
            URL url = null;
            try {
                url = new URL(requestUrl);
            } catch (MalformedURLException e) {
                Log.e(QuakeReportAsyncTask.class.getSimpleName(), e.getLocalizedMessage(), e);
            }
            return url;
        }

        private String makeHttpRequests(URL url) {
            HttpURLConnection connection = null;
            InputStream inputStream = null;
            String jsonResponse = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod(HTTP_REQUEST_METHOD);
                connection.setConnectTimeout(15000);
                connection.setReadTimeout(10000);
                connection.connect();
                if (connection.getResponseCode()==200) {
                    inputStream = connection.getInputStream();
                    jsonResponse = readFromStream(inputStream);
                }else{
                    Log.e(QuakeReportAsyncTask.class.getSimpleName(), "niewłaściwy status odpowiedzi=" + connection.getResponseCode() + " " + connection.getResponseMessage());
                }
            } catch (IOException e) {
                Log.e(QuakeReportAsyncTask.class.getSimpleName(), e.getLocalizedMessage(), e);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        Log.e(QuakeReportAsyncTask.class.getSimpleName(), e.getLocalizedMessage(), e);
                    }
                }
                if (connection != null) {
                    connection.disconnect();
                }
            }
            return jsonResponse;
        }

        private String readFromStream(InputStream inputStream) throws IOException {
            if (inputStream == null) {
                return null;
            }
            StringBuilder json = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(reader);
            String line = bufferedReader.readLine();
            while (line != null){
                json.append(line);
                line = bufferedReader.readLine();
            }
            return json.toString();
        }
    }
}
