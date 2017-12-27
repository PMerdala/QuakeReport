package com.example.pmerdala.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by merdala on 2017-12-27.
 */

public class EarthquakeListLoader extends AsyncTaskLoader<List<EarthquakeData>> {

    final static String LOG_TAG = EarthquakeListLoader.class.getSimpleName();
    final String urlString;

    private void info(String msg){
        Log.i(LOG_TAG,"TEST: " + msg);
    }

    @Override
    protected void onStartLoading() {
        info("start onStartLoading");

        super.onStartLoading();
        forceLoad();
        info("end onStartLoading");
    }

    public EarthquakeListLoader(Context context, String urlString) {
        super(context);
        info("start constructor EarthquakeListLoader");
        this.urlString = urlString;
        info("stop constructor EarthquakeListLoader");
    }

    @Override
    public List<EarthquakeData> loadInBackground() {
        info("start loadInBackground");
        List<EarthquakeData> data = QueryUtils.fetchListEarthquakeData(urlString);
        info("stop loadInBackground");
        return data;
    }
}
