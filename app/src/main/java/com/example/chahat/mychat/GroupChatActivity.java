package com.example.chahat.mychat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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

public class GroupChatActivity extends ActionBarActivity {

    ListView lv;
    EditText et;

    String groupname;

    List<String> messagelist;
    long sender,rec1,rec2,rec3,rec4,admintemp;
    long messenger;

    GroupMemberHandler groupMemberHandler=new GroupMemberHandler(this,null,null,1);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);

        lv=(ListView) findViewById(R.id.lvgrchat);
        et=(EditText) findViewById(R.id.etgrchat);

        messagelist=new ArrayList<>();

        groupname=getIntent().getExtras().getString("GroupName");

        setTitle(groupname);

        SharedPreferences shared = getSharedPreferences("userDetails", MODE_PRIVATE);
        sender = (shared.getLong("Phone", 111));

       fetchmember();




        FetchGroupChatData();

    }

    public void fetchmember()
    {
        GroupChat groupChat=new GroupChat(groupname,-1,-1,-1,-1,-1,"");

        FetchGroupMemberRequests groupChatRequests = new FetchGroupMemberRequests(this);
        groupChatRequests.fetchMemberDataAsyncTask(groupChat, new FetchMember() {
            @Override
            public void done(GroupChat returnedmember) {

                admintemp = returnedmember.getSender();
                rec1=returnedmember.getRec1();
                rec2=returnedmember.getRec2();
                rec3=returnedmember.getRec3();
                rec4=returnedmember.getRec4();

                Log.v("returned member",rec1+"");

                if (rec1 == sender) {
                    long temp = rec1;
                    rec1 = admintemp;
                    admintemp = temp;

                    Log.v("hii", "bii");
                } else if (rec2 == sender) {
                    long temp = rec2;
                    rec2 = admintemp;
                    admintemp = temp;
                } else if (rec3 == sender) {
                    long temp = rec3;
                    rec3 = admintemp;
                    admintemp = temp;
                } else if (rec4 == sender) {
                    long temp = rec4;
                    rec4 = admintemp;
                    admintemp = temp;
                }


            }
        });


    }


    public void btgrchat(View view)
    {

        String messageone = et.getText().toString();

        if (messageone.equals("")) {
            Toast.makeText(this,"oops",Toast.LENGTH_SHORT).show();
        }

        else
        {

            messagelist.add(messageone);


            GroupChat message = new GroupChat(groupname, admintemp, rec1, rec2, rec3, rec4, messageone);

            et.setText("");

            ArrayAdapter<String> arrayAdaptersend = new ArrayAdapter<String>(getBaseContext(), R.layout.message, R.id.tvnum, messagelist);
            lv.setAdapter(arrayAdaptersend);

            GroupChatRequests messageServerRequestsone = new GroupChatRequests(this);
            messageServerRequestsone.storeGroupChatDataInBackground(message, new GroupChatCallback() {
                @Override
                public void done(List<String> returnedMessage) {
                    Toast.makeText(getBaseContext(), "Message Sent", Toast.LENGTH_SHORT).show();

                }
            });
        }




    }

    public void FetchGroupChatData()
    {
        GroupChat groupChat=new GroupChat(groupname,sender,-1,-1,-1,-1,"");

        GroupChatRequests groupChatRequests = new GroupChatRequests(this);
        groupChatRequests.fetchGroupChatDataAsyncTask(groupChat, new GroupChatCallback() {
            @Override
            public void done(List<String> returnedlist) {

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.message, R.id.tvnum, returnedlist);

                lv.setAdapter(arrayAdapter);

                for (int i = 0; i < returnedlist.size(); i++) {
                    messagelist.add(i, returnedlist.get(i));
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


            Intent intent = new Intent(this,ProfileActivity.class);
            startActivity(intent);

            return true;
        }
        else if (id==R.id.action_refresh)
        {
            recreate();
        }
        return super.onOptionsItemSelected(item);
    }

}
