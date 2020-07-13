package com.minus21.mainapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Window;
import android.widget.TextView;

public class ContactActivity extends Activity {
    TextView name;
    TextView phnumber;
    TextView email;
    TextView note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.contact_popup);
        name = (TextView) findViewById(R.id.name);
        phnumber = (TextView) findViewById(R.id.number);
        email = (TextView) findViewById(R.id.email);
        note = (TextView) findViewById(R.id.note);

        Intent intent = getIntent();
        String Name = intent.getStringExtra("name");
        String Phnumber = intent.getStringExtra("phnumber");
        String Email = intent.getStringExtra("email");
        String Note = intent.getStringExtra("note");
        name.setText(Name);
        phnumber.setText(Phnumber.substring(0,3) + "-" + Phnumber.substring(3,7) + "-" + Phnumber.substring(7,11));
        email.setText(Email);
        note.setText(Note);

    }
}