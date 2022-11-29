package com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB;

import androidx.annotation.NonNull;
import androidx.room.Entity;

@Entity(tableName = "weather_instances", primaryKeys = "weatherInstanceId")
public class WeatherDataWeatherInstancePOJO4RDB {

  public long weatherInstanceId;
  public @NonNull String main = "";
  public @NonNull String description = "";
  public @NonNull String icon = "";

}
