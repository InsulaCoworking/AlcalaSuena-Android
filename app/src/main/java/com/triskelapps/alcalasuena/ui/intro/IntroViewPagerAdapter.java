package com.triskelapps.alcalasuena.ui.intro;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

/**
 * Created by julio on 16/01/17.
 */

public class IntroViewPagerAdapter extends FragmentPagerAdapter {


    public final static int NUM_INTRO_SCREENS = 5;

    public IntroViewPagerAdapter(final FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(final int position) {
        return IntroFragment.newIntroFragment(position);
    }

    @Override
    public int getCount() {
        return NUM_INTRO_SCREENS;
    }

}
