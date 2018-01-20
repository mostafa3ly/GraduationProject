package com.example.mostafa.graduationproject;


import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
public class LoginFragment extends Fragment {

    @BindView(R.id.LoginFragment_Button_Login)
    Button loginButton;
    @BindView(R.id.LoginFragment_EditText_Email)
    EditText emailEditText;
    @BindView(R.id.LoginFragment_EditText_Password)
    EditText passwordEditText;
    @BindView(R.id.LoginFragment_TextView_CreateAccount)
    TextView createAccountTextView;

    private Context mContext;


    public LoginFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view  = inflater.inflate(R.layout.fragment_login, container, false);
        // Inflate the layout for this fragment
        mContext = getContext();
        ButterKnife.bind(this, view);


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isValidData()) {
                    login();
                }

            }
        });

        createAccountTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).switchToPage(1);
            }
        });
        return view;
    }

    private void saveUser(String token)
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.current_user),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Utils.EMAIL_PROPERTY_KEY,getUserData().get(Utils.EMAIL_PROPERTY_KEY).toString());
        editor.putString(Utils.PASSWORD_PROPERTY_KEY,getUserData().get(Utils.PASSWORD_PROPERTY_KEY).toString());
        editor.putString(Utils.TOKEN_PROPERTY_KEY,token);
        editor.apply();
    }

    private JsonObject getUserData ()
    {
        JsonObject user = new JsonObject();
        user.addProperty(Utils.EMAIL_PROPERTY_KEY,emailEditText.getText().toString());
        user.addProperty(Utils.PASSWORD_PROPERTY_KEY,passwordEditText.getText().toString());

        return user;
    }

    private void login()
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(R.layout.progress_layout);
        final Dialog dialog =  builder.create();
        dialog.setCancelable(false);
        dialog.show();

        Utils.ApiEndPointsInterface apiService = Utils.getApiClient().create(Utils.ApiEndPointsInterface.class);
        JsonObject user = getUserData();
        Call<JsonObject> call = apiService.login(user);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                JsonObject userResponse = response.body();

                if(userResponse!=null)
                {
                    saveUser(userResponse.get(Utils.TOKEN_PROPERTY_KEY).toString());
                    Intent intent = new Intent(getActivity(), HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else {
                    Toast.makeText(mContext,mContext.getString(R.string.invalid_email_or_password),Toast.LENGTH_LONG).show();

                }
                dialog.dismiss();
            }

            @Override
            public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
                dialog.dismiss();
                Toast.makeText(mContext,mContext.getString(R.string.login_failed),Toast.LENGTH_LONG).show();
            }
        });

    }

    private boolean isValidData ()
    {
        emailEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
        passwordEditText.setCompoundDrawablesWithIntrinsicBounds(0,0,0,0);
       return (isValidInput(emailEditText) && isValidInput(passwordEditText));
    }

    private boolean isValidInput(EditText input)
    {
        String message = "";
        switch (input.getId())
        {
            case R.id.LoginFragment_EditText_Email:
                String email = input.getText().toString();
                if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    message = getString(R.string.invalid_email_message);
                }
                break;
            case R.id.LoginFragment_EditText_Password:
                if(input.getText().toString().isEmpty())
                    message = getString(R.string.empty_password_message);
                break;
        }
        if(!message.isEmpty()) {
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
            input.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.exclamation, 0);
            input.requestFocus();
        }

        return message.isEmpty();
    }

}
