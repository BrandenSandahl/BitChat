package io.bitfountain.matthewparker.bitchat;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.widget.EditText;

public class SignInActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_activity);

        //fetch the phones phone number, this needs a permission in manifest
        TelephonyManager telephonyManager = (TelephonyManager) this.getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        String phoneNumber = telephonyManager.getLine1Number();
        EditText userNumber = (EditText) findViewById(R.id.user_number);
        userNumber.setText(phoneNumber);

    }
}
