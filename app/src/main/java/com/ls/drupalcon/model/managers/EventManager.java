package com.ls.drupalcon.model.managers;

import com.ls.drupalcon.app.App;
import com.ls.drupalcon.model.dao.EventDao;
import com.ls.drupalcon.model.data.Event;
import com.ls.drupalcon.model.data.EventDetailsEvent;
import com.ls.drupalcon.model.data.TimeRange;

import java.util.List;

public class EventManager {

    protected EventDao mEventDao;

    public EventManager() {
        mEventDao = new EventDao(App.getContext());
    }

    public void saveEventSpeakers(Event data) {
        Long eventId = data.getId();
        List<Long> speakerEventIds = mEventDao.selectEventSpeakersSafe(eventId);

        for (Long speakerId : data.getSpeakers()) {
           if (!speakerEventIds.contains(speakerId)) {
               // mEventDao.insertEventSpeaker(eventId, speakerId);
            }

            speakerEventIds.remove(speakerId);
        }

        //Delete removed speakers
        for(Long speakerId:speakerEventIds){
            mEventDao.deleteByEventAndSpeaker(eventId,speakerId);
        }
    }

    public void deleteEvent(Event data) {
        mEventDao.deleteDataSafe(data.getId());
        mEventDao.deleteEventAndSpeakerByEvent(data.getId());
        mEventDao.setFavoriteSafe(data.getId(), false);
    }

    public EventDetailsEvent getEventById(long id) {
        return mEventDao.getEventById(id);
    }

    public List<TimeRange> getDistrictTimeRangeSafe(int eventClass, long day) {
        return mEventDao.selectDistrictTimeRangeSafe(eventClass, day);
    }

    public List<TimeRange> getDistrictTimeRangeSafe(int eventClass, long day, List<Long> levelIds, List<Long> trackIds) {
        return mEventDao.selectDistrictTimeRangeByLevelTrackIdsSafe(eventClass, day, levelIds, trackIds);
    }

    public List<TimeRange> getDistrictFavoriteTimeRangeSafe(int eventClass, List<Long> favoriteEventIds, long day) {
        return mEventDao.selectDistrictFavTimeRangeSafe(eventClass, favoriteEventIds, day);
    }

    public List<Long> getEventSpeakerSafe(long id) {
        return mEventDao.selectEventSpeakersSafe(id);
    }

    public List<Event> getEventsByIdsAndDaySafe(long day) {
        FavoriteManager favoriteManager = new FavoriteManager();
        List<Long> favoriteEventIds = favoriteManager.getFavoriteEventsSafe();
        return mEventDao.selectEventsByIdsAndDaySafe(favoriteEventIds, day);
    }

    public void clear() {
        mEventDao.deleteAll();
    }

    public EventDao getEventDao() {
        return mEventDao;
    }
}
