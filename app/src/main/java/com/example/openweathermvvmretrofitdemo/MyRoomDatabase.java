package com.example.openweathermvvmretrofitdemo;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB.GeocodingDataPOJO4RDB;
import com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB.WeatherDataAuxPOJO4RDB;
import com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB.WeatherDataWeatherInstancePOJO4RDB;

@Database(
    entities = {
        GeocodingDataPOJO4RDB.class,
        WeatherDataAuxPOJO4RDB.class,
        WeatherDataWeatherInstancePOJO4RDB.class
    },
    version = 31,
    exportSchema = false
)
public abstract class MyRoomDatabase extends RoomDatabase {

  public abstract MyDataAccessObject myDAO();

  private static volatile MyRoomDatabase SINGLETON_INSTANCE;

  public static MyRoomDatabase getDatabase(final Context context) {
    if (SINGLETON_INSTANCE == null) {
      synchronized (MyRoomDatabase.class) {
        if (SINGLETON_INSTANCE == null) {
          SINGLETON_INSTANCE =
              Room.databaseBuilder(context.getApplicationContext(), MyRoomDatabase.class, "myDB")
                  .fallbackToDestructiveMigration()
                  .build();
        }
      }
    }
    return SINGLETON_INSTANCE;
  }
}
