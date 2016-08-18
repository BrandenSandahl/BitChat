package io.bitfountain.matthewparker.bitchat;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.parse.ParseUser;

import java.util.ArrayList;

public class ChatActivity extends ActionBarActivity implements View.OnClickListener {

    public static final String CONTACT_NUMBER = "CONTACT_NUMBER";

    private ArrayList<Message> mMessages;
    private MessagesAdapter mAdapter;
    private String mRecipient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mRecipient = getIntent().getStringExtra(CONTACT_NUMBER);

        mMessages = new ArrayList<>();
        mMessages.add(new Message("a message", "18009999999"));

        ListView messageView = (ListView) findViewById(R.id.messages_list);
        mAdapter = new MessagesAdapter(mMessages);
        messageView.setAdapter(mAdapter);

        Button sendMessage = (Button) findViewById(R.id.send_message);
        sendMessage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        EditText newMessageView = (EditText) findViewById(R.id.new_message);
        String newMessage = newMessageView.getText().toString();
        newMessageView.setText("");

        Message message = new Message(newMessage, ContactDataSource.getCurrentUser().getPhoneNumber());

        mMessages.add(message);
        mAdapter.notifyDataSetChanged();
        MessageDataSource.sendMessage(message.getSender(), mRecipient, message.getText());

    }


    /* Extended Adapter Class */
    private class MessagesAdapter extends ArrayAdapter<Message> {
        MessagesAdapter(ArrayList<Message> messages) {
            super(ChatActivity.this, R.layout.messages_list_item, R.id.message, messages);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = super.getView(position, convertView, parent);
            Message message = getItem(position);

            TextView nameView = (TextView) convertView.findViewById(R.id.message);
            nameView.setText(message.getText());

            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) nameView.getLayoutParams();

            if (message.getSender().equals(ContactDataSource.getCurrentUser().getPhoneNumber())) {
                nameView.setBackground(getDrawable(R.drawable.bubble_right_green));
                layoutParams.gravity = Gravity.RIGHT;
            } else {
                nameView.setBackground(getDrawable(R.drawable.bubble_left_gray));
                layoutParams.gravity = Gravity.LEFT;
            }
            nameView.setLayoutParams(layoutParams);

            return convertView;
        }
    }
}
