package com.example.pmerdala.quakereport;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by merdala on 2017-12-27.
 */

public class EarthquakeListLoader extends AsyncTaskLoader<List<EarthquakeData>> {
    public EarthquakeListLoader(Context context) {
        super(context);
    }

    @Override
    public List<EarthquakeData> loadInBackground() {
        return null;
    }
}
