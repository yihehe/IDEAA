package com.takaitra.hello.ideaa;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.takaitra.hello.ideaa.model.Location;

import cyanogenmod.app.CMStatusBarManager;
import cyanogenmod.app.CustomTile;
import io.realm.Realm;
import io.realm.RealmResults;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Example sample activity to publish a tile with a toggle state
 */
public class MainActivity extends Activity implements View.OnClickListener {


    private static final int PLACE_PICKER_REQUEST = 1;

    public static final int REQUEST_CODE = 0;
    public static final int CUSTOM_TILE_ID = 1;
    public static final int CUSTOM_TILE_LIST_ID = 2;
    public static final int CUSTOM_TILE_GRID_ID = 3;
    public static final String ACTION_TOGGLE_STATE =
            "org.cyanogenmod.samples.customtiles.ACTION_TOGGLE_STATE";
    public static final String STATE = "state";

    private static Map<String, String> addresses = new LinkedHashMap<>();

    private Button mDatabaseButton;
    private Button mPlacesButton;
    private CustomTile mCustomTile;

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        realm = Realm.getInstance(this);

        initializeAddresses();

        setupCustomTile();

        mDatabaseButton =
                (Button) findViewById(R.id.database_button);
        mDatabaseButton.setOnClickListener(this);

        mPlacesButton =
                (Button) findViewById(R.id.places_button);
        mPlacesButton.setOnClickListener(this);

    }

    private void initializeAddresses() {
//        addresses.put("Home", "9425 34th Ave SW, Seattle, WA 98126");
//        addresses.put("Work", "2201 6th Ave, Seattle, WA 98121");
//        addresses.put("Impact Hub", "220 2nd Ave S, Seattle, WA 98104");
        RealmResults<Location> results = realm.allObjectsSorted(Location.class, "id", false);
        for (int i = 0; i < results.size(); i++) {
            Location location = results.get(i);
            addresses.put(location.getName(), location.getAddress());
        }
    }

    private void setupCustomTile() {
        ArrayList<CustomTile.ExpandedListItem> expandedListItems =
                new ArrayList<>();
        for (Map.Entry<String, String> address : addresses.entrySet()) {
            String uri = String.format("http://maps.google.com/maps?daddr=%s", address.getValue());
            Intent addressIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
            addressIntent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            PendingIntent addressPendingIntent = PendingIntent.getActivity(this, 0, addressIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            CustomTile.ExpandedListItem expandedListItem = new CustomTile.ExpandedListItem();
            expandedListItem.setExpandedListItemDrawable(R.drawable.ic_launcher);
            expandedListItem.setExpandedListItemTitle(address.getKey());
            expandedListItem.setExpandedListItemSummary(address.getValue());
            expandedListItem.setExpandedListItemOnClickIntent(addressPendingIntent);
            expandedListItems.add(expandedListItem);

        }

        CustomTile.ListExpandedStyle listExpandedStyle = new CustomTile.ListExpandedStyle();
        listExpandedStyle.setListItems(expandedListItems);

        mCustomTile = new CustomTile.Builder(this)
                .setExpandedStyle(listExpandedStyle)
                .setContentDescription("Choose an address to navigate to")
                .setLabel("Quick Directions")
                .setIcon(R.drawable.ic_launcher)
                .build();
        CMStatusBarManager.getInstance(this)
                .publishTile(CUSTOM_TILE_LIST_ID, mCustomTile);
    }

    private void pickPlace() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        Context context = getApplicationContext();
        try {
            startActivityForResult(builder.build(context), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
//                CharSequence address = place.getAddress();
//                LatLng coordinates = place.getLatLng();
                // TODO: Persist the place in realm DB

                realm.beginTransaction();
                int i = (int) realm.where(Location.class).maximumInt("id") + 1;
                Location location = realm.createObject(Location.class);
                location.setName(place.getName().toString());
                location.setAddress(place.getAddress().toString());
                location.setLatitude(place.getLatLng().latitude);
                location.setLatitude(place.getLatLng().longitude);
                location.setId(i);
                realm.commitTransaction();
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.database_button:
                startActivity(new Intent(this, DatabaseActivity.class));
                break;
            case R.id.places_button:
                pickPlace();
                break;
        }
    }
}
