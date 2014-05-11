package com.example.WordsLearner.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.Toast;
import com.example.WordsLearner.R;
import com.example.WordsLearner.adapters.WordsPagerAdapter;
import com.example.WordsLearner.db.WordsLearnerDataHelper;
import com.example.WordsLearner.model.Word;
import com.example.WordsLearner.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LearningActivity extends Activity {

    private final static String LOG_TAG = "LearnWords";

    public final static String SCROLL_TO_CLICKED = "scroll_to_clicked";

    private ViewPager viewPager;
    private WordsPagerAdapter pagerAdapter;
    private ProgressDialog progressDialog;

    private MediaPlayer mPlayer = null;

    private int firstWordId;
    private boolean scrollToClicked = true;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        firstWordId = getIntent().getIntExtra(Word.WORD_ID_EXTRA, -1);
        if (savedInstanceState != null) {
            scrollToClicked = savedInstanceState.getBoolean(SCROLL_TO_CLICKED);
        }

        new LisWordsTask().execute();
    }

    private void initViewPager(final List<Word> data){
        viewPager = (ViewPager) findViewById(R.id.pager);
        // TODO: put inside a constant
        viewPager.setOffscreenPageLimit(2);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                startPlaying(data.get(i).getSoundPath());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        pagerAdapter = new WordsPagerAdapter(getFragmentManager(), data);
        viewPager.setAdapter(pagerAdapter);

        // TODO: needs refactoring - put firstWordId in the beginning of the list
        //select fragment on word which item was clicked
//        if(scrollToClicked) {
//            scrollToClicked = false;
//            for(int i = 0; i < data.size(); i++) {
//                if(data.get(i).getId() == firstWordId) {
//                    viewPager.setCurrentItem(i);
//                    break;
//                }
//            }
//        }
    }

    private void startPlaying(String fileName) {
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(Utils.SOUNDS_FOLDER + File.separator + fileName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }


    public class LisWordsTask extends AsyncTask<Void, Void, List<Word>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(LearningActivity.this);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        protected List<Word> doInBackground(Void... args) {
            WordsLearnerDataHelper db = new WordsLearnerDataHelper(LearningActivity.this);
            List<Word> result =  db.getAllWords();
            Collections.shuffle(result, new Random());
            return result;
        }

        protected void onPostExecute(List<Word> result) {
            initViewPager(result);
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(SCROLL_TO_CLICKED, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, getString(R.string.please_click_back), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 3000);
    }
}
