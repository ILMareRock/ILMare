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

    FirebaseLatLng getLocation();

    Long getLevel();

    // following information could be changed
    List<Beacon> getNearbyBeacons();
}
