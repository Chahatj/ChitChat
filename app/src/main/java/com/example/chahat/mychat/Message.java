package com.example.chahat.mychat;

/**
 * Created by chahat on 19/3/16.
 */
public class Message
{
    String message;
    long Sender,Reciever;

    public Message(long Sender,long Reciever,String message)
    {
        this.message=message;
        this.Sender=Sender;
        this.Reciever=Reciever;
    }

    public long getSender() {
        return Sender;
    }

    public long getReciever()
    {
        return Reciever;
    }

    public String getMessage() {
        return message;
    }
}
