package com.crappyweather.app;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.crappyweather.app.network.NetworkClient;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainFragment extends Fragment {

    private static final String QUERY = "Nashville, TN";

    private ProgressBar mProgressBar;
    private TextView mWeatherTextView;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progress);
        mWeatherTextView = (TextView) view.findViewById(R.id.weather_TextView);

        NetworkClient.getInstance().getCrappyWeather(QUERY)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(() -> mProgressBar.setVisibility(View.VISIBLE))
                .doOnTerminate(() -> mProgressBar.setVisibility(View.GONE))
                .subscribe(
                        weather -> {
                            double temp = convert(weather.getMain().getTemp());
                            mWeatherTextView.setText(getString(R.string.weather, temp, weather.getName(), getText(temp)));
                        },
                        t -> Toast.makeText(getActivity(), "Error retrieving weather", Toast.LENGTH_LONG).show()
                );
    }

    private String getText(double temp) {
        if (temp > 100) {
            return "THAT'S FREAKING BLAZING";
        } else if (temp > 85) {
            return "THAT'S FREAKING HOT";
        } else if (temp > 75) {
            return "THAT'S FREAKING WARM";
        } else if (temp > 65) {
            return "THAT'S FREAKING TEMPERATE";
        } else if (temp > 45) {
            return "THAT'S FREAKING CHILLY";
        } else if (temp > 32) {
            return "THAT'S FREAKING COLD";
        } else {
            return "THAT'S FREAKING FREEZING";
        }
    }

    private double convert(double kelvin) {
        double celsius = kelvin - 273;
        return celsius * 1.8 + 32;
    }
}
