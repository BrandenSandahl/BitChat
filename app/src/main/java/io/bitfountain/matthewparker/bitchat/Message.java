package io.bitfountain.matthewparker.bitchat;

import java.util.Date;

/**
 * Created by branden on 8/10/16.
 */
public class Message {

    private String mText;
    private String mSender;
    private Date mDate;

    public Message(String text, String sender) {
        mText = text;
        mSender = sender;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public String getSender() {
        return mSender;
    }

    public void setSender(String sender) {
        mSender = sender;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }
}
