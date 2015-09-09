package moe.mzry.ilmare.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import moe.mzry.ilmare.MainApp;
import moe.mzry.ilmare.service.data.Message;
import moe.mzry.ilmare.service.data.eddystone.Beacon;

/**
 * Created by yfchen on 9/9/15.
 */
public class DataModel implements ServiceConnection {
  public interface Listener {
    void onMessageChanged(List<Message> messageList);

    void onBeaconChanged(List<Beacon> beaconList);
  }
  public static DataModel INSTANCE = new DataModel();
  private List<Message> messageList;
  private List<Beacon> beaconList;
  private Set<Listener> listeners = new HashSet<>();

  public List<Message> getMessageList() {
    return messageList;
  }

  public List<Beacon> getBeaconList() {
    return beaconList;
  }

  public void addListener(Listener listener) {
    listeners.add(listener);
  }

  public void removeListener(Listener listener) {
    listeners.remove(listener);
  }

  private void onMessageChanged() {
    for (Listener l : listeners) {
      l.onMessageChanged(messageList);
    }
  }

  private void onBeaconChanged() {
    for (Listener l : listeners) {
      l.onBeaconChanged(beaconList);
    }
  }

  @Override
  public void onServiceConnected(ComponentName name, IBinder service) {
    MainApp.getDataProvider().addMessageListener(new Callback<List<Message>>() {
      @Override
      public void apply(List<Message> data) {
        DataModel.this.messageList = data;
        onMessageChanged();
      }
    });
    MainApp.getLocationProvider().addBeaconListener(new Callback<List<Beacon>>() {
      @Override
      public void apply(List<Beacon> data) {
        DataModel.this.beaconList = data;
        onBeaconChanged();
      }
    });
  }

  @Override
  public void onServiceDisconnected(ComponentName name) {

  }
}
