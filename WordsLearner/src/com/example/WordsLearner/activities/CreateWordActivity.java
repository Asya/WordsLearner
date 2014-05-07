package com.example.WordsLearner.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import com.example.WordsLearner.R;
import com.example.WordsLearner.adapters.CreateWordPagerAdapter;

public class CreateWordActivity extends Activity {

    private String currentPhotoName;
    private String currentSoundName;

    private ViewPager viewPager;

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

        viewPager = (ViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(new CreateWordPagerAdapter(getFragmentManager()));
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
