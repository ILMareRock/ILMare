package moe.mzry.ilmare.activities;

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

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import moe.mzry.ilmare.R;

public class MainScreenActivity extends AppCompatActivity {

  private GoogleMap mMap; // Might be null if Google Play services APK is not available.

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_screen);

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
  }

  @Override
  protected void onResume() {
    super.onResume();
    //setUpMapIfNeeded();
  }

  /**
   * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
   * installed) and the map has not already been instantiated.. This will ensure that we only ever
   * call {@link #setUpMap()} once when {@link #mMap} is not null.
   * <p/>
   * If it isn't installed {@link SupportMapFragment} (and
   * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
   * install/update the Google Play services APK on their device.
   * <p/>
   * A user can return to this FragmentActivity after following the prompt and correctly
   * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
   * have been completely destroyed during this process (it is likely that it would only be
   * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
   * method in {@link #onResume()} to guarantee that it will be called.
   */
  private void setUpMapIfNeeded() {
    // Do a null check to confirm that we have not already instantiated the map.
    if (mMap == null) {
      // Try to obtain the map from the SupportMapFragment.
      mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
          .getMap();
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
  }
}
