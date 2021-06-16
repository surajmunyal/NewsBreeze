package com.greedygame.newsbreeze.ui.detail;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.greedygame.newsbreeze.R;
import com.greedygame.newsbreeze.base.BaseActivity;
import com.greedygame.newsbreeze.data.model.Article;
import com.greedygame.newsbreeze.ui.list.NewsListFragment;

public class NewsDetailsActivity extends BaseActivity {

    @Override
    protected int layoutRes() {
        return R.layout.activity_detail;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null) {
            NewsDetailFragment newsDetailFragment = NewsDetailFragment.newInstance(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.screenContainer, newsDetailFragment).commit();
        }
    }
}
