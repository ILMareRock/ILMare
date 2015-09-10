package moe.mzry.ilmare.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.ui.IconGenerator;

import java.util.ArrayList;
import java.util.List;

import moe.mzry.ilmare.MainApp;
import moe.mzry.ilmare.R;
import moe.mzry.ilmare.service.Callback;
import moe.mzry.ilmare.service.data.LocationSpec;
import moe.mzry.ilmare.service.data.Message;
import moe.mzry.ilmare.service.data.eddystone.Beacon;

/**
 * A wrapper which contains a google map fragment.
 */
public class MapController implements OnMapReadyCallback, GoogleMap.OnMapClickListener,
        GoogleMap.OnMapLoadedCallback, GoogleMap.OnMarkerClickListener {

    public static MapController INSTANCE = new MapController();

    private List<Message> messageList = new ArrayList<>();
    private GoogleMap map;
    private Context context;
    private MapController() {}

    public static void bindController(SupportMapFragment supportMapFragment) {
        supportMapFragment.getMapAsync(INSTANCE);
        GoogleMap map = supportMapFragment.getMap();
        if (map != null) {
            supportMapFragment.getMap().setMyLocationEnabled(true);
            map.setOnMapClickListener(INSTANCE);
            map.setOnMapLoadedCallback(INSTANCE);
            map.setOnMarkerClickListener(INSTANCE);
        }
    }

    public void renderBeacons(List<Beacon> beacons) {
        for (Beacon beacon : beacons) {
            MainApp.getLocationProvider().getBeaconLocation(beacon, new Callback<LocationSpec>() {
                @Override
                public void apply(LocationSpec data) {
                    LatLng curLoc = new LatLng(data.getLocation().getLatitude(),
                            data.getLocation().getLongitude());
                    //map.moveCamera(CameraUpdateFactory.newLatLngZoom(curLoc, 13));
                    map.addMarker(new MarkerOptions()
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.beacon_icon))
                            .position(curLoc));
                }
            });
        }
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void renderMessages(List<Message> messages) {
        messageList = messages;
        // MainApp.getLocationProvider().getLocationSpec();
        for (Message msg : messageList) {
            LatLng curLoc = new LatLng(msg.getLocationSpec().getLocation().getLatitude(),
                    msg.getLocationSpec().getLocation().getLongitude());
            //Log.i("loc", ">> latitude:" + msg.getLocationSpec().getLocation().getLatitude());
            //Log.i("loc", ">> longtitude:" + msg.getLocationSpec().getLocation().getLongitude());
            //map.moveCamera(CameraUpdateFactory.newLatLngZoom(curLoc, 13));
            /*map.addMarker(new MarkerOptions()
                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.message_icon))
                    .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                    .title(msg.getContent())
                    .snippet("Content:" + msg.getContent() + " Time:" + msg.getCreationTime())
                    .position(curLoc));*/
            IconGenerator iconFactory = new IconGenerator(context);
            addIcon(iconFactory, msg.getContent(), curLoc);

        }
        /*map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(
                        MainApp.getLocationProvider().getLocationSpec().getLocation().getLatitude(),
                        MainApp.getLocationProvider().getLocationSpec().getLocation().getLongitude()),
                13));
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(new LatLng(41.889, -87.622))
                .zoom(13)
                .bearing(90)
                .build();
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                2000, null);*/
    }

    private void addIcon(IconGenerator iconFactory, String text, LatLng position) {
        MarkerOptions markerOptions = new MarkerOptions().
                icon(BitmapDescriptorFactory.fromBitmap(iconFactory.makeIcon(text))).
                position(position).
                anchor(iconFactory.getAnchorU(), iconFactory.getAnchorV());

        map.addMarker(markerOptions);
    }

    @Override
    public void onMapReady(final GoogleMap map) {
        Log.i("GoogleMap", "onMapReady!");
        this.map = map;
        renderMessages(messageList);
        map.setMyLocationEnabled(true);
/*        // Add a marker in Sydney, Australia, and move the camera.
        LatLng sydney = new LatLng(-34, 151);
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 13));
        map.addMarker(new MarkerOptions()
                .title("Sydney")
                .snippet("The most populous city in Australia.")
                .position(sydney));

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(41.889, -87.622), 16));

        // You can customize the marker image using images bundled with
        // your app, or dynamically generated bitmaps.
        map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher))
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(41.889, -87.622))
                .draggable(true));

        // You can customize the marker image using images bundled with
        // your app, or dynamically generated bitmaps.
        map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_cast_on_0_light))
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(41.889, -87.622)));
        LatLng mapCenter = new LatLng(41.889, -87.622);

        map.moveCamera(CameraUpdateFactory.newLatLngZoom(mapCenter, 13));

        // Flat markers will rotate when the map is rotated,
        // and change perspective when the map is tilted.
        map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher))
                .position(mapCenter)
                .flat(true)
                .rotation(245));

        CameraPosition cameraPosition = CameraPosition.builder()
                .target(mapCenter)
                .zoom(13)
                .bearing(90)
                .build();

        // Animate the change in camera view over 2 seconds
        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                2000, null);
                */
    }

    @Override
    public void onMapClick(LatLng latLng) {
        Log.i("GoogleMap", "onMapClick!");
        // We should not enable this...
    }

    @Override
    public void onMapLoaded() {
        Log.i("GoogleMap", "onMapLoaded!");
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        // TODO: display the message
        return false;
    }
}
