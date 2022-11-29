package com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;

@Entity(
    tableName = "weather_aux",
    primaryKeys = "auxId",
    foreignKeys = {
        @ForeignKey(entity = GeocodingDataPOJO4RDB.class,
                    onDelete = ForeignKey.CASCADE,
                    parentColumns = {"name", "country"},
                    childColumns = {"gName", "gCountry"},
                    deferred = true)
    }
)
public class WeatherDataAuxPOJO4RDB {

  public static class Coordinates {

    public float lon;
    public float lat;

  }

  public static class Main {

    public float temp;
    public float feels_like;
    public float temp_min;
    public float temp_max;
    public float pressure;
    public float humidity;

  }

  public static class Wind {

    public float speed;
    public int deg;

  }

  public static class Sys {

    public int type;
    public @ColumnInfo(name = "sys_id") long id;
    public @NonNull String country = "";
    public int sunrise;
    public int sunset;

  }

  // Foreign key (composite) for geocoding data associated with this weather data
  public String gName, gCountry;

  public @Embedded Coordinates coord;
  public @NonNull String base = "";
  public @Embedded Main main;
  public long visibility;
  public @Embedded Wind wind;
  public long dt;
  public @Embedded Sys sys;
  public long timezone;
  public long auxId;
  public @NonNull String name = "";
  public long cod;

}
