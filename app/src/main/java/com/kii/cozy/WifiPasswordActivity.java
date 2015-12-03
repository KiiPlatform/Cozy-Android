package com.kii.cozy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WifiPasswordActivity extends AppCompatActivity {

    public static final String TAG = WifiPasswordActivity.class.getSimpleName();

    EditText passwordEditText;

    Intent intent;

    CheckBox mCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_password);

        TextView SSID = (TextView) findViewById(R.id.SSID);
        passwordEditText = (EditText) findViewById(R.id.password);
        Button next = (Button) findViewById(R.id.next);
        mCheckBox = (CheckBox) findViewById(R.id.checkbox);

        final SharedPreferenceHandler sharedPreferenceHandler = new SharedPreferenceHandler(this);
        if (sharedPreferenceHandler.getWifiPassword() != null) {
            mCheckBox.setChecked(true);
            passwordEditText.setText(sharedPreferenceHandler.getWifiPassword());
            Log.i(TAG, sharedPreferenceHandler.getWifiPassword());
        }

        intent = this.getIntent();

        SSID.setText("SSID: " + intent.getStringExtra("SSID"));

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //connect to the wifi
//                WifiConfiguration wifiConfiguration = new WifiConfiguration();
//                wifiConfiguration.SSID = String
//                        .format("\"%s\"", intent.getStringExtra("SSID"));
//                wifiConfiguration.preSharedKey = String
//                        .format("\"%s\"", passwordEditText.getText().toString());
//
//                WifiManager wifiManager = (WifiManager) getSystemService(
//                        WIFI_SERVICE);
//                //remember id
//                int netId = wifiManager.addNetwork(wifiConfiguration);
//                wifiManager.disconnect();
//                wifiManager.enableNetwork(netId, true);
//                wifiManager.reconnect();

                if (mCheckBox.isChecked()) {
                    sharedPreferenceHandler.setWifiPassword(passwordEditText.getText().toString());
                } else {
                    sharedPreferenceHandler.clearPassword();
                }

                Intent intent = new Intent(WifiPasswordActivity.this, DeviceScanActivity.class);
                intent.putExtra("SSID", intent.getStringExtra("SSID"));
                intent.putExtra("password", passwordEditText.getText().toString());
                startActivity(intent);
            }
        });
    }

    public class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager conMan = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMan.getActiveNetworkInfo();
            if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                Log.d("WifiReceiver", "Have Wifi Connection");
                Toast.makeText(WifiPasswordActivity.this, "connected", Toast.LENGTH_LONG).show();
            } else {
                Log.d("WifiReceiver", "Don't have Wifi Connection");
            }
        }
    }

    ;
}
