package com.triskelapps.alcalasuena.ui.intro;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.triskelapps.alcalasuena.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroFragment extends Fragment {


    private static final String EXTRA_POSITION = "extra_position";


    public IntroFragment() {
        // Required empty public constructor
    }

    public static final IntroFragment newIntroFragment(int position) {
        IntroFragment introFragment = new IntroFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_POSITION, position);
        introFragment.setArguments(args);
        return introFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        int position = getArguments().getInt(EXTRA_POSITION, -1);
        int layout = 0;
        switch (position) {
            case 0:
                layout = R.layout.fragment_intro_0;
                break;

            case 1:
                layout = R.layout.fragment_intro_1;
                break;

            case 2:
                layout = R.layout.fragment_intro_2;
                break;

            case 3:
                layout = R.layout.fragment_intro_3;
                break;

            case 4:
                layout = R.layout.fragment_intro_4;
                break;


            default:
                throw new IllegalArgumentException("No view for intro fragment position: " + position);
        }

        return inflater.inflate(layout, container, false);
    }

}
