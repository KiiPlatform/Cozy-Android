package com.kii.cozy;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WifiPasswordActivity extends AppCompatActivity {

    EditText passwordEditText;

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_password);

        TextView SSID = (TextView)findViewById(R.id.SSID);
        passwordEditText = (EditText)findViewById(R.id.password);
        Button next = (Button)findViewById(R.id.next);

        intent = this.getIntent();

        SSID.setText("SSID: "+intent.getStringExtra("SSID"));

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                WifiConfiguration wifiConfiguration = new WifiConfiguration();
                wifiConfiguration.SSID = String
                        .format("\"%s\"", intent.getStringExtra("SSID"));
                wifiConfiguration.preSharedKey = String
                        .format("\"%s\"", passwordEditText.getText().toString());

                WifiManager wifiManager = (WifiManager) getSystemService(
                        WIFI_SERVICE);
                //remember id
                int netId = wifiManager.addNetwork(wifiConfiguration);
                wifiManager.disconnect();
                wifiManager.enableNetwork(netId, true);
                wifiManager.reconnect();

                Intent intent = new Intent(WifiPasswordActivity.this, DeviceScanActivity.class);
                startActivity(intent);
            }
        });
    }

    public class WifiReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMan.getActiveNetworkInfo();
            if (netInfo != null && netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                Log.d("WifiReceiver", "Have Wifi Connection");
                Toast.makeText(WifiPasswordActivity.this, "connected", Toast.LENGTH_LONG).show();
            }else{
                Log.d("WifiReceiver", "Don't have Wifi Connection");
            }
        }
    };
}
