package com.example.pmerdala.quakereport;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by merdala on 2017-12-15.
 */

public class UtilIntent {
    public static void executeIntent(AppCompatActivity currentActivity, final Class<? extends AppCompatActivity> clazz){
        Intent intent = new Intent(currentActivity,clazz);
        currentActivity.startActivity(intent);
    }

}
