package com.example.openweathermvvmretrofitdemo;

import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.openweathermvvmretrofitdemo.databinding.ActivityMainBinding;
import com.neovisionaries.i18n.CountryCode;

import java.util.List;

public class MainActivity extends AppCompatActivity {

  ActivityMainBinding binding;
  View root;

  MyViewModel viewModel;

  @Override protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());
    setContentView(root = binding.getRoot());

    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
    WeatherCardListAdapter adapter = new WeatherCardListAdapter(this);
    adapter.setHasStableIds(true); // To preserve fancy animations with notifyDataSetChanged
    // Requires that you implement getItemId with unique return, which we can do with weather auxId
    binding.recyclerView.setLayoutManager(layoutManager);
    binding.recyclerView.setAdapter(adapter);

    viewModel = new ViewModelProvider(this).get(MyViewModel.class);
    viewModel.getWeatherData().observe(this, adapter::set);

    // Create and attach swipe helper to recycler view
    ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

      @Override public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
      }

      @Override public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        WeatherCardListAdapter.WeatherCardViewHolder v = (WeatherCardListAdapter.WeatherCardViewHolder) viewHolder;
        if (direction == ItemTouchHelper.LEFT) {
          if ((System.currentTimeMillis() / 1000L - v.weatherData.aux.dt) < Constants.WEATHER_UPDATE_INTERVAL) {
            Toast.makeText(getApplicationContext(), "Current data is already up-to-date", Toast.LENGTH_SHORT).show();
          } else {
            viewModel.updateWeather(
                v.weatherData.aux.coord.lat,
                v.weatherData.aux.coord.lon,
                v.weatherData.aux.dt,
                response_text -> {
                  Log.d(Constants.TAG, response_text);
                  Toast.makeText(getApplicationContext(), response_text, Toast.LENGTH_SHORT).show();
                }
            );
          }
          // It gets swiped into oblivion if we don't run this line
          adapter.notifyItemChanged(viewHolder.getLayoutPosition());
        } else if (direction == ItemTouchHelper.RIGHT) {
          viewModel.deleteWeatherData(v.weatherData);
        }
      }

      @Override public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
          WeatherCardListAdapter.WeatherCardViewHolder v = (WeatherCardListAdapter.WeatherCardViewHolder) viewHolder;
          if (dX > 100) { // Remove
            v.weatherCardBinding.updateConstraintLayout.setVisibility(View.INVISIBLE);
            v.weatherCardBinding.removeConstraintLayout.setVisibility(View.VISIBLE);
          } else if (dX < -100) { // Update
            v.weatherCardBinding.removeConstraintLayout.setVisibility(View.INVISIBLE);
            v.weatherCardBinding.updateConstraintLayout.setVisibility(View.VISIBLE);
          }
          if (!isCurrentlyActive) {
            v.weatherCardBinding.removeConstraintLayout.setVisibility(View.INVISIBLE);
            v.weatherCardBinding.updateConstraintLayout.setVisibility(View.INVISIBLE);
          }
          super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        }
      }

    });

    helper.attachToRecyclerView(binding.recyclerView);

    binding.citySearchBtn.setOnClickListener(v -> {

      binding.citySearchBtn.setEnabled(false);
      binding.citySearchET.onEditorAction(EditorInfo.IME_ACTION_DONE);
      binding.citySearchET.setEnabled(false);
      binding.citySearchET.clearFocus();

      // Extract city and country combination from input prompt
      String[] citySearchETPrompt = binding.citySearchET.getText().toString().split(",");
      String searchPrompt = citySearchETPrompt[0].trim();
      if (citySearchETPrompt.length > 1) {
        List<CountryCode> countryCodes = CountryCode.findByName(citySearchETPrompt[1].trim());
        if (countryCodes.size() > 0) {
          searchPrompt += "," + countryCodes.get(0).name();
          Log.d(Constants.TAG, "Search prompt: " + searchPrompt);
        }
      }

      // Send API to fetch exactly 1 corresponding city and add it to RoomDB
      viewModel.searchForAndInsertCity(searchPrompt, response_text -> {

        Log.d(Constants.TAG, response_text);
        Toast.makeText(getApplicationContext(), response_text, Toast.LENGTH_SHORT).show();
        binding.citySearchBtn.setEnabled(true);
        binding.citySearchET.setEnabled(true);
        binding.citySearchET.setText("");

      });

    });

  }

}
