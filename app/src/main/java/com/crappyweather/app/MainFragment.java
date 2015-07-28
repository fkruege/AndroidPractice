package com.crappyweather.app;

import com.crappyweather.app.adapter.CrappyWeatherAdapter;
import com.crappyweather.app.model.CrappyWeather;
import com.crappyweather.app.network.NetworkClient;

import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends ListFragment {

    private static final String[] QUERIES = new String[]{"Franklin, TN", "Conway, AR", "Atlanta, GA", "New York City, NY", "San Francisco, CA", "Anchorage, AK", "Paris, France"};

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ShittyWeatherTask task = new ShittyWeatherTask();
        task.execute(QUERIES);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.add("Refresh").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ShittyWeatherTask task = new ShittyWeatherTask();
                task.execute(QUERIES);

                return true;
            }
        });
    }

    class ShittyWeatherTask extends AsyncTask<String, Void, List<CrappyWeather>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            setListShown(false);
        }

        @Override
        protected List<CrappyWeather> doInBackground(String... params) {
            List<CrappyWeather> weathers = new ArrayList<CrappyWeather>();
            for (String query : params) {
                CrappyWeather weather = NetworkClient.getInstance().getShittyWeather(query);
                weathers.add(weather);
            }

            return weathers;
        }

        @Override
        protected void onPostExecute(List<CrappyWeather> crappyWeathers) {
            super.onPostExecute(crappyWeathers);
            setListAdapter(new CrappyWeatherAdapter(crappyWeathers));
            setListShown(true);
        }
    }

}
