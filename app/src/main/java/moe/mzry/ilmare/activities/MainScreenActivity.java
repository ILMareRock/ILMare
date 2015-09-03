package moe.mzry.ilmare.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.astuetz.PagerSlidingTabStrip;
import com.google.android.gms.maps.SupportMapFragment;

import moe.mzry.ilmare.R;
import moe.mzry.ilmare.fragments.MapController;

public class MainScreenActivity extends AppCompatActivity {

  private SupportMapFragment supportMapFragment;
  private static final int CREATE_MESSAGE_REQUEST = 1;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
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
        switch (position) {
          case 0:
            return setUpMapIfNeeded();
          default:
            Log.i("PagerAdapter", "How many times;;;");
            return SupportMapFragment.newInstance();
        }
      }

      @Override
      public int getCount() {
        Log.i("Main", "getCount!");
        return 2;
      }

      @Override
      public CharSequence getPageTitle(int position) {
        switch (position % 3) {
          case 0:
            return "MAP";
          case 1:
            return "DEBUG";
        }
        return "";
      }
    });

    // Bind the tabs to the ViewPager
    PagerSlidingTabStrip tabs = (PagerSlidingTabStrip) findViewById(R.id.tabs);
    tabs.setViewPager(pager);

    findViewById(R.id.newMessageButton).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent();
        intent.setClass(MainScreenActivity.this, NewMessageActivity.class);
        startActivityForResult(intent, CREATE_MESSAGE_REQUEST, null);
        MainScreenActivity.this.finish();
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == CREATE_MESSAGE_REQUEST) {
      // TODO: check three cases SUCCESS, FAIL, CANCEL
      Log.i("Main", "onActiveResult");
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    Log.i("Main", "onResume");
    setUpMapIfNeeded();
  }

  private SupportMapFragment setUpMapIfNeeded() {
    if (supportMapFragment == null) {
      supportMapFragment = SupportMapFragment.newInstance();
      MapController.bindController(supportMapFragment);
    }
    return supportMapFragment;
  }
}
