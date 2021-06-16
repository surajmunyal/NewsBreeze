package com.greedygame.newsbreeze.base;

import android.content.Context;

import dagger.android.AndroidInjector;
import dagger.android.support.DaggerApplication;
import com.greedygame.newsbreeze.di.component.ApplicationComponent;
import com.greedygame.newsbreeze.di.component.DaggerApplicationComponent;

public class BaseApplication extends DaggerApplication {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected AndroidInjector<? extends DaggerApplication> applicationInjector() {
        ApplicationComponent component = DaggerApplicationComponent.builder().application(this).build();
        component.inject(this);
        BaseApplication.context = getApplicationContext();

        return component;
    }

    public static Context getAppContext() {
        return BaseApplication.context;
    }
}
