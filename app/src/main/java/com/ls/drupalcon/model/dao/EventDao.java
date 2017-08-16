package com.ls.drupalcon.model.dao;

import android.content.Context;
import android.database.Cursor;

import com.ls.drupalcon.R;
import com.ls.drupalcon.model.AppDatabaseInfo;
import com.ls.drupalcon.model.data.Event;
import com.ls.drupalcon.model.data.EventDetailsEvent;
import com.ls.drupalcon.model.data.SpeakerDetailsEvent;
import com.ls.drupalcon.model.data.TimeRange;
import com.ls.drupalcon.model.database.AbstractEntityDAO;
import com.ls.drupalcon.model.database.ILAPIDBFacade;
import com.ls.ui.adapter.item.BofsItem;
import com.ls.ui.adapter.item.EventListItem;
import com.ls.ui.adapter.item.ProgramItem;
import com.ls.ui.adapter.item.SocialItem;
import com.ls.utils.ArrayUtils;
import com.ls.utils.CursorParser;
import com.ls.utils.CursorStringParser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class EventDao extends AbstractEntityDAO<Event, Long> {

    public static final String TABLE_NAME = "table_event";
    private final Context mContext;
    private boolean mShouldBreak;

    public EventDao(Context context) {
        mContext = context;
    }

    @Override
    protected String getSearchCondition() {
        return "_id=?";
    }

    @Override
    protected String[] getSearchConditionArguments(Long theId) {
        return new String[]{theId.toString()};
    }

    @Override
    protected String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public String getDatabaseName() {
        return AppDatabaseInfo.DATABASE_NAME;
    }

    @Override
    protected Event newInstance() {
        return new Event();
    }

    @Override
    protected String[] getKeyColumns() {
        return new String[0];
    }

    @Override
    public int deleteAll() {
        String query = mContext.getString(R.string.delete_event_and_speaker);
        getFacade().query(query, null);

        return super.deleteAll();
    }

    public void deleteEventAndSpeakerBySpeaker(long speakerId) {
        String[] args = ArrayUtils.build(speakerId);
        String query = mContext.getString(R.string.delete_event_and_speaker_by_speaker_id);
        getFacade().execSQL(query, args);
    }

    public void deleteEventAndSpeakerByEvent(long eventId) {
        String[] args = ArrayUtils.build(eventId);
        String query = mContext.getString(R.string.delete_event_and_speaker_by_event_id);
        getFacade().execSQL(query, args);
    }

    public void deleteByEventAndSpeaker(long eventId, long speakerId) {
        String[] args = ArrayUtils.build(eventId,speakerId);
        String query = mContext.getString(R.string.delete_event_and_speaker_by_event_and_speaker_id);
        getFacade().execSQL(query, args);
    }

    public void insertEventSpeaker(long eventId, long speakerId) {
        String[] bindArgs = ArrayUtils.build(eventId, speakerId);
        getFacade().execSQL(mContext.getString(R.string.insert_event_speaker), bindArgs);
    }

    public List<Event> selectProgramsSafe() {
        String[] selectionArgs = ArrayUtils.build(Event.PROGRAM_CLASS);
        String query = mContext.getString(R.string.select_events_by_class);

        return querySafe(query, selectionArgs);
    }

    public List<Event> selectBofsSafeSafe() {
        String[] selectionArgs = ArrayUtils.build(Event.BOFS_CLASS);
        String query = mContext.getString(R.string.select_events_by_class);

        return querySafe(query, selectionArgs);
    }

    public List<Event> selectEventsByDaySafe(int eventClass, long date) {
        String[] selectionArgs = ArrayUtils.build(eventClass, date);
        String query = mContext.getString(R.string.select_events_by_class_and_date);

        return querySafe(query, selectionArgs);
    }

    public List<Event> selectEventsByIdsSafe(List<Long> eventIds) {
        String query = mContext.getString(R.string.select_events_by_ids);
        return querySafe(String.format(query, getArrayAsString(eventIds)), null);
    }

    public List<Event> selectEventsByIdsAndDaySafe(List<Long> eventIds, long day) {
        String query = mContext.getString(R.string.select_events_by_ids_and_date);
        String[] selectionArgs = ArrayUtils.build(day);
        return querySafe(String.format(query, getArrayAsString(eventIds)), selectionArgs);
    }

    private String getArrayAsString(List<Long> eventIds) {
        StringBuilder builder = new StringBuilder();

        int count = 1;
        int size = eventIds.size();

        for (Long id : eventIds) {
            if (size == 1) {
                builder.append(id);
            } else if (count != size) {
                builder.append(id);
                builder.append(",");
            } else {
                builder.append(id);
            }

            count++;
        }
        return builder.toString();
    }

    public List<Long> selectEventSpeakersSafe(long eventId) {
        String[] selectionArgs = ArrayUtils.build(eventId);
        String query = mContext.getString(R.string.select_event_speakers);

        return selectLongArraySafe(selectionArgs, query);
    }

    public List<Long> selectSpeakerEventsSafe(long speakerId) {
        String[] selectionArgs = ArrayUtils.build(speakerId);
        String query = mContext.getString(R.string.select_speaker_events);

        return selectLongArraySafe(selectionArgs, query);
    }

    public List<Long> selectDistrictDateSafe() {
        String query = mContext.getString(R.string.select_date_distinct);
        return selectLongArraySafe(null, query);
    }

    public List<Long> selectDistrictFavoriteDateSafe() {
        String query = mContext.getString(R.string.select_favorite_date_distinct);
        return selectLongArraySafe(null, query);
    }

    public List<Long> selectDistrictDateSafe(int eventClass) {
        String[] selectionArgs = ArrayUtils.build(eventClass);
        String query = mContext.getString(R.string.select_date_distinct_by_class);
        return selectLongArraySafe(selectionArgs, query);
    }

    public List<Long> selectDistrictDateByLevelIdsSafe(int eventClass, List<Long> levelIds) {
        String[] selectionArgs = ArrayUtils.build(eventClass);
        String rawQuery = mContext.getString(R.string.select_date_distinct_by_class_and_expLevel_ids);
        String query = String.format(rawQuery, getArrayAsString(levelIds));
        return selectLongArraySafe(selectionArgs, query);
    }

    public List<Long> selectDistrictDateByTrackIdsSafe(int eventClass, List<Long> trackIds) {
        String[] selectionArgs = ArrayUtils.build(eventClass);
        String rawQuery = mContext.getString(R.string.select_date_distinct_by_class_and_track_ids);
        String query = String.format(rawQuery, getArrayAsString(trackIds));
        return selectLongArraySafe(selectionArgs, query);
    }

    public List<Long> selectDistrictDateByTrackAndLevelIdsSafe(int eventClass, List<Long> levelIds, List<Long> trackIds) {
        String[] selectionArgs = ArrayUtils.build(eventClass);
        String rawQuery = mContext.getString(R.string.select_date_distinct_by_class_and_expLevel_ids_and_track_ids);
        String query = String.format(rawQuery, getArrayAsString(levelIds), getArrayAsString(trackIds));
        return selectLongArraySafe(selectionArgs, query);
    }

    public void setFavoriteSafe(long evenId, boolean isFavorite) {
        ILAPIDBFacade facade = getFacade();

        try {
            facade.open();

            String[] selectionArgs = ArrayUtils.build(getIntFromBool(isFavorite), evenId);
            String query = mContext.getString(R.string.update_event_favorite);
            facade.execSQL(query, selectionArgs);

            selectionArgs = ArrayUtils.build(evenId);
            query = isFavorite
                    ? mContext.getString(R.string.insert_favorite_event)
                    : mContext.getString(R.string.delete_event_favorite)
            ;

            facade.execSQL(query, selectionArgs);
        } finally {
            facade.close();
        }
    }

    public List<Long> selectFavoriteEventsSafe() {
        String query = mContext.getString(R.string.select_favorite_events);
        return selectLongArraySafe(null, query);
    }

    private List<Long> selectLongArraySafe(String[] selectionArgs, String query) {
        ILAPIDBFacade facade = getFacade();
        List<Long> dataList = new ArrayList<Long>();

        try {
            facade.open();

            Cursor cursor = facade.query(query, selectionArgs);
            CursorParser parser = new CursorParser(cursor);

            boolean moved = parser.moveToFirst();
            while (moved) {
                dataList.add(parser.readLong());
                moved = parser.moveToNext();
            }
            parser.close();

        } finally {
            facade.close();
        }

        return dataList;
    }


    public List<TimeRange> selectDistrictTimeRangeSafe(List<Long> eventIds) {
        String query = mContext.getString(R.string.select_distinct_time_range_by_event_ids);
        return selectDistrictTimeRangeSafe(null, String.format(query, getArrayAsString(eventIds)));
    }

    public List<TimeRange> selectDistrictTimeRangeSafe(int eventClass, long date) {
        String[] selectionArgs = ArrayUtils.build(eventClass, date);
        String query = mContext.getString(R.string.select_distinct_time_range);

        return selectDistrictTimeRangeSafe(selectionArgs, query);
    }

    public List<TimeRange> selectDistrictTimeRangeByLevelTrackIdsSafe(int eventClass, long date, List<Long> levelIds, List<Long> trackIds) {
        String[] selectionArgs = ArrayUtils.build(eventClass, date);

        String query;

        if (levelIds.isEmpty() & trackIds.isEmpty()) {
            query = mContext.getString(R.string.select_distinct_time_range);
        } else if (!levelIds.isEmpty() & !trackIds.isEmpty()) {
            String rawQuery = mContext.getString(R.string.select_distinct_time_range_by_expLevel_and_track_ids);
            query = String.format(rawQuery, getArrayAsString(levelIds), getArrayAsString(trackIds));
        } else if (!levelIds.isEmpty() & trackIds.isEmpty()) {
            String rawQuery = mContext.getString(R.string.select_distinct_time_range_by_expLevel_ids);
            query = String.format(rawQuery, getArrayAsString(levelIds));
        } else {
            String rawQuery = mContext.getString(R.string.select_distinct_time_range_by_track_ids);
            query = String.format(rawQuery, getArrayAsString(trackIds));
        }

        return selectDistrictTimeRangeSafe(selectionArgs, query);
    }

    public List<TimeRange> selectDistrictFavTimeRangeSafe(int eventClass, List<Long> favoriteEventIds, long date) {
        String[] selectionArgs = ArrayUtils.build(eventClass, date);

        String rawQuery = mContext.getString(R.string.select_distinct_fav_time_range);
        String query = String.format(rawQuery, getArrayAsString(favoriteEventIds));

        return selectDistrictTimeRangeSafe(selectionArgs, query);
    }

    private List<TimeRange> selectDistrictTimeRangeSafe(String[] selectionArgs, String query) {
        ILAPIDBFacade facade = getFacade();
        List<TimeRange> dataList = new ArrayList<TimeRange>();

        try {
            facade.open();

            Cursor cursor = facade.query(query, selectionArgs);
            CursorParser parser = new CursorParser(cursor);

            boolean moved = parser.moveToFirst();
            while (moved) {
                if (mShouldBreak) {
                    return dataList;
                }

                Calendar from = null;
                Calendar to = null;

                long fromTime = parser.readLong();
                long toTime = parser.readLong();
                long date = parser.readLong();

                if (fromTime != Long.MAX_VALUE && toTime != Long.MAX_VALUE) {
                    from = Calendar.getInstance(Locale.UK);
                    from.setTimeInMillis(fromTime);

                    to = Calendar.getInstance(Locale.UK);
                    to.setTimeInMillis(toTime);
                }

                dataList.add(new TimeRange(date, from, to));
                moved = parser.moveToNext();
            }
            parser.close();

        } finally {
            facade.close();
        }

        return dataList;
    }


    public List<SpeakerDetailsEvent> getEventsBySpeakerId(long speakerId) {
        ArrayList<SpeakerDetailsEvent> ret = new ArrayList<SpeakerDetailsEvent>();
        String query =
                "SELECT table_event._id, _from, _to, _date, _name, _favorite, _place, level_name, track_name FROM table_event "
                        +
                        "LEFT OUTER JOIN table_level ON _experience_level = table_level._id " +
                        "LEFT OUTER JOIN table_track ON _track = table_track._id " +
                        "WHERE table_event._id IN (" +
                        "SELECT _event_id FROM table_event_and_speaker WHERE _speaker_id = "
                        + speakerId + ")";

        ILAPIDBFacade facade = getFacade();

        facade.open();
        try {
            Cursor cursor = facade.query(query, null);

            if (!cursor.moveToFirst()) {
                return ret;
            }

            do {
                SpeakerDetailsEvent event = new SpeakerDetailsEvent();

                event.setEventId(cursor.getLong(cursor.getColumnIndex("_id")));
                event.setEventName(cursor.getString(cursor.getColumnIndex("_name")));
                event.setLevelName(cursor.getString(cursor.getColumnIndex("level_name")));
                event.setTrackName(cursor.getString(cursor.getColumnIndex("track_name")));
                event.setDate(cursor.getLong(cursor.getColumnIndex("_date")));
                event.setFavorite(cursor.getInt(cursor.getColumnIndex("_favorite")) == 1);
                event.setPlace(cursor.getString(cursor.getColumnIndex("_place")));

                long timeFrom = cursor.getLong(cursor.getColumnIndex("_from"));
                event.setFrom(timeFrom);

                long timeTo = cursor.getLong(cursor.getColumnIndex("_to"));
                event.setTo(timeTo);

                ret.add(event);

            } while (cursor.moveToNext());
        } finally {
            facade.close();
        }

        return ret;
    }

    public EventDetailsEvent getEventById(long eventId) {
        String query =
                "SELECT table_event._id, _from, _to, _name, _place, _description, _link, _experience_level, _favorite, level_name, track_name FROM table_event "
                        +
                        "LEFT OUTER JOIN table_level ON _experience_level = table_level._id " +
                        "LEFT OUTER JOIN table_track ON _track = table_track._id " +
                        "WHERE table_event._id = " + eventId;

        ILAPIDBFacade facade = getFacade();

        facade.open();
        try {
            Cursor cursor = facade.query(query, null);

            if (!cursor.moveToFirst()) {
                return null;
            }

            EventDetailsEvent event = new EventDetailsEvent();
            event.setEventId(cursor.getLong(cursor.getColumnIndex("_id")));
            event.setEventName(cursor.getString(cursor.getColumnIndex("_name")));
            event.setLevel(cursor.getString(cursor.getColumnIndex("level_name")));
            event.setLevelId(cursor.getLong(cursor.getColumnIndex("_experience_level")));
            event.setTrack(cursor.getString(cursor.getColumnIndex("track_name")));
            event.setFavorite(cursor.getInt(cursor.getColumnIndex("_favorite")) == 1);
            event.setPlace(cursor.getString(cursor.getColumnIndex("_place")));
            event.setLink(cursor.getString(cursor.getColumnIndex("_link")));
            event.setDescription(cursor.getString(cursor.getColumnIndex("_description")));

            long from = cursor.getLong(cursor.getColumnIndex("_from"));
            event.setFrom(from);
            long to = cursor.getLong(cursor.getColumnIndex("_to"));
            event.setTo(to);

            return event;
        } finally {
            facade.close();
        }
    }

    public List<EventListItem> selectProgramItemsSafe(int eventClass, long date, List<Long> levelIds, List<Long> trackIds) {
        String[] selectionArgs = ArrayUtils.build(eventClass, date);
        String query;

        if (levelIds.isEmpty() & trackIds.isEmpty()) {
            query = mContext.getString(R.string.select_program_items_by_date);
        } else if (!levelIds.isEmpty() & !trackIds.isEmpty()) {
            String rawQuery = mContext.getString(R.string.select_program_items_by_date_and_track_ids_and_expLevel_ids);
            query = String.format(rawQuery, getArrayAsString(trackIds), getArrayAsString(levelIds));
        } else if (!levelIds.isEmpty() & trackIds.isEmpty()) {
            String rawQuery = mContext.getString(R.string.select_program_items_by_date_and_expLevel_ids);
            query = String.format(rawQuery, getArrayAsString(levelIds));
        } else {
            String rawQuery = mContext.getString(R.string.select_program_items_by_date_and_track_ids);
            query = String.format(rawQuery, getArrayAsString(trackIds));
        }

        ILAPIDBFacade facade = getFacade();
        List<EventListItem> dataList = new ArrayList<>();

        try {
            facade.open();

            Cursor cursor = facade.query(query, selectionArgs);
            CursorStringParser parser = new CursorStringParser(cursor);

            ProgramItem lastItem = new ProgramItem();
            long lastId = -1;

            boolean moved = cursor.moveToFirst();
            while (moved) {
                if (mShouldBreak) {
                    break;
                }

                long eventId = cursor.getLong(cursor.getColumnIndex("_id"));
                if (lastId != eventId) {
                    lastItem = new ProgramItem();
                    Event event = new Event();
                    event.initializePartly(parser);

                    lastItem.setTrack(parser.readString("track_name"));
                    lastItem.setLevel(parser.readString("level_name"));

                    lastItem.setEvent(event);
                    dataList.add(lastItem);
                }

                String speakerName = parser.readString("_speaker_name");
                if (speakerName != null) {
                    String[] speakerNames = speakerName.split(",");
                    for(int count = 0;count < speakerNames.length;count++) {
                        lastItem.addSpeaker(speakerNames[count]);
                    }
                }

                lastId = eventId;
                moved = cursor.moveToNext();
            }
            cursor.close();

        } finally {
            facade.close();
        }

        return dataList;
    }

    public List<EventListItem> selectFavoriteProgramItemsSafe(List<Long> eventIds, long date) {
        String[] selectionArgs = ArrayUtils.build(date);
        String rawQuery = mContext.getString(R.string.select_fav_program_items_by_date_and_fav_ids);
        String query = String.format(rawQuery, getArrayAsString(eventIds));

        ILAPIDBFacade facade = getFacade();
        List<EventListItem> dataList = new ArrayList<>();

        try {
            facade.open();

            Cursor cursor = facade.query(query, selectionArgs);
            CursorStringParser parser = new CursorStringParser(cursor);

            ProgramItem lastItem = new ProgramItem();
            long lastId = -1;

            boolean moved = cursor.moveToFirst();
            while (moved) {
                if (mShouldBreak) {
                    break;
                }

                long eventId = cursor.getLong(cursor.getColumnIndex("_id"));
                if (lastId != eventId) {
                    lastItem = new ProgramItem();
                    Event event = new Event();
                    event.initializePartly(parser);

                    lastItem.setTrack(parser.readString("track_name"));
                    lastItem.setLevel(parser.readString("level_name"));

                    lastItem.setEvent(event);
                    dataList.add(lastItem);
                }

                String speakerName = parser.readString("_speaker_name");
                if (speakerName != null) {
                    String[] speakerNames = speakerName.split(",");
                    for(int count = 0;count < speakerNames.length;count++) {
                        lastItem.addSpeaker(speakerNames[count]);
                    }
                }

                lastId = eventId;
                moved = cursor.moveToNext();
            }
            cursor.close();

        } finally {
            facade.close();
        }

        return dataList;
    }

    public List<EventListItem> selectBofsItemsSafe(int eventClass, long date) {
        String[] selectionArgs = ArrayUtils.build(eventClass, date);
        String query = mContext.getString(R.string.select_program_items_by_date);

        ILAPIDBFacade facade = getFacade();
        List<EventListItem> dataList = new ArrayList<EventListItem>();

        try {
            facade.open();
            Cursor cursor = facade.query(query, selectionArgs);
            CursorStringParser parser = new CursorStringParser(cursor);

            boolean moved = cursor.moveToFirst();
            while (moved) {
                if (mShouldBreak) {
                    break;
                }

                Event event = new Event();
                event.initializePartly(parser);

                BofsItem item = new BofsItem();
                item.setEvent(event);

                String speakerName = parser.readString("_speaker_name");
                if (speakerName != null) {
                    String[] speakerNames = speakerName.split(",");
                    for(int count = 0;count < speakerNames.length;count++) {
                        item.addSpeaker(speakerNames[count]);
                    }
                }

                dataList.add(item);

                moved = cursor.moveToNext();
            }
            cursor.close();

        } finally {
            facade.close();
        }

        return dataList;
    }

    public List<EventListItem> selectSocialItemsSafe(int eventClass, long date) {
        String[] selectionArgs = ArrayUtils.build(eventClass, date);
        String query = mContext.getString(R.string.select_events_partly_class_and_date);

        ILAPIDBFacade facade = getFacade();
        List<EventListItem> dataList = new ArrayList<EventListItem>();

        try {
            facade.open();
            Cursor cursor = facade.query(query, selectionArgs);
            CursorStringParser parser = new CursorStringParser(cursor);

            boolean moved = cursor.moveToFirst();
            while (moved) {
                if (mShouldBreak) {
                    break;
                }

                Event event = new Event();
                event.initializePartly(parser);

                SocialItem item = new SocialItem();
                item.setEvent(event);

                String speakerName = parser.readString("_speaker_name");
                if (speakerName != null) {
                    String[] speakerNames = speakerName.split(",");
                    for(int count = 0;count < speakerNames.length;count++) {
                        item.addSpeaker(speakerNames[count]);
                    }
                }

                dataList.add(item);


                moved = cursor.moveToNext();
            }
            cursor.close();

        } finally {
            facade.close();
        }

        return dataList;
    }

    public List<Long> selectSpeakerEventIds() {
        String query = mContext.getString(R.string.select_speaker_events_ids);

        ILAPIDBFacade facade = getFacade();
        List<Long> dataList = new ArrayList<Long>();

        try {
            facade.open();
            Cursor cursor = facade.query(query, null);
            CursorParser parser = new CursorParser(cursor);

            boolean moved = parser.moveToFirst();
            while (moved) {

                dataList.add(parser.readLong());
                moved = parser.moveToNext();
            }
            cursor.close();

        } finally {
            facade.close();
        }

        return dataList;
    }

    public void setShouldBreak(boolean shouldBreak) {
        mShouldBreak = shouldBreak;
    }
}
