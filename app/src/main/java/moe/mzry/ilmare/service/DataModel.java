package moe.mzry.ilmare.service;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.util.Pair;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import moe.mzry.ilmare.MainApp;
import moe.mzry.ilmare.service.data.BillboardMessage;
import moe.mzry.ilmare.service.data.FirebaseLatLng;
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
  private FirebaseLatLng location;
  private Handler mHandler = new Handler();

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

  public FirebaseLatLng getCurrentLocation() {
    return location;
  }

  /**
   * Get nearby messages and their relative location (compressed 100x) based on the given origin.
   */
  public List<BillboardMessage> getNearbyMessages(FirebaseLatLng location) {
    float minX = 1e9f;
    float minY = 1e9f;
    float maxX = -1e9f;
    float maxY = -1e9f;
    List<BillboardMessage> billboards = new ArrayList<>();
    for (Message message : messageList) {
      Pair<Double, Double> xy = message.getLocationSpec().getLocation().relativeDistance(location);
      if (Math.abs(xy.first) > 1000 || Math.abs(xy.second) > 1000) {
        continue;
      }
      if (minX > xy.first) {
        minX = xy.first.floatValue();
      }
      if (minY > xy.second) {
        minY = xy.second.floatValue();
      }
      if (maxX < xy.first) {
        maxX = xy.first.floatValue();
      }
      if (maxY < xy.second) {
        maxY = xy.second.floatValue();
      }
      billboards.add(new BillboardMessage(message.getId(), message.getContent(), xy.first.floatValue(),
          xy.second.floatValue()));
    }
    float ratioX = 20 * 1000 / Math.max(Math.abs(minX), Math.abs(maxX));
    float ratioY = 20 * 1000/ Math.max(Math.abs(minY), Math.abs(maxY));
    for (BillboardMessage b : billboards) {
      b.setX(b.getX() * ratioX);
      b.setY(b.getY() * ratioY);
    }
    return billboards;
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

  Runnable mLocationChecker = new Runnable() {
    @Override
    public void run() {
      location = MainApp.getLocationProvider().getLocation();
      mHandler.postDelayed(mLocationChecker, 5000);
    }
  };

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
    mLocationChecker.run();
  }

  @Override
  public void onServiceDisconnected(ComponentName name) {
    mHandler.removeCallbacks(mLocationChecker);
  }
}
