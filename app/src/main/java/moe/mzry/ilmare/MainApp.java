package moe.mzry.ilmare;

import android.app.Application;

import com.firebase.client.Firebase;

import java.util.List;

import moe.mzry.ilmare.service.IlMareDataProvider;
import moe.mzry.ilmare.service.IlMareLocationProvider;
import moe.mzry.ilmare.service.data.eddystone.Beacon;

/**
 * Main application
 */
public class MainApp extends Application {

    private static MainApp mainApp;
    private IlMareDataProvider dataProvider;
    private IlMareLocationProvider locationProvider;

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);

        mainApp = this;
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
}
