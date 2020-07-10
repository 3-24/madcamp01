package com.minus21.mainapp.ui.main;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.minus21.mainapp.R;
import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<ContactInfo> mList;

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView id;
        protected TextView name;
        protected TextView phnumber;

        public CustomViewHolder(View view) {
            super(view);
            this.id = (TextView) view.findViewById(R.id.list_id);
            this.name = (TextView) view.findViewById(R.id.list_name);
            this.phnumber = (TextView) view.findViewById(R.id.list_number);
        }
    }

    public CustomAdapter(ArrayList<ContactInfo> list) {
        this.mList = list;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        ContactInfo data = mList.get(position);
        viewholder.id.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.phnumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

        viewholder.id.setGravity(Gravity.CENTER);
        viewholder.name.setGravity(Gravity.CENTER);
        viewholder.phnumber.setGravity(Gravity.CENTER);

        viewholder.id.setText(String.valueOf(data.getInfo_id()));
        viewholder.name.setText(data.getName());
        viewholder.phnumber.setText(data.getPhNumber());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}