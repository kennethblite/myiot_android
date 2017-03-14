package com.example.colin.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Chronometer;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Tracking.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Tracking#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Tracking extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private LineChart graph;
    List<Entry> humidity = new ArrayList<Entry>();
    List<Entry> temperature = new ArrayList<Entry>();

    // TODO: Rename and change types of parameters
    Timer timer = new Timer();

    private Chronometer mchrono;

    private OnTrackingListener mListener;

    public Tracking() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Tracking.
     */
    // TODO: Rename and change types and number of parameters
    public static Tracking newInstance() {
        Tracking fragment = new Tracking();
        //Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_tracking, container, false);
        graph = (LineChart) view.findViewById(R.id.chart);
        mchrono = (Chronometer) view.findViewById(R.id.chronometer);
        mchrono.setBase(SystemClock.elapsedRealtime());
        mchrono.start();
        final WebApi web = WebApi.getInstance(getActivity());
        UpdateTimer update = new UpdateTimer(new UpdateListener() {
            @Override
            public void Update() {
                if (User.email == null){
                    timer.cancel();
                }
                web.GetDataPoints(User.CurrentlyTracking, new ResponseListener() {
                    @Override
                    public void Respond(String s)  {
                        JSONArray jsonArray = null;
                        ArrayList<DataPoints> points = new ArrayList<DataPoints>();
                        try {
                            jsonArray = new JSONArray(s.toString());
                        }catch(Exception e){
                            Log.i("Fuck", "off");
                        }
                        if(jsonArray.length() == 0){
                            return;
                        }

                        for(int i=0; i<jsonArray.length(); i++) {
                            try {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                points.add(new DataPoints(jsonObject));
                            }catch(Exception e){
                                Log.i("FUCK", "OFF");
                            }
                        }
                        humidity = new ArrayList<Entry>();
                        temperature = new ArrayList<Entry>();

                        for(DataPoints d : points){
                            humidity.add(d.getHumidity());
                            temperature.add(d.getTemperature());
                        }
                        LineDataSet humidSet = new LineDataSet(humidity, "Humidity"); // add entries to dataset
                        LineDataSet tempSet = new LineDataSet(temperature, "Temperature");
                        humidSet.setColor(16711680, 255);
                        humidSet.setValueTextColor(16711680); // styling, ...
                        humidSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                        tempSet.setColor(16711680, 255);
                        tempSet.setValueTextColor(16711680); // styling, ...
                        tempSet.setAxisDependency(YAxis.AxisDependency.LEFT);
                        LineData tempdata = new LineData(tempSet);
                        graph.setData(tempdata);
                        LineData humiddata = new LineData(humidSet);
                        graph.setData(humiddata);
                        //graph.setVisibleXRangeMaximum(2000);
                        graph.fitScreen();
                        graph.invalidate();

                    }
        });

            }
        });
        timer.scheduleAtFixedRate(update,1000L, 10000L);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTrackingListener) {
            mListener = (OnTrackingListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
        mchrono.stop();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnTrackingListener {
        // TODO: Update argument type and name
    }
}
