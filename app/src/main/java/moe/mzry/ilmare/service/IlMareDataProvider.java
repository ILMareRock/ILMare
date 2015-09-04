package moe.mzry.ilmare.service;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import moe.mzry.ilmare.service.data.BeaconSpecification;
import moe.mzry.ilmare.service.data.Message;

/**
 * Data provider.
 */
public interface IlMareDataProvider {

    List<Message> getMessageByBeacon(BeaconSpecification beaconSpecification);

    List<Message> getMessageByLocation(LatLng latLng, Long level);

    void createMessage(Message message, BeaconSpecification beaconSpecification);

    void createMessage(Message message, LatLng latLng, Long level);
}
