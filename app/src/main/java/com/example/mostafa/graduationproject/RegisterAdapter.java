package com.example.mostafa.graduationproject;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by mosta on 30/10/2017.
 */

public class RegisterAdapter extends FragmentPagerAdapter {

    private static final int PAGES = 2;
    private Context context;

    public RegisterAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }


    @Override
    public Fragment getItem(int position) {
        switch (position)
        {
            case 0:
                return new LoginFragment();
            case 1:
                return new SignUpFragment();
            default:
                return new Fragment();
        }
    }

    @Override
    public int getCount() {
        return PAGES;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position)
        {
            case 0:
                return context.getString(R.string.login);
            case 1:
                return context.getString(R.string.sign_up);
            default:
                return "";
        }
    }
}
