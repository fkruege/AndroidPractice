package com.crappyweather.app.Application;

import android.app.Application;
import android.content.Context;

/**
 * Created by fkruege on 11/19/2015.
 */
public class MyApplication extends Application {

    public static MyApplication getInstance(Context context) {
        return (MyApplication) context.getApplicationContext();
    }

    private AppComponent _AppComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        _AppComponent= DaggerAppComponent.builder()
                .appModule(new AppModule(this))
                .build();

    }

    public AppComponent getAppComponent() {
        return _AppComponent;
    }
}
