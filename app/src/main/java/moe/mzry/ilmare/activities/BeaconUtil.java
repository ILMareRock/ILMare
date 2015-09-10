package moe.mzry.ilmare.activities;

import android.location.Location;

import java.util.List;

import moe.mzry.ilmare.service.data.LocationSpec;
import moe.mzry.ilmare.service.data.eddystone.Beacon;

/**
 * Created by yunliyun on 9/10/15.
 */
public class BeaconUtil {

    public static LocationSpec calculateCurrentLocationByBeacons(List<Beacon> nearbyBeacons) {
        if (nearbyBeacons.size() <= 1) {
            // Trust GPS location
            return null;
        } else {
            return null;
        }
    }
}
