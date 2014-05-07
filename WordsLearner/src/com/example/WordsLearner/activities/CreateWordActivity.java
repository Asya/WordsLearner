package com.example.WordsLearner.activities;

import android.app.Activity;
import android.os.Bundle;
import com.example.WordsLearner.R;
import com.example.WordsLearner.adapters.CreateWordPagerAdapter;
import com.example.WordsLearner.views.OneSideViewPager;
import com.viewpagerindicator.CirclePageIndicator;

public class CreateWordActivity extends Activity {

    private String currentPhotoName;
    private String currentSoundName;

    private OneSideViewPager viewPager;
    private CirclePageIndicator circlePageIndicator;

    public String getCurrentPhotoName() {
        return currentPhotoName;
    }

    public void setCurrentPhotoName(String currentPhotoName) {
        this.currentPhotoName = currentPhotoName;
        this.currentSoundName = changeExtention(currentPhotoName);
    }

    public String getCurrentSoundName() {
        return currentSoundName;
    }

    public void setCurrentSoundName(String currentSoundName) {
        this.currentSoundName = currentSoundName;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_word);

        viewPager = (OneSideViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(new CreateWordPagerAdapter(getFragmentManager()));

        circlePageIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        circlePageIndicator.setViewPager(viewPager);
    }

    public void goToNextStep(int position) {
        viewPager.setCurrentItem(position);
    }

    private String changeExtention(String name) {
        String filenameArray[] = name.split("\\.");
        String extension = filenameArray[filenameArray.length-1];
        return name.replace("." + extension, ".mp3");
    }
}
