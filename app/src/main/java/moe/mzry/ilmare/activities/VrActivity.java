package moe.mzry.ilmare.activities;

import android.app.Activity;
import android.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.util.List;

import moe.mzry.ilmare.R;
import moe.mzry.ilmare.fragments.VrActivityFragment;
import moe.mzry.ilmare.service.DataModel;
import moe.mzry.ilmare.service.data.Message;
import moe.mzry.ilmare.service.data.eddystone.Beacon;

public class VrActivity extends Activity implements DataModel.Listener {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_vr);
  }

  @Override
  protected void onResume() {
    super.onResume();
    DataModel.INSTANCE.addListener(this);
  }

  @Override
  protected void onStop() {
    super.onStop();
    DataModel.INSTANCE.removeListener(this);
  }

  @Override
  public void onMessageChanged(List<Message> messageList) {
    VrActivityFragment fragment =
        (VrActivityFragment) getFragmentManager().findFragmentById(R.id.vrFragment);
    fragment.setMessages(messageList);
  }

  @Override
  public void onBeaconChanged(List<Beacon> beaconList) {

  }
}
