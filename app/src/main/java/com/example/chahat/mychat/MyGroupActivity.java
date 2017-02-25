package com.example.chahat.mychat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MyGroupActivity extends ActionBarActivity {

    ListView lv;
    List<String> grouplist;
    List<Long> groupmembers,groupmem;
    long sender,rec1,rec2,rec3,rec4;
    UserLocalStore userLocalStore;
    long[] mem={0,0,0,0};

    GroupMemberHandler groupMemberHandler=new GroupMemberHandler(this,null,null,1);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_group);

        lv=(ListView) findViewById(R.id.lvgroup);
        grouplist=new ArrayList<>();
        groupmembers=new ArrayList<>();
        groupmem=new ArrayList<>();


        userLocalStore=new UserLocalStore(this);

        SharedPreferences shared = getSharedPreferences("userDetails", MODE_PRIVATE);
        sender = (shared.getLong("Phone", 111));


     //   int i = getIntent().getExtras().getInt("Activity");


      /*  if (i==1)
        {
            String grname = getIntent().getExtras().getString("GroupName");
            Log.v("grname",grname);

            groupmembers = (ArrayList<Long>) getIntent().getSerializableExtra("GroupMembers");
            Log.v("grmem",groupmembers+"");

            for (int j=0;j<groupmembers.size();j++)
            {
                mem[j] = groupmembers.get(j);
            }

            rec1 = mem[0];
            rec2= mem[1];
            rec3 = mem[2];
            rec4 = mem[3];

            GroupChat groupChat=new GroupChat(grname,sender,rec1,rec2,rec3,rec4,"");

            groupMemberHandler.insertMember(groupChat);


            grouplist.add(grname);

            Toast.makeText(this,"Group Created",Toast.LENGTH_SHORT).show();
        }
        else if (i==0)
        {
            Toast.makeText(this,"Your Groups",Toast.LENGTH_SHORT).show();

        }*/


        FetchGroup();

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,R.layout.nameno,R.id.tvnum,grouplist);
        lv.setAdapter(arrayAdapter);

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                String name = grouplist.get(position);

                Intent intent = new Intent(getBaseContext(),GroupChatActivity.class);
                intent.putExtra("GroupName",name);

               // int i  = groupMemberHandler.searchsenderindex(sender,name);

             //   groupMemberHandler.updatetable(i,name);

               // groupmem=groupMemberHandler.getmember(name);

             //   Log.v("groupmmmm",groupmem+"");
              //  intent.putExtra("GroupMember",(ArrayList<Long>)groupmem);

                startActivity(intent);
            }
        });



    }

    public void FetchGroup()
    {
        GroupChat groupChat=new GroupChat("",sender,-1,-1,-1,-1,"");

        MyGroupRequests myGroupRequests = new MyGroupRequests(this);
        myGroupRequests.fetchMyGroupDataAsyncTask(groupChat, new GroupChatCallback() {
            @Override
            public void done(List<String> returnedlist) {

                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.nameno, R.id.tvnum, returnedlist);

                lv.setAdapter(arrayAdapter);

                for (int i = 0; i < returnedlist.size(); i++) {
                    grouplist.add(i, returnedlist.get(i));
                }


            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mygroup, menu);
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
        else if (id==R.id.action_logout)
        {
            userLocalStore.clearUserData();
            Intent i = new Intent(this, LoginActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(i);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }






}
