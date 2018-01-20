package com.example.mostafa.graduationproject;


import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.current_user), Context.MODE_PRIVATE);

        AndroidNetworking.get("https://infinitysurvey.herokuapp.com/api/survey")
                //.addHeaders("x-auth", sharedPref.getString(Utils.TOKEN_PROPERTY_KEY,""))
                .addHeaders("x-auth", "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI1YTMzZmQ2YTAxZGMzNTA5MjA2MjQ4MGQiLCJhY2Nlc3MiOiJhdXRoIiwiaWF0IjoxNTE2NDgxNzM1Nzg4fQ.XmAG-pthJGKuI0iAXZw7GbUJrG4PSWPNYk7G4M5-IVQ")
                .build()
                .getAsObjectList(GetSurveyByIdDTO.class, new ParsedRequestListener<List<GetSurveyByIdDTO>>() {
                    @Override
                    public void onResponse(List<GetSurveyByIdDTO> response) {
//                        for (GetSurveyByIdDTO surveyByIdDTO : response) {
//                            Log.d("response", response.ge)
//                        }

                    }

                    @Override
                    public void onError(ANError anError) {

                    }
                });
    }
}
