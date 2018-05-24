package com.example.marcu.androidros.List;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marcu.androidros.Database.Event;
import com.example.marcu.androidros.R;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

class NewAdapter extends RecyclerView.Adapter<NewAdapter.ViewHolder> {
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

    public NewAdapter(Context context, ArrayList<Event> events) {
        this.events = events;
        this.context = context;
    }


    @NonNull
    @Override
    public NewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.fragment_new ,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewAdapter.ViewHolder holder, int position) {
        Event event = events.get(position);

        String imageUrl = event.getPhotoPath();
        String title = event.getName();
        String time= timeMessage(event);

        Picasso.get().load(imageUrl).into(holder.eventImage);
        holder.eventName.setText(title);
        holder.timeView.setText(time);
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
        public TextView timeView;

        public ViewHolder(View itemView) {
            super(itemView);
            eventName = (TextView) itemView.findViewById(R.id.new_fragment_event_title_text_view);
            //description = (TextView) itemView.findViewById(R.id.top_fragment_event_description_text_view);
            eventImage = (ImageView) itemView.findViewById(R.id.new_fragment_image_view);
            favourite = (ImageButton) itemView.findViewById(R.id.new_fragment_favourite_button);
            unFavourite = (ImageButton) itemView.findViewById(R.id.new_fragment_favourite_button_clicked);
            timeView = (TextView) itemView.findViewById(R.id.new_fragment_time_text);

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
