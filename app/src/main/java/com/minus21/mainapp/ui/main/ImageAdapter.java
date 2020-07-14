package com.minus21.mainapp.ui.main;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.minus21.mainapp.PopupActivity;
import com.minus21.mainapp.R;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.CustomViewHolder> {
    private Context context;

    private ArrayList<String> mList = new ArrayList<String>();
    public int position;

    public int getPosition() {
        return position;
    }
    public void setPosition() {
        this.position = position;
    }

    public ImageAdapter(Context context,ArrayList<String> list) {
        this.context = context;
        this.mList.clear();
        this.mList = list;
    }
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView imageView;

        public CustomViewHolder(View view) {
            super(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    if (pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, PopupActivity.class);
                        intent.putExtra("url",mList.get(pos));
                        context.startActivity(intent);
                    }
                }
            });

            this.imageView = (ImageView) view.findViewById(R.id.i_am_image);
        }
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(context)
                .inflate(R.layout.gallery_layout, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        String data = mList.get(position);
        ImageView imageView = (ImageView) viewholder.imageView.findViewById(R.id.i_am_image);
        Glide.with(context).load(data).into(imageView);

    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }
}