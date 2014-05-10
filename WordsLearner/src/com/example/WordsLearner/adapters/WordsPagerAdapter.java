package com.example.WordsLearner.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import com.example.WordsLearner.fragments.WordFragment;
import com.example.WordsLearner.model.Word;

import java.util.List;

public class WordsPagerAdapter extends FragmentStatePagerAdapter {

    private List<Word> data;

    public WordsPagerAdapter(FragmentManager fm, List<Word> data) {
        super(fm);
        this.data = data;
    }

    @Override
    public Fragment getItem(int position) {
        WordFragment fragment = new WordFragment();
        fragment.setWord(data.get(position));
        return fragment;
    }

    @Override
    public int getCount() {
        return data.size();
    }
}