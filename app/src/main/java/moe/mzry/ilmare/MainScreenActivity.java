package moe.mzry.ilmare;

import android.os.*;
import android.support.v4.app.*;
import android.widget.TextView;

import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.*;

public class MainScreenActivity extends FragmentActivity implements OnMapReadyCallback {

  private GoogleMap mMap; // Might be null if Google Play services APK is not available.
  private TextView console;

  @Override
  protected void onCreate(Bundle savedInstaState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_screen);

    // Initialize the views.
    console = (TextView) findViewById(R.id.text);
    setUpMapIfNeeded();

    console.append("\n Finished!");
  }

  @Override
  protected void onResume() {
    super.onResume();
    setUpMapIfNeeded();
  }

  private void setUpMapIfNeeded() {
    // Do a null check to confirm that we have not already instantiated the map.
    if (mMap == null) {
      // Try to obtain the map from the SupportMapFragment.
      mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
          .getMap();

      console.append("\n trying to load map!");
      // Check if we were successful in obtaining the map.
      if (mMap != null) {
        setUpMap();
      }
    }
  }

  /**
   * This is where we can add markers or lines, add listeners or move the camera. In this case, we
   * just add a marker near Africa.
   * <p/>
   * This should only be called once and when we are sure that {@link #mMap} is not null.
   */
  private void setUpMap() {
    mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));
    console.append("map set up!");
  }

  @Override
  public void onMapReady(GoogleMap map) {
    // Add a marker in Sydney, Australia, and move the camera.
    LatLng sydney = new LatLng(-34, 151);
    map.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
    map.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    console.append("map loaded!");
  }
}
