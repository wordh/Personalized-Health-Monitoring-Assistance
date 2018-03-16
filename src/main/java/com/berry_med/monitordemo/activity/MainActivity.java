package com.berry_med.monitordemo.activity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.berry_med.monitordemo.R;
import com.berry_med.monitordemo.bluetooth.BTController;
import com.berry_med.monitordemo.data.DataParser;
import com.berry_med.monitordemo.data.ECG;
import com.berry_med.monitordemo.data.NIBP;
import com.berry_med.monitordemo.data.SpO2;
import com.berry_med.monitordemo.data.Temp;
import com.berry_med.monitordemo.dialog.BluetoothDeviceAdapter;
import com.berry_med.monitordemo.dialog.SearchDevicesDialog;
import com.berry_med.monitordemo.waveform.WaveForm;
import com.berry_med.monitordemo.waveform.WaveFormParams;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements BTController.Listener, DataParser.onPackageReceivedListener, View.OnClickListener{

    //firebase auth object
    private FirebaseAuth firebaseAuth;

    //firebase database reference
    private DatabaseReference databaseReference;

    //view objects
    private TextView textViewUserEmail;
    private Button buttonLogout;

    private BTController mBtController;

    //UI
    private Button   btnBtCtr;
    private TextView tvBtinfo;
    private TextView tvECGinfo;
    private TextView tvSPO2info;
    private TextView tvTEMPinfo;
    private TextView tvNIBPinfo;
    private LinearLayout llAbout;
    private TextView tvFWVersion;
    private TextView tvHWVersion;
    private SurfaceView svSpO2;
    private SurfaceView svECG;
    private WaveForm wfSpO2;
    private WaveForm wfECG;


    //Bluetooth
    BluetoothDeviceAdapter     mBluetoothDeviceAdapter;
    SearchDevicesDialog        mSearchDialog;
    ProgressDialog             mConnectingDialog;
    ArrayList<BluetoothDevice> mBluetoothDevices;

    //data
    DataParser mDataParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        initView();

        //initializing firebase authentication object
        firebaseAuth = FirebaseAuth.getInstance();

        //if the user is not logged in
        //that means current user will return null
        if(firebaseAuth.getCurrentUser() == null){
            //closing this activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

        databaseReference = FirebaseDatabase.getInstance().getReference();


        //getting current user
        FirebaseUser user = firebaseAuth.getCurrentUser();

        //initializing views
        textViewUserEmail = (TextView) findViewById(R.id.textViewUserEmail);
        buttonLogout = (Button) findViewById(R.id.logout);

        //displaying logged in user name
        textViewUserEmail.setText("Welcome "+user.getEmail());

        //adding listener to button
        buttonLogout.setOnClickListener(this);
    }




    private void initData() {
        // enable the Bluetooth Adapter
        mBtController = BTController.getDefaultBTController(this);
        mBtController.registerBroadcastReceiver(this);
        mBtController.enableBtAdpter();

        mDataParser = new DataParser(this);
        mDataParser.start();
    }

    private void initView() {
        //UI widgets
        btnBtCtr  = (Button) findViewById(R.id.btnBtCtr);
        tvBtinfo = (TextView) findViewById(R.id.tvbtinfo);
        tvECGinfo = (TextView) findViewById(R.id.tvECGinfo);
        tvSPO2info = (TextView) findViewById(R.id.tvSPO2info);
        tvTEMPinfo = (TextView) findViewById(R.id.tvTEMPinfo);
        tvNIBPinfo = (TextView) findViewById(R.id.tvNIBPinfo);
        llAbout = (LinearLayout) findViewById(R.id.llAbout);
        tvFWVersion = (TextView) findViewById(R.id.tvFWverison);
        tvHWVersion = (TextView) findViewById(R.id.tvHWverison);
        svSpO2 = (SurfaceView) findViewById(R.id.svSpO2);
        svECG = (SurfaceView) findViewById(R.id.svECG);

        //Bluetooth Search Dialog
        mBluetoothDevices = new ArrayList<>();
        mBluetoothDeviceAdapter = new BluetoothDeviceAdapter(MainActivity.this,mBluetoothDevices);
        mSearchDialog = new SearchDevicesDialog(MainActivity.this,mBluetoothDeviceAdapter) {
            @Override
            public void onStartSearch() {
                mBtController.startScan(true);
            }

            @Override
            public void onClickDeviceItem(int pos) {
                BluetoothDevice device = mBluetoothDevices.get(pos);
                mBtController.startScan(false);
                mBtController.connect(MainActivity.this,device);
                tvBtinfo.setText(device.getName() +": " + device.getAddress());
                mConnectingDialog.show();
                mSearchDialog.dismiss();
            }
        };
        mSearchDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                mBtController.startScan(false);
            }
        });

        mConnectingDialog = new ProgressDialog(MainActivity.this);
        mConnectingDialog.setMessage("Connecting...");

        //About Information
        llAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBtController.write(DataParser.CMD_FW_VERSION);
                mBtController.write(DataParser.CMD_HW_VERSION);
            }
        });

        //SpO2 & ECG waveform
        wfSpO2 = new WaveForm(MainActivity.this,svSpO2,new WaveFormParams(3,2,new int[]{0,100}));
        wfECG  = new WaveForm(MainActivity.this,svECG,new WaveFormParams(1,5,new int[]{0,250}));

    }

    public void onClick(View v){
        switch (v.getId()){
            case R.id.btnBtCtr:
                if(!mBtController.isBTConnected()){
                    mSearchDialog.show();
                    mSearchDialog.startSearch();
                    mBtController.startScan(true);
                }
                else {
                    mBtController.disconnect();
                    tvBtinfo.setText("");
                }
                break;
            case R.id.btnNIBPStart:
                mBtController.write(DataParser.CMD_START_NIBP);
                break;
            case R.id.btnNIBPStop:
                mBtController.write(DataParser.CMD_STOP_NIBP);
                break;
        }

        //if logout is pressed
        if(v == buttonLogout){
            //logging out the user
            firebaseAuth.signOut();
            //closing activity
            finish();
            //starting login activity
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
        System.exit(0); //for release "mBluetoothDevices" on key_back down
        mBtController.unregisterBroadcastReceiver(this);
    }






    //BTController implements
    @Override
    public void onFoundDevice(BluetoothDevice device) {
        if(mBluetoothDevices.contains(device))
            return;
        mBluetoothDevices.add(device);
        mBluetoothDeviceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStopScan() {
        mSearchDialog.stopSearch();
    }

    @Override
    public void onStartScan() {
        mBluetoothDevices.clear();
        mBluetoothDeviceAdapter.notifyDataSetChanged();
    }

    @Override
    public void onConnected() {
        mConnectingDialog.setMessage("Connected √");
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mConnectingDialog.dismiss();
                    }
                });
            }
        },800);

        btnBtCtr.setText("Disconnect");
    }

    @Override
    public void onDisconnected() {
        btnBtCtr.setText("Search Devices");
    }

    @Override
    public void onReceiveData(byte[] dat) {
        mDataParser.add(dat);
    }






    //DataParser implements
    @Override
    public void onSpO2WaveReceived(int dat) {
        wfSpO2.add(dat);


    }

    @Override
    public void onSpO2Received(final SpO2 spo2) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvSPO2info.setText(spo2.toString());
            }
        });

        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child(user.getUid()).setValue(spo2);
    }

    @Override
    public void onECGWaveReceived(int dat) {
        wfECG.add(dat);
    }

    @Override
    public void onECGReceived(final ECG ecg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvECGinfo.setText(ecg.toString());
                
            }
        });

        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child(user.getUid()).setValue(ecg);
    }

    @Override
    public void onTempReceived(final Temp temp) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvTEMPinfo.setText(temp.toString());
            }
        });
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child(user.getUid()).setValue(temp);
    }

    @Override
    public void onNIBPReceived(final NIBP nibp) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvNIBPinfo.setText(nibp.toString());
            }
        });
        FirebaseUser user = firebaseAuth.getCurrentUser();
        databaseReference.child(user.getUid()).setValue(nibp);
    }

    @Override
    public void onFirmwareReceived(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvFWVersion.setText("Firmware Version:" +str);
            }
        });
    }

    @Override
    public void onHardwareReceived(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvHWVersion.setText("Hardware Version:" +str);
            }
        });
    }
}
