package com.greedygame.newsbreeze.ui.main;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

import com.greedygame.newsbreeze.ui.detail.NewsDetailFragment;
import com.greedygame.newsbreeze.ui.list.NewsListFragment;

@Module
public abstract class MainFragmentBindingModule {

    @ContributesAndroidInjector
    abstract NewsListFragment provideListFragment();

    @ContributesAndroidInjector
    abstract NewsDetailFragment provideNewsDetailFragment();
}
