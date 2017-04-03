package com.albineli.udacity.popularmovies.injector.modules;

import android.content.Context;
import android.content.SharedPreferences;

import com.albineli.udacity.popularmovies.PopularMovieApplication;
import com.albineli.udacity.popularmovies.injector.PerFragment;
import com.albineli.udacity.popularmovies.repository.movie.MovieRepository;
import com.albineli.udacity.popularmovies.util.LogUtils;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class ApplicationModule {
    private static final String BASE_URL = "https://api.themoviedb.org/3/";
    private static final String SP_KEY = "sp_popular_movies";
    private final PopularMovieApplication mPopularMovieApplication;

    public ApplicationModule(PopularMovieApplication popularMovieApplication) {
        this.mPopularMovieApplication = popularMovieApplication;
    }

    @Provides
    @Singleton
    PopularMovieApplication providePopularMovieApplicationContext() {
        return mPopularMovieApplication;
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit() {
        // Add a log interceptor
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        if (LogUtils.LOGGING_ENABLED) {
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
            httpClient.addInterceptor(logging);
        }

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.newThread()))
                .client(httpClient.build())
                .build();
    }

    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(PopularMovieApplication popularMovieApplication) {
        return popularMovieApplication.getSharedPreferences(SP_KEY, Context.MODE_PRIVATE);
    }
}