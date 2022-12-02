package com.example.openweathermvvmretrofitdemo;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB.GeocodingDataPOJO4RDB;
import com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB.WeatherDataAggregatePOJO4RDB;
import com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB.WeatherDataAuxPOJO4RDB;
import com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB.WeatherDataWeatherInstancePOJO4RDB;

import java.util.List;

@Dao
public abstract class MyDataAccessObject {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  public abstract void insertGeocodingData(GeocodingDataPOJO4RDB entity);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  protected abstract void insertWeatherDataAux(WeatherDataAuxPOJO4RDB entity);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  protected abstract void insertWeatherDataInstance(WeatherDataWeatherInstancePOJO4RDB entity);

  @Transaction
  public void insertGeocodingAndWeatherData(GeocodingDataPOJO4RDB g, WeatherDataAggregatePOJO4RDB wagg) {

    // One-to-one mapping of the primary keys of g to wagg and the primary key of wagg to g, respectively
    wagg.aux.gCountry = g.country;
    wagg.aux.gName = g.name;
    g.aux_id = wagg.aux.auxId;
    insertGeocodingData(g);

    insertWeatherDataAux(wagg.aux);
    // One-to-many mapping between wagg.aux and wagg.weather
    for (WeatherDataWeatherInstancePOJO4RDB instance : wagg.weather) {
      instance.associatedAuxId = wagg.aux.auxId;
      insertWeatherDataInstance(instance);
    }

  }

  @Transaction
  @Query("SELECT * from weather_aux")
  public abstract LiveData<List<WeatherDataAggregatePOJO4RDB>> getAllWeatherData();

  @Query("SELECT * from geocoding where aux_id=:auxId")
  public abstract LiveData<List<GeocodingDataPOJO4RDB>> getWeatherGeocodingDataByWeatherAuxId(long auxId);

  @Query("SELECT * from geocoding where name=:cityName and country=:countryCode")
  public abstract LiveData<List<GeocodingDataPOJO4RDB>> getGeocodingDataByCityCountry(String cityName, String countryCode);

  @Query("DELETE from weather_aux where auxId=:w_aux_id")
  public abstract void deleteWeatherData(long w_aux_id);

}
