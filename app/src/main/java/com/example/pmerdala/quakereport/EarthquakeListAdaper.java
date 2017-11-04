package com.example.pmerdala.quakereport;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by merdala on 2017-11-02.
 */

public class EarthquakeListAdaper extends ArrayAdapter<EarthquakeData> {
    final static DateFormat dateFormater = new SimpleDateFormat("MMM dd, yyyy");
    final static DateFormat timeFormater = new SimpleDateFormat("h:mm a");


    public EarthquakeListAdaper(@NonNull Context context, List<EarthquakeData> data) {
        super(context, 0, data);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.earthquake_list_item, parent, false);
        }
        EarthquakeData data = getItem(position);
        makeItemView(listItemView, data);
        return listItemView;
    }

    protected void makeItemView(View view, EarthquakeData data) {
        TextView measureTextView = (TextView) view.findViewById(R.id.magnitude_text_view);
        TextView rangeTextView = (TextView) view.findViewById(R.id.range_text_view);
        TextView locationTextView = (TextView) view.findViewById(R.id.location_text_view);
        TextView dateTextView = (TextView) view.findViewById(R.id.date_text_view);
        TextView timeTextView = (TextView) view.findViewById(R.id.time_text_view);
        if (measureTextView != null) {
            measureTextView.setText("" + data.getMagnitude());
        }
        if (rangeTextView != null) {
            rangeTextView.setText(data.getRange());
        }
        if (locationTextView != null) {
            locationTextView.setText(data.getLocation());
        }
        if (dateTextView != null) {
            dateTextView.setText(dateFormater.format(data.getDatetime()));
        }
        if (timeTextView != null) {
            timeTextView.setText(timeFormater.format(data.getDatetime()));
        }

    }
}
