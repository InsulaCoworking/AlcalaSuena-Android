package com.triskelapps.alcalasuena.ui.intro;

import android.os.Bundle;
import androidx.viewpager.widget.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ToxicBakery.viewpager.transforms.TabletTransformer;
import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseActivity;
import com.triskelapps.alcalasuena.base.BasePresenter;

public class IntroActivity extends BaseActivity implements IntroView, View.OnClickListener, ViewPager.OnPageChangeListener {

    private IntroPresenter presenter;
    private ViewPager viewpagerIntro;
    private ImageView imgArrowLeft;
    private ImageView imgArrowRight;
    private IntroViewPagerAdapter adapter;
    private TextView tvIntroEnter;

    private void findViews() {
        viewpagerIntro = (ViewPager)findViewById( R.id.viewpager_intro );
        imgArrowLeft = (ImageView)findViewById( R.id.img_arrow_left );
        imgArrowRight = (ImageView)findViewById( R.id.img_arrow_right );
        tvIntroEnter = (TextView)findViewById( R.id.tv_intro_enter );

        imgArrowLeft.setOnClickListener(this);
        imgArrowRight.setOnClickListener(this);
        tvIntroEnter.setOnClickListener(this);
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        presenter = IntroPresenter.newInstance(this, this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        findViews();

        adapter = new IntroViewPagerAdapter(this, getSupportFragmentManager());
        viewpagerIntro.setAdapter(adapter);

        viewpagerIntro.setPageTransformer(false, new TabletTransformer());
        viewpagerIntro.addOnPageChangeListener(this);

        presenter.onCreate();
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_arrow_left:

                int newPosition = viewpagerIntro.getCurrentItem() > 0 ? viewpagerIntro.getCurrentItem() - 1 : 0;
                viewpagerIntro.setCurrentItem(newPosition, true);
                break;


            case R.id.tv_intro_enter:
            case R.id.img_arrow_right:

                if (viewpagerIntro.getCurrentItem() == adapter.getCount() - 1) {
                    presenter.onFinishIntro();
                    return;
                }

                int newPosition2 = viewpagerIntro.getCurrentItem() < adapter.getCount()-1 ?
                        viewpagerIntro.getCurrentItem() + 1 : adapter.getCount()-1;
                viewpagerIntro.setCurrentItem(newPosition2, true);
                break;
        }
    }

    @Override
    public void onBackPressed() {

    }

    // VIEW PAGER
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        imgArrowLeft.setVisibility(position == 0 ? View.INVISIBLE : View.VISIBLE);
        imgArrowRight.setVisibility(View.VISIBLE);
        boolean lastScreen = position == adapter.getCount() - 1;
//        imgArrowRight.setImageResource(lastScreen ? R.mipmap.ic_logo_alcala_suena : R.mipmap.ic_arrow_right_intro);
        imgArrowRight.setVisibility(lastScreen ? View.GONE : View.VISIBLE);
        tvIntroEnter.setVisibility(lastScreen ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
