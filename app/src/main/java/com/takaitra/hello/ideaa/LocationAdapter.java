package com.takaitra.hello.ideaa;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.takaitra.hello.ideaa.model.Location;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;

/**
 * Created by yihe on 8/8/15.
 */
public class LocationAdapter extends RealmBaseAdapter<Location> implements ListAdapter {

    private static class ViewHolder {
        TextView name;
    }

    public LocationAdapter(Context context, int resId,
                     RealmResults<Location> realmResults,
                     boolean automaticUpdate) {
        super(context, realmResults, automaticUpdate);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = inflater.inflate(android.R.layout.simple_list_item_1,
                    parent, false);
            viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(android.R.id.text1);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Location item = realmResults.get(position);
        viewHolder.name.setText(item.getName());
        return convertView;
    }

    public RealmResults<Location> getRealmResults() {
        return realmResults;
    }
}
