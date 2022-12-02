package com.example.openweathermvvmretrofitdemo.APICallbacks;

import static com.example.openweathermvvmretrofitdemo.MyRepository.getRepository;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.openweathermvvmretrofitdemo.POJOs.Converters.GeocodingDataPOJOConverter;
import com.example.openweathermvvmretrofitdemo.POJOs.ForRetrofit.GeocodingDataPOJO4RF;
import com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB.GeocodingDataPOJO4RDB;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GeocodingDataInsertCallback implements Callback<List<GeocodingDataPOJO4RF>> {

  private final CallbackResponseFunction cbk_r_fun;

  public GeocodingDataInsertCallback(@Nullable CallbackResponseFunction cbk_r_fun) {
    this.cbk_r_fun = cbk_r_fun;
  }

  @Override public void onResponse(@NonNull Call<List<GeocodingDataPOJO4RF>> call, Response<List<GeocodingDataPOJO4RF>> response) {

    if (!response.isSuccessful()) {

      if (cbk_r_fun != null) cbk_r_fun.respond("Unsuccessful request (geocode)");

    } else {

      List<GeocodingDataPOJO4RF> geocodingDataList = response.body();

      if (geocodingDataList == null || geocodingDataList.size() == 0) {

        if (cbk_r_fun != null) cbk_r_fun.respond("City not found");

      } else {

        // Just want the first one
        GeocodingDataPOJO4RF cityGeocodingDataRF = geocodingDataList.get(0);

        GeocodingDataPOJO4RDB cityGeocodingDataRDB = GeocodingDataPOJOConverter.toRoomPOJO(cityGeocodingDataRF);

        try {
          LiveData<List<GeocodingDataPOJO4RDB>> g = getRepository().getGeocodingDataByCityCountry(
              cityGeocodingDataRDB.name,
              cityGeocodingDataRDB.country
          );
          Observer<List<GeocodingDataPOJO4RDB>> o = new Observer<List<GeocodingDataPOJO4RDB>>() {
            @Override public void onChanged(List<GeocodingDataPOJO4RDB> geocodes) {
              if (geocodes.size() > 0) {
                if (cbk_r_fun != null)
                  cbk_r_fun.respond(cityGeocodingDataRF.name + " has already been added");
              } else {
                try {
                  // If the geocodes list is empty, that means this is a new geocode
                  // In this case, pass it on to the weather retrieval and insertion process
                  getRepository().getAndInsertWeatherByCityGeocode(cityGeocodingDataRF, cbk_r_fun);
                } catch (Exception e) {
                  e.printStackTrace();
                }
              }
              g.removeObserver(this);
            }
          };
          g.observeForever(o);
        } catch (Exception e) {
          e.printStackTrace();
        }

      }

    }

  }

  @Override public void onFailure(@NonNull Call<List<GeocodingDataPOJO4RF>> call, @NonNull Throwable t) {

    if (cbk_r_fun != null) cbk_r_fun.respond("Failure: " + t.getMessage());

  }
}
