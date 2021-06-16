package com.greedygame.newsbreeze.data.rest;

import javax.inject.Inject;

import io.reactivex.Single;

import com.greedygame.newsbreeze.data.model.NewsList;

public class NewsListRepo {

    private final NewsService newsService;

    @Inject
    public NewsListRepo(NewsService newsService) {
        this.newsService = newsService;
    }

    public Single<NewsList> getPlaceRepo() {
        return newsService.getNewsList();
    }
}
