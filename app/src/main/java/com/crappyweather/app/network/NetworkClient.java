package com.crappyweather.app.network;

import com.crappyweather.app.model.CrappyWeather;

import retrofit.RestAdapter;
import rx.Observable;

public class NetworkClient {

    private static NetworkClient sInstance;

    private NetworkClient() {
        // no-op
    }

    public static NetworkClient getInstance() {
        if (sInstance == null) {
            sInstance = new NetworkClient();
        }

        return sInstance;
    }

    public Observable<CrappyWeather> getCrappyWeather(String location) {
        return getNetworkInterface().getCrappyWeather(location);
    }

    private NetworkInterface getNetworkInterface() {
        RestAdapter retrofit = new RestAdapter.Builder()
                .setEndpoint("http://api.openweathermap.org")
                .build();

        return retrofit.create(NetworkInterface.class);
    }
}
