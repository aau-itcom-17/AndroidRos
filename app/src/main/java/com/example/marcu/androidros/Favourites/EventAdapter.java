package com.example.marcu.androidros.Favourites;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class EventAdapter extends RecyclerView.Adapter<EventAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Event> myEvents;
    private OnEventClickListener listener;


    public interface OnEventClickListener{
        void onEventClick(int position);
    }

    public void setOnEventClickListener(OnEventClickListener listener){
        this.listener = listener;
    }

    public EventAdapter(Context context, ArrayList<Event> myEvents) {
        this.myEvents = myEvents;
        this.context = context;
    }


    @NonNull
    @Override
    public EventAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_my_events ,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventAdapter.ViewHolder holder, int position) {
        Event event = myEvents.get(position);

        String imageUrl = event.getPhotoPath();
        String title = event.getName();
        String description = event.getDescription();

        Picasso.get().load(imageUrl).into(holder.eventImage);
        holder.eventName.setText(title);
        //holder.description.setText(description);
    }

    @Override
    public int getItemCount() {
        return myEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView eventName;
        public TextView description;
        public ImageView eventImage;

        public ViewHolder(View itemView) {
            super(itemView);
            eventName = (TextView) itemView.findViewById(R.id.my_events_fragment_event_title_text_view);
            //description = (TextView) itemView.findViewById(R.id.my_events_fragment_event_description_text_view);
            eventImage = (ImageView) itemView.findViewById(R.id.my_events_fragment_image_view);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onEventClick(position);
                        }
                    }
                }
            });
        }
    }
}
