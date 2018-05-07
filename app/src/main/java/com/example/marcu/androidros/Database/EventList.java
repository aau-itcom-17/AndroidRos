package com.example.marcu.androidros.Database;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.marcu.androidros.List.NearbyFragment;
import com.example.marcu.androidros.R;

import java.util.List;

public class EventList extends ArrayAdapter<Event> {
    private Activity context;
    private List<Event> eventList;

    public EventList(Activity context, int simple_list_item_1, List<Event> eventList) {

        super(context, R.layout.list_layout, eventList);
        this.context = context;
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();

        @SuppressLint("ViewHolder") View listViewItem = inflater.inflate(R.layout.list_layout, null, true);

        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);

        Event event = eventList.get(position);

        textViewName.setText(event.getEventID());

        return listViewItem;
    }
}