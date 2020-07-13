package com.minus21.mainapp.ui.main;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.minus21.mainapp.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment3 extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";
    private FusedLocationProviderClient fusedLocationClient;
    private double latitude = 0, longitude = 0;
    private WeatherInfo mWeatherInfo = null;

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

        /* Get location */
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        getWeather(latitude,longitude);
                    }
                    Log.d("location",String.valueOf(latitude)+" "+String.valueOf(longitude));
                }
            });
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main3, container, false);
        return root;
    }



    private interface ApiService {
        String BASE_URL = "https://api.openweathermap.org/data/2.5/";
        String serviceKey = "SECRET";
        @GET("weather")
        Call<JsonObject> getWeather ( @Query("lat") double lat,
                                      @Query("lon") double lon,
                                      @Query("appid") String appid,
                                      @Query("lang") String lang
        );
    }

    private void getWeather(double latitude, double longitude){
        if(latitude == 0 && longitude == 0) return;
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiService.BASE_URL)
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<JsonObject> call = apiService.getWeather(latitude, longitude, ApiService.serviceKey, "en");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject object = response.body();
                    JsonObject weather = (JsonObject)((JsonArray) object.get("weather")).get(0);
                    JsonObject main = (JsonObject) object.get("main");
                    mWeatherInfo = new WeatherInfo(
                            weather.get("main").toString(),
                            weather.get("icon").toString(),
                            weather.get("description").toString(),
                            object.get("name").toString(),
                            main.get("temp").getAsInt(),
                            main.get("feels_like").getAsInt()
                            );
                    mWeatherInfo.printAll();
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
}