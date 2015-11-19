package com.crappyweather.app.Application;

import android.content.Context;

import com.crappyweather.app.network.CrappyWeatherNetworkInterface;
import com.crappyweather.app.network.NetworkClient;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by fkruege on 11/19/2015.
 */
@Module
public class AppModule {

    private final MyApplication _application;

    public AppModule(MyApplication application) {
        this._application = application;
    }

    @Provides
    @Singleton
    Context provideApplicationContext() {
        return _application;
    }

    @Provides
    @Singleton
    CrappyWeatherNetworkInterface provideCrappyWeatherNetworkInterface() {
        return NetworkClient.getInstance().getCrappyWeatherNetworkInterface();
    }


}
