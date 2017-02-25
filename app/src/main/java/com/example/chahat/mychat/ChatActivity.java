package com.example.chahat.mychat;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class ChatActivity extends ActionBarActivity {

    ListView lvmessage;
    List<Long> chekedlist = new ArrayList<Long>();

    UserLocalStore userLocalStore;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Toast.makeText(getBaseContext(),"Welcome to MyChat",Toast.LENGTH_SHORT).show();

        lvmessage = (ListView) findViewById(R.id.lvmessage);
        userLocalStore = new UserLocalStore(this);

       NameNo nameNo=null;

        NameNoRequests nameNoRequests = new NameNoRequests(this);
        nameNoRequests.fetchNameNoDataAsyncTask(nameNo, new NameNoCallback() {
            @Override
            public void done(List<Long> returnedlist) {



                List<Long> ret=CheckNum(returnedlist);

                if (ret.isEmpty())
                {
                    Toast.makeText(getBaseContext(),"Your Contact Friends are not on MyChat",Toast.LENGTH_LONG).show();
                }

                else {

                    for (int i = 0; i < ret.size(); i++) {
                        chekedlist.add(ret.get(i));
                    }

                    ArrayAdapter<Long> arrayAdapter = new ArrayAdapter<Long>(getBaseContext(), R.layout.nameno, R.id.tvnum, chekedlist);

                    lvmessage.setAdapter(arrayAdapter);
                }
            }
        });

        lvmessage.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                long phone =  chekedlist.get(position);

                Intent intent=new Intent(getBaseContext(),SendActivity.class);
                intent.putExtra("Phone",phone);
                startActivity(intent);
            }
        });



    }

    public List<Long> CheckNum(List<Long> returnedlist)
    {
        List<Long> checkedlist = new ArrayList<>();


        int m = returnedlist.size();

        List<Long> numberArray = new ArrayList<>();

        numberArray = GetContact();

        int n = numberArray.size();

        Log.v("n size",n+"");




        for (int i =0;i<m;i++)
        {
            for (int j=0;j<n;j++)
            {

                    if (numberArray.get(j).equals(returnedlist.get(i))) {
                        checkedlist.add(returnedlist.get(i));
                    }

            }
        }

        return checkedlist;

    }

    public List<Long> GetContact()
    {
        Long singleNumber;

        List<Long> numberArray = new ArrayList<>();

        String ch = "+919571242526";
        Log.v("ch",ch.length()+"");

        Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);

        while (phone.moveToNext())
        {
            String number = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));

            int phoneType = phone.getInt(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));

        //    String mimeType = phone.getString(phone.getColumnIndex(ContactsContract.Data.HAS_PHONE_NUMBER));



           // Log.v("mime",mimeType);


//Log.v("phonetype",phoneType+"");


String newnum;
                if (phoneType == ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE && number.length() >= 10 )
                {
                    String numsp = number.replaceAll("\\s+", "");
                    Log.v("numsp",numsp);

                    if (numsp.startsWith("+91")) {
                         newnum = numsp.substring(number.length() - 10);
                    }
                   else {
                        newnum =numsp.substring(number.length()-10);
                    }

                    if (!(numberArray.contains(Long.parseLong(newnum))))
                    {
                       // Log.v("eachnum", number);

                        singleNumber = Long.parseLong(newnum);

                       // Log.v("single", singleNumber + "");

                        numberArray.add(singleNumber);
                    }

                }




        }
        phone.close();

        Log.v("numarry", numberArray + "");

       return numberArray;


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chatactivity, menu);
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
        else if (id==R.id.action_creategroup)
        {
            Intent intent = new Intent(this,CreateGroupActivity.class);
            intent.putExtra("PhoneList",(ArrayList<Long> )chekedlist);
            startActivity(intent);
        }

        else if (id==R.id.action_mygroup)
        {
           Intent intent = new Intent(this,MyGroupActivity.class);
          //  intent.putExtra("Activity",0);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

}
