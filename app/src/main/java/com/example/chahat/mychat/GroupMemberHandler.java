package com.example.chahat.mychat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chahat on 22/3/16.
 */
public class GroupMemberHandler extends SQLiteOpenHelper {
    private static final String databasename = "GroupMember.db";
    private static final int databaseversion = 1;

    public GroupMemberHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, databasename, factory, databaseversion);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE GroupMember(GroupName TEXT, Sender BIGINT, Recieverone BIGINT, Recievertwo BIGINT, Recieverthree BIGINT, Recieverfour BIGINT);");
    }

    public void insertMember(GroupChat groupChat) {

        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("GroupName", groupChat.getGroupname());
        values.put("Sender", groupChat.getSender());
        values.put("Recieverone", groupChat.getRec1());
        values.put("Recievertwo", groupChat.getRec2());
        values.put("Recieverthree", groupChat.getRec3());
        values.put("Recieverfour", groupChat.getRec4());
        db.insert("GroupMember", null, values);
        db.close();

    }

  /*  public void editmemo(String st,memo me)
    {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("memo",me.getMemo());

        db.update("Memory", values, "memo=?", new String[]{st});
        db.close();
    }*/

  /*  public void deletememo(memo memo)
    {
        SQLiteDatabase db = getWritableDatabase();

        db.delete("Memory", "memo=?", new String[]{memo.getMemo()});
        db.close();
    }*/


   /* Cursor cursor;


    public List<Long> getmember(String grname) {
        String dbstring = "";
        SQLiteDatabase db = getWritableDatabase();
        List<Long> list = new ArrayList<>();
        cursor = db.rawQuery(" SELECT Sender,Recieverone,Recievertwo,Recieverthree,Recieverfour FROM GroupMember WHERE GroupName = ? ", new String[] {grname});

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();
            do {
                if (cursor.getString(cursor.getColumnIndex("Sender")) != null) {
                  //  GroupChat groupChat = new GroupChat("",cursor.getLong(cursor.getColumnIndex("Sender")),,cursor.getLong(cursor.getColumnIndex("Recievertwo")),cursor.getLong(cursor.getColumnIndex("Recieverthree")),,"");
                  //  list.add(cursor.getLong(cursor.getColumnIndex("Sender")));
                    list.add(cursor.getLong(cursor.getColumnIndex("Recieverone")));
                    list.add(cursor.getLong(cursor.getColumnIndex("Recievertwo")));
                    list.add(cursor.getLong(cursor.getColumnIndex("Recieverthree")));
                    list.add(cursor.getLong(cursor.getColumnIndex("Recieverfour")));




                }

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return list;
    }

    public int searchsenderindex(long sender,String grname)
    {
        String dbstring = "";
        SQLiteDatabase db = getWritableDatabase();
        int index=0;
        long num;


        cursor = db.rawQuery(" SELECT Sender,Recieverone,Recievertwo,Recieverthree,Recieverfour FROM GroupMember WHERE GroupName = ? ", new String[] {grname});

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();
            do {
                if (cursor.getString(cursor.getColumnIndex("Sender")) != null) {
                    //  GroupChat groupChat = new GroupChat("",cursor.getLong(cursor.getColumnIndex("Sender")),,cursor.getLong(cursor.getColumnIndex("Recievertwo")),cursor.getLong(cursor.getColumnIndex("Recieverthree")),,"");
                    //  list.add(cursor.getLong(cursor.getColumnIndex("Sender")));
                    if(sender==cursor.getLong(cursor.getColumnIndex("Sender")))
                    {
                        index=cursor.getColumnIndex("Sender");


                    }
                    else  if (sender==cursor.getLong(cursor.getColumnIndex("Recieverone")))
                    {
                        index=cursor.getColumnIndex("Recieverone");

                    }
                    else if (sender==cursor.getLong(cursor.getColumnIndex("Recievertwo")))
                    {
                        index=cursor.getColumnIndex("Recievertwo");

                    }
                    else if (sender==cursor.getLong(cursor.getColumnIndex("Recieverthree")))
                    {
                        index=cursor.getColumnIndex("Recieverthree");

                    }
                    else if (sender==cursor.getLong(cursor.getColumnIndex("Recieverfour")))
                    {
                        index=cursor.getColumnIndex("Recieverfour");

                    }



                }

            } while (cursor.moveToNext());
        }
        cursor.close();

        db.close();
        return index;

    }


    public void updatetable(int i,String grname)
    {
        SQLiteDatabase db = getWritableDatabase();
        List<Long> list = new ArrayList<>();

        long my=0,mynum=0;
        String indexname="";




        cursor = db.rawQuery(" SELECT Sender,Recieverone,Recievertwo,Recieverthree,Recieverfour FROM GroupMember WHERE GroupName=? ", new String[]{grname});

        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();
            do {
                if (cursor.getString(cursor.getColumnIndex("Sender")) != null) {
                    //  GroupChat groupChat = new GroupChat("",cursor.getLong(cursor.getColumnIndex("Sender")),,cursor.getLong(cursor.getColumnIndex("Recievertwo")),cursor.getLong(cursor.getColumnIndex("Recieverthree")),,"");
                    //  list.add(cursor.getLong(cursor.getColumnIndex("Sender")));

                    my = cursor.getLong(cursor.getColumnIndex("Sender"));
                    mynum = cursor.getLong(i);
                    indexname = cursor.getColumnName(i);

                    Log.v("indexname",indexname);



                }

            } while (cursor.moveToNext());
        }
        cursor.close();

        String sql = "UPDATE GroupMember SET Sender= "+mynum+","+indexname+"="+my+"WHERE GroupName= "+grname;

        db.execSQL(sql);

        db.close();

    }*/
}
