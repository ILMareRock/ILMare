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
import android.widget.TextView;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import moe.mzry.ilmare.R;

public class MainScreenActivity extends AppCompatActivity implements OnMapReadyCallback {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main_screen);

    // Initialize the views.
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
    setUpMapIfNeeded();
  }

  private void setUpMapIfNeeded() {
    // Try to obtain the map from the SupportMapFragment.
    SupportMapFragment supportMapFragment = ((SupportMapFragment)
            getSupportFragmentManager().findFragmentById(R.id.map));
    if (supportMapFragment != null) {
      supportMapFragment.getMapAsync(this);
    }
  }

  @Override
  public void onMapReady(GoogleMap map) {
    // Add a marker in Sydney, Australia, and move the camera.
    LatLng sydney = new LatLng(-34, 151);
    map.setMyLocationEnabled(true);
    map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
    map.addMarker(new MarkerOptions()
            .title("Sydney")
            .snippet("The most populous city in Australia.")
            .position(sydney));
  }
}
