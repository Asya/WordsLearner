package com.example.WordsLearner.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import com.example.WordsLearner.R;
import com.example.WordsLearner.activities.CreateWordActivity;
import com.example.WordsLearner.db.WordsLearnerDataHelper;
import com.example.WordsLearner.model.Word;

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
                ((CreateWordActivity)getActivity()).getCurrentWord().setName(nameEdit.getText().toString());

                switch (((CreateWordActivity)getActivity()).getMode()) {
                    case CreateWordActivity.MODE_CREATE:
                        addWordToDB();
                        break;
                    case CreateWordActivity.MODE_EDIT:
                        updateWordInDB();
                        break;
                }


                getActivity().finish();
            }
        });
        return rootView;
    }

    private void updateWordInDB() {
        WordsLearnerDataHelper db = new WordsLearnerDataHelper(getActivity());
        Word word = ((CreateWordActivity)getActivity()).getCurrentWord();
        db.updateWord(word);
    }

    private void addWordToDB() {
        WordsLearnerDataHelper db = new WordsLearnerDataHelper(getActivity());
        Word word = ((CreateWordActivity)getActivity()).getCurrentWord();
        db.addWord(word);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Word word = ((CreateWordActivity)getActivity()).getCurrentWord();
        if(word.getName() != null) {
            nameEdit.setText(word.getName());
        } else {
            nameEdit.setText(word.getImagePath());
        }
    }
}
