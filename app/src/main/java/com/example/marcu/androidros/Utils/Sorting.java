package com.example.marcu.androidros.Utils;

import com.example.marcu.androidros.Database.Event;

import org.xml.sax.SAXException;
import java.io.IOException;
import java.util.List;
import javax.xml.parsers.ParserConfigurationException;

public class Sorting {



    public static void sortByMyEvents(List<Event> events, List<Event> myEvents, List<Event> newestEvents, String selection) throws ParserConfigurationException, SAXException, IOException {
        myEvents.clear();

        for (int i = 0; i < events.size(); i++) {

                if (events.get(i).getName().equals("THE USER")) {
                    myEvents.add(events.get(i));
                }
            }
    }




}
