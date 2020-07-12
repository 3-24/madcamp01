package com.minus21.mainapp.ui.main;

import android.Manifest;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.provider.ContactsContract;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.minus21.mainapp.R;

import java.util.ArrayList;

public class PlaceholderFragment1 extends Fragment {
    private ArrayList<ContactInfo> mArrayList = new ArrayList<ContactInfo>();
    private CustomAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ContentResolver contentResolver;

    private static final String ARG_SECTION_NUMBER = "section_number";

    public static PlaceholderFragment1 newInstance(int index) {
        PlaceholderFragment1 fragment = new PlaceholderFragment1();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mArrayList = new ArrayList<>();
        mAdapter = new CustomAdapter(mArrayList);
        
        contentResolver = getActivity().getContentResolver();
    }


    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main1, container, false);

        /* Setup LinearLayout */
        mLayoutManager = new LinearLayoutManager(getActivity());
        /* Init mRecyclerView */
        mRecyclerView = root.findViewById(R.id.recyclerview_list);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);
        updateData();
        return root;
    }

    /* Update mArrayList and notify the change to the adapter */
    private void updateData(){
        mArrayList.clear();
        String [] arrProjection = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
        };

        Cursor clsCursor=contentResolver.query (
                ContactsContract.Contacts.CONTENT_URI,
                arrProjection,
                ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1" ,
                null,
                "UPPER(" + ContactsContract.Contacts.DISPLAY_NAME + ") ASC"
        );

        if (clsCursor.moveToFirst()) {
            do {
                /* Get phone number. This requires new cursor since phone number is
                not located in ContactContract.Contacts */
                String contactId = clsCursor.getString(0);
                Cursor phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                        null, null
                );

                phoneCursor.moveToFirst();
                String phone = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                phoneCursor.close();

                /* Make ContactInfo and update the array */
                ContactInfo contactInfo = new ContactInfo(
                        clsCursor.getInt(clsCursor.getColumnIndex(ContactsContract.Contacts._ID)),
                        clsCursor.getString(clsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),
                        phone
                );
                mArrayList.add(contactInfo);
            } while (clsCursor.moveToNext());
        }
        clsCursor.close();

        /* Notify to the adapter */
        mAdapter.notifyDataSetChanged();
    }
}