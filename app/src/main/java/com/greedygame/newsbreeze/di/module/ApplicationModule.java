package com.greedygame.newsbreeze.di.module;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

import com.greedygame.newsbreeze.base.BaseApplication;
import com.greedygame.newsbreeze.data.rest.NewsService;
import com.greedygame.newsbreeze.util.CommonUtils;

import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Singleton
@Module(includes = {ViewModelModule.class,ContextModule.class})
public class ApplicationModule {

    private static final String BASE_URL = "https://newsapi.org/v2/";

    @Singleton
    @Provides
    static Retrofit provideRetrofit() {

        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(BaseApplication.getAppContext().getCacheDir(), cacheSize);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(offlineInterceptor)
                .addNetworkInterceptor(onlineInterceptor)
                .cache(cache)
                .build();

        return new Retrofit.Builder().baseUrl(BASE_URL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    static Interceptor onlineInterceptor = new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            okhttp3.Response response = chain.proceed(chain.request());
            int maxAge = 60;
            return response.newBuilder()
                    .header("Cache-Control", "public, max-age=" + maxAge)
                    .removeHeader("Pragma")
                    .build();
        }
    };

    static Interceptor offlineInterceptor= new Interceptor() {
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!CommonUtils.isNetworkAvailable(BaseApplication.getAppContext())) {
                int maxStale = 60 * 60 * 24 * 30; // Offline cache available for 30 days
                request = request.newBuilder()
                        .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                        .removeHeader("Pragma")
                        .build();
            }
            return chain.proceed(request);
        }
    };

    @Singleton
    @Provides
    static NewsService provideRetrofitService(Retrofit retrofit) {
        return retrofit.create(NewsService.class);
    }
}
