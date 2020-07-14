package com.minus21.mainapp.ui.main;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.minus21.mainapp.R;

import java.util.ArrayList;

public class WeeklyWeatherAdpater extends RecyclerView.Adapter<WeeklyWeatherAdpater.ViewHolder> {
    private ArrayList<Weather> mData = null;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayNameField;
        ImageView dayWeatherImageField;
        TextView dayMinTempField;
        TextView dayMaxTempField;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            dayNameField = itemView.findViewById(R.id.day_name);
            dayWeatherImageField = itemView.findViewById(R.id.day_weather_image);
            dayMaxTempField = itemView.findViewById(R.id.day_max_temp);
            dayMinTempField = itemView.findViewById(R.id.day_min_temp);
        }
    }

    WeeklyWeatherAdpater(Context context, ArrayList<Weather> list){
        this.context = context;
        mData = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weekly_unit, parent, false);

        WeeklyWeatherAdpater.ViewHolder vh = new WeeklyWeatherAdpater.ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Weather weather = mData.get(position);

        Uri image_uri = Uri.parse("file:///android_asset/weather_"+weather.icon+".png");

        holder.dayMaxTempField.setText(String.valueOf((int)weather.temp_max-273 ));
        holder.dayMinTempField.setText(String.valueOf((int)weather.temp_min-273 ));
        holder.dayNameField.setText("MON");
        Glide.with(context).load(image_uri).into(holder.dayWeatherImageField);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

}
