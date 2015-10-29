package com.crappyweather.app.network;

import com.crappyweather.app.model.CrappyWeather;

import retrofit.RestAdapter;

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

    public CrappyWeather getCrappyWeather(String location) {
        return getNetworkInterface().getCrappyWeather(location);
    }

    private NetworkInterface getNetworkInterface() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://api.openweathermap.org")
                .build();

        return restAdapter.create(NetworkInterface.class);
    }
}
