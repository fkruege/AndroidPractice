package com.crappyweather.app.injection;

import android.support.v4.app.Fragment;

import com.crappyweather.app.Views.Main.ListWeatherWorkerFragment;
import com.crappyweather.app.injection.scopes.FragmentScope;

import dagger.Module;
import dagger.Provides;

/**
 * Created by fkruege on 11/19/2015.
 */
@Module
public class FragmentModule {

    private Fragment _fragment;

    public FragmentModule(Fragment fragment) {

        _fragment = fragment;
    }

    @Provides
    @FragmentScope
    public Fragment providesFragment() {
        return _fragment;
    }

    @Provides
    @FragmentScope
    public ListWeatherWorkerFragment provideListWeatherWorkerFragment() {
        return new ListWeatherWorkerFragment();
    }
}
