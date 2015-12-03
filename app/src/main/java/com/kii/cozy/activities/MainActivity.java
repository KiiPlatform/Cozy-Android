package com.kii.cozy.activities;

import com.kii.cozy.R;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnClickListener {

    public static final String TAG = MainActivity.class.getSimpleName();

    Button setWifi;

    WifiManager wifiManager;

    WifiReceiver receiverWifi;

    List<ScanResult> wifiList;

    List<String> listOfProvider;

    ListAdapter adapter;

    ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listOfProvider = new ArrayList<String>();

		/*setting the resources in class*/
        mListView = (ListView) findViewById(R.id.list_view_wifi);
        setWifi = (Button) findViewById(R.id.btn_wifi);

        setWifi.setOnClickListener(this);
        wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
                /*checking wifi connection
                 * if wifi is on searching available wifi provider*/
        if (wifiManager.isWifiEnabled() == true) {
            setWifi.setText("ON");
            scaning();
        }

                /*opening a detail dialog of provider on click   */
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                    final int position, long id) {
                Intent intent = new Intent(MainActivity.this, WifiPasswordActivity.class);
                intent.putExtra("BSSID", wifiList.get(position).BSSID);
                intent.putExtra("SSID", wifiList.get(position).SSID);
                startActivity(intent);

            }
        });
    }

    private void scaning() {
        // wifi scaned value broadcast receiver
        receiverWifi = new WifiReceiver();
        // Register broadcast receiver
        // Broacast receiver will automatically call when number of wifi
        // connections changed
        registerReceiver(receiverWifi, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        wifiManager.startScan();

    }

    /*setting the functionality of ON/OFF button*/
    @Override
    public void onClick(View arg0) {
                /* if wifi is ON set it OFF and set button text "OFF" */
        if (wifiManager.isWifiEnabled() == true) {
            wifiManager.setWifiEnabled(false);
            setWifi.setText("OFF");
            mListView.setVisibility(ListView.GONE);
        }
                /* if wifi is OFF set it ON
                 * set button text "ON"
		 * and scan available wifi provider*/
        else if (wifiManager.isWifiEnabled() == false) {
            wifiManager.setWifiEnabled(true);
            setWifi.setText("ON");
            mListView.setVisibility(ListView.VISIBLE);
            scaning();
        }
    }

    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiverWifi);
    }

    protected void onResume() {
        registerReceiver(receiverWifi, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        super.onResume();
    }

    class WifiReceiver extends BroadcastReceiver {

        // This method call when number of wifi connections changed
        public void onReceive(Context c, Intent intent) {
            wifiList = wifiManager.getScanResults();

			/* sorting of wifi provider based on level */
            Collections.sort(wifiList, new Comparator<ScanResult>() {
                @Override
                public int compare(ScanResult lhs, ScanResult rhs) {
                    return (lhs.level > rhs.level ? -1
                            : (lhs.level == rhs.level ? 0 : 1));
                }
            });
            listOfProvider.clear();
            String providerName;
            for (int i = 0; i < wifiList.size(); i++) {
                                /* to get SSID and BSSID of wifi provider*/
                providerName = (wifiList.get(i).SSID).toString()
                        + "\n" + (wifiList.get(i).BSSID).toString();
                listOfProvider.add(providerName);
            }
                        /*setting list of all wifi provider in a List*/
            adapter = new ListAdapter(MainActivity.this, listOfProvider);
            mListView.setAdapter(adapter);

            adapter.notifyDataSetChanged();
        }
    }

    class ListAdapter extends ArrayAdapter<String> {

        Activity mActivity;

        List<String> wifiName;

        private LayoutInflater inflater;

        public ListAdapter(Activity a, List<String> wifiName) {
            super(a, R.layout.single_list, wifiName);
            mActivity = a;
            inflater = LayoutInflater.from(mActivity);
            this.wifiName = wifiName;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = inflater.inflate(R.layout.single_list, parent, false);
            TextView wifiProvider = (TextView) convertView
                    .findViewById(R.id.txt_wifi_provider);
            wifiProvider.setText(wifiName.get(position));
            return convertView;
        }

    }
}
