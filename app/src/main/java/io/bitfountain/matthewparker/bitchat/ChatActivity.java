package io.bitfountain.matthewparker.bitchat;


import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Date;

public class ChatActivity extends ActionBarActivity implements View.OnClickListener,
        MessageDataSource.Listener {

    public static final String CONTACT_NUMBER = "CONTACT_NUMBER";

    private ArrayList<Message> mMessages;
    private MessagesAdapter mAdapter;
    private String mRecipient;
    private ListView mMessageView;
    private Date mLastMessageDate = new Date();

    private Handler mHandler = new Handler(); //handler that takes and handles runnables
    private Runnable mRunnable = new Runnable() { //runnable to fetch messages every X seconds
        @Override
        public void run() {
            Log.d("ChatActivity", "Fetched new messages");
            MessageDataSource.fetchMessagesAfter(ContactDataSource.getCurrentUser().getPhoneNumber(),
                    mRecipient,
                    mLastMessageDate,
                    ChatActivity.this);
            mHandler.postDelayed(this, 2000);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mRecipient = getIntent().getStringExtra(CONTACT_NUMBER);

        mMessages = new ArrayList<>();

        mMessageView = (ListView) findViewById(R.id.messages_list);
        mAdapter = new MessagesAdapter(mMessages);
        mMessageView.setAdapter(mAdapter);

        Button sendMessage = (Button) findViewById(R.id.send_message);
        sendMessage.setOnClickListener(this);

        MessageDataSource.fetchMessages(ContactDataSource.getCurrentUser().getPhoneNumber(),
                mRecipient,
                this
        );
    }

    @Override
    public void onClick(View v) {
        EditText newMessageView = (EditText) findViewById(R.id.new_message);
        String newMessage = newMessageView.getText().toString();
        newMessageView.setText("");

        Message message = new Message(newMessage, ContactDataSource.getCurrentUser().getPhoneNumber());
        mAdapter.notifyDataSetChanged();
        MessageDataSource.sendMessage(message.getSender(), mRecipient, message.getText());

    }

    @Override
    public void onFetchedMessages(ArrayList<Message> messages) {
        mMessages.clear();
        addMessages(messages);
        mHandler.postDelayed(mRunnable, 1000);


    }

    @Override
    public void onAddMessages(ArrayList<Message> messages) {
        addMessages(messages);
    }

    private void addMessages(ArrayList<Message> messages) {
        mMessages.addAll(messages);
        mAdapter.notifyDataSetChanged();
        if (mMessages.size() > 0) {
            mMessageView.setSelection(mMessages.size() - 1);
            Message message = mMessages.get(mMessages.size() - 1);
            mLastMessageDate = message.getDate();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler.removeCallbacks(null); //removes all attached runnables
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    nameView.setBackground(getDrawable(R.drawable.bubble_right_green));
                } else {
                    nameView.setBackgroundDrawable(getDrawable(R.drawable.bubble_right_green));
                }
                layoutParams.gravity = Gravity.RIGHT;
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    nameView.setBackground(getDrawable(R.drawable.bubble_left_gray));
                } else {
                    nameView.setBackgroundDrawable(getDrawable(R.drawable.bubble_left_gray));
                }
                layoutParams.gravity = Gravity.LEFT;
            }
            nameView.setLayoutParams(layoutParams);

            return convertView;
        }
    }
}
