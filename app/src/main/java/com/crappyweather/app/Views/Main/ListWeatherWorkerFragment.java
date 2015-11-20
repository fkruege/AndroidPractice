package com.crappyweather.app.Views.Main;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.crappyweather.app.Application.MyApplication;
import com.crappyweather.app.injection.ActivityModule;
import com.crappyweather.app.injection.DaggerFragmentComponent;
import com.crappyweather.app.injection.FragmentModule;
import com.crappyweather.app.model.CrappyWeather;
import com.crappyweather.app.network.CrappyWeatherNetworkInterface;
import com.crappyweather.app.network.NetworkClient;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;

/**
 * Created by fkruege on 11/19/2015.
 */
public class ListWeatherWorkerFragment extends Fragment {

    public interface MasterFragment {
        void setStream(Observable<CrappyWeather> weatherStream, boolean forceRefresh);

        void ready();
    }

    private MasterFragment _MasterFragment;
    private Subscription _storedSubscription;
    private Subject<CrappyWeather, CrappyWeather> _CrappyWeatherStream;

    @Inject
    CrappyWeatherNetworkInterface _rxWeatherSearch;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        _MasterFragment = getMasterFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerFragmentComponent.builder()
                .appComponent(MyApplication.getInstance(getActivity()).getAppComponent())
                .activityModule(new ActivityModule((AppCompatActivity) getActivity()))
                .fragmentModule(new FragmentModule(this))
                .build()
                .inject(this);

        setRetainInstance(true);
    }


    @Override
    public void onResume() {
        super.onResume();
        if (_MasterFragment != null && _CrappyWeatherStream != null) {
            _MasterFragment.setStream(_CrappyWeatherStream.asObservable(), false);
        }

        if (_MasterFragment != null) {
            _MasterFragment.ready();
        }

    }


    @Override
    public void onDetach() {
        super.onDetach();
        _MasterFragment = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        _storedSubscription.unsubscribe();
    }


    //////////////////////////////////////////////////////////////
    // Begin: Support Methods
    //////////////////////////////////////////////////////////////

    public void getWeather(final boolean forceRefresh, final String... cities) {

        if (_storedSubscription == null || forceRefresh) {

            if (_storedSubscription != null) {
                _storedSubscription.unsubscribe();
            }

            _CrappyWeatherStream = getSubject();
            _MasterFragment.setStream(_CrappyWeatherStream.asObservable(), forceRefresh);
//
//
//            _storedSubscription = Observable.from(cities)
//                    .map(new Func1<String, CrappyWeather>() {
//                        @Override
//                        public CrappyWeather call(String s) {
//
//                            try {
//                                Thread.sleep(1000);
//                            } catch (InterruptedException e) {
//                                e.printStackTrace();
//                            }
//
//                            CrappyWeather weather = new CrappyWeather();
//                            weather.name = s;
//                            return weather;
//                        }
//                    })
//                    .subscribeOn(Schedulers.newThread())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(_CrappyWeatherStream);
//


            _storedSubscription = Observable.from(cities)
                    .flatMap(_rxWeatherSearch::getRxCrappyWeather)
                    .map(new Func1<CrappyWeather, CrappyWeather>() {

                        @Override
                        public CrappyWeather call(CrappyWeather weather) {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                            return weather;
                        }
                    })
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(_CrappyWeatherStream);

        }


    }

//
//    public void getWeather(String city) {
//
//        if (_storedSubscription != null) {
//            _storedSubscription.unsubscribe();
//            _CrappyWeatherStream = getSubject();
//            _MasterFragment.setStream(_CrappyWeatherStream.asObservable());
//        }
//
//        _storedSubscription = _rxWeatherSearch.getRxCrappyWeather(city)
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(_CrappyWeatherStream);
//
//
//    }

    private Subject<CrappyWeather, CrappyWeather> getSubject() {
        Subject<CrappyWeather, CrappyWeather> subject = ReplaySubject.createWithSize(10);
        return subject;
    }

    private MasterFragment getMasterFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ListWeatherFragment fragment = (ListWeatherFragment) fm.findFragmentByTag(ListWeatherFragment.class.getName());
        return fragment;
    }
}
