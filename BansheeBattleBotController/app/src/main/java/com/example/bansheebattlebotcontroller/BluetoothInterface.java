package com.example.bansheebattlebotcontroller;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothProfile;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.ParcelUuid;
import android.util.Log;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/*Bluetooth class that handles everything having to do with bluetooth communication to the bot. Implements a queue to
automatically prevent commands being lost

 */
public class BluetoothInterface {

    private Context mContext;
    private String TAG = "BluetoothInterface";
    private BluetoothLeScanner bluetoothLeScanner;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothGatt bluetoothGatt;

    private boolean mScanning = false;
    private boolean mConnected = false;
    private boolean serviceDiscovered = false;

    private BluetoothManager bm;

    //receiver
    private static BLEUpdateReceiver nReceiver;
    public static final String BLE_UPDATE = "com.companionApp.BLE_UPDATE";

    // Stops scanning after 10 seconds.
    public static final String deviceUUID = "b2ee9903-faef-4800-970b-28c4f243893f";

    public static final String RIGHT_MOTOR_UUID = "b2ee9903-faef-4800-970b-28c4f2438940";
    public static final String LEFT_MOTOR_UUID = "b2ee9903-faef-4800-970b-28c4f2438941";
    public static final String WEAPON_SPEED_UUID = "b2ee9903-faef-4800-970b-28c4f2438942";
    public static final String BATTERY_VOLTAGE_UUID = "b2ee9903-faef-4800-970b-28c4f2438943";
    private boolean writeInProgress = false;
    private ArrayList<QueuedOperation> operationQueue = new ArrayList<QueuedOperation>(0);

    public int getQueueLength(){
        return operationQueue.size();
    }

    public void clearQueue(){
        for(int a = 0; a < operationQueue.size(); a++) {
            operationQueue.remove(a);
        }
            }




    //init bluetooth interface
    public BluetoothInterface(Context mContext, BluetoothManager bm) {
        Log.d(TAG, "Bluetooth Interface Started");

        this.bm = bm;
        this.mContext = mContext;

        //set connection status and service discovery status, these will be updated later from the callbacks
        mConnected = false;
        serviceDiscovered = false;

        //attach the receiver that's used to update the remote BLE device
        try {
            //init notification receiver
            nReceiver = new BLEUpdateReceiver();
            IntentFilter filter = new IntentFilter();
            filter.addAction(BLE_UPDATE);
            mContext.registerReceiver(nReceiver, filter);
            Log.i(TAG, "Re-registered broadcast reciever");
        } catch (IllegalArgumentException e) {
            //this is basically designed to crash so eh whatever
            Log.e(TAG, "Failed to register broadcast reciever in BLESend: " + e.getLocalizedMessage());
        }

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

    //returns true if service is discovered. We want to know this because we can be connected without services being discoverred,
    //but we cannot send any data in that state.
    public boolean isServiceDiscovered() {
        return serviceDiscovered;
    }

    //writes data to the BLE device, if there is already an operation in progress then it will queue the data and send it as soon as possible.
    //queue is serviced the by the broadcast receiver below.
    public boolean write(String str, String uuid) {
        writeInProgress = true;
        BluetoothGattCharacteristic bgc;
        try {
            bgc = bluetoothGatt.getService(UUID.fromString(deviceUUID)).getCharacteristic(UUID.fromString(uuid));
        } catch (Exception e) {
            operationQueue.add(new QueuedOperation(uuid, str));
            return false;
        }
        if (bgc != null) {
            bgc.setValue(str);
            if (bluetoothGatt.writeCharacteristic(bgc)) {
                Log.d(TAG, "transmitted:" + str);
            } else {
                operationQueue.add(new QueuedOperation(uuid, str));
                return false;
            }
        } else {
            Log.e(TAG, "Characteristic is null");
        }
        return false;
    }

    //callbacks, only implemented the ones that are used below.
    private final BluetoothGattCallback gattCallback =
            new BluetoothGattCallback() {

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
                        Intent i = new Intent(BLE_UPDATE);
                        mContext.sendBroadcast(i);
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
                        Log.i(TAG, "Found characteristic: " + new String(chars.get(a).getUuid().toString()));
                        if ((chars.get(a).getProperties() & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                            gatt.setCharacteristicNotification(chars.get(a), true);
                            Log.i(TAG, "Subscribed to characteristic: " + new String(chars.get(a).getUuid().toString()));
                        }
                    }
                    serviceDiscovered = true;
                }


                @Override
                public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
                    super.onCharacteristicWrite(gatt, characteristic, status);
                    //indicate we can write again
                    writeInProgress = false;

                    //if success then try to send the next bit of data by sending a broadcast
                    //to the receiver and triggering an update.
                    if (status == BluetoothGatt.GATT_SUCCESS) {
                        Log.d(TAG, "BLE Write success");
                    } else {
                        //print scary warning message if something goes wrong
                        Log.e(TAG, "BLE Write failed");

                    }

                    //trigger broadcast receiver to service any items that may be in queue
                    Intent i = new Intent(BLE_UPDATE);
                    mContext.sendBroadcast(i);
                }

                @Override
                public void onCharacteristicChanged(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic) {
                    super.onCharacteristicChanged(gatt, characteristic);
                    if (characteristic.getUuid().toString().equals(BATTERY_VOLTAGE_UUID)) {
                        SecondFragment.batteryVoltage = Float.parseFloat(new String(characteristic.getValue(), StandardCharsets.US_ASCII));
                    }
                }
            };


    //broadcast receiver required in order to service the queue, this will be called whenever a write operation is completed
    //indicating that the bot would be ready to accept another write.
    class BLEUpdateReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "Bluetooth update request received, queue size: " + operationQueue.size());
            if (operationQueue.size() > 0) {
                write(operationQueue.get(0).getMessage(), operationQueue.get(0).getUUID());
                operationQueue.remove(0);
            }

        }
    }

    private ScanCallback cb =
            new ScanCallback() {
                @Override
                public void onScanResult(int callbackType, ScanResult result) {
                    super.onScanResult(callbackType, result);
                    if (!mConnected) {
                        Log.d(TAG, "Device Found, saving");
                        bluetoothGatt = result.getDevice().connectGatt(mContext, false, gattCallback);
                        bluetoothLeScanner.stopScan(cb);
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

//class (although it's basically just a data structure at this point) to hold the strings for items that need to be queued
//due to operations already being in progress.
class QueuedOperation {
    private String UUID, message;

    public QueuedOperation(String UUID, String message) {
        this.UUID = UUID;
        this.message = message;
    }

    String getMessage() {
        return message;
    }

    String getUUID() {
        return UUID;
    }
}

