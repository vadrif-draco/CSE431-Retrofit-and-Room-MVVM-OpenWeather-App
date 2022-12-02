package com.example.openweathermvvmretrofitdemo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.openweathermvvmretrofitdemo.APICallbacks.CallbackResponseFunction;
import com.example.openweathermvvmretrofitdemo.APICallbacks.GeocodingDataInsertCallback;
import com.example.openweathermvvmretrofitdemo.APICallbacks.WeatherDataInsertCallback;
import com.example.openweathermvvmretrofitdemo.APICallbacks.WeatherDataUpdateCallback;
import com.example.openweathermvvmretrofitdemo.DAOAsyncTasks.GWAsyncTaskParams;
import com.example.openweathermvvmretrofitdemo.DAOAsyncTasks.MyDataDeleteAsyncTask;
import com.example.openweathermvvmretrofitdemo.DAOAsyncTasks.MyDataInsertAsyncTask;
import com.example.openweathermvvmretrofitdemo.JSONAPIs.GeocodingDataAPI;
import com.example.openweathermvvmretrofitdemo.JSONAPIs.WeatherDataAPI;
import com.example.openweathermvvmretrofitdemo.POJOs.ForRetrofit.GeocodingDataPOJO4RF;
import com.example.openweathermvvmretrofitdemo.POJOs.ForRetrofit.WeatherDataPOJO4RF;
import com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB.GeocodingDataPOJO4RDB;
import com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB.WeatherDataAggregatePOJO4RDB;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MyRepository {

  private final MyDataAccessObject myDAO;
  private final LiveData<List<WeatherDataAggregatePOJO4RDB>> weatherData;

  private final WeatherDataAPI weatherDataJsonAPI;
  private final GeocodingDataAPI geocodingDataJsonAPI;

  private static volatile MyRepository SINGLETON_INSTANCE;

  public static MyRepository getRepository(final Application application) {
    if (SINGLETON_INSTANCE == null) {
      synchronized (MyRepository.class) {
        if (SINGLETON_INSTANCE == null) {
          SINGLETON_INSTANCE = new MyRepository(application);
        }
      }
    }
    return SINGLETON_INSTANCE;
  }

  public static MyRepository getRepository() throws Exception {
    if (SINGLETON_INSTANCE == null) {
      throw new Exception("MyRepository is not yet initialized");
    }
    return SINGLETON_INSTANCE;
  }

  private MyRepository(Application application) {

    MyRoomDatabase db = MyRoomDatabase.getDatabase(application);

    myDAO = db.myDAO();
    weatherData = myDAO.getAllWeatherData();

    Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
    weatherDataJsonAPI = retrofit.create(WeatherDataAPI.class);
    geocodingDataJsonAPI = retrofit.create(GeocodingDataAPI.class);

  }

  public LiveData<List<WeatherDataAggregatePOJO4RDB>> getWeatherData() {
    return weatherData;
  }

  public void insertGeocodingAndWeatherData(GeocodingDataPOJO4RDB g, WeatherDataAggregatePOJO4RDB w) {
    new MyDataInsertAsyncTask(myDAO).execute(new GWAsyncTaskParams(g, w));
  }

  public LiveData<List<GeocodingDataPOJO4RDB>> getWeatherGeocodingDataByWeatherAuxId(long auxId) {
    return myDAO.getWeatherGeocodingDataByWeatherAuxId(auxId);
  }

  public LiveData<List<GeocodingDataPOJO4RDB>> getGeocodingDataByCityCountry(String cityName, String countryCode) {
    return myDAO.getGeocodingDataByCityCountry(cityName, countryCode);
  }

  public void deleteWeatherData(WeatherDataAggregatePOJO4RDB w) {
    new MyDataDeleteAsyncTask(myDAO).execute(w);
  }

  public void searchForAndInsertCity(String searchPrompt, CallbackResponseFunction cbk_r_fun) {
    Call<List<GeocodingDataPOJO4RF>> call = geocodingDataJsonAPI.getData(searchPrompt, 1, Constants.API_KEY);
    call.enqueue(new GeocodingDataInsertCallback(cbk_r_fun));
  }

  public void getAndInsertWeatherByCityGeocode(GeocodingDataPOJO4RF cityGeocode, CallbackResponseFunction cbk_r_fun) {
    Call<WeatherDataPOJO4RF> call = weatherDataJsonAPI.getData(cityGeocode.lat, cityGeocode.lon, Constants.API_KEY);
    call.enqueue(new WeatherDataInsertCallback(cbk_r_fun, cityGeocode));
  }

  public void updateWeather(float lat, float lon, long prev_dt, CallbackResponseFunction cbk_r_fun) {
    Call<WeatherDataPOJO4RF> call = weatherDataJsonAPI.getData(lat, lon, Constants.API_KEY);
    call.enqueue(new WeatherDataUpdateCallback(cbk_r_fun, prev_dt));
  }

}
