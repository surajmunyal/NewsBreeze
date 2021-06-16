package com.greedygame.newsbreeze.ui.main;

import android.os.Bundle;
import android.view.Menu;

import com.greedygame.newsbreeze.R;
import com.greedygame.newsbreeze.base.BaseActivity;
import com.greedygame.newsbreeze.ui.list.NewsListFragment;

public class MainActivity extends BaseActivity {

    @Override
    protected int layoutRes() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState == null)
            getSupportFragmentManager().beginTransaction().add(R.id.screenContainer, new NewsListFragment()).addToBackStack(null).commit();
    }
}
