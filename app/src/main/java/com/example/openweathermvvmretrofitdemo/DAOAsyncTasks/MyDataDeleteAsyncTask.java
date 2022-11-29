package com.example.openweathermvvmretrofitdemo.DAOAsyncTasks;

import android.os.AsyncTask;

import com.example.openweathermvvmretrofitdemo.MyDataAccessObject;
import com.example.openweathermvvmretrofitdemo.POJOs.ForRoomDB.WeatherDataAggregatePOJO4RDB;

public class MyDataDeleteAsyncTask extends AsyncTask<WeatherDataAggregatePOJO4RDB, Void, Void> {

  private final MyDataAccessObject mAsyncTaskDAO;

  public MyDataDeleteAsyncTask(MyDataAccessObject DAO) {
    mAsyncTaskDAO = DAO;
  }

  @Override
  protected Void doInBackground(final WeatherDataAggregatePOJO4RDB... params) {
    mAsyncTaskDAO.deleteWeatherData(params[0].aux.auxId);
    return null;
  }

}
