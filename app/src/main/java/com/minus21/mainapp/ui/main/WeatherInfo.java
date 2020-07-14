package com.minus21.mainapp.ui.main;

import android.util.Log;

import java.util.ArrayList;

public class WeatherInfo {
    public long timezone_offset;
    public Weather current;
    public ArrayList<Weather> hourly;
    public ArrayList<Weather> daily;


    public WeatherInfo(){
        this.current = new Weather();
        this.hourly = new ArrayList<>();
        this.daily = new ArrayList<>();
    }

    public void setCurrentWeather(long dt, long sunrise, long sunset,
                                  double temp, double feels_like, int humidity,
                                  int cloud, double wind_speed,
                                  int id, String main, String desc, String icon) {
        this.current.setWeather(dt, sunrise, sunset, temp, feels_like, humidity,
                cloud, wind_speed, id, main, desc, icon);
    }

    public void addHourly(Weather w){
        hourly.add(w);
    }

    public void addDaily(Weather w){
        daily.add(w);
    }

    public void logAll(){
        Log.d("weather", String.valueOf(timezone_offset));
        this.current.logAll();
        for (Weather w: hourly){
            w.logAll();
        }
        for (Weather w:daily){
            w.logAll();
        }
    }

}
