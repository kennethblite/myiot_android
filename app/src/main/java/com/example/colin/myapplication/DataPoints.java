package com.example.colin.myapplication;

import android.util.Log;

import com.github.mikephil.charting.data.Entry;

import org.json.JSONObject;

/**
 * Created by Colin on 3/12/2017.
 */
public class DataPoints {
    int id;
    String time;
    int user;
    String tag;
    float temperature;
    float humidity;

    public DataPoints(JSONObject json){
        try {
            id = json.getInt("id");
            time = json.getString("time");
            tag = json.getString("tag");
            temperature = Float.parseFloat(json.getString("temperature"));
            humidity = Float.parseFloat(json.getString("humidity"));
        } catch (Exception e){
            Log.i(tag, "JavaScript error");
        }
    }

    public Entry getTemperature(){
        return new Entry(Float.parseFloat(time), temperature);
    }

    public Entry getHumidity(){
        return new Entry(Float.parseFloat(time), humidity);
    }
}
