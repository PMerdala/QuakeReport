package com.example.pmerdala.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by merdala on 2017-11-02.
 */

public class EarthquakeListAdaper extends ArrayAdapter<EarthquakeData> {


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
        TextView magnitudeTextView = (TextView) view.findViewById(R.id.magnitude_text_view);
        TextView rangeTextView = (TextView) view.findViewById(R.id.range_text_view);
        TextView locationTextView = (TextView) view.findViewById(R.id.location_text_view);
        TextView dateTextView = (TextView) view.findViewById(R.id.date_text_view);
        TextView timeTextView = (TextView) view.findViewById(R.id.time_text_view);
        if (magnitudeTextView != null) {
            magnitudeTextView.setText(QueryUtils.getFormatMagniture(data.getMagnitude()));
            GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeTextView.getBackground();
            int magnitudeColor = QueryUtils.getColorMagniture(data.getMagnitude(),getContext());
            magnitudeCircle.setColor(magnitudeColor);
        }
        if (rangeTextView != null) {
            rangeTextView.setText(data.getRange());
        }
        if (locationTextView != null) {
            locationTextView.setText(data.getLocation());
        }
        if (dateTextView != null) {
            if (data.getMiliseconds()==0) {
                dateTextView.setText("Brak");
            }else{
                dateTextView.setText(QueryUtils.getFormatDate(data.getDatetime()));
            }
        }
        if (timeTextView != null) {
            if (data.getMiliseconds()==0) {
                timeTextView.setText("Brak");
            }else {
                timeTextView.setText(QueryUtils.getFormatTime(data.getDatetime()));
            }
        }

    }




}
