package com.minus21.mainapp.ui.main;

import android.util.Log;

public class WeatherInfo {
    private String icon = null;
    private String description = null;
    private String location = null;
    private String main = null;
    private int temp = 0;
    private int temp_feel_like = 0;

    public WeatherInfo(String main, String icon, String description, String location, int temp, int temp_feel_like) {
        this.main = main.replace("\"","");
        this.icon = String.format("http://openweathermap.org/img/wn/%s@2x.png",icon).replace("\"","");
        this.description = description.replace("\"","");
        this.location = location.replace("\"","");
        this.temp = temp;
        this.temp_feel_like = temp_feel_like;
        Log.d("weather", main);
    }

    public void printAll(){
        Log.d("WeatherInfo", icon);
        Log.d("WeatherInfo", description);
        Log.d("WeatherInfo", location);
        Log.d("WeatherInfo", main);
        Log.d("WeatherInfo", String.valueOf(temp));
        Log.d("WeatherInfo", String.valueOf(temp_feel_like));
    }
}
