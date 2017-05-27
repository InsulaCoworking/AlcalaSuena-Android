package com.triskelapps.alcalasuena.ui.filter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.triskelapps.alcalasuena.R;
import com.triskelapps.alcalasuena.base.BaseFragment;
import com.triskelapps.alcalasuena.base.BasePresenter;
import com.triskelapps.alcalasuena.model.Tag;
import com.triskelapps.alcalasuena.views.SpaceItemDecoration;

import java.util.List;

/**
 * Created by julio on 23/05/17.
 */

public class FilterBandsFragment extends BaseFragment implements View.OnClickListener, FilterBandsView, TagsAdapter.OnItemClickListener {

    private AppCompatButton btnAllTags;
    private RecyclerView recyclerTags;
    private FilterBandsPresenter presenter;
    private TagsAdapter adapter;

    private void findViews(View layout) {
        btnAllTags = (AppCompatButton)layout.findViewById( R.id.btn_all_tags );
        recyclerTags = (RecyclerView)layout.findViewById( R.id.recycler_tags );

        btnAllTags.setOnClickListener( this );
    }

    @Override
    public BasePresenter getPresenter() {
        return presenter;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        View layout = inflater.inflate(R.layout.fragment_filter_bands, container, false);
        findViews(layout);

        presenter = FilterBandsPresenter.newInstance(this, getActivity());

//        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        recyclerTags.setLayoutManager(layoutManager);

        SpaceItemDecoration spaceItemDecoration = new SpaceItemDecoration(getResources().getDimensionPixelSize(R.dimen.separation_card_grid));
        recyclerTags.addItemDecoration(spaceItemDecoration);

        presenter.onCreate();

        return layout;
    }


    // INTERACTIONS

    @Override
    public void onClick(View v) {
        if ( v == btnAllTags ) {
            presenter.onAllTagsButtonClick();
        }
    }

    @Override
    public void onItemClick(View view, int position, int id) {
        presenter.onTagClick(id);
    }

    @Override
    public void showTags(List<Tag> tags) {

        if (adapter == null) {

            adapter = new TagsAdapter(getActivity(), tags);
            adapter.setOnItemClickListener(this);

            recyclerTags.setAdapter(adapter);

        } else {
            adapter.updateData(tags);

        }
    }

}
