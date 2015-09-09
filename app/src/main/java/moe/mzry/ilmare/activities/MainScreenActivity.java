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
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.astuetz.PagerSlidingTabStrip;
import com.getbase.floatingactionbutton.AddFloatingActionButton;
import com.google.android.gms.maps.SupportMapFragment;

import moe.mzry.ilmare.MainApp;
import moe.mzry.ilmare.R;
import moe.mzry.ilmare.fragments.MessageListFragment;
import moe.mzry.ilmare.service.IlMareService;
import moe.mzry.ilmare.service.data.Message;
import moe.mzry.ilmare.views.PopupTextBox;

/**
 * Main activity.
 */
public class MainScreenActivity extends AppCompatActivity implements PopupTextBox.EventHandler {

    private SupportMapFragment supportMapFragment;
    private MessageListFragment messageListFragment;
    private static final int CREATE_MESSAGE_REQUEST = 1;

    private boolean locationServiceRunning = false;
    private IlMareService ilMareService;

    private Toolbar toolbar;
    private AddFloatingActionButton newMessageButton;
    private PopupTextBox newMessageTextBox;
    private LinearLayout fabGroup;
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
        newMessageTextBox = (PopupTextBox) findViewById(R.id.newMessageTextBox);
        fabGroup = (LinearLayout) findViewById(R.id.fab_group);
        newMessageTextBox.setEventHandler(this);
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
            }
        });
    }

    private void popupNewMessageTextBox() {
        fabGroup.setVisibility(View.INVISIBLE);
        newMessageTextBox.setVisibility(View.VISIBLE);
        newMessageTextBox.requestFocus();
        inputManager.showSoftInput(newMessageTextBox, 0);
        toolbar.setTitle(R.string.title_new_message);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void backToMainScreen() {
        newMessageTextBox.setVisibility(View.INVISIBLE);
        newMessageTextBox.clearFocus();
        // Force hide keyboard
        inputManager.toggleSoftInput(0, 0);
        toolbar.setTitle(R.string.title_activity_main_screen);
        newMessageTextBox.setText("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        fabGroup.setVisibility(View.VISIBLE);
    }

    @Override
    public void onPopupTextBoxEnterPressed() {
        sendMessage(newMessageTextBox.getText().toString());
    }

    @Override
    public void onPopupTextBoxBackPressed() {
        backToMainScreen();
    }

    private void sendMessage(String message) {
        Log.i("NewMessage", message);
        ilMareService.createMessage(new Message(message), ilMareService.getLocationSpec());
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
        bindService(new Intent(this, IlMareService.class), locationServiceConnection,
                Context.BIND_AUTO_CREATE);
        setUpMapIfNeeded();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (locationServiceRunning) {
            unbindService(locationServiceConnection);
        }
    }

    public void onVirtualView(View view) {
        Intent intent = new Intent(this, VrActivity.class);
        startActivity(intent);
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
            messageListFragment = MessageListFragment.newInstance();
        }
        return messageListFragment;
    }

    public ServiceConnection locationServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.i("ServiceConnection", "connected!!");
            IlMareService.IlMareServiceBinder binder = (IlMareService.IlMareServiceBinder) service;
            ilMareService = binder.getService();
            MainApp.setDataProvider(ilMareService);
            MainApp.setLocationProvider(ilMareService);
            locationServiceRunning = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.i("ServiceConnection", "disconnected!!");
            locationServiceRunning = false;
        }
    };
}
