package com.crappyweather.app.adapter;

import com.crappyweather.app.R;
import com.crappyweather.app.model.CrappyWeather;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CrappyWeatherAdapter extends BaseAdapter {

    List<CrappyWeather> mCrappyWeatherList;

    public CrappyWeatherAdapter(List<CrappyWeather> crappyWeatherList) {
        mCrappyWeatherList = crappyWeatherList;
    }

    @Override
    public int getCount() {
        return mCrappyWeatherList.size();
    }

    @Override
    public CrappyWeather getItem(int position) {
        return mCrappyWeatherList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_weather, parent, false);

        CrappyWeather crappyWeather = getItem(position);
        TextView weatherTextView = (TextView) view.findViewById(R.id.weather_TextView);

        double temp = kelvinToFahrenheit(crappyWeather.getMain().getTemp());
        weatherTextView.setText(parent.getContext().getString(R.string.weather, temp, crappyWeather.getName(), getText(temp)));

        return view;
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

    private double kelvinToFahrenheit(double kelvin) {
        double celius = kelvin - 273;
        return celius * 1.8 + 32;
    }
}
