package com.example.WordsLearner.activities;

import android.app.Activity;
import android.os.Bundle;
import com.example.WordsLearner.R;
import com.example.WordsLearner.adapters.CreateWordPagerAdapter;
import com.example.WordsLearner.model.Word;
import com.example.WordsLearner.views.OneSideViewPager;
import com.viewpagerindicator.CirclePageIndicator;

public class CreateWordActivity extends Activity {

    public final static String MODE_EXTRA = "mode_extra";
    public final static int MODE_CREATE = 0;
    public final static int MODE_EDIT = 1;

    private int mode = MODE_CREATE;

    private Word currentWord;
    private String imageTempFilePath;
    private String soundTempFilePath;

    private OneSideViewPager viewPager;
    private CirclePageIndicator circlePageIndicator;

    public Word getCurrentWord() {
        return currentWord;
    }

    public int getMode() {
        return mode;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_word);

        viewPager = (OneSideViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(new CreateWordPagerAdapter(getFragmentManager()));

        circlePageIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
        circlePageIndicator.setViewPager(viewPager);

        if(getIntent() != null) {
            currentWord = (Word)getIntent().getSerializableExtra(Word.WORD_EXTRA);
            mode = getIntent().getIntExtra(MODE_EXTRA, MODE_CREATE);
        }
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    public void goToNextStep(int position) {
        viewPager.setCurrentItem(position);
    }

    public String getImageTempFilePath() {
        return imageTempFilePath;
    }

    public void setImageTempFilePath(String imageTempFilePath) {
        this.imageTempFilePath = imageTempFilePath;
    }

    public String getSoundTempFilePath() {
        return soundTempFilePath;
    }

    public void setSoundTempFilePath(String soundTempFilePath) {
        this.soundTempFilePath = soundTempFilePath;
    }
}
