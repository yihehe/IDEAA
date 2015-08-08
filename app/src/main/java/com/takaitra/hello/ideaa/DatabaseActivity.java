package com.takaitra.hello.ideaa;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.takaitra.hello.ideaa.model.Location;

import io.realm.Realm;
import io.realm.RealmResults;

public class DatabaseActivity extends AppCompatActivity {

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        realm = Realm.getInstance(this);

        setContentView(R.layout.database_list_view);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        ListView listView = (ListView) findViewById(android.R.id.list);
        RealmResults<Location> results = realm.allObjects(Location.class);
        LocationAdapter adapter = new LocationAdapter(this, 0, results, true);
        listView.setAdapter(adapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (realm != null) {
            realm.close();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.database_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add_location) {
            realm.beginTransaction();
//            RealmResults<Location> results = realm.allObjectsSorted(Location.class, "id", false);
//            int i = 1;
//            if (results.size() > 0) {
//                i = results.first().getId() + 1;
//            }
            int i = (int) realm.where(Location.class).maximumInt("id") + 1;
            Location location = realm.createObject(Location.class);
            location.setName("location " + i);
            location.setAddress("address " + i);
            location.setLatitude(i);
            location.setLatitude(i);
            location.setId(i);
            realm.commitTransaction();
            return true;
        } else if (id == R.id.action_delete_all) {
            realm.beginTransaction();
            RealmResults<Location> results = realm.allObjects(Location.class);
            results.clear();
            realm.commitTransaction();
        }

        return super.onOptionsItemSelected(item);
    }
}
