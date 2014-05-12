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

    private Word word;

    private ImageView imageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_word, container, false);
        imageView = (ImageView)rootView.findViewById(R.id.image);

        if (savedInstanceState != null) {
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

    public void setWord(Word word) {
        this.word = word;
    }

    public Word getWord() {
        return word;
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
            return BitmapFactory.decodeFile(new File(Utils.IMAGES_FOLDER, word.getImagePath()).getAbsolutePath());
        }

        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}