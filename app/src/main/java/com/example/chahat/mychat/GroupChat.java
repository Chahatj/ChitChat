package com.example.chahat.mychat;

/**
 * Created by chahat on 21/3/16.
 */
public class GroupChat
{
    String groupmessage,groupname;
    long sender ,rec1=0, rec2=0 ,rec3=0,rec4=0;

    public GroupChat(String groupname,long sender,long rec1,long rec2,long rec3,long rec4,String groupmessage)
    {
        this.groupmessage=groupmessage;
        this.groupname=groupname;
        this.sender=sender;
        this.rec1=rec1;
        this.rec2=rec2;
        this.rec3=rec3;
        this.rec4=rec4;
    }

    public long getSender() {
        return sender;
    }

    public String getGroupname() {
        return groupname;
    }

    public String getGroupmessage() {
        return groupmessage;
    }

    public long getRec1() {
        return rec1;
    }

    public long getRec2() {
        return rec2;
    }

    public long getRec3() {
        return rec3;
    }

    public long getRec4() {
        return rec4;
    }
}
