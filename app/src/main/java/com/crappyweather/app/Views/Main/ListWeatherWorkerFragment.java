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

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import rx.subjects.ReplaySubject;
import rx.subjects.Subject;

/**
 * Created by fkruege on 11/19/2015.
 */
public class ListWeatherWorkerFragment extends Fragment{

    public interface MasterFragment {
        void setStream(Observable<CrappyWeather> weatherStream);

        void ready();
    }

    private MasterFragment _MasterFragment;
    private Subscription _storedSubscription;

    private Subject<CrappyWeather, CrappyWeather> _CrappyWeatherStream = getSubject();

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
        _MasterFragment.setStream(_CrappyWeatherStream.asObservable());
        _MasterFragment.ready();

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

    public void getWeather(final String... cities) {
//        Observable.just(cities)
//                .flatMap()
//                .subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(_CrappyWeatherStream);

        if (_storedSubscription == null) {

            _storedSubscription = Observable.from(cities)
                    .flatMap(_rxWeatherSearch::getRxCrappyWeather)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(_CrappyWeatherStream);
        }



    }



    public void getWeather(String city) {

        if (_storedSubscription != null) {
            _storedSubscription.unsubscribe();
            _CrappyWeatherStream = getSubject();
            _MasterFragment.setStream(_CrappyWeatherStream.asObservable());
        }

        _storedSubscription = _rxWeatherSearch.getRxCrappyWeather(city)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(_CrappyWeatherStream);


    }

    private Subject<CrappyWeather, CrappyWeather> getSubject() {
       return ReplaySubject.createWithSize(10);
    }

    private MasterFragment getMasterFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ListWeatherFragment fragment =(ListWeatherFragment) fm.findFragmentByTag(ListWeatherFragment.class.getName());
        return fragment;
    }
}
