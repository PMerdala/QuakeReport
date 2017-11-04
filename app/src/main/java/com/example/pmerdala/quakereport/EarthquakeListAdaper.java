package com.example.pmerdala.quakereport;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
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
            magnitudeTextView.setText(getFormatMagniture(data.getMagnitude()));
            GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeTextView.getBackground();
            int magnitudeColor = getColorMagniture(data.getMagnitude());
            magnitudeCircle.setColor(magnitudeColor);
        }
        if (rangeTextView != null) {
            rangeTextView.setText(data.getRange());
        }
        if (locationTextView != null) {
            locationTextView.setText(data.getLocation());
        }
        if (dateTextView != null) {
            dateTextView.setText(getFormatDate(data.getDatetime()));
        }
        if (timeTextView != null) {
            timeTextView.setText(getFormatTime(data.getDatetime()));
        }

    }

    private String getFormatMagniture(float magniture){
        NumberFormat numberFormat = new DecimalFormat("#0.0");
        return numberFormat.format(magniture);
    }

    private String getFormatDate(Date date){
        DateFormat dateFormater = new SimpleDateFormat("MMM dd, yyyy");
        return dateFormater.format(date);
    }

    private String getFormatTime(Date date){
        DateFormat timeFormater = new SimpleDateFormat("h:mm a");
        return timeFormater.format(date);
    }

    private int getColorMagniture(float magniture){
        int circleColor = R.color.magnitude1;
        switch(Float.valueOf(magniture).intValue()){
            case 10:circleColor = R.color.magnitude10plus;
                break;
            case 9:circleColor = R.color.magnitude9;
                break;
            case 8:circleColor = R.color.magnitude8;
                break;
            case 7:circleColor = R.color.magnitude7;
                break;
            case 6:circleColor = R.color.magnitude6;
                break;
            case 5:circleColor = R.color.magnitude5;
                break;
            case 4:circleColor = R.color.magnitude4;
                break;
            case 3:circleColor = R.color.magnitude3;
                break;
            case 2:circleColor = R.color.magnitude2;
                break;
            case 1:
            default: circleColor = R.color.magnitude1;
                break;
        }
        return ContextCompat.getColor(getContext(), circleColor);
    }
}
