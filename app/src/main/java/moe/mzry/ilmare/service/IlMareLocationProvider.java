package moe.mzry.ilmare.service;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import moe.mzry.ilmare.service.data.eddystone.Beacon;

/**
 * Location provider.
 */
public interface IlMareLocationProvider {

    LatLng getLocation();

    Long getLevel();

    // following information could be changed
    List<Beacon> getNearbyBeacons();
}
