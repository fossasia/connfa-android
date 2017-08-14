package com.ls.api;

import android.util.Log;

import com.ls.drupalcon.model.data.Event;
import com.ls.drupalcon.model.data.Location;
import com.ls.drupalcon.model.data.Speaker;
import com.ls.drupalcon.model.data.TimeRange;
import com.ls.drupalcon.model.data.Track;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Varun Kumar on 7/27/2017.
 */

public class Processor {
    public List<Track> trackList = new ArrayList<>();
    public List<Location> locationList = new ArrayList<>();
    public List<Speaker> speakerList = new ArrayList<>();
    public List<Event> eventList = new ArrayList<>();
    private String output;


    public Processor(String output) {
        this.output = output;
    }

    public List<Event> eventProcessor() {
        try {
            JSONArray events = new JSONArray(output);
            int i, j;

            Event event;

            for (i = 0; i < events.length(); i++) {
                JSONObject eventJSONObject = events.getJSONObject(i);
                event = new Event();

                event.setId(eventJSONObject.getLong("id"));
                event.setFromTime(eventJSONObject.getString("start_time"));
                event.setToTime(eventJSONObject.getString("end_time"));

                JSONObject typeJSONObject = eventJSONObject.getJSONObject("session_type");
                event.setType(typeJSONObject.getLong("id"));
                event.setDescription(eventJSONObject.getString("long_abstract"));

                List<Long> speakers = new ArrayList<>();
                JSONArray speakersArray = eventJSONObject.getJSONArray("speakers");

                for (j = 0; j < speakersArray.length(); j++) {
                    JSONObject speaker = speakersArray.getJSONObject(i);
                    speakers.add(speaker.getLong("id"));
                }

                event.setSpeakers(speakers);
                event.setName(eventJSONObject.getString("title"));

                JSONObject location = eventJSONObject.getJSONObject("microlocation");
                event.setPlace(location.getString("name"));
                event.setLink(eventJSONObject.getString("signup_url"));

                Log.e("******name******",event.getName());

                eventList.add(event);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return eventList;
    }

    public List<Speaker> speakerProcessor() {
        try {
            JSONArray speakers = new JSONArray(output);
            int i;

            Speaker speaker;
            DatabaseUrl databaseUrl = new DatabaseUrl();

            for (i = 0; i < speakers.length(); i++) {
                JSONObject speakerJSONObject = speakers.getJSONObject(i);
                speaker = new Speaker();

                speaker.setId(speakerJSONObject.getLong("id"));
                String name = speakerJSONObject.getString("name");

                String firstName;
                String lastName;

                if (name.contains(" ")) {
                    firstName = name.substring(0, name.indexOf(' '));
                    lastName = name.substring(name.indexOf(' '));
                    speaker.setFirstName(firstName);
                    speaker.setLastName(lastName);
                } else {
                    firstName = name;
                    lastName = "";
                    speaker.setFirstName(firstName);
                    speaker.setLastName(lastName);
                }

                speaker.setAvatarImageUrl(databaseUrl.getBaseUrl() + speakerJSONObject.getString("photo"));
                speaker.setOrganization(speakerJSONObject.getString("organisation"));
                speaker.setJobTitle(speakerJSONObject.getString("position"));
                if (speaker.getJobTitle().equals("null")) {
                    speaker.setJobTitle("");
                }
                speaker.setCharact(speakerJSONObject.getString("long_biography"));
                String twitter = speakerJSONObject.getString("twitter");
                if (twitter.length() > 0) {
                    twitter = twitter.substring(19);
                    if (twitter.contains("/"))
                        twitter = twitter.substring(1);
                    speaker.setTwitterName(twitter);
                } else {
                    speaker.setTwitterName(null);
                }
                speaker.setWebSite(speakerJSONObject.getString("website"));

                speakerList.add(speaker);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return speakerList;
    }

    public List<Location> locationProcessor() {

        try {
            JSONArray locations = new JSONArray(output);
            int i;

            Location location;

            for (i = 0; i < locations.length(); i++) {
                JSONObject locationJSONObject = locations.getJSONObject(i);
                location = new Location();

                location.setId(locationJSONObject.getLong("id"));
                location.setName(locationJSONObject.getString("name"));
                location.setAddress("Floor " + locationJSONObject.getLong("floor") + ", " + locationJSONObject.getString("name"));
                location.setLat(locationJSONObject.getDouble("latitude"));
                location.setLon(locationJSONObject.getDouble("longitude"));

                locationList.add(location);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return locationList;
    }

    public List<Track> trackProcessor() {
        try {
            JSONArray tracks = new JSONArray(output);
            int i;

            Track track;

            for (i = 0; i < tracks.length(); i++) {
                JSONObject tracksJSONObject = tracks.getJSONObject(i);

                track = new Track();
                track.setId(tracksJSONObject.getLong("id"));
                track.setName(tracksJSONObject.getString("name"));

                trackList.add(track);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return trackList;
    }

}