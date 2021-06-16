package com.greedygame.newsbreeze.ui.detail;

import com.greedygame.newsbreeze.data.model.Article;

import java.util.ArrayList;
import java.util.List;

public class SingletonClass {

    private static volatile SingletonClass sSoleInstance;
    private List<Article> articleList = new ArrayList<>();

    private SingletonClass(){
        if (sSoleInstance != null){
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }

    public static SingletonClass getInstance() {

        if (sSoleInstance == null) {
            synchronized (SingletonClass.class) {
              if (sSoleInstance == null) sSoleInstance = new SingletonClass();
            }
        }

        return sSoleInstance;
    }

    public void saveNews(Article article){
        articleList.add(article);
    }

    public List<Article> getArticleList(){
        return articleList;
    }
}