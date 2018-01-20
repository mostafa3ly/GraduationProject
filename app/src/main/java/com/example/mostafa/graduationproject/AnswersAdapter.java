package com.example.mostafa.graduationproject;

import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by mosta on 14/12/2017.
 */

public class AnswersAdapter extends RecyclerView.Adapter<AnswersAdapter.AnswerViewHolder> {

    private List<String> answers;
    private int questionType;
    private boolean isAnswerEditable;
    private static int checkedPosition;
    private static int focusedPosition;

    public AnswersAdapter(List<String> answers, int questionType, boolean isAnswerEditable) {
        this.answers = answers;
        this.questionType = questionType;
        this.isAnswerEditable = isAnswerEditable;
        checkedPosition = -1;
        focusedPosition = -1;
    }

    @Override
    public AnswerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_list_item,parent,false);
        return new AnswerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final AnswerViewHolder holder, final int position) {
        holder.answerEditText.setText(answers.get(position));
        holder.answerEditText.setEnabled(isAnswerEditable);
        holder.answerEditText.setSelection(holder.answerEditText.getText().length());
        if (position==focusedPosition && isAnswerEditable)
            holder.answerEditText.requestFocus();

    holder.answerEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus)
                focusedPosition = holder.getAdapterPosition();
        }
    });


    holder.answerEditText.addTextChangedListener(new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence answer, int start, int before, int count) {
            answers.set(holder.getAdapterPosition(), answer.toString());
        }

        @Override
        public void afterTextChanged(Editable answer) {

        }
    });

        if(questionType == Utils.SINGLE_CHOICE_QUESTION_TYPE) {
            holder.answerRadioButton.setVisibility(View.VISIBLE);
            holder.answerCheckbox.setVisibility(View.GONE);

            holder.answerRadioButton.setChecked(holder.getAdapterPosition()==checkedPosition);
            holder.answerRadioButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkedPosition = holder.getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
            holder.answerRadioButton.setClickable(!isAnswerEditable);
        }
        else {
            holder.answerCheckbox.setVisibility(View.VISIBLE);
            holder.answerRadioButton.setVisibility(View.GONE);
            holder.answerCheckbox.setClickable(!isAnswerEditable);
        }

        if(position<2 || !isAnswerEditable)
            holder.removeAnswerImageButton.setVisibility(View.GONE);
        else {
            holder.removeAnswerImageButton.setVisibility(View.VISIBLE);
            holder.removeAnswerImageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(focusedPosition>holder.getAdapterPosition() || focusedPosition==answers.size()-1)
                    focusedPosition--;
                    answers.remove(holder.getAdapterPosition());
                    notifyDataSetChanged();
                }
            });
        }


    }



    @Override
    public int getItemCount() {
        return answers.size();
    }

    public class AnswerViewHolder extends RecyclerView.ViewHolder
    {
        @BindView(R.id.QuestionsActivity_RadioButton_Answer)RadioButton answerRadioButton;
        @BindView(R.id.QuestionsActivity_CheckBox_Answer)CheckBox answerCheckbox;
        @BindView(R.id.QuestionsActivity_EditText_Answer)EditText answerEditText;
        @BindView(R.id.QuestionsActivity_ImageButton_RemoveAnswer)ImageButton removeAnswerImageButton;
        public AnswerViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void add(String answer)
    {
        answers.add(answer);
        focusedPosition=answers.size()-1;
        notifyDataSetChanged();
    }

    public void removeEmptyAnswers()
    {
        ArrayList<String> list = new ArrayList<>();
        list.add("");
        answers.removeAll(list);
    }

    public List<String> getAnswers ()
    {
        return answers;
    }
    public static void resetFocusedPosition()
    {
        focusedPosition = -1;
    }

    public boolean hasAtLeastTwoAnswers()
    {
        int i = 0;
        for (String answer :answers) {
            if(!answer.equals(""))
                i++;
        }
        return i>=2;
    }
}
