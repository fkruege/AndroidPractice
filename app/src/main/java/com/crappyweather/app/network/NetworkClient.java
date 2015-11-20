package com.crappyweather.app.network;

import com.crappyweather.app.model.CrappyWeather;

import retrofit.RestAdapter;
import retrofit.android.AndroidLog;

public class NetworkClient {

    public static final String HTTP_API_OPENWEATHERMAP_ORG = "http://api.openweathermap.org";
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

    public CrappyWeatherNetworkInterface getCrappyWeatherNetworkInterface() {
        return getNetworkInterface();
    }



    private CrappyWeatherNetworkInterface getNetworkInterface() {
        RestAdapter restAdapter = getRestAdapter();
        return restAdapter.create(CrappyWeatherNetworkInterface.class);
    }

    private RestAdapter getRestAdapter() {
        return new RestAdapter.Builder()
                    .setEndpoint(HTTP_API_OPENWEATHERMAP_ORG)
                    .setLogLevel(RestAdapter.LogLevel.FULL).setLog(new AndroidLog("RETROFIT"))
                    .build();
    }
}
