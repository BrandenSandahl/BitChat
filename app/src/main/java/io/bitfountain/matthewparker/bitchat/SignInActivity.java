package io.bitfountain.matthewparker.bitchat;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignInActivity extends ActionBarActivity {

    private EditText mUserNumber;
    private EditText mPassword;
    private EditText mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_activity);

        //fetch the phones phone number, this needs a permission in manifest
        TelephonyManager telephonyManager = (TelephonyManager) this.getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        String phoneNumber = telephonyManager.getLine1Number();

        mUserNumber = (EditText) findViewById(R.id.user_number);
        mUserNumber.setText(phoneNumber);

        mPassword = (EditText) findViewById(R.id.user_password);
        mName = (EditText) findViewById(R.id.user_name);

        Button signUpButton = (Button) findViewById(R.id.sign_up_button);
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNumber = mUserNumber.getText().toString();
                ParseUser user = new ParseUser();
                user.setUsername(phoneNumber);
                user.setPassword(mPassword.getText().toString());

                user.put("name", mName.getText().toString());

                user.signUpInBackground(new SignUpCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            SignInActivity.this.finish();
                        } else {
                            Log.d("HERE", e.getMessage().toString());
                        }
                    }
                });
            }
        });

        Button logInButton = (Button) findViewById(R.id.log_in_button);
        logInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logInInBackground(mUserNumber.getText().toString(), mPassword.getText().toString(),
                        new LogInCallback() {
                            @Override
                            public void done(ParseUser parseUser, ParseException e) {
                                if (e == null) {
                                    SignInActivity.this.finish();
                                } else {
                                    Log.d("HERE", e.getMessage().toString());
                                }
                            }
                        });
            }
        });


    }
}
