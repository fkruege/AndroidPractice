package com.crappyweather.app.Views.Main;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.crappyweather.app.Application.MyApplication;
import com.crappyweather.app.R;
import com.crappyweather.app.RxSupport.RxUtils;
import com.crappyweather.app.injection.ActivityModule;
import com.crappyweather.app.injection.DaggerFragmentComponent;
import com.crappyweather.app.injection.FragmentModule;
import com.crappyweather.app.model.CrappyWeather;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import dagger.Lazy;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.subscriptions.CompositeSubscription;

public class ListWeatherFragment extends Fragment implements ListWeatherWorkerFragment.MasterFragment, ItemClickListener {

    private static final String[] QUERIES = new String[]{"Franklin, TN", "Conway, AR", "Atlanta, GA", "New York City, NY", "San Francisco, CA", "Anchorage, AK", "Paris, France"};
//    private static final String[] QUERIES = new String[]{"Franklin, TN", "Conway, AR"};


    private CompositeSubscription _subscriptions = new CompositeSubscription();

    private List<CrappyWeather> _WeatherList;
    private WeatherAdapter _WeatherAdapter;


    private Subscriber<CrappyWeather> _subscriber;

    @Inject
    Lazy<ListWeatherWorkerFragment> _lazyWorkerFragment;

    @Bind(R.id.recycler_view)
    RecyclerView recyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerFragmentComponent.builder()
                .appComponent(MyApplication.getInstance(getActivity()).getAppComponent())
                .activityModule(new ActivityModule((AppCompatActivity) getActivity()))
                .fragmentModule(new FragmentModule(this))
                .build()
                .inject(this);

        setupWorkerFragment();

        if (savedInstanceState == null) {

        }

        setupWeatherAdapterAndList();

        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.list_weather_fragment, container, false);

        ButterKnife.bind(this, view);
        return view;

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(_WeatherAdapter);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add("Refresh").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                _subscriber.unsubscribe();
                int saveSize = _WeatherList.size();

                _WeatherList.clear();
                _WeatherAdapter.notifyItemRangeRemoved(0, saveSize);
                getWeather(true);

                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        _subscriptions = RxUtils.getNewCompositeSubIfUnsubscribed(_subscriptions);


    }

    @Override
    public void onPause() {
        super.onPause();
        RxUtils.unsubscribeIfNotNull(_subscriptions);

    }

    //////////////////////////////////////////////////////////////
    // Begin: Support Methods
    //////////////////////////////////////////////////////////////


    private void setupWorkerFragment() {
        ListWeatherWorkerFragment workerFragment = getWorkerFragment();
        if (workerFragment == null) {
            addWorkerFragment();
        }
    }


    private ListWeatherWorkerFragment getWorkerFragment() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        ListWeatherWorkerFragment fragment = (ListWeatherWorkerFragment) fm.findFragmentByTag(ListWeatherWorkerFragment.class.getName());
        return fragment;
    }

    private void addWorkerFragment() {
        ListWeatherWorkerFragment fragment = _lazyWorkerFragment.get();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.beginTransaction().add(fragment, ListWeatherWorkerFragment.class.getName()).commit();
    }

    private void setupWeatherAdapterAndList() {
        _WeatherList = new ArrayList<>();
        _WeatherAdapter = new WeatherAdapter(_WeatherList, this);
    }

    private void getWeather(boolean forceRefresh) {
        ListWeatherWorkerFragment workerFragment = getWorkerFragment();
        workerFragment.getWeather(forceRefresh, QUERIES);
    }

    //////////////////////////////////////////////////////////////////////////
    // Begin: ListWeatherWorkerFragment.MasterFragment
    //////////////////////////////////////////////////////////////////////////

    @Override
    public void setStream(Observable<CrappyWeather> weatherStream, boolean forceRefresh) {

        if (_subscriber == null || forceRefresh) {

            _subscriber = new WeatherSubscriber();

            _subscriptions
                    .add(weatherStream.doOnSubscribe(new Action0() {
                        @Override
                        public void call() {

                        }
                    }).subscribe(_subscriber));
        }


    }

    @Override
    public void ready() {
        getWeather(false);

    }

    /////////////////////////////////////////////////////////////////////////
    // Begin: ItemClickListener
    /////////////////////////////////////////////////////////////////////////
    @Override
    public void onItemClick(View v, Object itemClicked) {

        CrappyWeather weather = (CrappyWeather) itemClicked;

        Snackbar.make(
                getActivity().findViewById(android.R.id.content),
                weather.getName(),
                Snackbar.LENGTH_LONG)
                .setActionTextColor(Color.RED)
                .show();

    }


    //////////////////////////////////////////////////////////////////////////
    // Begin: WeatherSubscriber
    //////////////////////////////////////////////////////////////////////////


    private class WeatherSubscriber extends Subscriber<CrappyWeather> {

        @Override
        public void onCompleted() {
            Log.d(this.getClass().getName(), "onCompleted");
        }

        @Override
        public void onError(Throwable e) {
            Log.d(this.getClass().getName(), e.getMessage());
        }

        @Override
        public void onNext(CrappyWeather weather) {
            _WeatherList.add(weather);
            _WeatherAdapter.notifyItemInserted(_WeatherList.size() - 1);
        }
    }


}
