package com.ls.drupalcon.model.managers;

import android.util.Log;

import com.ls.drupalcon.app.App;
import com.ls.drupalcon.model.PreferencesManager;
import com.ls.drupalcon.model.dao.EventDao;
import com.ls.drupalcon.model.data.Event;
import com.ls.drupalcon.model.data.Track;
import com.ls.ui.adapter.item.EventListItem;

import java.util.ArrayList;
import java.util.List;

public class ProgramManager extends EventManager {

    private EventDao mEventDao;

    public ProgramManager() {
        mEventDao = getEventDao();
    }

    public boolean storeResponse(List<Event> sessions) {

        if (sessions == null) {
            return false;
        }

        List<Long> ids = mEventDao.selectFavoriteEventsSafe();

        int i;
        for (i = 0; i < sessions.size(); i++)
            if (sessions.get(i) != null) {

                for (long id : ids) {
                    if (sessions.get(i).getId() == id) {
                        sessions.get(i).setFavorite(true);
                        break;
                    }
                }

                //saveEventSpeakers(sessions.get(i));
                mEventDao.saveOrUpdateSafe(sessions.get(i));

                if (sessions.get(i).isDeleted()) {
                    deleteEvent(sessions.get(i));
                }
            }

        return true;
    }

    public List<Long> getProgramDays() {
        List<Long> trackIds = new ArrayList<>();
        TracksManager tracksManager = new TracksManager();
        List<Track> tracks;
        tracks = tracksManager.getTracks();

        int i;

        for (i = 0; i < tracks.size(); i++) {
            trackIds.add(tracks.get(i).getId());
        }
        //return mEventDao.selectDistrictDateByTrackIdsSafe(Event.PROGRAM_CLASS, trackIds);
        return trackIds;
    }

    public List<EventListItem> getProgramItemsSafe(int eventClass, long day, List<Long> levelIds, List<Long> trackIds) {
        return mEventDao.selectProgramItemsSafe(eventClass, day, levelIds, trackIds);
    }

    public List<EventListItem> getFavoriteProgramItemsSafe(List<Long> favoriteEventIds, long day) {
        return mEventDao.selectFavoriteProgramItemsSafe(favoriteEventIds, day);
    }
}