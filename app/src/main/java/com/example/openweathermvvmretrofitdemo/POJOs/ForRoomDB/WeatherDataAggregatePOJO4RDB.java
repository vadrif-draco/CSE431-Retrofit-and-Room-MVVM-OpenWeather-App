package com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class WeatherDataAggregatePOJO4RDB {

  public @Embedded WeatherDataAuxPOJO4RDB aux;

  public @Relation(
      parentColumn = "auxId",
      entityColumn = "weatherInstanceId",
      associateBy = @Junction(WeatherDataPOJO4RDB.class)
  )
  List<WeatherDataWeatherInstancePOJO4RDB> weather;

}
