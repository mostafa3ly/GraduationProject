package com.example.mostafa.graduationproject;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.example.mostafa.graduationproject.api.response.GetSurveyByIdDTO;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView surveysRecyclerView;
    private SurveysAdapter surveysAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        surveysRecyclerView = view.findViewById(R.id.surveys_list);

        surveysRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        surveysRecyclerView.setAdapter(surveysAdapter);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        surveysAdapter = new SurveysAdapter();
    }


    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.current_user), Context.MODE_PRIVATE);
        String token = sharedPref.getString(Utils.TOKEN_PROPERTY_KEY,"");
        Log.d("token", token);

        AndroidNetworking.get("https://infinitysurvey.herokuapp.com/api/survey")
                .addHeaders("x-auth", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1YTMzZmQ2YTAxZGMzNTA5MjA2MjQ4MGQiLCJhY2Nlc3MiOiJhdXRoIiwiaWF0IjoxNTE2NDkxMzg0NDYwfQ.pZ0nHwBuyDiTewxhVW03YSp4YcwLAnEs8wqVjGTD25s")
                .build()
                .getAsObjectList(GetSurveyByIdDTO.class, new ParsedRequestListener<List<GetSurveyByIdDTO>>() {
                    @Override
                    public void onResponse(List<GetSurveyByIdDTO> response) {
                        for (GetSurveyByIdDTO surveyByIdDTO : response) {
                            Log.d("title", surveyByIdDTO.title);
                        }

                        surveysAdapter.addList(response);

                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(anError.getErrorCode()+"", anError.getErrorBody(), anError);
                    }
                });
    }
}
