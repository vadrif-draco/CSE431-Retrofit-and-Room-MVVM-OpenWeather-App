package com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB;

import androidx.room.Entity;

@Entity(primaryKeys = {"auxId", "weatherInstanceId"})
public class WeatherDataPOJO4RDB {
  public long auxId;
  public long weatherInstanceId;
}
