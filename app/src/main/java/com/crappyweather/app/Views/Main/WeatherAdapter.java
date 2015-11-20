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
    private ItemClickListener _ClickListener;

    public WeatherAdapter(List<CrappyWeather> list, ItemClickListener clickListener) {
        _CrappyWeatherList = list;
        _ClickListener = clickListener;
    }

    @Override
    public WeatherViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_card, parent, false);
        WeatherViewHolder vh = new WeatherViewHolder(view);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (_ClickListener != null) {
                    _ClickListener.onItemClick(view, vh.itemView.getTag());
                }
            }
        });


        return vh;
    }

    @Override
    public void onBindViewHolder(WeatherViewHolder holder, int position) {
        CrappyWeather weather = _CrappyWeatherList.get(position);
        holder.txtname.setText(weather.getName());
        holder.txtDescription.setText(getTemperatureTranslationText(weather.main.getTemp()));
        holder.itemView.setTag(weather);

    }

    @Override
    public int getItemCount() {
        return _CrappyWeatherList.size();
    }


    private String getTemperatureTranslationText(double temp) {
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

    static class WeatherViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.txtName)
        TextView txtname;

        @Bind(R.id.txtDescription)
        TextView txtDescription;

        public WeatherViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }

}
