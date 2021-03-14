package com.example.bansheebattlebotcontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.ParcelUuid;
import android.util.Log;
import android.widget.Toast;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static androidx.core.content.ContextCompat.getSystemService;

public class BluetoothInterface {

    private int connectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    public final static String ACTION_GATT_CONNECTED = "com.example.bluetooth.le.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "com.example.bluetooth.le.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "com.example.bluetooth.le.ACTION_GATT_SERVICES_DISCOVERED";
    public final static String ACTION_DATA_AVAILABLE = "com.example.bluetooth.le.ACTION_DATA_AVAILABLE";
    public final static String EXTRA_DATA = "com.example.bluetooth.le.EXTRA_DATA";

    private Context mContext;
    private String TAG = "BluetoothInterface";
    private BluetoothLeScanner bluetoothLeScanner;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;

    private boolean mScanning = false;
    private boolean mConnected = false;
    private boolean serviceDiscovered = false;

    private BluetoothManager bm;


    // Stops scanning after 10 seconds.
    private static final long SCAN_PERIOD = 20000;
    public static final String deviceUUID = "b2ee9903-faef-4800-970b-28c4f243893f";

    public static final String RIGHT_MOTOR_UUID = "b2ee9903-faef-4800-970b-28c4f2438940";
    public static final String LEFT_MOTOR_UUID = "b2ee9903-faef-4800-970b-28c4f2438941";
    public static final String WEAPON_SPEED_UUID = "b2ee9903-faef-4800-970b-28c4f2438942";
    public static final String BATTERY_VOLTAGE_UUID = "b2ee9903-faef-4800-970b-28c4f2438943";

    public BluetoothInterface(Context mContext, BluetoothManager bm) {
        Log.d(TAG, "Bluetooth Interface Started");

        this.bm = bm;
        this.mContext = mContext;

        bluetoothAdapter = bm.getAdapter();
        bluetoothLeScanner = bluetoothAdapter.getBluetoothLeScanner();

        // Ensures Bluetooth is available on the device and it is enabled. If not,
        // displays a dialog requesting user permission to enable Bluetooth.
        if (bluetoothAdapter == null || !bluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            Log.d(TAG, "BLUETOOTH NOT ENABLED");
            //startActivityForResult(enableBtIntent, BluetoothAdapter.REQUEST_ENABLE_BT);
        }

        Log.d(TAG, "Beginning Scan");
        scanLeDevice();
    }

    public boolean isConnected(){
        return mConnected;
    }


    private final BluetoothGattCallback gattCallback =
            new BluetoothGattCallback() {
                @Override
                public void onPhyUpdate(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
                    super.onPhyUpdate(gatt, txPhy, rxPhy, status);
                }

                @Override
                public void onPhyRead(BluetoothGatt gatt, int txPhy, int rxPhy, int status) {
                    super.onPhyRead(gatt, txPhy, rxPhy, status);
                }

                @Override
                public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
                    super.onConnectionStateChange(gatt, status, newState);
                    //device is connected
                    if (newState == BluetoothProfile.STATE_CONNECTED) {

                        //set connection state
                        mConnected = true;

                        //request higher connection priority (increases service discovery speed)
                        bluetoothGatt.requestConnectionPriority(BluetoothGatt.CONNECTION_PRIORITY_HIGH);

                        //discover services that the device has available
                        gatt.discoverServices();


                        //device is disconnected
                    } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                        //update status variables
                        mConnected = false;

                        Log.i(TAG, "Disconnected from GATT server.");

                    } else {
                        //this isn't actually possible but whatever
                        Log.i(TAG, "Other status change in BLE connection:" + newState);
                    }
                }

                @Override
                public void onServicesDiscovered(BluetoothGatt gatt, int status) {
                    super.onServicesDiscovered(gatt, status);
                    //log spam

                    //if any of the characteristics available are subscribeable then subscribe to them
                    List<BluetoothGattCharacteristic> chars = gatt.getService(UUID.fromString(deviceUUID)).getCharacteristics();

                    for (int a = 0; a < chars.size(); a++) {
                        if ((chars.get(a).getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                            gatt.setCharacteristicNotification(chars.get(a), true);
                            Log.i(TAG, "Subscribed to characteristic: " + new String(chars.get(a).getUuid().toString()));
                        }
                    }
                }

                @Override
                public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                    super.onCharacteristicRead(gatt, characteristic, status);
                }

                @Override
                public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                    super.onCharacteristicWrite(gatt, characteristic, status);
                }

                @Override
                public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                    super.onCharacteristicChanged(gatt, characteristic);
                }

                @Override
                public void onDescriptorRead(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                    super.onDescriptorRead(gatt, descriptor, status);
                }

                @Override
                public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
                    super.onDescriptorWrite(gatt, descriptor, status);
                }

                @Override
                public void onReliableWriteCompleted(BluetoothGatt gatt, int status) {
                    super.onReliableWriteCompleted(gatt, status);
                }

                @Override
                public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
                    super.onReadRemoteRssi(gatt, rssi, status);
                }

                @Override
                public void onMtuChanged(BluetoothGatt gatt, int mtu, int status) {
                    super.onMtuChanged(gatt, mtu, status);
                }
            };

    private ScanCallback cb =
            new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    if (!mConnected) {
                        bluetoothLeScanner.stopScan(cb);
                        Log.d(TAG, "Device Found, saving");
                        bluetoothGatt = result.getDevice().connectGatt(mContext, true, gattCallback);

                    }
                }
            };


    //https://medium.com/@martijn.van.welie/making-android-ble-work-part-1-a736dcd53b02
    private void scanLeDevice() {
        UUID[] serviceUUIDs = new UUID[]{UUID.fromString(deviceUUID)};
        List<ScanFilter> filters = null;
        if (serviceUUIDs != null) {
            filters = new ArrayList<>();
            for (UUID serviceUUID : serviceUUIDs) {
                ScanFilter filter = new ScanFilter.Builder()
                        .setServiceUuid(new ParcelUuid(serviceUUID))
                        .build();
                filters.add(filter);
            }
        }

        ScanSettings scanSettings = new ScanSettings.Builder()
                .setScanMode(ScanSettings.SCAN_MODE_LOW_POWER)
                .setCallbackType(ScanSettings.CALLBACK_TYPE_ALL_MATCHES)
                .setMatchMode(ScanSettings.MATCH_MODE_AGGRESSIVE)
                .setNumOfMatches(ScanSettings.MATCH_NUM_ONE_ADVERTISEMENT)
                .setReportDelay(0L)
                .build();

        if (!mScanning) {
            mScanning = true;
            bluetoothLeScanner.startScan(filters, scanSettings, cb);
        } else {
            mScanning = false;
            bluetoothLeScanner.stopScan(cb);
        }
    }
}
