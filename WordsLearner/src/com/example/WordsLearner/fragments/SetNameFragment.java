package com.example.WordsLearner.fragments;

import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    private EditText nameEdit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_set_name, container, false);

        nameEdit = (EditText)rootView.findViewById(R.id.edit_name);
        Button saveBtn = (Button)rootView.findViewById(R.id.btn_save);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


                getActivity().finish();
            }
        });
        return rootView;
    }

    private String moveSoundFile() {
        String resultFileName = UUID.randomUUID().toString() + Utils.SOUND_EXTENTION;
        String soundTempFilePath = ((CreateWordActivity)getActivity()).getSoundTempFilePath();
        if(soundTempFilePath == null) {
            return null;
        }

        File tempFile = new File(soundTempFilePath);
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
        String resultFileName = UUID.randomUUID().toString() + Utils.IMAGE_EXTENTION;
        String imageTempFilePath = ((CreateWordActivity)getActivity()).getImageTempFilePath();
        if(imageTempFilePath == null) {
            return null;
        }

        File file = new File(imageTempFilePath);
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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Word word = ((CreateWordActivity)getActivity()).getCurrentWord();
        if(word != null && word.getName() != null) {
            nameEdit.setText(word.getName());
        }
    }
}
