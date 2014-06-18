package com.example.WordsLearner.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import com.example.WordsLearner.R;
import com.example.WordsLearner.adapters.LearningPagerAdapter;
import com.example.WordsLearner.db.WordsLearnerDataHelper;
import com.example.WordsLearner.model.Word;
import com.example.WordsLearner.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class LearningActivity extends Activity {

    private final static String LOG_TAG = "LearnWordsActivity";

    private final static int PAGER_CACHE_PAGE_COUNT = 2;
    private final static int BACK_PRESS_TIMEOUT = 3000;

    private ViewPager viewPager;
    private LearningPagerAdapter pagerAdapter;
    private ProgressDialog progressDialog;
    private TextView counter;

    private MediaPlayer mPlayer = null;

    private int firstWordId;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        if(getIntent() != null) {
            firstWordId = getIntent().getIntExtra(Word.WORD_ID_EXTRA, -1);
            Utils.log(LOG_TAG, "Selected word id in Intent = " + firstWordId);
        }

        counter = (TextView)findViewById(R.id.counter);
        new LisWordsTask().execute();
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
        }, BACK_PRESS_TIMEOUT);
    }

    /**************************************************/

    private void initViewPager(final List<Word> data){
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOffscreenPageLimit(PAGER_CACHE_PAGE_COUNT);
        ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
                Utils.log(LOG_TAG, "onPageSelected position = " + i + " word = " + data.get(i));

                startPlaying(data.get(i));

                StringBuilder builder = new StringBuilder();
                builder.append(i + 1).append("/").append(data.size()).append(" ").append(data.get(i).getName());
                counter.setText(builder.toString());
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        };
        viewPager.setOnPageChangeListener(onPageChangeListener);

        pagerAdapter = new LearningPagerAdapter(getFragmentManager(), data);
        viewPager.setAdapter(pagerAdapter);
        onPageChangeListener.onPageSelected(0);  // select first item when pager was just created
    }

    private void startPlaying(Word word) {
        String fileName = word.getSoundPath();
        Utils.log(LOG_TAG, "Playing audio file " + fileName);
        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(new File(Utils.SOUNDS_FOLDER, fileName).getAbsolutePath());
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "playback failed");
            e.printStackTrace();
        }
    }

    /**************************************************/

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

            //put selected word in the beginning of the list
            for(int i = 0; i < result.size(); i++) {
                if(result.get(i).getId() == firstWordId) {
                    Word temp = result.get(i);
                    result.set(i, result.get(0));
                    result.set(0, temp);
                    break;
                }
            }

            Utils.log(LOG_TAG, "LisWordsTask resulted words collection = " + result.toString());
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
}
