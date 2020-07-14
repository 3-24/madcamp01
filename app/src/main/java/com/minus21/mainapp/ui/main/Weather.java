package com.minus21.mainapp.ui.main;

import android.util.Log;

public class Weather {
    public int dt;
    public int sunrise;
    public int sunset;
    public double temp;
    public double feels_like;
    public int humidity;
    public int cloud;
    public double wind_speed;
    public int id;
    public String main;
    public String desc;
    public String icon;
    public double temp_max;
    public double temp_min;

    public void setWeather(int dt, int sunrise, int sunset, double temp,
                           double feels_like, int humidity, int cloud, double wind_speed,
                           int id, String main, String desc, String icon){
        this.dt = dt;
        this.sunrise = sunrise;
        this.sunset = sunset;
        this.temp = temp;
        this.feels_like = feels_like;
        this.humidity = humidity;
        this.cloud = cloud;
        this.wind_speed = wind_speed;
        this.id = id;
        this.main = main;
        this.desc = desc;
        this.icon = icon;
    }

    public void setMaxMinTemp(double temp_max, double temp_min){
        this.temp_max = temp_max;
        this.temp_min = temp_min;
    }

    public void logAll(){
//        Log.d("weather",
//                String.valueOf(this.dt)+" "+
//                        String.valueOf(this.sunrise)+" "+
//                        String.valueOf(this.sunset)+" "+
//                        String.valueOf(this.temp)+" "+
//                        String.valueOf(this.feels_like)+" "+
//                        String.valueOf(this.humidity)+" "+
//                        String.valueOf(this.cloud)+" "+
//                        String.valueOf(this.wind_speed)+" "+
//                        String.valueOf(this.id)+" "+
//                        this.main+" "+
//                        this.desc+" "+
//                        this.icon+" "+
//                        String.valueOf(this.temp_max)+" "+
//                        String.valueOf(this.temp_min));
    }
}
