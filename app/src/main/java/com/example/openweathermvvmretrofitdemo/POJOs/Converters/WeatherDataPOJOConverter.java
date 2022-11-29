package com.example.openweathermvvmretrofitdemo.POJOs.Converters;

import com.example.openweathermvvmretrofitdemo.POJOs.ForRetrofit.WeatherDataPOJO4RF;
import com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB.WeatherDataAggregatePOJO4RDB;
import com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB.WeatherDataAuxPOJO4RDB;
import com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB.WeatherDataWeatherInstancePOJO4RDB;

import java.util.ArrayList;

public final class WeatherDataPOJOConverter {

  public static WeatherDataAggregatePOJO4RDB toRoomPOJO(WeatherDataPOJO4RF pojo_in) {
    WeatherDataAggregatePOJO4RDB pojo_out = new WeatherDataAggregatePOJO4RDB();
    pojo_out.aux = new WeatherDataAuxPOJO4RDB();
    pojo_out.aux.auxId = pojo_in.id;
    pojo_out.aux.dt = pojo_in.dt;
    pojo_out.aux.cod = pojo_in.cod;
    pojo_out.aux.name = pojo_in.name;
    pojo_out.aux.base = pojo_in.base;
    pojo_out.aux.timezone = pojo_in.timezone;
    pojo_out.aux.coord = new WeatherDataAuxPOJO4RDB.Coordinates();
    pojo_out.aux.coord.lon = pojo_in.coord.lon;
    pojo_out.aux.coord.lat = pojo_in.coord.lat;
    pojo_out.aux.visibility = pojo_in.visibility;
    pojo_out.aux.main = new WeatherDataAuxPOJO4RDB.Main();
    pojo_out.aux.main.feels_like = pojo_in.main.feels_like;
    pojo_out.aux.main.humidity = pojo_in.main.humidity;
    pojo_out.aux.main.pressure = pojo_in.main.pressure;
    pojo_out.aux.main.temp = pojo_in.main.temp;
    pojo_out.aux.main.temp_max = pojo_in.main.temp_max;
    pojo_out.aux.main.temp_min = pojo_in.main.temp_min;
    pojo_out.aux.sys = new WeatherDataAuxPOJO4RDB.Sys();
    pojo_out.aux.sys.country = pojo_in.sys.country;
    pojo_out.aux.sys.id = pojo_in.sys.id;
    pojo_out.aux.sys.sunrise = pojo_in.sys.sunrise;
    pojo_out.aux.sys.sunset = pojo_in.sys.sunset;
    pojo_out.aux.sys.type = pojo_in.sys.type;
    pojo_out.aux.wind = new WeatherDataAuxPOJO4RDB.Wind();
    pojo_out.aux.wind.deg = pojo_in.wind.deg;
    pojo_out.aux.wind.speed = pojo_in.wind.speed;
    // Won't map clouds

    pojo_out.weather = new ArrayList<>();
    for (WeatherDataPOJO4RF.WeatherInstance weather_in : pojo_in.weather) {
      WeatherDataWeatherInstancePOJO4RDB weather_out = new WeatherDataWeatherInstancePOJO4RDB();
      weather_out.weatherInstanceId = weather_in.id;
      weather_out.description = weather_in.description;
      weather_out.main = weather_in.main;
      weather_out.icon = weather_in.icon;
      pojo_out.weather.add(weather_out);
    }
    return pojo_out;
  }

  public static WeatherDataPOJO4RF toRetrofitPOJO(WeatherDataAggregatePOJO4RDB pojo_in) {
    WeatherDataPOJO4RF pojo_out = new WeatherDataPOJO4RF();
    pojo_out.id = pojo_in.aux.auxId;
    pojo_out.dt = pojo_in.aux.dt;
    pojo_out.cod = pojo_in.aux.cod;
    pojo_out.name = pojo_in.aux.name;
    pojo_out.base = pojo_in.aux.base;
    pojo_out.timezone = pojo_in.aux.timezone;
    pojo_out.coord = new WeatherDataPOJO4RF.Coordinates();
    pojo_out.coord.lon = pojo_in.aux.coord.lon;
    pojo_out.coord.lat = pojo_in.aux.coord.lat;
    pojo_out.visibility = pojo_in.aux.visibility;
    pojo_out.main = new WeatherDataPOJO4RF.Main();
    pojo_out.main.feels_like = pojo_in.aux.main.feels_like;
    pojo_out.main.humidity = pojo_in.aux.main.humidity;
    pojo_out.main.pressure = pojo_in.aux.main.pressure;
    pojo_out.main.temp = pojo_in.aux.main.temp;
    pojo_out.main.temp_max = pojo_in.aux.main.temp_max;
    pojo_out.main.temp_min = pojo_in.aux.main.temp_min;
    pojo_out.sys = new WeatherDataPOJO4RF.Sys();
    pojo_out.sys.country = pojo_in.aux.sys.country;
    pojo_out.sys.id = pojo_in.aux.sys.id;
    pojo_out.sys.sunrise = pojo_in.aux.sys.sunrise;
    pojo_out.sys.sunset = pojo_in.aux.sys.sunset;
    pojo_out.sys.type = pojo_in.aux.sys.type;
    pojo_out.wind = new WeatherDataPOJO4RF.Wind();
    pojo_out.wind.deg = pojo_in.aux.wind.deg;
    pojo_out.wind.speed = pojo_in.aux.wind.speed;
    // Won't map clouds

    pojo_out.weather = new ArrayList<>();
    for (WeatherDataWeatherInstancePOJO4RDB instance_in : pojo_in.weather) {
      WeatherDataPOJO4RF.WeatherInstance instance_out = new WeatherDataPOJO4RF.WeatherInstance();
      instance_out.id = instance_in.weatherInstanceId;
      instance_out.description = instance_in.description;
      instance_out.main = instance_in.main;
      instance_out.icon = instance_in.icon;
      pojo_out.weather.add(instance_out);
    }
    return pojo_out;
  }

}
