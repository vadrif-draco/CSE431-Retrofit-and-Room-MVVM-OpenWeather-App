package com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(tableName = "weather_instances",
        primaryKeys = {"associatedAuxId", "weatherInstanceId", "weatherInstanceIconId"},
        foreignKeys = {
            @ForeignKey(entity = WeatherDataAuxPOJO4RDB.class,
                        onDelete = ForeignKey.CASCADE,
                        parentColumns = "auxId",
                        childColumns = "associatedAuxId",
                        deferred = true)
        }
)
public class WeatherDataWeatherInstancePOJO4RDB {

  // Foreign Key for the associated weather aux data
  public long associatedAuxId;

  public long weatherInstanceId;
  public @NonNull String main = "";
  public @NonNull String description = "";
  public @NonNull String weatherInstanceIconId = "03d"; // Default value of "cloudy"

}
