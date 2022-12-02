package com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;

@Entity(
    tableName = "geocoding",
    primaryKeys = {"name", "country"},
    foreignKeys = {@ForeignKey(
        deferred = true,
        parentColumns = "auxId",
        childColumns = "aux_id",
        entity = WeatherDataAuxPOJO4RDB.class,
        onDelete = ForeignKey.CASCADE
    )
    },
    indices = {
        @Index("aux_id")
    }
)
public class GeocodingDataPOJO4RDB {

  // Foreign key for weather data associated with this city geocoding data
  public long aux_id;

  // Name of the found location
  public @NonNull String name = "";

  // Won't use the local_names member since it will be unnecessarily complex (Like Weather stuff)

  // Geographical coordinates of the found location (latitude)
  public float lat;

  // Geographical coordinates of the found location (longitude)
  public float lon;

  // Country(-code) of the found location
  public @NonNull String country = "";

  // State of the found location (where available)
  public String state;

}
