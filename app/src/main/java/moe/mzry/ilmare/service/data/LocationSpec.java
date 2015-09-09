package moe.mzry.ilmare.service.data;

import moe.mzry.ilmare.MainApp;
import moe.mzry.ilmare.service.data.eddystone.Beacon;

/**
 * Mapping the physical beacon to the logic indoor location.
 */
public class LocationSpec {

    private FirebaseLatLng location;

    private Long level;

    private double r;

    public LocationSpec() {}

    public LocationSpec(FirebaseLatLng latLng, Long level, double r) {
        this.location = latLng;
        this.level = level;
        this.r = r;
    }

    public FirebaseLatLng getLocation() {
        return location;
    }

    public void setLocation(FirebaseLatLng location) {
        this.location = location;
    }

    public Long getLevel() {
        return level;
    }

    public void setLevel(Long level) {
        this.level = level;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }
}
