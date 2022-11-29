package com.example.openweathermvvmretrofitdemo.POJOs.Converters;

import com.example.openweathermvvmretrofitdemo.POJOs.ForRetrofit.GeocodingDataPOJO4RF;
import com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB.GeocodingDataPOJO4RDB;

public final class GeocodingDataPOJOConverter {

  public static GeocodingDataPOJO4RDB toRoomPOJO(GeocodingDataPOJO4RF retrofitPOJO) {

    GeocodingDataPOJO4RDB roomPOJO =
        new GeocodingDataPOJO4RDB();

    roomPOJO.name = retrofitPOJO.name;
    roomPOJO.lat = retrofitPOJO.lat;
    roomPOJO.lon = retrofitPOJO.lon;
    roomPOJO.country = retrofitPOJO.country;
    roomPOJO.state = retrofitPOJO.state;

    return roomPOJO;

  }

  public static GeocodingDataPOJO4RF toRetrofitPOJO(GeocodingDataPOJO4RDB roomPOJO) {

    GeocodingDataPOJO4RF retrofitPOJO =
        new GeocodingDataPOJO4RF();

    retrofitPOJO.name = roomPOJO.name;
    retrofitPOJO.lat = roomPOJO.lat;
    retrofitPOJO.lon = roomPOJO.lon;
    retrofitPOJO.country = roomPOJO.country;
    retrofitPOJO.state = roomPOJO.state;

    return retrofitPOJO;

  }

}
