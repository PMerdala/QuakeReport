package com.example.pmerdala.quakereport;


import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by merdala on 2017-11-02.
 */

public class EarthquakeData {

    final float magnitude;
    final String place;
    final long miliseconds;
    final static DateFormat inputDateFormater = new SimpleDateFormat("MMM dd, yyyy h:mm a", Locale.ENGLISH);

    public EarthquakeData(float magnitude, String place, long miliseconds) {
        this.magnitude = magnitude;
        this.place = place;
        this.miliseconds = miliseconds;
    }

    public EarthquakeData(float magnitude, String place, Date datetime) {
        this.magnitude = magnitude;
        this.place = place;
        if (datetime != null) {
            this.miliseconds = datetime.getTime();
        } else {
            this.miliseconds = new Date().getTime();
        }
    }

    public EarthquakeData(float magnitude, String place, String datetime) {
        this.magnitude = magnitude;
        this.place = place;
        Date tempDatetime;
        try {
            if (datetime != null) {
                tempDatetime = inputDateFormater.parse(datetime);
            } else {
                tempDatetime = null;
            }
        } catch (ParseException e) {
            e.printStackTrace();
            Log.v(this.getClass().getName(), inputDateFormater.format(new Date()));
            tempDatetime = null;
        }
        if (tempDatetime != null) {
            this.miliseconds = tempDatetime.getTime();
        } else {
            this.miliseconds = new Date().getTime();
        }
    }

    public float getMagnitude() {
        return magnitude;
    }

    public String getPlace() {
        return place;
    }


    public String getRange() {
        if (place != null) {
            int pos = place.indexOf(" of ");
            if (pos >= 0) {
                return place.substring(0, pos + 3);
            }
        }
        return null;
    }

    public String getLocation() {
        if (place != null) {
            int pos = place.indexOf(" of ");
            if (pos >= 0) {
                return place.substring(pos + 4);
            } else {
                return place;
            }
        }
        return null;
    }

    public Date getDatetime() {
        return new Date(miliseconds);
    }

    public long getMiliseconds(){
        return miliseconds;
    }
}
