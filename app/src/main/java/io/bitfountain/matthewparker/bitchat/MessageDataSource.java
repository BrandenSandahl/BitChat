package io.bitfountain.matthewparker.bitchat;

import com.parse.ParseObject;

/**
 * Created by branden on 8/18/16.
 */
public class MessageDataSource {
    public static void sendMessage(String sender, String recipient, String text){
        ParseObject message = new ParseObject("Message");
        message.put("sender", sender);
        message.put("recipient", recipient);
        message.put("text", text);
        message.saveInBackground();
    }
}
