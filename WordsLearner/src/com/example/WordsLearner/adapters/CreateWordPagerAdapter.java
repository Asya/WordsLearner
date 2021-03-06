package com.example.WordsLearner.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;
import com.example.WordsLearner.R;
import com.example.WordsLearner.fragments.ChoosePhotoFragment;
import com.example.WordsLearner.fragments.RecordSoundFragment;
import com.example.WordsLearner.fragments.SetNameFragment;
import com.viewpagerindicator.IconPagerAdapter;

public class CreateWordPagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {

    public static final int FRAGMENT_PHOTO = 0;
    public static final int FRAGMENT_SOUND = 1;
    public static final int FRAGMENT_NAME = 2;

    private static final int ITEMS_COUNT = 3;

    public CreateWordPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case FRAGMENT_PHOTO:
                return new ChoosePhotoFragment();
            case FRAGMENT_SOUND:
                return new RecordSoundFragment();
            case FRAGMENT_NAME:
                return new SetNameFragment();
        }
        return null;
    }

    @Override
    public int getIconResId(int index) {
        return R.drawable.custom_tab_indicator;
    }

    @Override
    public int getCount() {
        return ITEMS_COUNT;
    }
}