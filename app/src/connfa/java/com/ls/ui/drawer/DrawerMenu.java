package com.ls.ui.drawer;

import com.ls.drupalcon.R;

import java.util.ArrayList;
import java.util.List;

public class DrawerMenu {

    public static List<DrawerMenuItem> getNavigationDrawerItems() {
        List<DrawerMenuItem> result = new ArrayList<>();
        result.add(new DrawerMenuItem(R.string.Sessions, R.drawable.menu_icon_program, R.drawable.menu_icon_program_sel, EventMode.Program, false));
        result.add(new DrawerMenuItem(R.string.social_media, R.drawable.menu_icon_social_media, R.drawable.menu_icon_social_media_sel, EventMode.SocialMedia, true));
        result.add(new DrawerMenuItem(R.string.my_schedule, R.drawable.menu_icon_my_schedule, R.drawable.menu_icon_my_schedule_sel, EventMode.Favorites, false));
        result.add(new DrawerMenuItem(R.string.floor_plan, R.drawable.menu_icon_floor_plan, R.drawable.menu_icon_floor_plan_sel, EventMode.FloorPlan, false));
        result.add(new DrawerMenuItem(R.string.location, R.drawable.menu_icon_location, R.drawable.menu_icon_location_sel, EventMode.Location, false));
        result.add(new DrawerMenuItem(R.string.speakers, R.drawable.menu_icon_speakers, R.drawable.menu_icon_speakers_sel, EventMode.Speakers, true));
        result.add(new DrawerMenuItem(R.string.about, R.drawable.menu_icon_about, R.drawable.menu_icon_about_sel, EventMode.About, false));

        return result;
    }
}
