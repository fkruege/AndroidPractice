package com.crappyweather.app.injection;

import android.support.v7.app.AppCompatActivity;

import com.crappyweather.app.Views.Main.ListWeatherFragment;
import com.crappyweather.app.injection.scopes.ActivityScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by fkruege on 11/19/2015.
 */
@Module
public class ActivityModule {

    private final AppCompatActivity _Activity;

    public ActivityModule(AppCompatActivity activity) {
        _Activity = activity;
    }

    @Provides
    @ActivityScope
    AppCompatActivity provideAppCompatActivity() {
        return _Activity;
    }

    @Provides
    @ActivityScope
    ListWeatherFragment provideListWeatherFragment() {
        return new ListWeatherFragment();
    }

}
