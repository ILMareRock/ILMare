package moe.mzry.ilmare.service;

import java.util.List;

import moe.mzry.ilmare.service.data.FirebaseLatLng;
import moe.mzry.ilmare.service.data.LocationSpec;
import moe.mzry.ilmare.service.data.eddystone.Beacon;

/**
 * Location provider.
 */
public interface IlMareLocationProvider {

    LocationSpec getLocationSpec();

    void addBeaconListener(Callback<List<Beacon>> callback);

    FirebaseLatLng getLocation();

    Long getLevel();

    void getBeaconLocation(Beacon beacon, Callback<LocationSpec> callback);

    // For debug
    void setBeaconLocation(Beacon beacon, LocationSpec locationSpec);

    // following information could be changed
    List<Beacon> getNearbyBeacons();
}
