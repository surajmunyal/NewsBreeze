package com.greedygame.newsbreeze.ui.list;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Collections;
import java.util.Comparator;

import javax.inject.Inject;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import com.greedygame.newsbreeze.data.model.Article;
import com.greedygame.newsbreeze.data.model.NewsList;
import com.greedygame.newsbreeze.data.rest.NewsListRepo;

public class NewsListViewModel extends ViewModel {

    private final NewsListRepo newsListRepo;
    private CompositeDisposable disposable;

    private final MutableLiveData<NewsList> repos = new MutableLiveData<>();
    private final MutableLiveData<Boolean> placesLoadError = new MutableLiveData<>();
    private final MutableLiveData<Boolean> loading = new MutableLiveData<>();

    @Inject
    public NewsListViewModel(NewsListRepo newsListRepo) {
        this.newsListRepo = newsListRepo;
        disposable = new CompositeDisposable();
        fetchPlacesList();
    }

    LiveData<NewsList> getRepos() {
        return repos;
    }
    LiveData<Boolean> getError() {
        return placesLoadError;
    }
    LiveData<Boolean> getLoading() {
        return loading;
    }

    private void fetchPlacesList() {
        loading.setValue(true);
        disposable.add(newsListRepo.getPlaceRepo().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribeWith(new DisposableSingleObserver<NewsList>() {
                    @Override
                    public void onSuccess(NewsList value) {
                        placesLoadError.setValue(false);
                        repos.setValue(value);
                        loading.setValue(false);
                    }

                    @Override
                    public void onError(Throwable e) {
                        placesLoadError.setValue(true);
                        loading.setValue(false);
                    }
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null) {
            disposable.clear();
            disposable = null;
        }
    }

    public void sortNewsList(){
        NewsList newsList = repos.getValue();
        Collections.sort(newsList.getArticles(), new Comparator<Article>() {
            public int compare(Article o1, Article o2) {
                return o1.getPublishedAt().compareTo(o2.getPublishedAt());
            }
        });
        repos.setValue(newsList);
    }
}
