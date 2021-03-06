package com.jesus.bluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_ENABLE_BT = 0;
    private static final int REQUEST_DISCOVER_BT = 1;

    TextView mStatusBlueTv, mPairedTv;
    ImageView mBlueIv;
    Button mOnBtn, mOffBtn, mDiscoverBtn, mPairedBtn;

    BluetoothAdapter mBlueAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStatusBlueTv = findViewById(R.id.statusBluetooth);
        mPairedTv = findViewById(R.id.conecBlu);
        mBlueIv = findViewById(R.id.bluetoothImg);
        mOnBtn = findViewById(R.id.onBtn);
        mOffBtn = findViewById(R.id.offBtn);
        mDiscoverBtn = findViewById(R.id.discoverableBtn);
        mPairedBtn = findViewById(R.id.pairedBtn);

        //Adaptador
        mBlueAdapter = BluetoothAdapter.getDefaultAdapter();

        //Comprueba si bluetooth está disponible o no
        if (mBlueAdapter == null) {
            mStatusBlueTv.setText("Bluetooth no esta disponible");
        } else {
            mStatusBlueTv.setText("Bluetooth esta disponible");
        }

        //establecer la imagen de acuerdo con el estado de bluetooth (ON / OFF)
        if (mBlueAdapter.isEnabled()) {
            mBlueIv.setImageResource(R.drawable.ic_baseline_bluetooth_24);
        } else {
            mBlueIv.setImageResource(R.drawable.ic_baseline_bluetooth_disabled_24);
        }

        //Encender bluetooth
        mOnBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                if (!mBlueAdapter.isEnabled()) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Encendiendo Bluetooth...", Toast.LENGTH_SHORT);
                    toast.show();
                    //intent para encender bluetooth
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

                    startActivityForResult(intent, REQUEST_ENABLE_BT);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "El bluetooth ya esta encendido", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        // encontrar dispositivos
        mDiscoverBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {

                if (!mBlueAdapter.isDiscovering()) {
                    Toast toast = Toast.makeText(getApplicationContext(), "Descubriendo Dispositivos", Toast.LENGTH_SHORT);
                    toast.show();
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                    startActivityForResult(intent, REQUEST_DISCOVER_BT);
                }
            }
        });

        //boton de apagado click
        mOffBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View view) {
                if (mBlueAdapter.isEnabled()) {

                    mBlueAdapter.disable();
                    Toast toast = Toast.makeText(getApplicationContext(), "Apagando bluetooth", Toast.LENGTH_SHORT);
                    toast.show();

                    mBlueIv.setImageResource(R.drawable.ic_baseline_bluetooth_disabled_24);
                } else {
                    Toast toast = Toast.makeText(getApplicationContext(), "El bluetooth ya esta apagado", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });

        //emparejar dispositivos
        mPairedBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                if (mBlueAdapter.isEnabled()) {
                    mPairedTv.setText("Dispositivos emparejados");

                    @SuppressLint("MissingPermission") Set<BluetoothDevice> devices = mBlueAdapter.getBondedDevices();
                    for (BluetoothDevice device: devices){
                        mPairedTv.append("\nDispositivo: " + device.getName()+ ", " + device);
                    }
                }
                else {
                    //Bluetooth está desactivado, por lo que no se pueden emparejar dispositivos

                    Toast toast = Toast.makeText(getApplicationContext(), "Activa bluetooth para emparejar dispositivos ", Toast.LENGTH_SHORT);
                    toast.show();
                }
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case REQUEST_ENABLE_BT:
                if (resultCode == RESULT_OK){
                    //bluetooth esta encendido
                    mBlueIv.setImageResource(R.drawable.ic_baseline_bluetooth_24);
                    showToast("Bluetooth esta encendido");
                }
                else {
                    //usuario deniega el bluethooth
                    showToast("No se pudo encender el bluetooth");
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    //toast mensaje
    private void showToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }
}