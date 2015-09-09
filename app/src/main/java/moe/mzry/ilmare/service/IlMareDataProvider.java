package moe.mzry.ilmare.service;

import java.util.List;

import moe.mzry.ilmare.service.data.LocationSpec;
import moe.mzry.ilmare.service.data.Message;

/**
 * Data provider.
 */
public interface IlMareDataProvider {

    void addMessageListener(Callback<List<Message>> callback);

    List<Message> getMessageByBeacon(LocationSpec locationSpec);

    void createMessage(Message message, LocationSpec locationSpec);
}
