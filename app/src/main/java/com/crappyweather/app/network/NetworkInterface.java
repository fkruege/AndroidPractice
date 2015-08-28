package com.crappyweather.app.network;

import com.crappyweather.app.model.CrappyWeather;

import retrofit.http.GET;
import retrofit.http.Query;

public interface NetworkInterface {

    @GET("/data/2.5/weather")
    CrappyWeather getCrappyWeather(@Query("q") String location);
}
