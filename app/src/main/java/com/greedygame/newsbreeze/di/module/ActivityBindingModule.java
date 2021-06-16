package com.greedygame.newsbreeze.di.module;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

import com.greedygame.newsbreeze.ui.detail.NewsDetailsActivity;
import com.greedygame.newsbreeze.ui.main.MainActivity;
import com.greedygame.newsbreeze.ui.main.MainFragmentBindingModule;

@Module
public abstract class ActivityBindingModule {

    @ContributesAndroidInjector(modules = {MainFragmentBindingModule.class})
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector(modules = {MainFragmentBindingModule.class})
    abstract NewsDetailsActivity bindDetailActivity();
}
