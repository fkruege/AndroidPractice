package com.crappyweather.app;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.crappyweather.app.Application.MyApplication;
import com.crappyweather.app.Views.Main.ListWeatherFragment;
import com.crappyweather.app.injection.ActivityModule;
import com.crappyweather.app.injection.DaggerActivityComponent;

import javax.inject.Inject;

import dagger.Lazy;


public class MainActivity extends AppCompatActivity {


    @Inject
    Lazy<ListWeatherFragment> _lazyListWeatherFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerActivityComponent.builder()
                .appComponent(MyApplication.getInstance(this).getAppComponent())
                .activityModule(new ActivityModule(this))
                .build()
                .inject(this);

        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, _lazyListWeatherFragment.get(), ListWeatherFragment.class.getName())
                    .commit();
        }
    }
}
