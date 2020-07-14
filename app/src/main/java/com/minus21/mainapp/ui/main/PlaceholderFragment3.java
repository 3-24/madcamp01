package com.minus21.mainapp.ui.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.minus21.mainapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class PlaceholderFragment3 extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude = 0, longitude = 0;
    private WeatherInfo mWeatherInfo = null;
    private View root;
    TextView mainField = null;
    TextView temperatureField = null;

    public static PlaceholderFragment3 newInstance(int index) {
        PlaceholderFragment3 fragment = new PlaceholderFragment3();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /* Get the location and load a weather info on success */
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), location-> {
                        if (location != null){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            getWeather(latitude,longitude);
                        }
                        else Log.d("location", "NULL");
                    });
        }
    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_main3, container, false);

        mainField = root.findViewById(R.id.main);
        temperatureField = root.findViewById(R.id.temp);

        return root;
    }

    /* Interface for retrofit request */
    private interface ApiService {
        String BASE_URL = "https://api.openweathermap.org/data/2.5/";
        @GET("onecall")
        Call<JsonObject> getWeather ( @Query("lat") double lat,
                                      @Query("lon") double lon,
                                      @Query("appid") String appid,
                                      @Query("lang") String lang,
                                      @Query("exclude") String excludes
        );
    }

    /* Update MWEATHERINFO*/
    private void getWeather(double latitude, double longitude){
        String weather_key = getActivity().getResources().getString(R.string.weather_key);
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiService.BASE_URL)
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<JsonObject> call = apiService.getWeather(latitude, longitude, weather_key, "en", "miniutely");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject object = response.body();
                    JsonObject current = (JsonObject) object.get("current");
                    JsonObject current_weather = (JsonObject)((JsonArray) current.get("weather")).get(0);

                    mWeatherInfo = new WeatherInfo();
                    mWeatherInfo.timezone_offset = object.get("timezone_offset").getAsInt();
                    mWeatherInfo.setCurrentWeather(
                            current.get("dt").getAsInt(),
                            current.get("sunrise").getAsInt(),
                            current.get("sunset").getAsInt(),
                            current.get("temp").getAsDouble(),
                            current.get("feels_like").getAsDouble(),
                            current.get("humidity").getAsInt(),
                            current.get("clouds").getAsInt(),
                            current.get("wind_speed").getAsDouble(),
                            current_weather.get("id").getAsInt(),
                            current_weather.get("main").getAsString(),
                            current_weather.get("description").getAsString(),
                            current_weather.get("icon").getAsString()
                            );

                    JsonArray hourly = (JsonArray) object.get("hourly");
                    JsonArray daily = (JsonArray) object.get("daily");

                    for (JsonElement hour : hourly) {
                        JsonObject hourObj = hour.getAsJsonObject();
                        JsonObject hourObj_weather = (JsonObject)((JsonArray) hourObj.get("weather")).get(0);
                        Weather w = new Weather();
                        w.setWeather(
                                hourObj.get("dt").getAsInt(),
                                0,0,
                                hourObj.get("temp").getAsDouble(),
                                hourObj.get("feels_like").getAsDouble(),
                                hourObj.get("humidity").getAsInt(),
                                hourObj.get("clouds").getAsInt(),
                                hourObj.get("wind_speed").getAsDouble(),
                                hourObj_weather.get("id").getAsInt(),
                                hourObj_weather.get("main").getAsString(),
                                hourObj_weather.get("description").getAsString(),
                                hourObj_weather.get("icon").getAsString()
                        );
                        mWeatherInfo.addHourly(w);
                    }

                    for (JsonElement day : daily){
                        JsonObject dayObj = day.getAsJsonObject();
                        JsonObject dayObj_temp = (JsonObject) dayObj.get("temp");
                        JsonObject dayObj_weather = (JsonObject)((JsonArray) dayObj.get("weather")).get(0);
                        Weather w = new Weather();
                        w.setWeather(
                            dayObj.get("dt").getAsInt(),
                            dayObj.get("sunrise").getAsInt(),
                            dayObj.get("sunset").getAsInt(),
                            0,0,
                            dayObj.get("humidity").getAsInt(),
                            dayObj.get("clouds").getAsInt(),
                            dayObj.get("wind_speed").getAsDouble(),
                            dayObj_weather.get("id").getAsInt(),
                            dayObj_weather.get("main").getAsString(),
                            dayObj_weather.get("description").getAsString(),
                            dayObj_weather.get("icon").getAsString()
                        );
                        w.setMaxMinTemp(
                                dayObj_temp.get("max").getAsDouble(),
                                dayObj_temp.get("min").getAsDouble());
                        mWeatherInfo.addDaily(w);
                    }
                    mWeatherInfo.logAll();
                    renderWeather();
                }
                else{
                    Log.d("weather", response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<JsonObject> call, Throwable t){
                Log.d("weather",t.toString());
            }
        });
    }


    private void renderWeather(){
        //mainField.setText(mWeatherInfo.getMain());
        //temperatureField.setText(String.format("%d℃",mWeatherInfo.getTemp()));
    }
}