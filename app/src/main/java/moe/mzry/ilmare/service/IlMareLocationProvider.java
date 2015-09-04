package moe.mzry.ilmare.service;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Location provider.
 */
public interface IlMareLocationProvider {
    LatLng getLocation();
    Long getLevel();

    // following information could be changed
    List<String> getNearbyBeacons();
}
