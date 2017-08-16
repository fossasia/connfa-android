package com.ls.ui.drawer;

import com.ls.drupalcon.R;
import com.ls.drupalcon.model.Model;
import com.ls.drupalcon.model.UpdateRequest;
import com.ls.drupalcon.model.managers.BofsManager;

import java.util.ArrayList;
import java.util.List;

public class BofsStrategy implements EventHolderFragmentStrategy {

    private static UpdateRequest request = UpdateRequest.BOFS;

     @Override
    public List<Long> getDayList() {
        List<Long> dayList = new ArrayList<>();
        BofsManager manager = new BofsManager();
        dayList.addAll(manager.getBofsDays());
        return dayList;
    }

    @Override
    public int getTextResId() {
        return R.string.placeholder_bofs;
    }

    @Override
    public int getImageResId() {
        return R.drawable.ic_no_bofs;
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
        return EventMode.Bofs;
    }
}
