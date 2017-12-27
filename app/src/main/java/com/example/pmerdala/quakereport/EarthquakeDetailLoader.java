package com.example.pmerdala.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

/**
 * Created by merdala on 2017-12-27.
 */

public class EarthquakeDetailLoader extends AsyncTaskLoader<EarthquakeData> {
    final String urlString;
    public EarthquakeDetailLoader(Context context,String urlString) {
        super(context);
        this.urlString = urlString;
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }

    @Override
    public EarthquakeData loadInBackground() {
        return QueryUtils.fetchEarthquakeData(urlString);
    }
}
