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
import com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB.WeatherDataPOJO4RDB;
import com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB.WeatherDataWeatherInstancePOJO4RDB;

import java.util.List;

@Dao
public abstract class MyDataAccessObject {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  public abstract void insertGeocodingData(GeocodingDataPOJO4RDB entity);

  @Query("SELECT * from geocoding")
  public abstract LiveData<List<GeocodingDataPOJO4RDB>> getAllGeocodingData();

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  public abstract void insertWeatherDataAggregate(WeatherDataPOJO4RDB entity);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  public abstract void insertWeatherDataAux(WeatherDataAuxPOJO4RDB entity);

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  public abstract void insertWeatherDataInstance(WeatherDataWeatherInstancePOJO4RDB entity);

  @Transaction
  @Query("SELECT * from weather_aux")
  public abstract LiveData<List<WeatherDataAggregatePOJO4RDB>> getAllWeatherData();

  @Query("DELETE from weather_aux where auxId=:w_aux_id")
  public abstract void deleteWeatherData(long w_aux_id);

  @Query("SELECT * from geocoding where aux_id=:auxId")
  public abstract LiveData<List<GeocodingDataPOJO4RDB>> getWeatherGeocodingDataByWeatherAuxId(long auxId);

  @Query("SELECT * from geocoding where name=:cityName and country=:countryCode")
  public abstract LiveData<List<GeocodingDataPOJO4RDB>> getGeocodingDataByCityCountry(String cityName, String countryCode);

  @Transaction
  public void insertGeocodingAndWeatherData(GeocodingDataPOJO4RDB g, WeatherDataAggregatePOJO4RDB wagg) {
    wagg.aux.gCountry = g.country;
    wagg.aux.gName = g.name;
    g.aux_id = wagg.aux.auxId;
    insertGeocodingData(g);
    insertWeatherDataAggregate(wagg);
  }

  @Transaction
  public void insertWeatherDataAggregate(WeatherDataAggregatePOJO4RDB wagg) {
    insertWeatherDataAux(wagg.aux);
    for (WeatherDataWeatherInstancePOJO4RDB instance : wagg.weather) {
      insertWeatherDataInstance(instance);
      WeatherDataPOJO4RDB w = new WeatherDataPOJO4RDB();
      w.auxId = wagg.aux.auxId;
      w.weatherInstanceId = instance.weatherInstanceId;
      insertWeatherDataAggregate(w);
    }
  }

}
