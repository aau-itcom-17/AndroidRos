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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    private ArrayList<Event> events;
    private OnEventClickListener listener;
    private FirebaseDatabase database;
    private FirebaseUser firebaseUser;


    public interface OnEventClickListener{
        void onEventClick(int position);
        void onFavouriteClick(int position);
        void onUnFavouriteClick(int position);
    }

    public void setOnEventClickListener(OnEventClickListener listener){
        this.listener = listener;
    }

    public MainAdapter(ArrayList<Event> events) {
        this.events = events;
    }

    public synchronized ArrayList<Event> updateItems(ArrayList<Event> newList) {
        if (events != null) {
            for (int i = 0; i < events.size(); i++) {
                events.remove(i);
            }
            events.addAll(newList);

        }else{
            events = newList;

        }
        this.notifyDataSetChanged();
        return events;
    }


    @NonNull
    @Override
    public MainAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_top ,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MainAdapter.ViewHolder holder, final int position) {
        final Event event = events.get(position);

        String imageUrl = event.getPhotoPath();
        String title = event.getName();
        String distance = String.valueOf(event.getDistance() +  " meters");
        String time = timeMessage(event);

        Picasso.get().load(imageUrl).into(holder.eventImage);
        holder.eventName.setText(title);
        holder.distance.setText(distance);
        holder.time.setText(time);

        /**Resets like button color*/
        holder.unFavourite.setVisibility(View.INVISIBLE);
        holder.favourite.setVisibility(View.VISIBLE);

        holder.likeCount.setText(Integer.toString(event.getLikes()));

        database = FirebaseDatabase.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        database.getReference("users").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                String id = firebaseUser.getUid();

                /**Changes like button color to red, if the user has already liked the event:*/
                if (dataSnapshot.child(id).child("favourites").hasChild(event.getEventID()))
                {
                    holder.favourite.setVisibility(View.INVISIBLE);
                    holder.unFavourite.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        public TextView likeCount;
        public TextView distance;
        public TextView time;


        public ViewHolder(View itemView) {

            super(itemView);
            eventName = (TextView) itemView.findViewById(R.id.top_fragment_event_title_text_view);
            eventImage = (ImageView) itemView.findViewById(R.id.top_fragment_image_view);
            favourite = (ImageButton) itemView.findViewById(R.id.favourite_button);
            unFavourite = (ImageButton) itemView.findViewById(R.id.favourite_button_clicked);
            likeCount = (TextView) itemView.findViewById(R.id.like_count_view);
            distance = (TextView) itemView.findViewById(R.id.top_fragment_distance_text);
            time = (TextView)itemView.findViewById(R.id.top_fragment_time_text);


            /**Changing the color of the like button, when pressing like:*/
            favourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        final int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onFavouriteClick(position);
                            favourite.setVisibility(View.INVISIBLE);
                            unFavourite.setVisibility(View.VISIBLE);

                            Event event = events.get(position);
                            likeCount.setText(Integer.toString(event.getLikes()));
                        }
                    }
                }
            });

            /**Changing the color of the like button, when pressing unLike:*/
            unFavourite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        final int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onUnFavouriteClick(position);
                            unFavourite.setVisibility(View.INVISIBLE);
                            favourite.setVisibility(View.VISIBLE);

                            Event event = events.get(position);
                            likeCount.setText(Integer.toString(event.getLikes()));
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
    public String timeMessage (Event event) {

        DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Date today = Calendar.getInstance().getTime();
        String reportDate = df.format(today);

        String[] separatedDate = reportDate.split(" ", 2);
        String date = separatedDate[0];
        String time = separatedDate[1];
        String[] separated = date.split("/");
        String[] separatedTime = time.split(":");
        String day = separated[0];
        String month = separated[1];
        String year = separated[2];
        String hour = separatedTime[0];
        String minutes = separatedTime[1];

        String[] separatedEvent = event.getDate().split("/");
        String[] separatedEventTime = event.getTime().split(":");
        String eventDay = separatedEvent[0];
        String eventMonth = separatedEvent[1];
        String eventYear = separatedEvent[2];
        String eventHour = separatedEventTime[0];
        String eventMinutes = separatedEventTime[1];

        if (Integer.parseInt(day) > Integer.parseInt(eventDay) && Integer.parseInt(month) <= Integer.parseInt(eventMonth)) {
            return event.getDate() + " " + event.getTime();
        } else if (Integer.parseInt(day) == Integer.parseInt(eventDay)) {
            if (Integer.parseInt(hour) + (Double.parseDouble(minutes) / 100) > Integer.parseInt(eventHour) + (Double.parseDouble(eventMinutes) / 100) + -1
                    && Integer.parseInt(hour) + (Double.parseDouble(minutes) / 100)  < Integer.parseInt(eventHour) + (Double.parseDouble(eventMinutes) / 100)) {
                return  "Soon, " + event.getTime();
            } else if (18 < Integer.parseInt(eventHour) && Integer.parseInt(eventHour) < 24) {
                return "Tonight, " + event.getTime();
            } else {
                return "Today, " + event.getTime();
            }
        } else if (Integer.parseInt(day) == Integer.parseInt(eventDay) - 1) {
            return "Tomorrow, " + event.getTime();
        } else {
            return  event.getDate() + " " + event.getTime();
        }

    }
}
