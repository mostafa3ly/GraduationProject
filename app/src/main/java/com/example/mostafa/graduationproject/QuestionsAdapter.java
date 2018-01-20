package com.example.mostafa.graduationproject;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by mostafa on 12/12/2017.
 */

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionViewHolder> {

    private List<Question> questions;
    private Context context;


    public QuestionsAdapter(ArrayList<Question> questions, Context context) {
        this.questions = questions;
        this.context = context;
    }

    @Override
    public QuestionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view  = LayoutInflater.from(parent.getContext()).inflate(R.layout.questions_list_item,parent,false);
        return new QuestionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final QuestionViewHolder holder, int position) {

        holder.questionEditText.setText(questions.get(position).getQuestion());
        holder.questionEditText.setFocusable(false);
            final AnswersAdapter answersAdapter = new AnswersAdapter(questions.get(position).getAnswers(),questions.get(position).getType(),false);
            holder.answersRecyclerView.setLayoutManager(new LinearLayoutManager(context));
            holder.answersRecyclerView.setAdapter(answersAdapter);
        holder.editImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editQuestion(holder.getAdapterPosition());
            }
        });

    }

    @Override
    public int getItemCount() {
        return questions.size();
    }

    public class QuestionViewHolder extends RecyclerView.ViewHolder
    {

        @BindView(R.id.QuestionsActivity_EditText_Question)EditText questionEditText;
        @BindView(R.id.QuestionsActivity_RecyclerView_Answers)RecyclerView answersRecyclerView;
        @BindView(R.id.QuestionsActivity_ImageButton_EditQuestion)ImageButton editImageButton;

        public QuestionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public void add (Question question)
    {
        this.questions.add(question);
        notifyDataSetChanged();
    }

    public void remove (int position)
    {
        questions.remove(position);
        notifyDataSetChanged();
    }


    private void editQuestion(final int position)
    {
        final Question question = questions.get(position);
        final int questionType = question.getType();
        final View dialogView = ((QuestionsActivity)context).getLayoutInflater().inflate(R.layout.questions_list_item,null);

        final EditText questionEditText = dialogView.findViewById(R.id.QuestionsActivity_EditText_Question);
        questionEditText.setEnabled(true);
        questionEditText.requestFocus();
        questionEditText.setText(question.getQuestion());
        questionEditText.setSelection(questionEditText.length());

        dialogView.findViewById(R.id.QuestionsActivity_ImageButton_EditQuestion).setVisibility(View.GONE);

        final RecyclerView answersRecyclerView = dialogView.findViewById(R.id.QuestionsActivity_RecyclerView_Answers);

        final List<String> previousAnswers = new ArrayList<>();
        previousAnswers.addAll(question.getAnswers());

        final AnswersAdapter answersAdapter = new AnswersAdapter(question.getAnswers(),questionType,true);
        answersRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        answersRecyclerView.setAdapter(answersAdapter);

        questionEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AnswersAdapter.resetFocusedPosition();
                return false;
            }
        });
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setTitle(context.getString(R.string.edit_question));
        alertDialog.setView(dialogView);
        alertDialog.setCanceledOnTouchOutside(false);

        answersRecyclerView.setOnFlingListener(new RecyclerView.OnFlingListener() {
            @Override
            public boolean onFling(int velocityX, int velocityY) {
                hideInputTool(dialogView);
                return false;
            }
        });

        dialogView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideInputTool(dialogView);
                return false;
            }
        });

        if(questionType!=Utils.ESSAY_QUESTION_TYPE) {
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, context.getString(R.string.add_answer), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }


        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, context.getString(R.string.submit), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, context.getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        questions.get(position).setAnswers(previousAnswers);
                        notifyDataSetChanged();
                        hideInputTool(dialogView);
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                questions.get(position).setAnswers(previousAnswers);
                notifyDataSetChanged();
                hideInputTool(dialogView);
            }
        });

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(questionEditText.getText().toString().isEmpty())
                {
                    Toast.makeText(context,context.getString(R.string.empty_question_message),Toast.LENGTH_SHORT).show();
                    questionEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.exclamation, 0);
                    questionEditText.requestFocus();
                }

                else if(questionType!=Utils.ESSAY_QUESTION_TYPE && !answersAdapter.hasAtLeastTwoAnswers())
                {
                    questionEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    Toast.makeText(context,context.getString(R.string.less_than_two_answers_message),Toast.LENGTH_SHORT).show();
                }
                else {
                    answersAdapter.removeEmptyAnswers();
                    Question question = new Question();
                    question.setAnswers(answersAdapter.getAnswers());
                    question.setType(questionType);
                    question.setQuestion(questionEditText.getText().toString());
                    questions.set(position, question);
                    notifyDataSetChanged();
                    hideInputTool(dialogView);
                    alertDialog.dismiss();
                }
            }
        });

        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answersAdapter.add("");
            }
        });
    }


    private void hideInputTool(View view)
    {
        InputMethodManager inputMethodManager = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
