package com.example.openweathermvvmretrofitdemo;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.openweathermvvmretrofitdemo.APICallbacks.CallbackResponseFunction;
import com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB.WeatherDataAggregatePOJO4RDB;

import java.util.List;

public class MyViewModel extends AndroidViewModel {

  private final MyRepository repository;
  private final LiveData<List<WeatherDataAggregatePOJO4RDB>> weatherData;

  public MyViewModel(Application application) {

    super(application);
    repository = MyRepository.getRepository(application);
    weatherData = repository.getWeatherData();

  }

  public LiveData<List<WeatherDataAggregatePOJO4RDB>> getWeatherData() {
    return weatherData;
  }

  public void deleteWeatherData(WeatherDataAggregatePOJO4RDB w) {
    repository.deleteWeatherData(w);
  }

  public void searchForAndInsertCity(String searchPrompt, CallbackResponseFunction cbk_r_fun) {
    repository.searchForAndInsertCity(searchPrompt, cbk_r_fun);
  }

  public void updateWeather(float lat, float lon, long prev_dt, CallbackResponseFunction cbk_r_fun) {
    repository.updateWeather(lat, lon, prev_dt, cbk_r_fun);
  }

}
