package com.example.openweathermvvmretrofitdemo.JSONAPIs;

import com.example.openweathermvvmretrofitdemo.POJOs.ForRetrofit.WeatherDataPOJO4RF;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface WeatherDataAPI {

  @GET("data/2.5/weather")
  Call<WeatherDataPOJO4RF> getData(
      @Query("lat") float latitude,
      @Query("lon") float longitude,
      @Query("appid") String appid
  );

}
