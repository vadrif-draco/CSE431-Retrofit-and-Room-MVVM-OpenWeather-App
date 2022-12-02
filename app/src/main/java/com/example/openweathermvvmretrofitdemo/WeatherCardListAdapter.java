package com.example.openweathermvvmretrofitdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB.WeatherDataAggregatePOJO4RDB;
import com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB.WeatherDataWeatherInstancePOJO4RDB;
import com.example.openweathermvvmretrofitdemo.databinding.WeatherCardBinding;
import com.neovisionaries.i18n.CountryCode;
import com.squareup.picasso.Picasso;

import java.util.List;

public class WeatherCardListAdapter extends RecyclerView.Adapter<WeatherCardListAdapter.WeatherCardViewHolder> {

  private final LayoutInflater mInflater;
  private List<WeatherDataAggregatePOJO4RDB> weatherDataList;

  public WeatherCardListAdapter(Context context) {
    mInflater = LayoutInflater.from(context);
  }

  class WeatherCardViewHolder extends RecyclerView.ViewHolder {

    WeatherCardBinding weatherCardBinding;
    WeatherDataAggregatePOJO4RDB weatherData;

    private WeatherCardViewHolder(WeatherCardBinding b) {
      super(b.getRoot());
      weatherCardBinding = b;
    }
  }

  @NonNull
  @Override
  public WeatherCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    return new WeatherCardViewHolder(WeatherCardBinding.inflate(mInflater, parent, false));
  }

  @SuppressLint("SetTextI18n") @Override
  public void onBindViewHolder(@NonNull WeatherCardViewHolder holder, int position) {
    if (weatherDataList != null) {
      holder.weatherData = weatherDataList.get(position);
      if (holder.weatherData.weather.size() != 0) {
        WeatherDataWeatherInstancePOJO4RDB currentWeather = holder.weatherData.weather.get(0);
        String url = "https://openweathermap.org/img/wn/" + currentWeather.weatherInstanceIconId + "@4x.png";
        Log.d(Constants.TAG, url + " :: Picasso for " + holder.weatherData.aux.gName);
        Picasso.get().load(url).into(holder.weatherCardBinding.weatherIcon);
        holder.weatherCardBinding.weatherMain.setText(currentWeather.main);
        holder.weatherCardBinding.weatherDescription.setText(currentWeather.description);
      }
      holder.weatherCardBinding.cityName.setText(holder.weatherData.aux.gName + ", " + CountryCode.getByCode(holder.weatherData.aux.gCountry).getName());
      holder.weatherCardBinding.temp.setText("Current: " + Math.round(holder.weatherData.aux.main.temp - 273) + "°C");
      holder.weatherCardBinding.feelsLike.setText("Feels like: " + Math.round(holder.weatherData.aux.main.feels_like - 273) + "°C");
      holder.weatherCardBinding.pressure.setText("Pressure: " + Math.round(holder.weatherData.aux.main.pressure) + " hPa");
      holder.weatherCardBinding.humidity.setText("Humidity: " + Math.round(holder.weatherData.aux.main.humidity) + "%");
      holder.weatherCardBinding.updateTimestamp.setText("Last updated at: " + new java.util.Date(holder.weatherData.aux.dt * 1000L));
    }
  }

  void set(List<WeatherDataAggregatePOJO4RDB> entities) {
    weatherDataList = entities;
    notifyDataSetChanged();
  }

  @Override
  public int getItemCount() {
    return weatherDataList != null ? weatherDataList.size() : 0;
  }

  @Override public long getItemId(int position) {
    return weatherDataList.get(position).aux.auxId;
  }

}