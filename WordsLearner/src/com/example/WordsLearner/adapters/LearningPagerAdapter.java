package com.example.WordsLearner.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import com.example.WordsLearner.fragments.WordFragment;
import com.example.WordsLearner.model.Word;

import java.util.List;

public class LearningPagerAdapter extends FragmentStatePagerAdapter {

    private List<Word> data;

    public LearningPagerAdapter(FragmentManager fm, List<Word> data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        return new WordFragment(data.get(position));
    }

    @Override
    public int getCount() {
        return data.size();
    }
}