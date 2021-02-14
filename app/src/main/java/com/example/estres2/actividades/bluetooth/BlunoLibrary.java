package com.example.estres2.actividades.bluetooth;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.estres2.R;
import com.example.estres2.almacenamiento.basededatos.DB;
import com.example.estres2.almacenamiento.entidades.wearable.Wearable;

import java.util.ArrayList;
import java.util.List;

public abstract class BlunoLibrary extends AppCompatActivity {
    private final Context mainContext = this;

    public abstract void onConectionStateChange(connectionStateEnum theconnectionStateEnum);

    public abstract void onSerialReceived(String theString);

    public void serialSend(String theString) {
        if (mConnectionState == connectionStateEnum.isConnected) {
            mSCharacteristic.setValue(theString);
            mBluetoothLeService.writeCharacteristic(mSCharacteristic);
        }
    }

    private int mBaudrate = 115200;    //set the default baud rate to 115200
    private String mBaudrateBuffer = "AT+CURRUART=" + mBaudrate + "\r\n";

    public void serialBegin(int baud) {
        mBaudrate = baud;
        mBaudrateBuffer = "AT+CURRUART=" + mBaudrate + "\r\n";
    }

    static class ViewHolder {
        TextView deviceName;
        TextView deviceAddress;
    }

    private static BluetoothGattCharacteristic mSCharacteristic, mModelNumberCharacteristic, mSerialPortCharacteristic, mCommandCharacteristic;
    BluetoothLeService mBluetoothLeService;
    private LeDeviceListAdapter mLeDeviceListAdapter = null;
    private BluetoothAdapter mBluetoothAdapter;
    private boolean mScanning = false;
    AlertDialog mScanDeviceDialog;
    AlertDialog mRegisterDevice;
    private String mDeviceName;
    private String mDeviceAddress;

    public enum connectionStateEnum {isNull, isScanning, isToScan, isConnecting, isConnected, isDisconnecting}

    public connectionStateEnum mConnectionState = connectionStateEnum.isNull;
    private static final int REQUEST_ENABLE_BT = 1;
    private final Handler mHandler = new Handler();
    public boolean mConnected = false;
    private final static String TAG = BlunoLibrary.class.getSimpleName();
    public DB db = new DB(this);

    private final Runnable mConnectingOverTimeRunnable = new Runnable() {
        @Override
        public void run() {
            if (mConnectionState == connectionStateEnum.isConnecting)
                mConnectionState = connectionStateEnum.isToScan;
            onConectionStateChange(mConnectionState);
            mBluetoothLeService.close();
        }
    };

    private final Runnable mDisonnectingOverTimeRunnable = new Runnable() {

        @Override
        public void run() {
            if (mConnectionState == connectionStateEnum.isDisconnecting)
                mConnectionState = connectionStateEnum.isToScan;
            onConectionStateChange(mConnectionState);
            mBluetoothLeService.close();
        }
    };

    public static final String SerialPortUUID = "0000dfb1-0000-1000-8000-00805f9b34fb";
    public static final String CommandUUID = "0000dfb2-0000-1000-8000-00805f9b34fb";
    public static final String ModelNumberStringUUID = "00002a24-0000-1000-8000-00805f9b34fb";

    public void onCreateProcess() {
        if (!initiate()) {
            Toast.makeText(mainContext, R.string.error_bluetooth_not_supported,
                    Toast.LENGTH_SHORT).show();
            ((Activity) mainContext).finish();
        }

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, Context.BIND_AUTO_CREATE);

