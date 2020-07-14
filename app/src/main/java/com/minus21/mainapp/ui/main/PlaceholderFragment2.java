package com.minus21.mainapp.ui.main;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.minus21.mainapp.R;

import java.util.ArrayList;


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

    public static PlaceholderFragment2 newInstance() {
        PlaceholderFragment2 fragment = new PlaceholderFragment2();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();

        mArrayList = new ArrayList<>();
        mAdapter = new ImageAdapter(context,mArrayList);

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

        final SwipeRefreshLayout pullToRefresh = root.findViewById(R.id.pullToRefresh2);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateData();
                pullToRefresh.setRefreshing(false);
            }
        });

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
            } while (cursor.moveToNext());
        }
        cursor.close();

        /* Notify to the adapter */
        mAdapter.notifyDataSetChanged();
    }

}