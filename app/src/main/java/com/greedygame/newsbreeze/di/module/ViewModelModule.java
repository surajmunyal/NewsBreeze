package com.greedygame.newsbreeze.di.module;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import javax.inject.Singleton;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import com.greedygame.newsbreeze.ui.list.NewsListViewModel;
import com.greedygame.newsbreeze.di.util.ViewModelKey;
import com.greedygame.newsbreeze.util.ViewModelFactory;

@Singleton
@Module
public abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(NewsListViewModel.class)
    abstract ViewModel bindListViewModel(NewsListViewModel listViewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}
