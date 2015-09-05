package moe.mzry.ilmare.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.common.collect.Lists;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import moe.mzry.ilmare.R;
import moe.mzry.ilmare.fragments.MessageListFragment;
import moe.mzry.ilmare.service.IlMareService;

/**
 * Main activity.
 */
public class MainScreenActivity extends AppCompatActivity {

    private SupportMapFragment supportMapFragment;
    private MessageListFragment messageListFragment;
    private static final int CREATE_MESSAGE_REQUEST = 1;

    private boolean locationServiceRunning = false;
    private IlMareService ilMareService;

    private Toolbar toolbar;
    private AddFloatingActionButton newMessageButton;
    private EditText newMessageTextBox;
    private InputMethodManager inputManager;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        inputManager =
                (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);

        // Initialize the views.
        toolbar = (Toolbar) findViewById(R.id.main_screen_toolbar);
        newMessageButton = (AddFloatingActionButton) findViewById(R.id.newMessageButton);
        newMessageTextBox = (EditText) findViewById(R.id.newMessageTextBox);

        newMessageTextBox.setVisibility(View.INVISIBLE);
        setSupportActionBar(toolbar);

        // Initialize the ViewPager and set an adapter
        ViewPager pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return setUpMapIfNeeded();
                    case 1:
                        return setUpMessageList();
                    default:
                        Log.i("PagerAdapter", "How many times;;;");
                        return SupportMapFragment.newInstance();
                }
            }

            @Override
            public int getCount() {
                Log.i("Main", "getCount!");
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

        newMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupNewMessageTextBox();
                /*Intent intent = new Intent();
                intent.setClass(MainScreenActivity.this, NewMessageActivity.class);
                Bundle bundle = new Bundle();

                LatLng location = ilMareService.getLocation();
                Long level = ilMareService.getLevel();
                bundle.putDouble("lat", location.latitude);
                bundle.putDouble("lon", location.longitude);
                bundle.putLong("level", level);

                startActivityForResult(intent, CREATE_MESSAGE_REQUEST, bundle);
                MainScreenActivity.this.finish();*/
            }
        });

        bindService(new Intent(this, IlMareService.class), locationServiceConnection,
                Context.BIND_AUTO_CREATE);
    }

    private void popupNewMessageTextBox() {
        newMessageButton.setVisibility(View.INVISIBLE);
        newMessageTextBox.setVisibility(View.VISIBLE);
        newMessageTextBox.requestFocus();
        inputManager.showSoftInput(newMessageTextBox, InputMethodManager.SHOW_FORCED);
        newMessageTextBox.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    sendMessage(newMessageTextBox.getText().toString());
                    return true;
                }
                return false;
            }
        });
        toolbar.setTitle(R.string.title_new_message);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void backToMainScreen() {
        newMessageButton.setVisibility(View.VISIBLE);
        newMessageTextBox.setVisibility(View.INVISIBLE);
        newMessageTextBox.setText("");
        newMessageTextBox.clearFocus();
        // Force hide keyboard
        inputManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        toolbar.setTitle(R.string.title_activity_main_screen);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    private void sendMessage(String message) {
        Log.i("NewMessage", message);
        // todo
        backToMainScreen();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                backToMainScreen();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    @Override
    protected void onStop() {
        super.onStop();
        if (locationServiceRunning) {
            unbindService(locationServiceConnection);
        }
    }

    private SupportMapFragment setUpMapIfNeeded() {
        if (supportMapFragment == null) {
            supportMapFragment = SupportMapFragment.newInstance();
            MapController.bindController(supportMapFragment);
        }
        return supportMapFragment;
    }

    private MessageListFragment setUpMessageList() {
        if (messageListFragment == null) {
            messageListFragment = MessageListFragment.newInstance(ilMareService);
        }
        return messageListFragment;
    }

    public ServiceConnection locationServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("ServiceConnection", "connected!!");
            IlMareService.IlMareServiceBinder binder = (IlMareService.IlMareServiceBinder) service;
            ilMareService = binder.getService();
            locationServiceRunning = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("ServiceConnection", "disconnected!!");
            locationServiceRunning = false;
        }
    };
}
