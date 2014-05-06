package com.example.WordsLearner.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import com.example.WordsLearner.R;
import com.example.WordsLearner.adapters.WordsPagerAdapter;
import com.example.WordsLearner.db.WordsLearnerDataHelper;
import com.example.WordsLearner.model.Word;

import java.util.List;

public class LearningActivity extends Activity {

    public final static String SCROOL_TO_CLICKED = "scroll_to_clicked";

    private ViewPager viewPager;
    private WordsPagerAdapter pagerAdapter;
    private ProgressDialog progressDialog;

    private int firstWordId;
    private boolean scrollToClicked = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learning);

        firstWordId = getIntent().getIntExtra(Word.WORD_ID_EXTRA, -1);
        if (savedInstanceState != null) {
            scrollToClicked = savedInstanceState.getBoolean(SCROOL_TO_CLICKED);
        }

        new LisWordsTask().execute();
    }

    private void initViewPager(List<Word> data){
        viewPager = (ViewPager) findViewById(R.id.pager);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i2) {
            }

            @Override
            public void onPageSelected(int i) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (state == ViewPager.SCROLL_STATE_IDLE) {
                    int curr = viewPager.getCurrentItem();
                    int lastReal = viewPager.getAdapter().getCount() - 2;
                    if (curr == 0) {
                        viewPager.setCurrentItem(lastReal, false);
                    } else if (curr > lastReal) {
                        viewPager.setCurrentItem(1, false);
                    }
                }
            }
        });

        pagerAdapter = new WordsPagerAdapter(getFragmentManager(), data);
        viewPager.setAdapter(pagerAdapter);

        //select fragment on word which item was clicked
        if(scrollToClicked) {
            for(int i = 0; i < data.size(); i++) {
                if(data.get(i).getId() == firstWordId) {
                    viewPager.setCurrentItem(i);
                    break;
                }
            }
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
            return db.getAllWords();
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
        outState.putBoolean(SCROOL_TO_CLICKED, false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
