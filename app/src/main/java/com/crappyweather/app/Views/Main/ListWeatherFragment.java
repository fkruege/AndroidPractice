package com.crappyweather.app.Views.Main;

import com.crappyweather.app.Application.MyApplication;
import com.crappyweather.app.R;
import com.crappyweather.app.RxSupport.RxUtils;
import com.crappyweather.app.adapter.CrappyWeatherAdapter;
import com.crappyweather.app.injection.ActivityModule;
import com.crappyweather.app.injection.DaggerFragmentComponent;
import com.crappyweather.app.injection.FragmentModule;
import com.crappyweather.app.model.CrappyWeather;
import com.crappyweather.app.network.NetworkClient;

import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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

public class ListWeatherFragment extends Fragment implements ListWeatherWorkerFragment.MasterFragment {

    private static final String[] QUERIES = new String[]{"Franklin, TN", "Conway, AR", "Atlanta, GA", "New York City, NY", "San Francisco, CA", "Anchorage, AK", "Paris, France"};
//    private static final String[] QUERIES = new String[]{"Franklin, TN"};


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
//                CrappyWeatherTask task = new CrappyWeatherTask();
//                task.execute(QUERIES);

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
        _WeatherAdapter = new WeatherAdapter(_WeatherList);
    }

    private void getWeather() {
        ListWeatherWorkerFragment workerFragment = getWorkerFragment();
        workerFragment.getWeather(QUERIES);
    }

    //////////////////////////////////////////////////////////////////////////
    // Begin: ListWeatherWorkerFragment.MasterFragment
    //////////////////////////////////////////////////////////////////////////

    @Override
    public void setStream(Observable<CrappyWeather> weatherStream) {

        if (_subscriber == null) {
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
        getWeather();

    }


    //////////////////////////////////////////////////////////////////////////
    // Begin: WeatherSubscriber
    //////////////////////////////////////////////////////////////////////////


    private class WeatherSubscriber extends Subscriber<CrappyWeather> {

        @Override
        public void onCompleted() {



        }

        @Override
        public void onError(Throwable e) {

        }

        @Override
        public void onNext(CrappyWeather weather) {
            _WeatherList.add(weather);
            _WeatherAdapter.notifyItemInserted(_WeatherList.size() - 1);

        }
    }


}
