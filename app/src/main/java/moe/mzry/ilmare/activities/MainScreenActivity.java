package moe.mzry.ilmare.activities;

<<<<<<< HEAD:app/src/main/java/moe/mzry/ilmare/MainScreenActivity.java
import android.os.*;
import android.support.v4.app.*;
import android.widget.TextView;
=======
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
>>>>>>> 5b81707c99ec16ae73fbe5ee8cb98bd7780e7aee:app/src/main/java/moe/mzry/ilmare/activities/MainScreenActivity.java

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

<<<<<<< HEAD:app/src/main/java/moe/mzry/ilmare/MainScreenActivity.java
public class MainScreenActivity extends FragmentActivity implements OnMapReadyCallback {
=======
import moe.mzry.ilmare.R;

public class MainScreenActivity extends AppCompatActivity {
>>>>>>> 5b81707c99ec16ae73fbe5ee8cb98bd7780e7aee:app/src/main/java/moe/mzry/ilmare/activities/MainScreenActivity.java

  private GoogleMap mMap; // Might be null if Google Play services APK is not available.
  private TextView console;

  @Override
  protected void onCreate(Bundle savedInstaState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_screen);

<<<<<<< HEAD:app/src/main/java/moe/mzry/ilmare/MainScreenActivity.java
    // Initialize the views.
    console = (TextView) findViewById(R.id.text);
    setUpMapIfNeeded();

    console.append("\n Finished!");
=======
    Toolbar toolbar = (Toolbar) findViewById(R.id.main_screen_toolbar);
    setSupportActionBar(toolbar);

    // Initialize the ViewPager and set an adapter
    ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
    pager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {

      @Override
      public Fragment getItem(int position) {
        switch (position % 3) {
          //case 0:
          //    return RecyclerViewFragment.newInstance();
          //case 1:
          //    return RecyclerViewFragment.newInstance();
          //case 2:
          //    return WebViewFragment.newInstance();
          default:
            return SupportMapFragment.newInstance();
        }
      }

      @Override
      public int getCount() {
        return 3;
      }

      @Override
      public CharSequence getPageTitle(int position) {
        switch (position % 3) {
          case 0:
            return "MAP";
          case 1:
            return "LIST";
          case 2:
            return "DEBUG";
        }
        return "";
      }
    });

    // Bind the tabs to the ViewPager
    PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
    tabs.setViewPager(pager);
>>>>>>> 5b81707c99ec16ae73fbe5ee8cb98bd7780e7aee:app/src/main/java/moe/mzry/ilmare/activities/MainScreenActivity.java
  }

  @Override
  protected void onResume() {
    super.onResume();
    //setUpMapIfNeeded();
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
