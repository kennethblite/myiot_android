package com.example.colin.myapplication;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Colin on 3/13/2017.
 */
public class User {
    public static String SecurityCode;
    public static ArrayList<String> cycleIds;
    public static int userid;
    public static String email;
    public static String password;
    public static String CurrentlyTracking;

    public User(JSONObject j) throws JSONException{
        try {
            userid = j.getInt("id");
            SecurityCode = j.getString("access_data_key");
            cycleIds = new ArrayList<String>();
            email = j.getString("email");
            password = j.getString("password");
            JSONArray jArray = new JSONArray(j.getString("cycles"));
            if (jArray != null) {
                for (int i = 0; i < jArray.length(); i++) {
                    cycleIds.add(jArray.getString(i));
                }
            }
            Log.i("User", "User Created");
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void Clear(){
        SecurityCode  = null;
        cycleIds = null;
        email = null;
        password = null;
    }

    public boolean getBoolean(String s, Boolean b){
        return false;
    }


}
