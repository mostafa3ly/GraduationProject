package com.example.mostafa.graduationproject;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mostafa.graduationproject.api.response.GetSurveyByIdDTO;

import java.util.List;

/**
 * Created by mosta on 20/1/2018.
 */

public class SurveysAdapter extends RecyclerView.Adapter<SurveysAdapter.SurveyViewHolder> {

    private List<GetSurveyByIdDTO> surveysList;


    SurveysAdapter(List<GetSurveyByIdDTO> surveysList){
        surveysList.clear();
        this.surveysList.addAll(surveysList);
    }

    @Override
    public SurveysAdapter.SurveyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.surveys_list_item, parent, false);

        return new SurveyViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(SurveysAdapter.SurveyViewHolder holder, int position) {
        GetSurveyByIdDTO survey = surveysList.get(position);

        holder.surveyTitleTextView.setText(survey.getTitle());
        holder.questionsCountTextView.setText(survey.getQuestions().size() + "questions");
    }

    @Override
    public int getItemCount() {
        return surveysList.size();
    }

    public class SurveyViewHolder extends RecyclerView.ViewHolder {

         TextView surveyTitleTextView;
         TextView questionsCountTextView;
        public SurveyViewHolder(View itemView) {
            super(itemView);

            surveyTitleTextView = itemView.findViewById(R.id.survey_title);
            questionsCountTextView = itemView.findViewById(R.id.questions_count);
        }
    }
}
