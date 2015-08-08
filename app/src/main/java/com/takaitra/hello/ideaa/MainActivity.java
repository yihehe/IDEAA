package com.takaitra.hello.ideaa;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import cyanogenmod.app.CMStatusBarManager;
import cyanogenmod.app.CustomTile;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Example sample activity to publish a tile with a toggle state
 */
public class MainActivity extends Activity {

    public static final int REQUEST_CODE = 0;
    public static final int CUSTOM_TILE_ID = 1;
    public static final int CUSTOM_TILE_LIST_ID = 2;
    public static final int CUSTOM_TILE_GRID_ID = 3;
    public static final String ACTION_TOGGLE_STATE =
            "org.cyanogenmod.samples.customtiles.ACTION_TOGGLE_STATE";
    public static final String STATE = "state";

    private static Map<String, String> addresses = new LinkedHashMap<>();

    private CustomTile mCustomTile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        initializeAddresses();

        setupCustomTile();
    }

    private void initializeAddresses() {
        addresses.put("Home", "9425 34th Ave SW, Seattle, WA 98126");
        addresses.put("Work", "2201 6th Ave, Seattle, WA 98121");
        addresses.put("Impact Hub", "220 2nd Ave S, Seattle, WA 98104");
    }

    private void setupCustomTile() {
        ArrayList<CustomTile.ExpandedListItem> expandedListItems =
                new ArrayList<>();
        for (Map.Entry<String, String> address : addresses.entrySet()) {
            String uri = String.format("http://maps.google.com/maps?daddr=%s", address.getValue());
            Intent addressIntent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
            addressIntent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            PendingIntent addressPendingIntent = PendingIntent.getActivity(this, 0, addressIntent, PendingIntent.FLAG_ONE_SHOT);

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
}
