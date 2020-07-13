package com.minus21.mainapp.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


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

    public static PlaceholderFragment3 newInstance(int index) {
        PlaceholderFragment3 fragment = new PlaceholderFragment3();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main3, container, false);
        getWeather(0,0);
        return root;
    }

    private interface ApiService {
        String BASE_URL = "https://api.openweathermap.org/data/2.5/";
        String serviceKey = "SECRET";
        @GET("weather")
        Call<JsonObject> getWeather ( @Query("q") String city,
                                      @Query("appid") String appKey);
    }

    private void getWeather(double latitude, double longitude){
        Retrofit retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create())
                .baseUrl(ApiService.BASE_URL)
                .build();
        ApiService apiService = retrofit.create(ApiService.class);
        Call<JsonObject> call = apiService.getWeather("seoul", ApiService.serviceKey);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    JsonObject object = response.body();
                    Log.d("weather",object.toString());
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