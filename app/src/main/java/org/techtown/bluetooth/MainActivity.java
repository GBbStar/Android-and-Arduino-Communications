package org.techtown.bluetooth;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class MainActivity extends AppCompatActivity {
    TextView inputTemp, inputHumid;
    Button setTemp, setHumid;
    NumberPicker minTemp1, minTemp2, maxTemp1, maxTemp2, minHumid1, minHumid2, maxHumid1, maxHumid2;
    private BluetoothSPP bt;
    ArrayList<String> temp = new ArrayList<String>();
    ArrayList<String> humid = new ArrayList<String>();
    int count = 0;
    ImageView showHistory, setAlarm;
    Double minTemperature = 0.0, maxTemperature = 100.0;
    Double minHumidity =0.0, maxHumidity = 100.0;
    boolean alarmState = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = new BluetoothSPP(this); //Initializing
        inputTemp = (TextView)findViewById(R.id.inputTemp);
        inputHumid = (TextView)findViewById(R.id.inputHumid);

        setTemp = (Button) findViewById(R.id.setTemp);
        setHumid = (Button) findViewById(R.id.setHumid);

        minTemp1 = (NumberPicker) findViewById(R.id.picker_mintemp);
        minTemp1.setMinValue(0);
        minTemp1.setMaxValue(100);

        minTemp2 = (NumberPicker) findViewById(R.id.picker_mintemp2);
        minTemp2.setMinValue(0);
        minTemp2.setMaxValue(9);


        maxTemp1 = (NumberPicker) findViewById(R.id.picker_maxtemp);
        maxTemp1.setMinValue(0);
        maxTemp1.setMaxValue(100);
        maxTemp1.setValue(100);

        maxTemp2 = (NumberPicker) findViewById(R.id.picker_maxtemp2);
        maxTemp2.setMinValue(0);
        maxTemp2.setMaxValue(9);

        //

        minHumid1 = (NumberPicker) findViewById(R.id.picker_minhumid);
        minHumid1.setMinValue(0);
        minHumid1.setMaxValue(100);

        minHumid2 = (NumberPicker) findViewById(R.id.picker_minhumid2);
        minHumid2.setMinValue(0);
        minHumid2.setMaxValue(9);


        maxHumid1 = (NumberPicker) findViewById(R.id.picker_maxhumid);
        maxHumid1.setMinValue(0);
        maxHumid1.setMaxValue(100);
        maxHumid1.setValue(100);

        maxHumid2 = (NumberPicker) findViewById(R.id.picker_maxhumid2);
        maxHumid2.setMinValue(0);
        maxHumid2.setMaxValue(9);

        showHistory = (ImageView) findViewById(R.id.history);
        showHistory.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), SecondActivity.class);
                myIntent.putExtra("temp", temp);
                myIntent.putExtra("humid", humid);
                startActivity(myIntent);
            }
        });

        if (!bt.isBluetoothAvailable()) { //블루투스 사용 불가
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }

        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() { //연결됐을 때
            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() { //연결해제
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() { //연결실패
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView btnConnect = findViewById(R.id.bluetoothPower); //연결시도
        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });



        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() { //데이터 수신
            public void onDataReceived(byte[] data, String message) {
                String[] array = message.split(",");
                inputTemp.setText(array[0].concat("C"));
                inputHumid.setText(array[1].concat("%"));
                temp.add(array[0]);
                humid.add(array[1]);
                count++;

            }
        });






        setTemp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minTemperature =  minTemp1.getValue() + (minTemp2.getValue() / 10.0);
                maxTemperature =  maxTemp1.getValue() + (maxTemp2.getValue() / 10.0);
                Toast.makeText(getApplicationContext(), minTemperature.toString()+" - "+maxTemperature.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        setHumid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minHumidity =  minHumid1.getValue() + (minHumid2.getValue() / 10.0);
                maxHumidity =  maxHumid1.getValue() + (maxHumid2.getValue() / 10.0);
                Toast.makeText(getApplicationContext(), minHumidity.toString()+" - "+maxHumidity.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        setAlarm = (ImageView) findViewById(R.id.setAlarm);
        setAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(alarmState){
                    alarmState = false;
                    setAlarm.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);

                }
                else{
                    alarmState = true;
                    setAlarm.setColorFilter(Color.BLACK, PorterDuff.Mode.SRC_IN);
                }
            }
        });

        new Thread(new Runnable() {
            @Override public void run() {
                while (true) {
                    if (alarmState) {
                        if ((Double.parseDouble(temp.get(temp.size() - 1)) < minTemperature) || (Double.parseDouble(temp.get(temp.size() - 1)) > maxTemperature)
                            || (Double.parseDouble(humid.get(humid.size() - 1)) < minHumidity) || (Double.parseDouble(humid.get(temp.size() - 1)) > maxHumidity)) {
                            Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            vibrator.vibrate(50);
                            //Toast.makeText(getApplicationContext(), "진동", Toast.LENGTH_SHORT).show();
                            //return;
                        } else {
                            //Toast.makeText(getApplicationContext(), "진동x", Toast.LENGTH_SHORT).show();
                            Log.i("종료", temp.get(temp.size() - 1));
                        }
                    } else {
                        //Toast.makeText(getApplicationContext(), "진동x", Toast.LENGTH_SHORT).show();
                        //Log.i("종료", temp.get(temp.size() - 1));
                    }
                }
            }
        }).start();

    }

    public void onDestroy() {
        super.onDestroy();
        bt.stopService(); //블루투스 중지
    }

    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) { //
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기 끼리
                //setup();
            }
        }
    }
/*
    public void setup() {
        Button btnSend = findViewById(R.id.btnSend); //데이터 전송
        btnSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bt.send("Text", true);
            }
        });
    }
*/
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                //setup();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}






