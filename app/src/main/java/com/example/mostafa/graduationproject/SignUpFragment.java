package com.example.mostafa.graduationproject;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class SignUpFragment extends Fragment {


    @BindView(R.id.SignUpFragment_Button_SignUp)Button signUpButton;
    @BindView(R.id.SignUpFragment_EditText_FirstName)EditText firstNameEditText;
    @BindView(R.id.SignUpFragment_EditText_LastName)EditText lastNameEditText;
    @BindView(R.id.SignUpFragment_EditText_Email)EditText emailEditText;
    @BindView(R.id.SignUpFragment_EditText_Password)EditText passwordEditText;
    @BindView(R.id.SignUpFragment_EditText_RePassword)EditText rePasswordEditText;
    @BindView(R.id.SignUpFragment_TextView_HaveAccount)TextView haveAccountTextView;

   private Context mContext;


    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mContext = getContext();
        View view  = inflater.inflate(R.layout.fragment_signup, container, false);
        ButterKnife.bind(this, view);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidData()) {
                    createNewUser();
                }

            }
        });

        haveAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).switchToPage(0);
            }
        });

        return view;
    }


    private void createNewUser () {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.progress_layout);
        final Dialog dialog =  builder.create();
        dialog.setCancelable(false);
        dialog.show();

        Utils.ApiEndPointsInterface apiService = Utils.getApiClient().create(Utils.ApiEndPointsInterface.class);
        JsonObject user = getUserData();
        Call<JsonObject> call = apiService.createUser(user);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                JsonObject newUserResponse = response.body();
                if(newUserResponse!=null)
                {
                    login();
                }
                else {
                    Toast.makeText(mContext,mContext.getString(R.string.used_email_message),Toast.LENGTH_LONG).show();
                    emailEditText.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.exclamation, 0);
                    emailEditText.requestFocus();

                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                dialog.dismiss();
                Toast.makeText(mContext,mContext.getString(R.string.register_failed),Toast.LENGTH_LONG).show();
            }
        });
    }


    private void login()
    {

        saveUser();
        Intent intent = new Intent(getActivity(), HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void saveUser()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.current_user),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Utils.EMAIL_PROPERTY_KEY,getUserData().get(Utils.EMAIL_PROPERTY_KEY).toString());
        editor.putString(Utils.PASSWORD_PROPERTY_KEY,getUserData().get(Utils.PASSWORD_PROPERTY_KEY).toString());
        editor.putString(Utils.TOKEN_PROPERTY_KEY,getUserData().get(Utils.TOKEN_PROPERTY_KEY).toString());
        editor.apply();
    }

    private JsonObject getUserData ()
    {
        JsonObject user = new JsonObject();
        user.addProperty(Utils.FIRST_NAME_PROPERTY_KEY,firstNameEditText.getText().toString());
        user.addProperty(Utils.LAST_NAME_PROPERTY_KEY,lastNameEditText.getText().toString());
        user.addProperty(Utils.EMAIL_PROPERTY_KEY,emailEditText.getText().toString());
        user.addProperty(Utils.PASSWORD_PROPERTY_KEY,passwordEditText.getText().toString());
        return user;
    }

    private boolean isValidData ()
    {
        firstNameEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        lastNameEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        emailEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        rePasswordEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);

    return (isValidInput(firstNameEditText) && isValidInput(lastNameEditText) && isValidInput(emailEditText) && isValidInput(passwordEditText) && isValidInput(rePasswordEditText));
    }


    private boolean isValidInput(EditText input){
        String message = "";
        switch (input.getId())
        {
            case R.id.SignUpFragment_EditText_FirstName:
                if (input.getText().toString().isEmpty())
                    message = getString(R.string.empty_first_name_message);
                break;

            case R.id.SignUpFragment_EditText_LastName:
                if (input.getText().toString().isEmpty())
                    message = getString(R.string.empty_last_name_message);
                break;

            case R.id.SignUpFragment_EditText_Email:
                String email = input.getText().toString();
                if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
                    message = getString(R.string.invalid_email_message);
                break;

            case R.id.SignUpFragment_EditText_Password:
                if(input.getText().length()<8)
                    message = getString(R.string.invalid_password_message);
                break;

            case R.id.SignUpFragment_EditText_RePassword:
                if(!input.getText().toString().equals(passwordEditText.getText().toString()))
                    message = getString(R.string.password_mismatch_message);
                break;
        }
        if(!message.isEmpty()) {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
            input.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.exclamation, 0);
            input.requestFocus();
        }

        return message.isEmpty();
    }



}
