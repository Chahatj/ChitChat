package com.example.chahat.mychat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;

public class ProfileActivity extends ActionBarActivity{

    TextView tvname,tvphone,tvemail;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        tvname=(TextView) findViewById(R.id.tvname);
        tvphone=(TextView) findViewById(R.id.tvphone);
        tvemail=(TextView) findViewById(R.id.tvemail);

        SharedPreferences shared = getSharedPreferences("userDetails", MODE_PRIVATE);
        long phone = (shared.getLong("Phone", 111));
        String name = shared.getString("Name","name");
        String email= shared.getString("Email","email");

        tvname.setText(name);
        tvemail.setText(email);
        tvphone.setText(Long.toString(phone));




    }

}
