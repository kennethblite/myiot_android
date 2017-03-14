package com.example.colin.myapplication;

import android.app.DownloadManager;
import android.content.Context;
import android.os.AsyncTask;
import android.renderscript.ScriptGroup;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.io.BufferedInputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import com.android.volley.*;
import com.android.volley.toolbox.*;

/**
 * Created by Colin on 3/9/2017.
 */
public class WebApi {
    static public final String tag = "wepapi";
    private Context c;
    private static WebApi webapi;
    private WebApi(Context c){
        this.c = c;
    }
    public static WebApi getInstance(Context c){
        if (webapi == null){
            webapi = new WebApi(c);
        }
        return webapi;
    }

    public static Boolean CheckRegistration(String Username, String password){
        return false;
    }

    public void GetUserInfo(final ResponseListener r,String... headers){
        RequestQueue queue = Volley.newRequestQueue(c);
        String url ="http://ec2-52-23-227-218.compute-1.amazonaws.com:8000/user/?email="+headers[0]+"&password="+headers[1];

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        r.Respond(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(c,new String(error.networkResponse.data), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    public Boolean Register(String... data) {
        final String[] params = data;
        RequestQueue queue = Volley.newRequestQueue(c);
        String url ="http://ec2-52-23-227-218.compute-1.amazonaws.com:8000/user/register/";
         //url = "http://httpbin.org/post";
        Log.i(tag, url);
        StringRequest jsonrequest = new StringRequest(Request.Method.POST,url,
                new Response.Listener<String>(){
                   @Override
                    public void onResponse(String response){
                       Log.i(tag, "Response is: " + response);
                       Toast.makeText(c, "Account Created Succesfully, you may now login", Toast.LENGTH_SHORT).show();
                   }
                }, new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error){
                        Toast.makeText(c,new String(error.networkResponse.data), Toast.LENGTH_SHORT).show();
                    }
        }){
            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONObject j = new JSONObject();
                try {
                    j.put("name", params[0]);
                    j.put("email", params[1]);
                    j.put("password", params[2]);
                }catch (Exception e){
                    Log.i(tag, "FUCK OFF");
                }
                byte[] param = j.toString().getBytes();
                Log.i(tag,j.toString());
                return param;
            }
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> param = new HashMap<String, String>();
                param.put("Content-Type","application/x-www-form-urlencoded");
                return param;
            }

            @Override
            public String getBodyContentType(){
                return "application/json";
            }
        };
// Add the request to the RequestQueue.
        jsonrequest.setShouldCache(false);
        queue.add(jsonrequest);
        return false;
    }

    public Boolean CheckDryerCycle(String dryertag, final ResponseListener r){
        RequestQueue queue = Volley.newRequestQueue(c);
        String url = "http://ec2-52-23-227-218.compute-1.amazonaws.com:8000/cycles/?access_data_key=%242b%2412%24l24KcoFv0wVZvmuAoraEcOFJ4EWDRDv6hq.6f8qSGypj2wuyuIMR.&tag="+dryertag;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(tag,"Response is: "+ response);
                        r.Respond(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(c,new String(error.networkResponse.data), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
        return false;
    }

    public Boolean GetDryerCycle(){
        RequestQueue queue = Volley.newRequestQueue(c);
        String url = "http://ec2-52-23-227-218.compute-1.amazonaws.com:8000/cycles/?access_data_key=%242b%2412%24l24KcoFv0wVZvmuAoraEcOFJ4EWDRDv6hq.6f8qSGypj2wuyuIMR.";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(tag,"Response is: "+ response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(c,new String(error.networkResponse.data), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
        return false;
    }

    public void GetDataPoints(String headers, final ResponseListener r){
        RequestQueue queue = Volley.newRequestQueue(c);
        String url = "http://ec2-52-23-227-218.compute-1.amazonaws.com:8000/data/?access_data_key=%242b%2412%24l24KcoFv0wVZvmuAoraEcOFJ4EWDRDv6hq.6f8qSGypj2wuyuIMR.&tag="+headers;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(tag,"Response is: "+ response);
                        r.Respond(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("tracking", "nothing to track");
            }
        });
        queue.add(stringRequest);
        return;
    }
}
