package com.example.mostafa.graduationproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.tabLayout)TabLayout tabLayout;
    @BindView(R.id.viewPager)ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        RegisterAdapter registerAdapter = new RegisterAdapter(getSupportFragmentManager(),this);
        viewPager.setAdapter(registerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.current_user),Context.MODE_PRIVATE);
            if(!sharedPref.getString(Utils.EMAIL_PROPERTY_KEY,"").isEmpty())
            {
                Intent intent = new Intent(this, HomeActivity.class);
                startActivity(intent);
                finish();
            }

    }


    public void switchToPage(int page)
    {
        viewPager.setCurrentItem(page);
    }
}
