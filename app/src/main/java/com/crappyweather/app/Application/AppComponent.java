package com.crappyweather.app.Application;

import android.content.Context;

import com.crappyweather.app.network.CrappyWeatherNetworkInterface;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by fkruege on 11/19/2015.
 */
@Singleton
@Component(modules={AppModule.class})
public interface AppComponent {

    Context context();
    CrappyWeatherNetworkInterface crappyWeatherNetworkInterface();
}
