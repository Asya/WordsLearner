package com.example.WordsLearner.activities;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.example.WordsLearner.R;
import com.example.WordsLearner.adapters.CreateWordPagerAdapter;
import com.example.WordsLearner.model.Word;
import com.example.WordsLearner.utils.Utils;
import com.example.WordsLearner.views.OneSideViewPager;
import com.viewpagerindicator.CirclePageIndicator;
import com.viewpagerindicator.IconPageIndicator;

public class CreateWordActivity extends Activity {

    private final static String LOG_TAG = "CreateWordActivity";

    public final static String MODE_EXTRA = "mode_extra";
    public final static int MODE_CREATE = 0;
    public final static int MODE_EDIT = 1;

    private int mode = MODE_CREATE;

    private Word currentWord;
    private String imageTempFilePath;
    private String soundTempFilePath;

    private OneSideViewPager viewPager;
    private IconPageIndicator circlePageIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_word);

        viewPager = (OneSideViewPager)findViewById(R.id.pager);
        viewPager.setAdapter(new CreateWordPagerAdapter(getFragmentManager()));

        circlePageIndicator = (IconPageIndicator)findViewById(R.id.indicator);
        circlePageIndicator.setViewPager(viewPager);

        if(getIntent() != null) {
            currentWord = (Word)getIntent().getSerializableExtra(Word.WORD_EXTRA);
            mode = getIntent().getIntExtra(MODE_EXTRA, MODE_CREATE);
        }

        Utils.log(LOG_TAG, "Word in Intent = " + currentWord);
        Utils.log(LOG_TAG, "Mode = " + mode);
    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            Utils.log(LOG_TAG, "Close activity on back pressed at the first page");
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
            Utils.log(LOG_TAG, "On back pressed to select the previous page = " + viewPager.getCurrentItem());
        }
    }

    /**************************************************/

    public String getImageTempFilePath() {
        return imageTempFilePath;
    }

    public void setImageTempFilePath(String imageTempFilePath) {
        Utils.log(LOG_TAG, "setting path to image " + imageTempFilePath);
        this.imageTempFilePath = imageTempFilePath;
    }

    public String getSoundTempFilePath() {
        return soundTempFilePath;
    }

    public void setSoundTempFilePath(String soundTempFilePath) {
        Utils.log(LOG_TAG, "setting path to audio " + soundTempFilePath);
        this.soundTempFilePath = soundTempFilePath;
    }

    public Word getCurrentWord() {
        return currentWord;
    }

    public int getMode() {
        return mode;
    }

    /**************************************************/

    public void goToNextStep(int position) {
        viewPager.setCurrentItem(position);
    }
}
