package com.example.openweathermvvmretrofitdemo.POJOs.ForRetrofit;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;

public class WeatherDataPOJO4RF {

  public static class Coordinates {

    public float lon;
    public float lat;

  }

  public static class WeatherInstance {

    public long id;
    public String main;
    public String description;
    public String icon;

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
    public long id;
    public @NonNull String country = "";
    public int sunrise;
    public int sunset;

  }

  public Coordinates coord;
  public ArrayList<WeatherInstance> weather;
  public String base;
  public Main main;
  public long visibility;
  public Wind wind;
  public HashMap<String, Integer> clouds;
  public long dt;
  public Sys sys;
  public long timezone;
  public long id;
  public String name;
  public long cod;

}
