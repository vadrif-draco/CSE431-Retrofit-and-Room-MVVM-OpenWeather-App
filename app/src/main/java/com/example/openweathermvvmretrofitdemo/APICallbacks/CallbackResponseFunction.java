package com.example.openweathermvvmretrofitdemo.APICallbacks;

@FunctionalInterface public interface CallbackResponseFunction {
  void respond(String response_text);
}