package moe.mzry.ilmare.service.data;

import android.util.Pair;

import com.google.android.gms.maps.model.LatLng;

/**
 * Firebase friendly LatLng object
 */
public class FirebaseLatLng {
    public static final double R = 6371000; // radius of earth

    public double latitude;
    public double longitude;

    public FirebaseLatLng() {}

    public FirebaseLatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public static FirebaseLatLng of(LatLng latLng) {
        return new FirebaseLatLng(latLng.latitude, latLng.longitude);
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String toString() {
        return latitude + " " + longitude;
    }

    /**
     * Relative (x, y) position according to origin. X axis points to North. Units in meters.
     */
    public Pair<Double, Double> relativeDistance(FirebaseLatLng origin) {
        double dx = Math.toRadians(this.latitude - origin.getLatitude());
        dx *= R;
        double dy = Math.toRadians(origin.getLongitude() - this.longitude); // east points to -Y
        dy *= Math.cos(Math.toRadians(origin.getLongitude())) * R;
        return Pair.create(dx, dy);
    }
}
