package com.example.marcu.androidros.Favourites;

import android.content.Context;
import android.support.annotation.NonNull;
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

import java.util.ArrayList;

class MyPageAdapter extends RecyclerView.Adapter<MyPageAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Event> myEvents;
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

    public MyPageAdapter(Context context, ArrayList<Event> myEvents) {
        this.myEvents = myEvents;
        this.context = context;
    }


    @NonNull
    @Override
    public MyPageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_top ,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyPageAdapter.ViewHolder holder, int position) {
        final Event event = myEvents.get(position);

        String imageUrl = event.getPhotoPath();
        String title = event.getName();
        String description = event.getDescription();

        Picasso.get().load(imageUrl).into(holder.eventImage);
        holder.eventName.setText(title);

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
        return myEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView eventName;
        public TextView description;
        public ImageView eventImage;
        public ImageButton favourite;
        public ImageButton unFavourite;
        public TextView likeCount;


        public ViewHolder(View itemView) {

            super(itemView);
            eventName = (TextView) itemView.findViewById(R.id.top_fragment_event_title_text_view);
            eventImage = (ImageView) itemView.findViewById(R.id.top_fragment_image_view);
            favourite = (ImageButton) itemView.findViewById(R.id.favourite_button);
            unFavourite = (ImageButton) itemView.findViewById(R.id.favourite_button_clicked);
            likeCount = (TextView) itemView.findViewById(R.id.like_count_view);


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

                            Event event = myEvents.get(position);
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

                            Event event = myEvents.get(position);
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
}
