package com.ls.drupalcon.model.managers;

import com.ls.drupalcon.model.PreferencesManager;
import com.ls.drupalcon.model.data.Event;
import com.ls.ui.adapter.item.EventListItem;
import com.ls.utils.DateUtils;

import java.util.Date;
import java.util.List;

public class ProgramManager extends EventManager {

    public ProgramManager() {
    }

    protected boolean storeResponse(Event.Holder requestResponse, String tag) {
        List<Event.Day> sessions = requestResponse.getDays();
        if (sessions == null) {
            return false;
        }

        List<Long> ids = mEventDao.selectFavoriteEventsSafe();
        for (Event.Day day : sessions) {

            for (Event event : day.getEvents()) {
                if (event != null) {

                    Date date = DateUtils.getInstance().convertEventDayDate(day.getDate());
                    if (date != null) {
                        event.setDate(date);
                    }
                    event.setEventClass(Event.PROGRAM_CLASS);

                    for (long id : ids) {
                        if (event.getId() == id) {
                            event.setFavorite(true);
                            break;
                        }
                    }

                    mEventDao.saveOrUpdateSafe(event);
                    saveEventSpeakers(event);

                    if (event.isDeleted()) {
                        deleteEvent(event);
                    }
                }
            }
        }
        return true;
    }

    public List<Long> getProgramDays() {
        List<Long> trackIds = PreferencesManager.getInstance().loadTracks();
        return mEventDao.selectDistrictDateByTrackIdsSafe(Event.PROGRAM_CLASS, trackIds);
    }

    public List<EventListItem> getProgramItemsSafe(int eventClass, long day, List<Long> levelIds, List<Long> trackIds) {
        return mEventDao.selectProgramItemsSafe(eventClass, day, levelIds, trackIds);
    }

    public List<EventListItem> getFavoriteProgramItemsSafe(List<Long> favoriteEventIds, long day) {
        return mEventDao.selectFavoriteProgramItemsSafe(favoriteEventIds, day);
    }
}