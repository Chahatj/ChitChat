package com.example.chahat.mychat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import java.util.logging.LogRecord;

public class SendActivity extends ActionBarActivity {

    EditText etsend;
    ListView lvsend,lvrec;
    long Reciever,Sender;

    //Handler mHandler;
    //SwipeRefreshLayout myrefreshlayout;


    List<String> messagelist=new ArrayList<>();
    List<String> messagelistrec = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);

        etsend = (EditText) findViewById(R.id.etsend);
        lvsend = (ListView) findViewById(R.id.lvsend);
        lvrec = (ListView) findViewById(R.id.lvrec);

      //  myrefreshlayout=(SwipeRefreshLayout) findViewById(R.id.swipView);

        Reciever = getIntent().getLongExtra("Phone",123);

        Log.v("Phone", Reciever + "");

        setTitle(Reciever + "");

        SharedPreferences shared = getSharedPreferences("userDetails", MODE_PRIVATE);
        Sender = (shared.getLong("Phone", 111));

        Log.v("myphone", Sender + "");

        FetchData();
        FetchhisData();




    }
    public void sendbtnclicklistner(View view)
    {

        String messageone = etsend.getText().toString();

        if (messageone.equals("")) {
            Toast.makeText(this,"oops",Toast.LENGTH_SHORT).show();
        }
        else {

            messagelist.add(messageone);


            Message message1 = new Message(Sender, Reciever, messageone);

            etsend.setText("");

            ArrayAdapter<String> arrayAdaptersend = new ArrayAdapter<String>(getBaseContext(), R.layout.message, R.id.tvnum, messagelist);
            lvsend.setAdapter(arrayAdaptersend);

            MessageServerRequests messageServerRequestsone = new MessageServerRequests(this);
            messageServerRequestsone.storeMessageDataInBackground(message1, new MessageCallback() {
                @Override
                public void done(List<String> returnedMessage) {
                    Toast.makeText(getBaseContext(), "Message Sent", Toast.LENGTH_SHORT).show();

                }
            });
        }



    }

    public void FetchData()
    {
        Message message=new Message(Sender,Reciever,"");

        MessageServerRequests messageServerRequests = new MessageServerRequests(this);
        messageServerRequests.fetchMessageDataAsyncTask(message, new MessageCallback() {
            @Override
            public void done(List<String> returnedlist) {

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.message, R.id.tvnum, returnedlist);

                lvsend.setAdapter(arrayAdapter);

                for (int i = 0; i < returnedlist.size(); i++) {
                    messagelist.add(i, returnedlist.get(i));
                }


            }
        });


    }

    public void FetchhisData()
    {
        Message message=new Message(Reciever,Sender,"");

        MessageServerRequests messageServerRequests = new MessageServerRequests(this);
        messageServerRequests.fetchMessageDataAsyncTask(message, new MessageCallback() {
            @Override
            public void done(List<String> returnedlist) {

                ArrayAdapter<String> arrayAdapterrec = new ArrayAdapter<String>(getBaseContext(), R.layout.message, R.id.tvnum, returnedlist);

                lvrec.setAdapter(arrayAdapterrec);

                for (int i = 0; i < returnedlist.size(); i++) {
                    messagelistrec.add(i, returnedlist.get(i));
                }


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sendactivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else if (id==R.id.action_refresh)
        {
            recreate();
        }
        return super.onOptionsItemSelected(item);
    }
}
