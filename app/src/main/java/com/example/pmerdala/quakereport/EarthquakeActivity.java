package com.example.pmerdala.quakereport;

import android.app.Dialog;
import android.app.LoaderManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class EarthquakeActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<EarthquakeData>> {

    final static String LOG_TAG = EarthquakeActivity.class.getSimpleName();
    private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&starttime=2014-01-01&endtime=2016-01-01&minmagnitude=5";
    //private static final String USGS_REQUEST_URL = "https://earthquake.usgs.gov/fdsnws/event/1/query?format=geojson&eventtype=earthquake&orderby=time&minmag=6&limit=10";
    public static final int PLEASE_WAIT_DIALOG = 1;
    public static final int LOADER_ID = 0;
    static final String Bundle_Url = "Url";
//    ListView listView = null;
    private EarthquakeListAdaper listAdapter;
    private TextView emptyTextView;

    private void info(String msg){
        Log.i(LOG_TAG,"TEST: " + msg);
    }


    private void prepareListView(){
        listAdapter = new EarthquakeListAdaper(this,new ArrayList<EarthquakeData>());
        ListView listView = findViewById(R.id.quake_list_view_id);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                EarthquakeData data = (EarthquakeData) parent.getAdapter().getItem(position);
                openDetail(data.getUrlDetail());
            }
        });
        listView.setAdapter(listAdapter);
        emptyTextView = findViewById(R.id.empty_list_item);
        emptyTextView.setText("Loading ...");
        listView.setEmptyView(emptyTextView);
    }

    //https://developer.android.com/training/monitoring-device-state/connectivity-monitoring.html?utm_source=udacity&utm_medium=course&utm_campaign=android_basics#DetermineType
    private boolean isNetworkConnection(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo!=null && networkInfo.isConnectedOrConnecting())
            return true;
        View progressBar = findViewById(R.id.loading_progress);
        progressBar.setVisibility(View.GONE);
        emptyTextView.setText("No internet connection");
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.earthquake_activity);
        //ArrayList<EarthquakeData> earthquakes = getData();
        prepareListView();
        if (isNetworkConnection()) {
            Bundle bundle = new Bundle();
            bundle.putString(Bundle_Url, USGS_REQUEST_URL);
            info("before initLoader");
            Loader<List<EarthquakeData>> loader = getLoaderManager().initLoader(LOADER_ID, bundle, this);
            info("after initLoader");
        }
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

    private void openDetail(String url) {
        Intent intent = new Intent(this, EarthquakeDetailActivity.class);
        intent.putExtra("url", url);
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

    @Override
    public Loader<List<EarthquakeData>> onCreateLoader(int id, Bundle args) {
        info("start onCreateLoader");
        String url = args.getString(Bundle_Url);
        if (url == null) {
            url = USGS_REQUEST_URL;
        }
        emptyTextView.setText("Loading ...");
        EarthquakeListLoader loader = new EarthquakeListLoader(this, url);
        info("end onCreateLoader");
        return loader;
    }

    @Override
    public void onLoadFinished(Loader<List<EarthquakeData>> loader, List<EarthquakeData> data) {
        info("start onLoadFinished");
        ProgressBar progressBar = findViewById(R.id.loading_progress);
        progressBar.setVisibility(View.GONE);
        listAdapter.clear();
        if (data!=null && !data.isEmpty()) {
            listAdapter.addAll(data);
        }else {
            emptyTextView.setText("No earthquakes found.");
        }

        info("end onLoadFinished");
    }

    @Override
    public void onLoaderReset(Loader<List<EarthquakeData>> loader) {
        info("start onLoaderReset");
        listAdapter.clear();
        info("stop onLoaderReset");
    }


}
