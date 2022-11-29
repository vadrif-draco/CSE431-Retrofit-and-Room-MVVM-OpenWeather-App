package com.example.openweathermvvmretrofitdemo.DAOAsyncTasks;

import android.os.AsyncTask;

import com.example.openweathermvvmretrofitdemo.MyDataAccessObject;

public class MyDataInsertAsyncTask extends AsyncTask<GWAsyncTaskParams, Void, Void> {

  private final MyDataAccessObject mAsyncTaskDAO;

  public MyDataInsertAsyncTask(MyDataAccessObject DAO) {
    mAsyncTaskDAO = DAO;
  }

  @Override
  protected Void doInBackground(final GWAsyncTaskParams... params) {
    mAsyncTaskDAO.insertGeocodingAndWeatherData(params[0].geocodingData, params[0].weatherDataAggregate);
    return null;
  }

}
