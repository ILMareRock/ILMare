package moe.mzry.ilmare;

import android.app.Application;

import com.firebase.client.Firebase;

/**
 * Main application
 */
public class MainApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Firebase.setAndroidContext(this);
    }
}
