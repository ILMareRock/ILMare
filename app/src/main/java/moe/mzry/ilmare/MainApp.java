package moe.mzry.ilmare;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.firebase.client.Firebase;

import moe.mzry.ilmare.activities.MapController;
import moe.mzry.ilmare.service.IlMareDataProvider;
import moe.mzry.ilmare.service.IlMareLocationProvider;
import moe.mzry.ilmare.service.IlMareService;
import moe.mzry.ilmare.service.DataModel;

/**
 * Main application
 */
public class MainApp extends Application {

    private static MainApp mainApp;
    private IlMareDataProvider dataProvider;
    private IlMareLocationProvider locationProvider;
    private IlMareService ilMareService;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
        bindService(new Intent(this, IlMareService.class), locationServiceConnection,
            Context.BIND_AUTO_CREATE);
        mainApp = this;
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        unbindService(locationServiceConnection);
    }

    public static IlMareDataProvider getDataProvider() {
        return mainApp.dataProvider;
    }

    public static void setDataProvider(IlMareDataProvider dataProvider) {
        mainApp.dataProvider = dataProvider;
    }

    public static IlMareLocationProvider getLocationProvider() {
        return mainApp.locationProvider;
    }

    public static void setLocationProvider(IlMareLocationProvider locationProvider) {
        mainApp.locationProvider = locationProvider;
    }

    public IlMareService getIlMareService() {
        return ilMareService;
    }

    public ServiceConnection locationServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("ServiceConnection", "connected!!");
            IlMareService.IlMareServiceBinder binder = (IlMareService.IlMareServiceBinder) service;
            ilMareService = binder.getService();
            MainApp.setDataProvider(ilMareService);
            MainApp.setLocationProvider(ilMareService);

            DataModel.INSTANCE.onServiceConnected(name, service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("ServiceConnection", "disconnected!!");

            DataModel.INSTANCE.onServiceDisconnected(name);
        }
    };
}
