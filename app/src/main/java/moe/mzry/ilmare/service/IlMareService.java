package moe.mzry.ilmare.service;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanRecord;
import android.bluetooth.le.ScanResult;
import android.bluetooth.le.ScanSettings;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.ParcelUuid;
import android.util.Log;

import com.firebase.client.Firebase;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import moe.mzry.ilmare.service.data.FirebaseLatLng;
import moe.mzry.ilmare.service.data.LocationSpec;
import moe.mzry.ilmare.service.data.Message;
import moe.mzry.ilmare.service.data.eddystone.Beacon;
import moe.mzry.ilmare.service.data.eddystone.Utils;

/**
 * The background service provider location and data service for the UI.
 */
public class IlMareService extends Service implements IlMareLocationProvider, IlMareDataProvider {

    private static final String FIREBASE_URL = "https://ilmare.firebaseio.com/";
    private static final String FIREBASE_MESSAGES_SPEC = "messages";
    private Firebase firebaseRef;
    private Firebase firebaseMessagesRef;

    private final IlMareServiceBinder ilMareServiceBinder = new IlMareServiceBinder();
    private BluetoothLeScanner scanner;
    private Map<String, Beacon> beaconMap = new HashMap<>();

    private ScanCallback scanCallback;
    private static final ScanSettings SCAN_SETTINGS =
            new ScanSettings.Builder().setScanMode(ScanSettings.SCAN_MODE_LOW_LATENCY)
                    .setReportDelay(0).build();
    // The Eddystone Service UUID, 0xFEAA.
    private static final ParcelUuid EDDYSTONE_SERVICE_UUID =
            ParcelUuid.fromString("0000FEAA-0000-1000-8000-00805F9B34FB");

    public class IlMareServiceBinder extends Binder {
        private IlMareServiceBinder() {}
        public IlMareService getService(){
            return IlMareService.this;
        }
    }

    // Attempts to create the scanner.
    private void initScanner() {
        BluetoothManager manager = (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        BluetoothAdapter btAdapter = manager.getAdapter();
        if (btAdapter == null) {
            Log.e("IlMareService", "Bluetooth not detected on this device!");
        } else if (!btAdapter.isEnabled()) {
            Log.e("IlMareService", "Bluetooth not enabled on this device!");
        } else {
            Log.i("IlMareService", "Bluetooth scanner connected!");
            scanner = btAdapter.getBluetoothLeScanner();
        }
    }

    private void initFirebase() {
        firebaseRef = new Firebase(FIREBASE_URL);
        firebaseMessagesRef = firebaseRef.child(FIREBASE_MESSAGES_SPEC);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // create bluetooth scanner.
        initScanner();
        initFirebase();
        Log.i("IlMareService", "onCreate");
    }

    @Override
    public IBinder onBind(Intent intent) {
        scanCallback = new ScanCallback() {
            @Override
            public void onScanResult(int callbackType, ScanResult result) {
                ScanRecord scanRecord = result.getScanRecord();
                if (scanRecord == null) {
                    return;
                }

                String deviceAddress = result.getDevice().getAddress();
                Beacon beacon = beaconMap.get(deviceAddress);
                if (beacon == null) {
                    beacon = new Beacon(deviceAddress, result.getRssi());
                    beaconMap.put(deviceAddress, beacon);
                }

                byte[] serviceData = scanRecord.getServiceData(EDDYSTONE_SERVICE_UUID);
                Utils.parseServiceData(deviceAddress, serviceData,
                        beacon.updateRssi(result.getRssi()));
                Log.i("BLE", deviceAddress + " " + Utils.toHexString(serviceData));
                Log.i("Beacon", beacon.toString());
            }

            @Override
            public void onScanFailed(int errorCode) {
                Log.e("BLE-ScanFailed", "ErrorCode:" + errorCode);
            }
        };

        List<ScanFilter> scanFilters = new ArrayList<>();
        scanFilters.add(new ScanFilter.Builder().setServiceUuid(EDDYSTONE_SERVICE_UUID).build());
        scanner.startScan(scanFilters, SCAN_SETTINGS, scanCallback);
        return ilMareServiceBinder;
    }

    @Override
    public void onRebind(Intent intent) {
        // do something necessary
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopSelf();
        return true;
    }

    // The following methods should be the real service
    @Override
    public FirebaseLatLng getLocation() {
        return new FirebaseLatLng(123, 456);
    }

    @Override
    public Long getLevel() {
        return 1L;
    }

    @Override
    public LocationSpec getLocationSpec() {
        return new LocationSpec(getLocation(), getLevel(), 1.0);
    }

    @Override
    public List<Beacon> getNearbyBeacons() {
        // only return the beacons scanned in last 5 seconds
        List<Beacon> nearbyBeacons = new ArrayList<>();
        for (Beacon beacon : beaconMap.values()) {
            if (System.currentTimeMillis() - beacon.getTimestamp() < 5 /* s */ * 1000000) {
                nearbyBeacons.add(beacon);
            }
        }
        return nearbyBeacons;
    }

    @Override
    public List<Message> getMessageByBeacon(LocationSpec locationSpec) {
        return null;
    }

    @Override
    public void createMessage(Message message, LocationSpec locationSpec) {
        Firebase newMessageRef = firebaseMessagesRef.push();
        message.setLocationSpec(locationSpec);
        newMessageRef.setValue(message);
    }
}
