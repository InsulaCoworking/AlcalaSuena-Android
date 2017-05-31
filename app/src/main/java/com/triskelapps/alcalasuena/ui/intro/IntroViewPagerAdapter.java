package com.triskelapps.alcalasuena.ui.intro;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.triskelapps.alcalasuena.R;

/**
 * Created by julio on 16/01/17.
 */

public class IntroViewPagerAdapter extends FragmentPagerAdapter {


    private final int[] screens;

    public IntroViewPagerAdapter(Context context, final FragmentManager fm) {
        super(fm);
        TypedArray layouts = context.getResources().obtainTypedArray(R.array.intro_tutorial_screens);
        screens = new int[layouts.length()];
        for (int i = 0; i < screens.length; i++) {
            screens[i] = layouts.getResourceId(i, -1);
        }
    }

    @Override
    public Fragment getItem(final int position) {
        return IntroFragment.newIntroFragment(screens[position]);
    }

    @Override
    public int getCount() {
        return screens.length;
    }

}
