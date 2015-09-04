package moe.mzry.ilmare.service;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;
import com.google.common.collect.Lists;

import java.util.List;

import moe.mzry.ilmare.service.data.BeaconSpecification;
import moe.mzry.ilmare.service.data.Message;

/**
 * The background service provider location and data service for the UI.
 */
public class IlMareService extends Service implements IlMareLocationProvider, IlMareDataProvider {

    private final IlMareServiceBinder ilMareServiceBinder = new IlMareServiceBinder();

    public class IlMareServiceBinder extends Binder {
        private IlMareServiceBinder() {}

        public IlMareService getService(){
            return IlMareService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
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

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("IlMareService", "onCreate");
    }

    // The following methods should be the real service
    @Override
    public LatLng getLocation() {
        return new LatLng(123, 456);
    }

    @Override
    public Long getLevel() {
        return 1L;
    }

    @Override
    public List<String> getNearbyBeacons() {
        return Lists.newArrayList("Beacon1", "Beacon2", "Hello");
    }

    @Override
    public List<Message> getMessageByBeacon(BeaconSpecification beaconSpecification) {
        return null;
    }

    @Override
    public List<Message> getMessageByLocation(LatLng latLng, Long level) {
        return null;
    }

    @Override
    public void createMessage(Message message, BeaconSpecification beaconSpecification) {
    }

    @Override
    public void createMessage(Message message, LatLng latLng, Long level) {
    }
}
