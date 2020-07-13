package com.minus21.mainapp.ui.main;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.minus21.mainapp.ContactActivity;
import com.minus21.mainapp.PopupActivity;
import com.minus21.mainapp.R;
import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private ArrayList<ContactInfo> mList;
    private Context context;

    public CustomAdapter(Context context, ArrayList<ContactInfo> list) {
        this.context = context;
        this.mList = list;
    }
    /* CustomViewHolder constructed with textViews */
    public class CustomViewHolder extends RecyclerView.ViewHolder {
        protected TextView name;
        protected TextView phnumber;

        public CustomViewHolder(View view) {
            super(view);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();
                    if (pos != RecyclerView.NO_POSITION) {
                        Intent intent = new Intent(context, ContactActivity.class);
                        intent.putExtra("name",mList.get(pos).getName());
                        intent.putExtra("phnumber",mList.get(pos).getPhNumber());
                        intent.putExtra("email",mList.get(pos).getEmail());
                        intent.putExtra("note",mList.get(pos).getNote());
                        context.startActivity(intent);
                    }
                }
            });

            this.name = view.findViewById(R.id.list_name);
            this.phnumber = view.findViewById(R.id.list_number);
        }
    }



    /* Called when RecyclerView needs a new RecyclerView.ViewHolder of the given type
     * to represent an item. */
    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_list, viewGroup, false);

        CustomViewHolder viewHolder = new CustomViewHolder(view);

        return viewHolder;
    }

    /* Called when notifyItemChanged */
    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {
        ContactInfo data = mList.get(position);
        viewholder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
        viewholder.phnumber.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);

        viewholder.name.setGravity(Gravity.LEFT);
        viewholder.phnumber.setGravity(Gravity.LEFT);

        viewholder.name.setText(data.getName());
        viewholder.phnumber.setText(data.getPhNumber());
    }

    @Override
    public int getItemCount() {
        return (null != mList ? mList.size() : 0);
    }

}