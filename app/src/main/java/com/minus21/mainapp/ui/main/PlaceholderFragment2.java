package com.minus21.mainapp.ui.main;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.minus21.mainapp.BuildConfig;
import com.minus21.mainapp.R;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Target;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment2 extends Fragment {
    private ArrayList<String> mArrayList = new ArrayList<String>();
    private ImageAdapter mAdapter;
    private Context context;
    private RecyclerView mRecyclerView;
    private GridLayoutManager mLayoutManager;
    private ContentResolver contentResolver;

    private static final String ARG_SECTION_NUMBER = "section_number";
    public static PlaceholderFragment2 newInstance(int index) {
        PlaceholderFragment2 fragment = new PlaceholderFragment2();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        //pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }

        mArrayList = new ArrayList<>();
        mAdapter = new ImageAdapter(context,mArrayList);
        mAdapter.setOnItemClickListener(new CustomAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int pos) {
                switch (v.getId()) {
                    case R.id.i_am_image:
                        Intent i = getActivity().getIntent();
                        Bundle extras = i.getExtras();
                        String imgPath = extras.getString("filename");
                        ImageView img = (ImageView) v;
                        Glide.with(context).load(imgPath).into(img);
                        break;
                }
            }
        });
        contentResolver = context.getContentResolver();
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main2, container, false);
        mRecyclerView = (RecyclerView) root.findViewById(R.id.gallery);
        mRecyclerView.setHasFixedSize(true);

        int numberOfColumns = 4;
        mLayoutManager = new GridLayoutManager(context,numberOfColumns);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mArrayList = new ArrayList<>();

        mAdapter = new ImageAdapter(context,mArrayList);
        mRecyclerView.setAdapter(mAdapter);
        updateData();

        return root;
    }

    /* Update mArrayList and notify the change to the adapter */
    private void updateData(){
        mArrayList.clear();

        String[] projection = {
                MediaStore.Images.Media._ID, MediaStore.Images.Media.DISPLAY_NAME};

        Cursor cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, "UPPER(" + MediaStore.Images.Media.DATE_TAKEN + ") ASC");

        if (cursor.moveToFirst()) {
            do {
                Uri ContentUri = Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media._ID)));
                mArrayList.add(String.valueOf(ContentUri));
                mAdapter.notifyDataSetChanged();
            } while (cursor.moveToNext());
        }
        cursor.close();

        /* Notify to the adapter */
        mAdapter.notifyDataSetChanged();
    }

}