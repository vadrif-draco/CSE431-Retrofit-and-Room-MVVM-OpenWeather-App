package com.example.openweathermvvmretrofitdemo.DAOAsyncTasks;

import com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB.GeocodingDataPOJO4RDB;
import com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB.WeatherDataAggregatePOJO4RDB;

public class GWAsyncTaskParams {
  public GeocodingDataPOJO4RDB geocodingData;
  public WeatherDataAggregatePOJO4RDB weatherDataAggregate;

  public GWAsyncTaskParams(GeocodingDataPOJO4RDB g, WeatherDataAggregatePOJO4RDB w) {
    this.geocodingData = g;
    this.weatherDataAggregate = w;
  }
}
