package com.triskelapps.alcalasuena.ui.intro;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroFragment extends Fragment {


    private static final String EXTRA_LAYOUT_RESOURCE = "extra_layout_resource";


    public IntroFragment() {
        // Required empty public constructor
    }

    public static final IntroFragment newIntroFragment(int layoutRes) {
        IntroFragment introFragment = new IntroFragment();
        Bundle args = new Bundle();
        args.putInt(EXTRA_LAYOUT_RESOURCE, layoutRes);
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

        int layoutRes = getArguments().getInt(EXTRA_LAYOUT_RESOURCE, -1);
        return inflater.inflate(layoutRes, container, false);
    }

}
