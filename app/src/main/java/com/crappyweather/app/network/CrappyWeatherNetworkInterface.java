package com.crappyweather.app.network;

import com.crappyweather.app.model.CrappyWeather;

import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;

    public interface CrappyWeatherNetworkInterface {

    @GET("/data/2.5/weather?APPID=c80d8eabf7e092d62caf82ba370c7473")
    CrappyWeather getCrappyWeather(@Query("q") String location);

    @GET("/data/2.5/weather?APPID=c80d8eabf7e092d62caf82ba370c7473")
    Observable<CrappyWeather> getRxCrappyWeather(@Query("q") String location);
}
