package com.example.WordsLearner.fragments;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import com.example.WordsLearner.R;
import com.example.WordsLearner.activities.CreateWordActivity;
import com.example.WordsLearner.db.WordsLearnerDataHelper;
import com.example.WordsLearner.model.Word;
import com.example.WordsLearner.utils.Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

public class SetNameFragment extends Fragment {

    // TODO: this fragment is responsible for setting name, moving image/audio files and creating/updating Words in db. I am going to refactor that soon.

    private final static String LOG_TAG = "SetNameFragment";

    private EditText nameEdit;
    private Button saveBtn;
    private ProgressDialog progressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        final View rootView = inflater.inflate(R.layout.fragment_set_name, container, false);

        nameEdit = (EditText)rootView.findViewById(R.id.edit_name);
        saveBtn = (Button)rootView.findViewById(R.id.btn_save);
        nameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                saveBtn.setEnabled(!"".equals(s.toString()));
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SaveTask().execute();
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Word word = ((CreateWordActivity)getActivity()).getCurrentWord();
        if(word != null && word.getName() != null) {
            nameEdit.setText(word.getName());
            saveBtn.setEnabled(!"".equals(word.getName()));
        }
    }

    /**************************************************/

    private String moveSoundFile() {
        String soundTempFilePath = ((CreateWordActivity)getActivity()).getSoundTempFilePath();
        if(soundTempFilePath == null) {
            return null;
        }

        String resultFileName = UUID.randomUUID().toString() + Utils.SOUND_EXTENTION;
        File tempFile = new File(soundTempFilePath);

        Utils.log(LOG_TAG, "Move sound file from = " + soundTempFilePath + " to = " + resultFileName);
        try {
            Utils.copyFile(tempFile.getAbsolutePath(), Utils.SOUNDS_FOLDER, resultFileName);
            tempFile.delete();
            return resultFileName;
        } catch (FileNotFoundException e) {
            // TODO: better error handling
            e.printStackTrace();
        } catch (IOException e) {
            // TODO: better error handling
            e.printStackTrace();
        }

        return null;
    }

    private String moveImageFile() {
        String imageTempFilePath = ((CreateWordActivity)getActivity()).getImageTempFilePath();
        if(imageTempFilePath == null) {
            return null;
        }

        String resultFileName = UUID.randomUUID().toString() + Utils.IMAGE_EXTENTION;
        File file = new File(imageTempFilePath);

        Utils.log(LOG_TAG, "Move image file from = " + imageTempFilePath + " to = " + resultFileName);
        try {
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            Utils.copyAndResizeToScreen(file.getAbsolutePath(),
                    new File(Utils.IMAGES_FOLDER, resultFileName).getAbsolutePath(),
                    size.x, size.y);

            if(imageTempFilePath.contains(Utils.IMAGES_FOLDER)) {
                new File(imageTempFilePath).delete();
            }
            return resultFileName;
        } catch (FileNotFoundException e) {
            // TODO: better error handling
            e.printStackTrace();
        }
        catch (IOException e) {
            // TODO: better error handling
            e.printStackTrace();
        }

        return null;
    }

    private void updateWordInDB(String imageFileName, String soundFileName, String name) {
        WordsLearnerDataHelper db = new WordsLearnerDataHelper(getActivity());
        Word word = ((CreateWordActivity)getActivity()).getCurrentWord();
        if(imageFileName != null) {
            word.setImagePath(imageFileName);
        }
        if(soundFileName != null) {
            word.setSoundPath(soundFileName);
        }
        word.setName(name);
        db.updateWord(word);
    }

    private void addWordToDB(String imageFileName, String soundFileName, String name) {
        WordsLearnerDataHelper db = new WordsLearnerDataHelper(getActivity());
        Word word = new Word(imageFileName, soundFileName, name);
        db.addWord(word);
    }

    /**************************************************/

    private class SaveTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(getString(R.string.loading));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... args) {
            String imageFileName = moveImageFile();
            String soundFileName = moveSoundFile();

            switch (((CreateWordActivity)getActivity()).getMode()) {
                case CreateWordActivity.MODE_CREATE:
                    addWordToDB(imageFileName, soundFileName, nameEdit.getText().toString());
                    break;
                case CreateWordActivity.MODE_EDIT:
                    updateWordInDB(imageFileName, soundFileName, nameEdit.getText().toString());
                    break;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (progressDialog != null) {
                progressDialog.dismiss();
                progressDialog = null;
            }
            getActivity().finish();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (progressDialog != null) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }
}
