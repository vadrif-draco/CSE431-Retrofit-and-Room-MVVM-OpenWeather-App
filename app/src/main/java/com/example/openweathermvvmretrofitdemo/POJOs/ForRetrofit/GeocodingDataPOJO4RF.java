package com.example.openweathermvvmretrofitdemo.POJOs.ForRetrofit;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.HashMap;

public class GeocodingDataPOJO4RF {

  // Name of the found location
  public @NonNull String name = "";

  public @Nullable HashMap<String, String> local_names;

  // Geographical coordinates of the found location (latitude)
  public float lat;

  // Geographical coordinates of the found location (longitude)
  public float lon;

  // Country(-code) of the found location
  public @NonNull String country = "";

  // State of the found location (where available)
  public String state;

}
