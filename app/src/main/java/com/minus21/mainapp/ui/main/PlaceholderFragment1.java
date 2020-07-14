package com.minus21.mainapp.ui.main;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.provider.ContactsContract;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.minus21.mainapp.R;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderFragment1 extends Fragment {
    private ArrayList<ContactInfo> mArrayList = new ArrayList<ContactInfo>();
    private CustomAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ContentResolver contentResolver;
    private Context context;
    private RecyclerView recyclerView;
    private EditText editSearch;
    private ArrayList<ContactInfo> list;
    private ArrayList<ContactInfo> arraylist;
    private CustomAdapter adapter;
    private LinearLayoutManager layoutManager;

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
        context = getActivity();
        mArrayList = new ArrayList<>();
        mAdapter = new CustomAdapter(context,mArrayList);

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

        final SwipeRefreshLayout pullToRefresh = root.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable(){
                    @Override
                    public void run(){
                        updateData();
                    }
                }
                ).start();
                //updateData();
                pullToRefresh.setRefreshing(false);
            }
        });

        editSearch = (EditText) root.findViewById(R.id.editSearch);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView = (RecyclerView) root.findViewById(R.id.recyclerview_list);

        list = new ArrayList<ContactInfo>();
        settingList();

        arraylist = new ArrayList<ContactInfo>();
        arraylist.addAll(list);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CustomAdapter(context,list);
        recyclerView.setAdapter(adapter);

        // input창에 검색어 입력 시 "addTextChangedListener" 이벤트 정의
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // input창에 문자를 입력할 때마다 호출됨
                // search 메소드 호출
                String text = editSearch.getText().toString();
                search(text);
            }
        });

        return root;
    }

    /* Update mArrayList and notify the change to the adapter */
    private void updateData(){
        mArrayList.clear();
        String [] arrProjection = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME
        };
        String[] arrPhoneProjection = {
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };
        String[] arrEmailProjection = {
                ContactsContract.CommonDataKinds.Email.DATA
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

                // phone number
                Cursor phoneCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        arrPhoneProjection,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + contactId,
                        null, null
                );
                phoneCursor.moveToNext();
                String realnumber = phoneCursor.getString(phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                String phone = realnumber.replace("-","");
                phoneCursor.close();

                // email
                Cursor emailCursor = contentResolver.query(
                        ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        arrEmailProjection,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,null,null
                );
                emailCursor.moveToNext();
                String email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                emailCursor.close();


                // note
                String noteWhere = ContactsContract.Data.CONTACT_ID + " = ? AND " + ContactsContract.Data.MIMETYPE + " = ?";
                String[] noteWhereParams = new String[] {
                        contactId, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE
                };
                Cursor noteCursor = contentResolver.query(
                        ContactsContract.Data.CONTENT_URI, null, noteWhere, noteWhereParams, null
                );

                noteCursor.moveToFirst();
                String note = noteCursor.getString(noteCursor.getColumnIndex(ContactsContract.CommonDataKinds.Note.NOTE));
                noteCursor.close();


                /* Make ContactInfo and update the array */
                ContactInfo contactInfo = new ContactInfo(
                        clsCursor.getString(clsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)),
                        phone, email, note
                );
                mArrayList.add(contactInfo);
            } while (clsCursor.moveToNext());
        }
        clsCursor.close();
        /* Notify to the adapter */
        mAdapter.notifyDataSetChanged();
    }

    // 검색을 수행하는 메소드
    public void search(String charText) {

        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
        list.clear();

        // 문자 입력이 없을때는 모든 데이터를 보여준다.
        if (charText.length() == 0) {
            list.addAll(arraylist);
        }
        // 문자 입력을 할때..
        else
        {
            // 리스트의 모든 데이터를 검색한다.
            for(int i = 0;i < arraylist.size(); i++)
            {
                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
                if (arraylist.get(i).getName().toLowerCase().contains(charText))
                {
                    // 검색된 데이터를 리스트에 추가한다.
                    list.add(arraylist.get(i));
                }
                if (arraylist.get(i).getPhNumber().toLowerCase().contains(charText))
                {
                    list.add(arraylist.get(i));
                }
            }
        }
        // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
        adapter.notifyDataSetChanged();
    }

    // 검색에 사용될 데이터를 리스트에 추가한다.
    private void settingList(){
        list = mArrayList;
    }
}