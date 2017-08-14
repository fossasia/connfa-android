package com.ls.ui.drawer;

import com.ls.drupalcon.R;
import com.ls.drupalcon.model.Model;
import com.ls.drupalcon.model.UpdateRequest;
import com.ls.drupalcon.model.managers.ProgramManager;

import java.util.ArrayList;
import java.util.List;

public class ProgramStrategy implements EventHolderFragmentStrategy {

    private static UpdateRequest request = UpdateRequest.PROGRAMS;

    @Override
    public List<Long> getDayList() {
        List<Long> dayList = new ArrayList<>();
        ProgramManager programManager = new ProgramManager();
        dayList.addAll(programManager.getProgramDays());
        return dayList;
    }

    @Override
    public int getTextResId() {
        return R.string.placeholder_sessions;
    }

    @Override
    public int getImageResId() {
        return R.drawable.ic_no_session;
    }

    @Override
    public boolean enableOptionMenu() {
        return true;
    }

    @Override
    public boolean updateFavorites() {
        return false;
    }

    @Override
    public boolean update(List<UpdateRequest> requests) {
        return requests.contains(request);
    }

    @Override
    public EventMode getEventMode() {
        return EventMode.Program;
    }
}
