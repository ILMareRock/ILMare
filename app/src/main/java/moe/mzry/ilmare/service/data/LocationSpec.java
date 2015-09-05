package moe.mzry.ilmare.service.data;

import com.google.android.gms.maps.model.LatLng;

import moe.mzry.ilmare.service.data.eddystone.Beacon;

/**
 * Mapping the physical beacon to the logic indoor location.
 */
public class LocationSpec {

    private LatLng location;

    private Long level;

    private double r;

    public LocationSpec(LatLng latLng, Long level, double r) {
        this.location = latLng;
        this.level = level;
        this.r = r;
    }

    public LocationSpec of(Beacon beacon, double r) {
        // fetch real location from server or local configuration.
        return new LocationSpec(new LatLng(0, 0), 1L, r);
    }
}
