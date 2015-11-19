package com.crappyweather.app.injection;

import android.support.v7.app.AppCompatActivity;

import com.crappyweather.app.Application.AppComponent;
import com.crappyweather.app.MainActivity;
import com.crappyweather.app.injection.scopes.ActivityScope;

import dagger.Component;

/**
 * Created by fkruege on 11/19/2015.
 */

@ActivityScope
@Component(dependencies = {AppComponent.class}, modules = ActivityModule.class)
public interface ActivityComponent {

    AppCompatActivity activity();

    void inject(MainActivity activity);



}
