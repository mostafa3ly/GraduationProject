package com.example.mostafa.graduationproject;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionsActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.QuestionsActivity_FloatingActionButton_Essay)FloatingActionButton mEssayFloatingButton;
    @BindView(R.id.QuestionsActivity_FloatingActionButton_SingleChoice)FloatingActionButton mSingleChoiceFloatingButton;
    @BindView(R.id.QuestionsActivity_RecyclerView_Questions)RecyclerView mQuestionsRecyclerView;
    @BindView(R.id.QuestionsActivity_FloatingActionMenu_NewQuestion)FloatingActionMenu mNewQuestionFloatingMenu;
    @BindView(R.id.QuestionsActivity_FloatingActionButton_MultiChoice)FloatingActionButton mMultiChoiceFloatingButton;
    @BindView(R.id.QuestionsActivity_TextView_Empty)TextView mEmptyListTextView;
    @BindView(R.id.QuestionsActivity_CoordinatorLayout)CoordinatorLayout mCoordinatorLayout;


    private QuestionsAdapter mQuestionsAdapter;
    private boolean isHintShowed = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        ButterKnife.bind(this);

        mQuestionsAdapter = new QuestionsAdapter(new ArrayList<Question>(),this);
        mQuestionsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mQuestionsRecyclerView.setAdapter(mQuestionsAdapter);

       mNewQuestionFloatingMenu.setOnTouchListener(new View.OnTouchListener() {
           @Override
           public boolean onTouch(View v, MotionEvent event) {
               if (mNewQuestionFloatingMenu.isOpened())
                   mNewQuestionFloatingMenu.close(false);
               return false;
           }
       });



        ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                if(direction==ItemTouchHelper.RIGHT || direction == ItemTouchHelper.LEFT) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(QuestionsActivity.this).create();
                    alertDialog.setTitle(getString(R.string.warning));
                    alertDialog.setMessage(getString(R.string.deleting_question_message));
                    alertDialog.setCancelable(false);
                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes),
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    mQuestionsAdapter.remove(viewHolder.getAdapterPosition());
                                    if (mQuestionsAdapter.getItemCount() == 0)
                                        mEmptyListTextView.setVisibility(View.VISIBLE);

                                    dialog.dismiss();
                                }
                            });
                    alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mQuestionsAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
                    alertDialog.show();
                }
            }
        };
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(mQuestionsRecyclerView);
        mEssayFloatingButton.setOnClickListener(this);
        mSingleChoiceFloatingButton.setOnClickListener(this);
        mMultiChoiceFloatingButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.QuestionsActivity_FloatingActionButton_Essay)
            addNewQuestion(Utils.ESSAY_QUESTION_TYPE);
        else if (v.getId()==R.id.QuestionsActivity_FloatingActionButton_SingleChoice)
            addNewQuestion(Utils.SINGLE_CHOICE_QUESTION_TYPE);
        else if (v.getId()==R.id.QuestionsActivity_FloatingActionButton_MultiChoice)
            addNewQuestion(Utils.MULTI_CHOICE_QUESTION_TYPE);
    }


    private void addNewQuestion(final int questionType)
    {

        final View dialogView = getLayoutInflater().inflate(R.layout.questions_list_item,null);

        final EditText questionEditText = dialogView.findViewById(R.id.QuestionsActivity_EditText_Question);
        questionEditText.setEnabled(true);
        questionEditText.requestFocus();

        dialogView.findViewById(R.id.QuestionsActivity_ImageButton_EditQuestion).setVisibility(View.GONE);

        final RecyclerView answersRecyclerView = dialogView.findViewById(R.id.QuestionsActivity_RecyclerView_Answers);
        List<String> mandatoryAnswers = (questionType==Utils.ESSAY_QUESTION_TYPE) ? new ArrayList<String>():new ArrayList<>(Arrays.asList("",""));
        final AnswersAdapter answersAdapter = new AnswersAdapter(mandatoryAnswers,questionType,true);
        answersRecyclerView.setLayoutManager(new LinearLayoutManager(QuestionsActivity.this));
        answersRecyclerView.setAdapter(answersAdapter);

        questionEditText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                AnswersAdapter.resetFocusedPosition();
                return false;
            }
        });
        final AlertDialog alertDialog = new AlertDialog.Builder(QuestionsActivity.this).create();
        alertDialog.setTitle(getString(R.string.new_question));
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
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.add_answer), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }


            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.submit), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                }
            });


        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        hideInputTool(dialogView);
                        dialog.dismiss();
                    }
                });
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(questionEditText.getText().toString().isEmpty())
                {
                    Toast.makeText(QuestionsActivity.this,getString(R.string.empty_question_message),Toast.LENGTH_SHORT).show();
                    questionEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.exclamation, 0);
                    questionEditText.requestFocus();
                }

                else if(questionType!=Utils.ESSAY_QUESTION_TYPE && !answersAdapter.hasAtLeastTwoAnswers())
                {
                    questionEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    Toast.makeText(QuestionsActivity.this,getString(R.string.less_than_two_answers_message),Toast.LENGTH_SHORT).show();
                }
                else {
                    answersAdapter.removeEmptyAnswers();
                    Question question = new Question();
                    question.setAnswers(answersAdapter.getAnswers());
                    question.setType(questionType);
                    question.setQuestion(questionEditText.getText().toString());
                    mQuestionsAdapter.add(question);
                    mQuestionsRecyclerView.smoothScrollToPosition(mQuestionsAdapter.getItemCount() - 1);
                    if (mNewQuestionFloatingMenu.isOpened())
                        mNewQuestionFloatingMenu.close(true);

                    mEmptyListTextView.setVisibility(View.GONE);
                    hideInputTool(dialogView);

                    if (!isHintShowed) {
                        isHintShowed = true;
                        Snackbar.make(mCoordinatorLayout, getString(R.string.delete_hint),
                                Snackbar.LENGTH_SHORT)
                                .show();
                    }
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
        InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.QuestionsActivity_MenuOption_Logout:
                logout();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void logout()
    {
        AlertDialog alertDialog = new AlertDialog.Builder(QuestionsActivity.this).create();
        alertDialog.setTitle(getString(R.string.logout));
        alertDialog.setMessage(getString(R.string.logout_confirmation_message));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        removeUser();
                        Intent intent = new Intent(QuestionsActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        dialog.dismiss();
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mQuestionsAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        alertDialog.show();

    }

    private void removeUser()
    {
        SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.current_user),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public void onBackPressed() {

        if(mNewQuestionFloatingMenu.isOpened())
            mNewQuestionFloatingMenu.close(true);
        else
            super.onBackPressed();
    }
}
