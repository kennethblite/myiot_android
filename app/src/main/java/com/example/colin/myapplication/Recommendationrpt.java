package com.example.colin.myapplication;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Recommendationrpt.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Recommendationrpt#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Recommendationrpt extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String GOOD = "good";
    private static final String BAD = "bad";
    private static final String RECOMMENDATIONS = "recommendations";

    // TODO: Rename and change types of parameters
    private String mgood;
    private String mbad;
    private String mrecommendations;

    private ImageButton BackButton;
    //private ImageButton DeleteButton;

    private TextView mTextgood;
    private TextView mTextbad;
    private TextView mTextrecommendations;

    private OnRecommendReport mListener;

    public Recommendationrpt() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Recommendationrpt.
     */


    public static Recommendationrpt newInstance(String good, String bad, String recommendations) {
        Recommendationrpt fragment = new Recommendationrpt();
        Bundle args = new Bundle();
        args.putString(GOOD, good);
        args.putString(BAD, bad);
        args.putString(RECOMMENDATIONS, recommendations);
        fragment.setArguments(args);
        return fragment;
    }

    public static Recommendationrpt newInstance(Recommendation r){
        Recommendationrpt fragment = new Recommendationrpt();
        Bundle args = new Bundle();
        args.putString(RECOMMENDATIONS, r.getRecommendations());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mrecommendations = getArguments().getString(RECOMMENDATIONS);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_recommendationrpt, container, false);
        BackButton = (ImageButton) view.findViewById(R.id.imageButton);
        //DeleteButton = (ImageButton) view.findViewById(R.id.imageButton2);
        BackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity().getApplicationContext(),"BackButtonClicked", Toast.LENGTH_SHORT).show();
                onArrowPushed();
            }
        });

        /*DeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                Toast.makeText(getActivity().getApplicationContext(), "Delete pushed", Toast.LENGTH_SHORT).show();
                onDeletePushed();
            }
        });*/
        mTextrecommendations = (TextView) view.findViewById(R.id.list);
        mTextrecommendations.setText(mrecommendations);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onArrowPushed() {
        if (mListener != null) {
            mListener.onArrowPushed();
        }
    }

    public void onDeletePushed(){
        if (mListener != null){
            mListener.onDeletePushed();
        }

    }

    @Override
    public void onAttach(Context context) {
        Log.d("Hello", "attached");
        super.onAttach(context);
        if (context instanceof OnRecommendReport) {
            mListener = (OnRecommendReport) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
    public interface OnRecommendReport {
        void onDeletePushed();
        void onArrowPushed();
    }
}
