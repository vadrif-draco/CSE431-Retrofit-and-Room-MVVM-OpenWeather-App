package com.example.openweathermvvmretrofitdemo.APICallbacks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.openweathermvvmretrofitdemo.MyRepository;
import com.example.openweathermvvmretrofitdemo.POJOs.Converters.WeatherDataPOJOConverter;
import com.example.openweathermvvmretrofitdemo.POJOs.ForRetrofit.WeatherDataPOJO4RF;
import com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB.GeocodingDataPOJO4RDB;
import com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB.WeatherDataAggregatePOJO4RDB;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherDataUpdateCallback implements Callback<WeatherDataPOJO4RF> {

  private final CallbackResponseFunction cbk_r_fun;
  private final long previous_dt;

  public WeatherDataUpdateCallback(@Nullable CallbackResponseFunction cbk_r_fun, long previous_dt) {
    this.cbk_r_fun = cbk_r_fun;
    this.previous_dt = previous_dt;
  }

  @Override public void onResponse(@NonNull Call<WeatherDataPOJO4RF> call, @NonNull Response<WeatherDataPOJO4RF> response) {

    if (!response.isSuccessful()) {

      if (cbk_r_fun != null) cbk_r_fun.respond("Unsuccessful request (weather)");

    } else {

      WeatherDataPOJO4RF weatherData = response.body();

      if (weatherData == null) {

        if (cbk_r_fun != null) cbk_r_fun.respond("Update failed; Weather data not found");

      } else {

        WeatherDataAggregatePOJO4RDB w = WeatherDataPOJOConverter.toRoomPOJO(weatherData);
        try {
          LiveData<List<GeocodingDataPOJO4RDB>> g = MyRepository.getRepository().getWeatherGeocodingDataByWeatherAuxId(w.aux.auxId);
          Observer<List<GeocodingDataPOJO4RDB>> o = new Observer<List<GeocodingDataPOJO4RDB>>() {
            @Override public void onChanged(List<GeocodingDataPOJO4RDB> geocodingData) {
              try {
                if (w.aux.dt > previous_dt) {
                  MyRepository.getRepository().insertGeocodingAndWeatherData(geocodingData.get(0), w);
                  if (cbk_r_fun != null)
                    cbk_r_fun.respond("Weather information has been updated for " + geocodingData.get(0).name);
                } else {
                  if (cbk_r_fun != null)
                    cbk_r_fun.respond("No recent updates for " + geocodingData.get(0).name + ", try again later");
                }
              } catch (Exception e) {
                e.printStackTrace();
                if (cbk_r_fun != null)
                  cbk_r_fun.respond("An internal error has occurred, please remove this city and add it again");
              }
              g.removeObserver(this);
            }
          };
          g.observeForever(o);
        } catch (Exception e) {
          e.printStackTrace();
        }

      }

    }

  }

  @Override public void onFailure(@NonNull Call<WeatherDataPOJO4RF> call, @NonNull Throwable t) {

    if (cbk_r_fun != null) cbk_r_fun.respond("Failure: " + t);

  }

}
