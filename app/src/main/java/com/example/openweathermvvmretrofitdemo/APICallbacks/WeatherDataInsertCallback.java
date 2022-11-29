package com.example.openweathermvvmretrofitdemo.APICallbacks;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.openweathermvvmretrofitdemo.Constants;
import com.example.openweathermvvmretrofitdemo.MyRepository;
import com.example.openweathermvvmretrofitdemo.POJOs.Converters.GeocodingDataPOJOConverter;
import com.example.openweathermvvmretrofitdemo.POJOs.Converters.WeatherDataPOJOConverter;
import com.example.openweathermvvmretrofitdemo.POJOs.ForRetrofit.GeocodingDataPOJO4RF;
import com.example.openweathermvvmretrofitdemo.POJOs.ForRetrofit.WeatherDataPOJO4RF;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WeatherDataInsertCallback implements Callback<WeatherDataPOJO4RF> {

  private final CallbackResponseFunction cbk_r_fun;
  private final GeocodingDataPOJO4RF cityGeocodingData;

  public WeatherDataInsertCallback(@Nullable CallbackResponseFunction cbk_r_fun, GeocodingDataPOJO4RF cityGeocodingData) {
    this.cbk_r_fun = cbk_r_fun;
    this.cityGeocodingData = cityGeocodingData;
  }

  @Override public void onResponse(@NonNull Call<WeatherDataPOJO4RF> call, Response<WeatherDataPOJO4RF> response) {

    if (!response.isSuccessful()) {

      if (cbk_r_fun != null) cbk_r_fun.respond("Unsuccessful request (weather)");

    } else {

      WeatherDataPOJO4RF weatherData = response.body();

      if (weatherData == null) {

        if (cbk_r_fun != null) cbk_r_fun.respond("Weather data not found");

      } else {

        Log.d(Constants.TAG, "cityGeocodingData.name: " + cityGeocodingData.name);
        Log.d(Constants.TAG, "cityGeocodingData.lat: " + cityGeocodingData.lat);
        Log.d(Constants.TAG, "cityGeocodingData.lon: " + cityGeocodingData.lon);
        Log.d(Constants.TAG, "cityGeocodingData.country: " + cityGeocodingData.country);
        Log.d(Constants.TAG, "cityGeocodingData.state: " + cityGeocodingData.state);

        Log.d(Constants.TAG, "weatherData.coord.lat: " + weatherData.coord.lat);
        Log.d(Constants.TAG, "weatherData.coord.lon: " + weatherData.coord.lon);
        Log.d(Constants.TAG, "weatherData.weather.get(0).description: " + weatherData.weather.get(0).description);
        Log.d(Constants.TAG, "weatherData.weather.get(0).main: " + weatherData.weather.get(0).main);
        Log.d(Constants.TAG, "weatherData.weather.get(0).id: " + weatherData.weather.get(0).id);
        Log.d(Constants.TAG, "weatherData.weather.get(0).icon: " + weatherData.weather.get(0).icon);
        Log.d(Constants.TAG, "weatherData.base: " + weatherData.base);
        Log.d(Constants.TAG, "weatherData.main.feels_like: " + weatherData.main.feels_like);
        Log.d(Constants.TAG, "weatherData.main.humidity: " + weatherData.main.humidity);
        Log.d(Constants.TAG, "weatherData.main.pressure: " + weatherData.main.pressure);
        Log.d(Constants.TAG, "weatherData.main.temp: " + weatherData.main.temp);
        Log.d(Constants.TAG, "weatherData.main.temp_max: " + weatherData.main.temp_max);
        Log.d(Constants.TAG, "weatherData.main.temp_min: " + weatherData.main.temp_min);
        Log.d(Constants.TAG, "weatherData.visibility: " + weatherData.visibility);
        Log.d(Constants.TAG, "weatherData.wind.deg: " + weatherData.wind.deg);
        Log.d(Constants.TAG, "weatherData.wind.speed: " + weatherData.wind.speed);
        Log.d(Constants.TAG, "weatherData.clouds: " + weatherData.clouds);
        Log.d(Constants.TAG, "weatherData.dt: " + weatherData.dt);
        Log.d(Constants.TAG, "weatherData.sys.country: " + weatherData.sys.country);
        Log.d(Constants.TAG, "weatherData.sys.id: " + weatherData.sys.id);
        Log.d(Constants.TAG, "weatherData.sys.sunrise: " + weatherData.sys.sunrise);
        Log.d(Constants.TAG, "weatherData.sys.sunset: " + weatherData.sys.sunset);
        Log.d(Constants.TAG, "weatherData.sys.type: " + weatherData.sys.type);
        Log.d(Constants.TAG, "weatherData.timezone: " + weatherData.timezone);
        Log.d(Constants.TAG, "weatherData.id: " + weatherData.id);
        Log.d(Constants.TAG, "weatherData.name: " + weatherData.name);
        Log.d(Constants.TAG, "weatherData.cod: " + weatherData.cod);

        try {
          MyRepository.getRepository().insertGeocodingAndWeatherData(
              GeocodingDataPOJOConverter.toRoomPOJO(cityGeocodingData),
              WeatherDataPOJOConverter.toRoomPOJO(weatherData)
          );
        } catch (Exception ignored) {
        }

        if (cbk_r_fun != null) cbk_r_fun.respond(cityGeocodingData.name + " has been added");

      }

    }

  }

  @Override public void onFailure(@NonNull Call<WeatherDataPOJO4RF> call, @NonNull Throwable t) {

    if (cbk_r_fun != null) cbk_r_fun.respond("Failure: " + t.getMessage());

  }
}
