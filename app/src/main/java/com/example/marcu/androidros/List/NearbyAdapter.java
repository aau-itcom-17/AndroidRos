package com.example.marcu.androidros.List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

class NearbyAdapter extends RecyclerView.Adapter<NearbyAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Event> events;
    private OnEventClickListener listener;

    public interface OnEventClickListener{
        void onEventClick(int position);
        void onFavouriteClick(int position);
        void onUnFavouriteClick(int position);

    }

    public void setOnEventClickListener(OnEventClickListener listener){
        this.listener = listener;
    }

    public NearbyAdapter(Context context, ArrayList<Event> events) {
        this.events = events;
        this.context = context;
    }


    @NonNull
    @Override
    public NearbyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_nearby ,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NearbyAdapter.ViewHolder holder, int position) {
        Event event = events.get(position);

        String imageUrl = event.getPhotoPath();
        String title = event.getName();
        String distance = String.valueOf(event.getDistance());

        Picasso.get().load(imageUrl).into(holder.eventImage);
        holder.eventName.setText(title);
        holder.distanceView.setText(distance);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView eventName;
        public TextView description;
        public ImageView eventImage;
        public ImageButton favourite;
        public ImageButton unFavourite;
        public TextView distanceView;

        public ViewHolder(View itemView) {
            super(itemView);
            eventName = (TextView) itemView.findViewById(R.id.nearby_fragment_event_title_text_view);
            //description = (TextView) itemView.findViewById(R.id.top_fragment_event_description_text_view);
            eventImage = (ImageView) itemView.findViewById(R.id.nearby_fragment_image_view);
            favourite = (ImageButton) itemView.findViewById(R.id.nearby_fragment_favourite_button);
            unFavourite = (ImageButton) itemView.findViewById(R.id.nearby_fragment_favourite_button_clicked);
            distanceView = (TextView) itemView.findViewById(R.id.nearby_fragment_distance_text);

            //favourite.setVisibility();
            favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onFavouriteClick(position);
                            favourite.setVisibility(View.INVISIBLE);
                            unFavourite.setVisibility(View.VISIBLE);
                        }
                    }
                }
            });

            unFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onUnFavouriteClick(position);
                            unFavourite.setVisibility(View.INVISIBLE);
                            favourite.setVisibility(View.VISIBLE);
                        }
                    }

                }
            });

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