package com.ls.ui.drawer;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.ls.ui.fragment.AboutFragment;
import com.ls.ui.fragment.EventHolderFragment;
import com.ls.ui.fragment.FloorPlanFragment;
import com.ls.ui.fragment.LocationFragment;
import com.ls.ui.fragment.SocialMediaFragment;
import com.ls.ui.fragment.SpeakersListFragment;
import org.jetbrains.annotations.NotNull;

public class DrawerManager {

	private FragmentManager fragmentManager;
	private int fragmentHolderId;

	public static DrawerManager getInstance(FragmentManager theFragmentManager, int theMainFragmentId) {
		return new DrawerManager(theFragmentManager, theMainFragmentId);
	}

	private DrawerManager(FragmentManager theFragmentManager, int theMainFragmentId) {
		this.fragmentManager = theFragmentManager;
		this.fragmentHolderId = theMainFragmentId;
	}

	public void setFragment(@NotNull EventMode mode) {
		Fragment fragment;
		String fragmentTag = null;

		switch (mode) {
			case Program:
				fragment = EventHolderFragment.newInstance(mode);
				fragmentTag = EventHolderFragment.TAG;
				break;

			case Favorites:
				fragment = EventHolderFragment.newInstance(mode);
				fragmentTag = EventHolderFragment.TAG;
				break;

			case Speakers:
				fragment = new SpeakersListFragment();
				fragmentTag = SpeakersListFragment.TAG;
				break;

			case FloorPlan:
				fragment = new FloorPlanFragment();
				fragmentTag = FloorPlanFragment.TAG;
				break;

			case Location:
				fragment = new LocationFragment();
				fragmentTag = LocationFragment.TAG;
				break;
			case SocialMedia:
				fragment = new SocialMediaFragment();
				fragmentTag = SocialMediaFragment.TAG;
				break;
			case About:
				fragment = new AboutFragment();
				fragmentTag = SocialMediaFragment.TAG;
				break;
			default:
				fragment = EventHolderFragment.newInstance(mode);
		}
		fragmentManager.beginTransaction().replace(fragmentHolderId, fragment, fragmentTag).commit();
	}

	public void reloadPrograms(@NotNull EventMode mode) {
		FragmentTransaction ft = fragmentManager.beginTransaction();
		ft.replace(fragmentHolderId, EventHolderFragment.newInstance(mode), EventHolderFragment.TAG);
		ft.commitAllowingStateLoss();
	}
}
