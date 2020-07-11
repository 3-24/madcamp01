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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.minus21.mainapp.R;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment1 extends Fragment {
    private ArrayList<ContactInfo> mArrayList = new ArrayList<ContactInfo>();
    private CustomAdapter mAdapter;
    private Context context;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;


    private static final String ARG_SECTION_NUMBER = "section_number";
    public static PlaceholderFragment1 newInstance(int index) {
        PlaceholderFragment1 fragment = new PlaceholderFragment1();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_main1, container, false);

        mRecyclerView = (RecyclerView) root.findViewById(R.id.recyclerview_list);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());

        mRecyclerView.setLayoutManager(mLayoutManager);

        mArrayList = new ArrayList<>();

        mAdapter = new CustomAdapter(mArrayList);
        mRecyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_CONTACTS}, 100);

        }
//        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
//                mLinearLayoutManager.getOrientation());
        String [] arrProjection = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        String [] arrPhoneProjection = {
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        String [] arrEmailProjection = {
                ContactsContract.CommonDataKinds.Email.DATA
        };
        // get user list
        ContentResolver contentResolver=getActivity().getContentResolver();
        Cursor clsCursor=contentResolver.query (
                ContactsContract.Contacts.CONTENT_URI,
                arrProjection,
                ContactsContract.Contacts.HAS_PHONE_NUMBER + "=1" ,
                null, null
        );
        if (clsCursor.moveToFirst()) {
            do {
                String strContactId = clsCursor.getString(0);

                //phone number
                Cursor clsPhoneCursor = contentResolver.query (
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        arrPhoneProjection,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + strContactId,
                        null, null
                );

                clsPhoneCursor.moveToFirst();
                ContactInfo contactInfo = new ContactInfo(clsCursor.getInt(clsCursor.getColumnIndex(ContactsContract.Contacts._ID)),
                        clsCursor.getString(clsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),clsPhoneCursor.getString(clsPhoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                mArrayList.add(contactInfo);
                mAdapter.notifyDataSetChanged();
                clsPhoneCursor.close();
            }while (clsCursor.moveToNext());
        }
        clsCursor.close( );


        return root;
    }
}