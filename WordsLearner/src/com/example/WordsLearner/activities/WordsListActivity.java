package com.example.WordsLearner.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.example.WordsLearner.adapters.WordsListAdapter;
import com.example.WordsLearner.R;
import com.example.WordsLearner.db.WordsLearnerDataHelper;
import com.example.WordsLearner.fragments.ChoosePhotoFragment;
import com.example.WordsLearner.model.Word;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.fortysevendeg.swipelistview.SwipeListView;

import java.util.ArrayList;
import java.util.List;

public class WordsListActivity extends Activity {

    private WordsListAdapter adapter;
    private List<Word> words;

    private SwipeListView swipeListView;
    private ProgressDialog progressDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_words_list);

        words = new ArrayList<Word>();
        adapter = new WordsListAdapter(this, words, new CloseListMenuListener());

        swipeListView = (SwipeListView) findViewById(R.id.example_lv_list);
        swipeListView.setSwipeListViewListener(new BaseSwipeListViewListener() {
            @Override
            public void onOpened(int position, boolean toRight) {
            }

            @Override
            public void onClosed(int position, boolean fromRight) {
            }

            @Override
            public void onListChanged() {
            }

            @Override
            public void onMove(int position, float x) {
            }

            @Override
            public void onStartOpen(int position, int action, boolean right) {
            }

            @Override
            public void onStartClose(int position, boolean right) {
            }

            @Override
            public void onClickFrontView(int position) {
                Intent intent = new Intent(WordsListActivity.this, LearningActivity.class);
                intent.putExtra(Word.WORD_ID_EXTRA, words.get(position).getId());
                startActivity(intent);
            }

            @Override
            public void onClickBackView(int position) {
            }

            @Override
            public void onDismiss(int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {
                    words.remove(position);
                }
                adapter.notifyDataSetChanged();
            }

        });

        Button addItemBtn = (Button) findViewById(R.id.add_item_btn);
        addItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WordsListActivity.this, CreateWordActivity.class);
                intent.putExtra(CreateWordActivity.MODE_EXTRA, CreateWordActivity.MODE_CREATE);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        swipeListView.setAdapter(adapter);
        swipeListView.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_REVEAL);
        swipeListView.setSwipeOpenOnLongPress(true);

        new LisWordsTask().execute();
    }

    public class LisWordsTask extends AsyncTask<Void, Void, List<Word>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(WordsListActivity.this);
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        protected List<Word> doInBackground(Void... args) {
            WordsLearnerDataHelper db = new WordsLearnerDataHelper(WordsListActivity.this);
            return db.getAllWords();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    public class CloseListMenuListener {
        public void closeMenu() {
            swipeListView.closeOpenedItems();
        }
    }
}
