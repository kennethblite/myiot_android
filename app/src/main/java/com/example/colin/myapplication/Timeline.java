package com.example.colin.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Timeline.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Timeline#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Timeline extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    Timer timer;
    private List<Recommendation> reclist = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private TimelineAdapter mAdapter;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnTimelineListener mListener;

    public Timeline() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Timeline.
     */
    // TODO: Rename and change types and number of parameters
    public static Timeline newInstance() {
        Timeline fragment = new Timeline();
        //Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //if (getArguments() != null) {
        //    mParam1 = getArguments().getString(ARG_PARAM1);
        //    mParam2 = getArguments().getString(ARG_PARAM2);
        //}

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_timeline, container, false);
        mAdapter = new TimelineAdapter(reclist);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(view.getContext(), mRecyclerView, new ClickListener(){
            @Override
            public void onClick(View v, int position){
                Recommendation rec = reclist.get(position);
                Toast.makeText(v.getContext(), rec.getDate(), Toast.LENGTH_SHORT).show();
                onRecommendation(rec);
            }

            @Override
            public void onLongClick(View v, int position){

            }
        }));
        timer = new Timer();
        timer.scheduleAtFixedRate(new UpdateTimer(new UpdateListener() {
            @Override
            public void Update() {
                    prepareRecommendations();
            }
        }), 0L, 10000L);
        return view;
    }


    public void prepareRecommendations(){
        Log.i("recommendations", "fetching recommendations");
        reclist.clear();
        WebApi api = WebApi.getInstance(getActivity());
        //return;
        if (User.cycleIds == null || User.cycleIds.size() == 0){
            Recommendation rec = new Recommendation("Recommendations are here", "Beginning of Time");
            reclist.add(rec);
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mAdapter.notifyDataSetChanged();
//stuff that updates ui
                }
            });
            return;
        }
        for (final String cycle : User.cycleIds){
            api.CheckDryerCycle( cycle, new ResponseListener() {
                @Override
                public void Respond(String s) {
                    try {
                        JSONObject json = new JSONObject(s);
                        if (json.getBoolean("done")){
                            Recommendation rec = new Recommendation(json.getString("recommendations"), json.getString("end_time"));
                            reclist.add(rec);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    mAdapter.notifyDataSetChanged();
//stuff that updates ui
                                }
                            });
                        }else{
                            User.CurrentlyTracking = cycle;
                        }
                    }catch(Exception e){
                        Log.i("recommendations", "recommendations completed");
                    }
                }
            });
        }
    }

    public interface ClickListener {
        void onClick(View view, int position);

        void onLongClick(View view, int position);
    }


    public static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildLayoutPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onRecommendation(Recommendation d) {
        if (mListener != null) {
            mListener.onRecommendation(d);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnTimelineListener) {
            mListener = (OnTimelineListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        timer.cancel();
        mListener = null;
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
    public interface OnTimelineListener{

        public void onRecommendation(Recommendation r);
        // TODO: Update argument type and name
    }
}
