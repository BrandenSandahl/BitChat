package io.bitfountain.matthewparker.bitchat;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

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


    public static void fetchMessages(String sender, String recipient, final Listener listener) {
        ParseQuery<ParseObject> querySent = ParseQuery.getQuery("Message");
        querySent.whereEqualTo("sender", sender);
        querySent.whereEqualTo("recipient", recipient);

        ParseQuery<ParseObject> queryReceived = ParseQuery.getQuery("Message");
        querySent.whereEqualTo("sender", recipient);
        querySent.whereEqualTo("recipient", sender);

        //a list of both queries
        List<ParseQuery<ParseObject>> queries = new ArrayList<>();
        queries.add(querySent);
        queries.add(queryReceived);

        //if one or the other queries returns something
        ParseQuery<ParseObject> mainQuery = ParseQuery.or(queries);
        mainQuery.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                ArrayList<Message> messages = new ArrayList<Message>();
                for (ParseObject parseObject : list) {
                    Message message = new Message(((String) parseObject.get("text")), ((String) parseObject.get("sender")));
                    messages.add(message);
                }
                listener.onFetchedMessages(messages);
            }
        });
    }


    public interface Listener {
        public void onFetchedMessages(ArrayList<Message> messages);
    }

}
