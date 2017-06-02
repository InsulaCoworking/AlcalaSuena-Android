package com.triskelapps.alcalasuena.ui.news;

import com.triskelapps.alcalasuena.base.BaseView;
import com.triskelapps.alcalasuena.model.News;

import java.util.List;

/**
 * Created by julio on 2/06/17.
 */

public interface NewsView extends BaseView {

    void showNewsList(List<News> newsList);
}