        // Initializes list view adapter.
        mLeDeviceListAdapter = new LeDeviceListAdapter();
        // Initializes and show the scan Device Dialog
        mScanDeviceDialog = new AlertDialog.Builder(mainContext)
                .setTitle("Buscando Dispositivos BLE").setAdapter(mLeDeviceListAdapter, (dialog, which) -> {
                    final BluetoothDevice device = mLeDeviceListAdapter.getDevice(which);
                    if (device == null)
                        return;
                        scanLeDevice(false);

                    if (device.getName() == null || device.getAddress() == null) {
                        mConnectionState = connectionStateEnum.isToScan;
                        onConectionStateChange(mConnectionState);
                    } else {
                        System.out.println("onListItemClick " + device.getName());
                        System.out.println("Device Name:" + device.getName() + "   " + "Device Name:" + device.getAddress());
                        mDeviceName = device.getName();
                        mDeviceAddress = device.getAddress();

                        if (db.getWearable(mDeviceAddress)) {
                            if (mBluetoothLeService.connect(mDeviceAddress)) {
                                Log.d(TAG, "Connect request success");
                                mConnectionState = connectionStateEnum.isConnecting;
                                onConectionStateChange(mConnectionState);
                                mHandler.postDelayed(mConnectingOverTimeRunnable, 10000);
                            } else {
                                Log.d(TAG, "Connect request fail");
                                mConnectionState = connectionStateEnum.isToScan;
                                onConectionStateChange(mConnectionState);
                            }
                        } else {
                            Log.d(TAG, "El dispositivo no esta registrado");
                            mConnectionState = connectionStateEnum.isToScan;
                            onConectionStateChange(mConnectionState);
                            mRegisterDevice = new AlertDialog.Builder(mainContext)
                                    .setTitle("Registrar Dispositivo").setPositiveButton("Aceptar", (dialog1, which1) -> {
                                        Log.d(TAG, "Aceptar el registro");
                                        Wearable wearable = new Wearable(mDeviceName, mDeviceAddress);
                                        registrarWearable(wearable);

                                        if (mBluetoothLeService.connect(mDeviceAddress)) {
                                            Log.d(TAG, "Connect request success");
                                            mConnectionState = connectionStateEnum.isConnecting;
                                            onConectionStateChange(mConnectionState);
                                            mHandler.postDelayed(mConnectingOverTimeRunnable, 10000);
                                        } else {
                                            Log.d(TAG, "Connect request fail");
                                            mConnectionState = connectionStateEnum.isToScan;
                                            onConectionStateChange(mConnectionState);
                                        }
                                    }).setNegativeButton("Cancelar", (dialog12, which12) -> Log.d(TAG, "Cancelar el registro")).show();
                        }
                    }
                })
                .setOnCancelListener(arg0 -> {
                    System.out.println("mBluetoothAdapter.stopLeScan");
                    mConnectionState = connectionStateEnum.isToScan;
                    onConectionStateChange(mConnectionState);
                    mScanDeviceDialog.dismiss();
                    scanLeDevice(false);
                }).create();
    }

    public void registrarWearable(Wearable wearable) {
        DB bd = new DB(getApplicationContext());
        if (bd.insertWearable(wearable)) {
            Toast.makeText(this, "Se inserto correctamente el Wearable", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No se inserto el wearable", Toast.LENGTH_LONG).show();
        }
    }

    public void onResumeProcess() {
        System.out.println("BlUNOActivity onResume");
        // Ensures Bluetooth is enabled on the device. If Bluetooth is not
        // currently enabled,
        // fire an intent to display a dialog asking the user to grant
        // permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(
                        BluetoothAdapter.ACTION_REQUEST_ENABLE);
                ((Activity) mainContext).startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }

        mainContext.registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
    }

    public void onPauseProcess() {
        /*System.out.println("BLUNOActivity onPause");
        scanLeDevice(false);
        mainContext.unregisterReceiver(mGattUpdateReceiver);
        mLeDeviceListAdapter.clear();
        mConnectionState=connectionStateEnum.isToScan;
        onConectionStateChange(mConnectionState);
        mScanDeviceDialog.dismiss();
        if(mBluetoothLeService!=null)
        {
            mBluetoothLeService.disconnect();
            mHandler.postDelayed(mDisonnectingOverTimeRunnable, 10000);

			mBluetoothLeService.close();
        }
        mSCharacteristic=null;*/
    }


    public void onStopProcess() {
        /*System.out.println("MiUnoActivity onStop");
        if(mBluetoothLeService!=null)
        {
			mBluetoothLeService.disconnect();
            mHandler.postDelayed(mDisonnectingOverTimeRunnable, 10000);
            mHandler.removeCallbacks(mDisonnectingOverTimeRunnable);
            mBluetoothLeService.close();
        }
        mSCharacteristic=null;*/
    }

    public void onDestroyProcess() {
        scanLeDevice(false);
        mainContext.unregisterReceiver(mGattUpdateReceiver);
        mLeDeviceListAdapter.clear();
        mConnectionState = connectionStateEnum.isToScan;
        onConectionStateChange(mConnectionState);
        mScanDeviceDialog.dismiss();

        if (mBluetoothLeService != null) {
            mBluetoothLeService.disconnect();
            mHandler.postDelayed(mDisonnectingOverTimeRunnable, 10000);
            mHandler.removeCallbacks(mDisonnectingOverTimeRunnable);
            mBluetoothLeService.close();
        }
        mSCharacteristic = null;
        mainContext.unbindService(mServiceConnection);
        mBluetoothLeService = null;
    }

    public void onActivityResultProcess(int requestCode, int resultCode) {
        // User chose not to enable Bluetooth.
        if (requestCode == REQUEST_ENABLE_BT
                && resultCode == Activity.RESULT_CANCELED) {
            ((Activity) mainContext).finish();
        }
    }

    boolean initiate() {
        // Use this check to determine whether BLE is supported on the device.
        // Then you can
        // selectively disable BLE-related features.
        if (!mainContext.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_BLUETOOTH_LE)) {
            return false;
        }

        // Initializes a Bluetooth adapter. For API level 18 and above, get a
        // reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager = (BluetoothManager) mainContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        return mBluetoothAdapter != null;
    }

    // Handles various events fired by the Service.
    // ACTION_GATT_CONNECTED: connected to a GATT server.
    // ACTION_GATT_DISCONNECTED: disconnected from a GATT server.
    // ACTION_GATT_SERVICES_DISCOVERED: discovered GATT services.
    // ACTION_DATA_AVAILABLE: received data from the device.  This can be a result of read
    //                        or notification operations.
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @SuppressLint("DefaultLocale")
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            System.out.println("mGattUpdateReceiver->onReceive->action=" + action);

            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                mHandler.removeCallbacks(mConnectingOverTimeRunnable);

            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                mConnectionState = connectionStateEnum.isToScan;
                onConectionStateChange(mConnectionState);
                mHandler.removeCallbacks(mDisonnectingOverTimeRunnable);
                mBluetoothLeService.close();

            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                // Show all the supported services and characteristics on the user interface.
                for (BluetoothGattService gattService : mBluetoothLeService.getSupportedGattServices()) {
                    System.out.println("ACTION_GATT_SERVICES_DISCOVERED  " +
                            gattService.getUuid().toString());
                }
                getGattServices(mBluetoothLeService.getSupportedGattServices());
            } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {
                if (mSCharacteristic == mModelNumberCharacteristic) {
                    if (intent.getStringExtra(BluetoothLeService.EXTRA_DATA).toUpperCase().startsWith("DF BLUNO")) {
                        mBluetoothLeService.setCharacteristicNotification(mSCharacteristic, false);
                        mSCharacteristic = mCommandCharacteristic;
                        String mPassword = "AT+PASSWOR=DFRobot\r\n";
                        mSCharacteristic.setValue(mPassword);
                        mBluetoothLeService.writeCharacteristic(mSCharacteristic);
                        mSCharacteristic.setValue(mBaudrateBuffer);
                        mBluetoothLeService.writeCharacteristic(mSCharacteristic);
                        mSCharacteristic = mSerialPortCharacteristic;
                        mBluetoothLeService.setCharacteristicNotification(mSCharacteristic, true);
                        mConnectionState = connectionStateEnum.isConnected;

                    } else {
                        Toast.makeText(mainContext, "Please select DFRobot devices", Toast.LENGTH_SHORT).show();
                        mConnectionState = connectionStateEnum.isToScan;
                    }
                    onConectionStateChange(mConnectionState);

                } else if (mSCharacteristic == mSerialPortCharacteristic) {
                    onSerialReceived(intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
                }

                System.out.println("displayData " + intent.getStringExtra(BluetoothLeService.EXTRA_DATA));
            }
        }
    };

    protected void buttonScanOnClickProcess() {
        switch (mConnectionState) {
            case isNull:

            case isToScan:
                mConnectionState = connectionStateEnum.isScanning;
                onConectionStateChange(mConnectionState);
                scanLeDevice(true);
                mScanDeviceDialog.show();
                break;

            case isConnected:
                mBluetoothLeService.disconnect();
                mHandler.postDelayed(mDisonnectingOverTimeRunnable, 10000);
                mConnectionState = connectionStateEnum.isDisconnecting;
                onConectionStateChange(mConnectionState);
                break;

            default:
                break;
        }
    }

    void scanLeDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
            System.out.println("mBluetoothAdapter.startLeScan");
            if (mLeDeviceListAdapter != null) {
                mLeDeviceListAdapter.clear();
                mLeDeviceListAdapter.notifyDataSetChanged();
            }

            if (!mScanning) {
                mScanning = true;
                mBluetoothAdapter.startLeScan(mLeScanCallback);
            }
        } else {
            if (mScanning) {
                mScanning = false;
                mBluetoothAdapter.stopLeScan(mLeScanCallback);
            }
        }
    }

    // Code to manage Service lifecycle.
    ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            System.out.println("mServiceConnection onServiceConnected");
            mBluetoothLeService = ((BluetoothLeService.LocalBinder) service).getService();

            if (!mBluetoothLeService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                ((Activity) mainContext).finish();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            System.out.println("mServiceConnection onServiceDisconnected");
            mBluetoothLeService = null;
        }
    };

    // Device scan callback.
    private final BluetoothAdapter.LeScanCallback mLeScanCallback = (device, rssi, scanRecord) -> ((Activity) mainContext).runOnUiThread(() -> {
        System.out.println("mLeScanCallback onLeScan run ");
        mLeDeviceListAdapter.addDevice(device);
        mLeDeviceListAdapter.notifyDataSetChanged();
    });

    private void getGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null) return;
        String uuid;
        mModelNumberCharacteristic = null;
        mSerialPortCharacteristic = null;
        mCommandCharacteristic = null;
        ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = new ArrayList<ArrayList<BluetoothGattCharacteristic>>();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            uuid = gattService.getUuid().toString();
            System.out.println("displayGattServices + uuid=" + uuid);

            List<BluetoothGattCharacteristic> gattCharacteristics = gattService.getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                uuid = gattCharacteristic.getUuid().toString();
                switch (uuid) {
                    case ModelNumberStringUUID:
                        mModelNumberCharacteristic = gattCharacteristic;
                        System.out.println("mModelNumberCharacteristic  " + mModelNumberCharacteristic.getUuid().toString());
                        break;

                    case SerialPortUUID:
                        mSerialPortCharacteristic = gattCharacteristic;
                        System.out.println("mSerialPortCharacteristic  " + mSerialPortCharacteristic.getUuid().toString());
                        break;

                    case CommandUUID:
                        mCommandCharacteristic = gattCharacteristic;
                        System.out.println("mSerialPortCharacteristic  " + mSerialPortCharacteristic.getUuid().toString());
                        break;
                }
            }
            mGattCharacteristics.add(charas);
        }

        if (mModelNumberCharacteristic == null || mSerialPortCharacteristic == null || mCommandCharacteristic == null) {
            Toast.makeText(mainContext, "Please select DFRobot devices", Toast.LENGTH_SHORT).show();
            mConnectionState = connectionStateEnum.isToScan;
            onConectionStateChange(mConnectionState);
        } else {
            mSCharacteristic = mModelNumberCharacteristic;
            mBluetoothLeService.setCharacteristicNotification(mSCharacteristic, true);
            mBluetoothLeService.readCharacteristic(mSCharacteristic);
        }
    }

    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }

    private class LeDeviceListAdapter extends BaseAdapter {
        private final ArrayList<BluetoothDevice> mLeDevices;
        private final LayoutInflater mInflator;

        public LeDeviceListAdapter() {
            super();
            mLeDevices = new ArrayList<>();
            mInflator = ((Activity) mainContext).getLayoutInflater();
        }

        public void addDevice(BluetoothDevice device) {
            if (!mLeDevices.contains(device)) {
                mLeDevices.add(device);
            }
        }

        public BluetoothDevice getDevice(int position) {
            return mLeDevices.get(position);
        }

        public void clear() {
            mLeDevices.clear();
        }

        @Override
        public int getCount() {
            return mLeDevices.size();
        }

        @Override
        public Object getItem(int i) {

            return mLeDevices.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder viewHolder;
            // General ListView optimization code.
            if (view == null) {
                view = mInflator.inflate(R.layout.item_device, null);
                viewHolder = new ViewHolder();
                viewHolder.deviceAddress = view
                        .findViewById(R.id.device_address);
                viewHolder.deviceName = view
                        .findViewById(R.id.device_name);
                System.out.println("mInflator.inflate  getView");
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }

            BluetoothDevice device = mLeDevices.get(i);
            final String deviceName = device.getName();
            if (deviceName != null && deviceName.length() > 0)
                viewHolder.deviceName.setText(deviceName);
            else
                viewHolder.deviceName.setText(R.string.unknown_device);
            viewHolder.deviceAddress.setText(device.getAddress());
            return view;
        }
    }
}