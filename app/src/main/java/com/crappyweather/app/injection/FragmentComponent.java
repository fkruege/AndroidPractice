package com.crappyweather.app.injection;

import android.support.v4.app.Fragment;

import com.crappyweather.app.Application.AppComponent;
import com.crappyweather.app.Views.Main.ListWeatherFragment;
import com.crappyweather.app.Views.Main.ListWeatherWorkerFragment;
import com.crappyweather.app.injection.scopes.FragmentScope;

import dagger.Component;

/**
 * Created by fkruege on 11/19/2015.
 */

@FragmentScope
@Component(dependencies = {AppComponent.class}, modules = { ActivityModule.class, FragmentModule.class})
public interface FragmentComponent {

    Fragment fragment();

    void inject(ListWeatherFragment fragment);
    void inject(ListWeatherWorkerFragment fragment);
}
