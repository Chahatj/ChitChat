package com.example.chahat.mychat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class CreateGroupActivity extends ActionBarActivity {

    ListView lv;
    EditText et;
    List<Long> phonelist;
    List<Long> numingroup;
    String grname;

    long sender,rec1,rec2,rec3,rec4;
    long[] mem={0,0,0,0};

    SparseBooleanArray ids;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);

        lv = (ListView) findViewById(R.id.listView);
        et =  (EditText) findViewById(R.id.editText);
        phonelist= new ArrayList<>();
        numingroup=new ArrayList<>();

        ids = new SparseBooleanArray();

        phonelist=(ArrayList<Long>)getIntent().getSerializableExtra("PhoneList");

        Log.v("phonelist", phonelist + "");

        SharedPreferences shared = getSharedPreferences("userDetails", MODE_PRIVATE);
        sender = (shared.getLong("Phone", 111));

        final CustomAdapter customAdapter = new CustomAdapter(this,R.id.tvnum,phonelist);
        lv.setAdapter(customAdapter);

        lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, final View view, int position, long id) {

                lv.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);

                lv.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
                    @Override
                    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

                        mode.setTitle(lv.getCheckedItemCount() + "Selected");



                        customAdapter.toggleSelection(position);


                    }

                    @Override
                    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                        mode.getMenuInflater().inflate(R.menu.menu_creategroup, menu);
                        return true;
                    }

                    @Override
                    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                        return false;
                    }

                    @Override
                    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                        int id = item.getItemId();

                        if (id == R.id.action_ok) {
                             grname = et.getText().toString();

                            if (grname.equals("")) {
                                Toast.makeText(getBaseContext(),"Enter Group Name",Toast.LENGTH_SHORT).show();
                            } else {

                                SparseBooleanArray selected = customAdapter.getSelectedIds();
                                short size = (short) selected.size();
                                for (byte I = 0; I < size; I++) {
                                    if (selected.valueAt(I)) {
                                        long selectedItem = customAdapter.getItem(selected.keyAt(I));
                                        numingroup.add(selectedItem);
                                    }
                                }

                                // Close CAB (Contextual Action Bar)
                                mode.finish();


                                sendgrouptoserver();

                                Intent intent = new Intent(getBaseContext(), MyGroupActivity.class);
                             //   intent.putExtra("GroupName", grname);
                               // intent.putExtra("Activity", 1);
                                //intent.putExtra("GroupMembers", (ArrayList<Long>) numingroup);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                                startActivity(intent);
                                finish();
                            }


                            return true;
                        }


                        return false;
                    }

                    @Override
                    public void onDestroyActionMode(ActionMode mode) {

                    }
                });

                return false;
            }
        });


    }

    public void sendgrouptoserver()
    {

        for (int i=0;i<numingroup.size();i++)
        {
            mem[i] = numingroup.get(i);
        }

        rec1 = mem[0];
        rec2= mem[1];
        rec3 = mem[2];
        rec4 = mem[3];



        GroupChat message = new GroupChat(grname,sender ,rec1, rec2, rec3, rec4, "Welcome");

        GroupChatRequests messageServerRequestsone = new GroupChatRequests(this);
        messageServerRequestsone.storeGroupChatDataInBackground(message, new GroupChatCallback() {
            @Override
            public void done(List<String> returnedMessage) {

                Toast.makeText(getBaseContext(),"Group Created",Toast.LENGTH_SHORT).show();
            }
        });


        FetchGroupMemberRequests memberRequests = new FetchGroupMemberRequests(this);
        memberRequests.storeMemberDataInBackground(message, new FetchMember() {
            @Override
            public void done(GroupChat returnedMember) {


            }
        });
    }

}
