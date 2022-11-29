package com.example.openweathermvvmretrofitdemo.JSONAPIs;

import com.example.openweathermvvmretrofitdemo.POJOs.ForRetrofit.GeocodingDataPOJO4RF;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeocodingDataAPI {

  @GET("geo/1.0/direct")
  Call<List<GeocodingDataPOJO4RF>> getData(
      @Query("q") String cityStateCountryQuery,
      @Query("limit") int limit,
      @Query("appid") String api_key
  );

}
