package com.example.WordsLearner.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.example.WordsLearner.adapters.WordsListAdapter;
import com.example.WordsLearner.R;
import com.example.WordsLearner.db.WordsLearnerDataHelper;
import com.example.WordsLearner.model.Word;
import com.example.WordsLearner.utils.PreferencesManager;
import com.example.WordsLearner.utils.Utils;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;

import java.util.ArrayList;
import java.util.List;

public class WordsListActivity extends Activity {

    private final static String LOG_TAG = "WordsListActivity";

    private WordsListAdapter adapter;
    private SwipeListView swipeListView;
    private ProgressDialog progressDialog;

    private PreferencesManager prefs;

    private List<Word> words;
    private long dbTimestamp = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_words_list);

        prefs = new PreferencesManager(this);
        words = new ArrayList<Word>();
        adapter = new WordsListAdapter(this, words);

        swipeListView = (SwipeListView) findViewById(R.id.example_lv_list);
        swipeListView.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onClickFrontView(int position) {
                position -= 1;
                Utils.log(LOG_TAG, "List item clicked at position = " + position + " word = " + adapter.getItem(position));

                Intent intent = new Intent(WordsListActivity.this, LearningActivity.class);
                intent.putExtra(Word.WORD_ID_EXTRA, words.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {
                    position -= 1;
                    Utils.log(LOG_TAG, "Dismiss list item at position = " + position + " word = " + adapter.getItem(position));
                    words.remove(position);
                }
                adapter.notifyDataSetChanged();
            }
        });

        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/chalk.ttf");
        Button addItemBtn = (Button) findViewById(R.id.add_item_btn);
        addItemBtn.setTypeface(typeFace);
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WordsListActivity.this, CreateWordActivity.class);
                intent.putExtra(CreateWordActivity.MODE_EXTRA, CreateWordActivity.MODE_CREATE);
                startActivity(intent);
            }
        });

        LayoutInflater inflater = getLayoutInflater();
        View header = inflater.inflate(R.layout.title_header, swipeListView, false);
        TextView title = ((TextView)header.findViewById(R.id.text));
        title.setTypeface(typeFace);
        title.setText(getString(R.string.words));
        swipeListView.addHeaderView(header, null, false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        swipeListView.setAdapter(adapter);
        swipeListView.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL);
        swipeListView.setSwipeOpenOnLongPress(true);

        Utils.log(LOG_TAG, "Resume.DBTimestamp = " + prefs.getDbTimestamp() + " ListDBTimestamp = " + dbTimestamp);
        if(prefs.getDbTimestamp() != dbTimestamp) {
            new LisWordsTask().execute();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    /**************************************************/

    public class LisWordsTask extends AsyncTask<Void, Void, List<Word>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dbTimestamp = prefs.getDbTimestamp();

            progressDialog = new ProgressDialog(WordsListActivity.this);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        protected List<Word> doInBackground(Void... args) {
            WordsLearnerDataHelper db = new WordsLearnerDataHelper(WordsListActivity.this);
            List<Word> result = db.getAllWords();
            Utils.log(LOG_TAG, "LisWordsTask resulted words collection = " + result.toString());
            return result;
        }

        protected void onPostExecute(List<Word> result) {
            words.clear();
            words.addAll(result);
            adapter.notifyDataSetChanged();

            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
        }
    }
}
