package moe.mzry.ilmare.service;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import moe.mzry.ilmare.service.data.LocationSpec;
import moe.mzry.ilmare.service.data.Message;

/**
 * Data provider.
 */
public interface IlMareDataProvider {

    List<Message> getMessageByBeacon(LocationSpec locationSpec);

    void createMessage(Message message, LocationSpec locationSpec);
}
