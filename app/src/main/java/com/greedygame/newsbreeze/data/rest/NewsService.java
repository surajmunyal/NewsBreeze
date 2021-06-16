package com.greedygame.newsbreeze.data.rest;

import io.reactivex.Single;

import com.greedygame.newsbreeze.data.model.NewsList;

import retrofit2.http.GET;

public interface NewsService {

    @GET("everything?q=apple&from=2021-06-01&to=2021-06-14&sortBy=popularity&apiKey=8f5bf6c53e704ebdaa5acffe72742bb3")
    Single<NewsList> getNewsList();
}
