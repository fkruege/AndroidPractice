package com.crappyweather.app.Views.Main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.crappyweather.app.R;
import com.crappyweather.app.model.CrappyWeather;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by fkruege on 11/19/2015.
 */
public class WeatherAdapter extends RecyclerView.Adapter<WeatherAdapter.WeatherViewHolder> {

    private List<CrappyWeather> _CrappyWeatherList;

    public WeatherAdapter(List<CrappyWeather> list) {
        _CrappyWeatherList = list;
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_card, parent, false);

        WeatherViewHolder vh = new WeatherViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        CrappyWeather weather = _CrappyWeatherList.get(position);
        holder.txtname.setText(weather.getName());
    }

    @Override
    public int getItemCount() {
        return _CrappyWeatherList.size();
    }

    static class WeatherViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txtName)
        TextView txtname;

        public WeatherViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

}
