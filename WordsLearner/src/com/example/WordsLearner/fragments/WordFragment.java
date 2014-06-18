package com.example.WordsLearner.fragments;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.WordsLearner.R;
import com.example.WordsLearner.model.Word;
import com.example.WordsLearner.utils.Utils;

import java.io.File;

public class WordFragment extends Fragment {

    private final static String LOG_TAG = "WordFragment";
    private Word word;

    private ImageView imageView;

    public WordFragment(Word word) {
        Utils.log(LOG_TAG, "initialised with word = " + word);
        this.word = word;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_word, container, false);
        imageView = (ImageView)rootView.findViewById(R.id.image);

        if (savedInstanceState != null) {
            Utils.log(LOG_TAG, "savedInstanceState word = " + word);
            word = (Word)savedInstanceState.getSerializable(Word.WORD_EXTRA);
        }

        new ShowImageTask().execute();
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable(Word.WORD_EXTRA, word);
    }

    /**************************************************/

    public class ShowImageTask extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //get screen size to scale image to screen size
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point screenSize = new Point();
            display.getSize(screenSize);
        }

        protected Bitmap doInBackground(Void... args) {
            String location = new File(Utils.IMAGES_FOLDER, word.getImagePath()).getAbsolutePath();
            Utils.log(LOG_TAG, "loading image %s", location);
            return BitmapFactory.decodeFile(location);
        }

        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}