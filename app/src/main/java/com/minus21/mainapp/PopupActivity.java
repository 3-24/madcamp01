package com.minus21.mainapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.minus21.mainapp.ui.main.PlaceholderFragment2;

public class PopupActivity extends Activity {
    PhotoView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.img_popup);
        imageView = (PhotoView) findViewById(R.id.expanded_img);

        Intent intent = getIntent();
        String uri = intent.getStringExtra("url");
        Glide.with(this).load(uri).into(imageView);

    }

}