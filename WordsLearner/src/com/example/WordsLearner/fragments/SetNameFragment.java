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
import com.example.WordsLearner.adapters.CreateWordPagerAdapter;
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
                String imageName = ((CreateWordActivity)getActivity()).getCurrentPhotoName();
                String soundName = ((CreateWordActivity)getActivity()).getCurrentSoundName();

                WordsLearnerDataHelper db = new WordsLearnerDataHelper(getActivity());
                 db.addWord(new Word(imageName, soundName, nameEdit.getText().toString()));
                ((CreateWordActivity)getActivity()).goToNextStep(CreateWordPagerAdapter.FRAGMENT_NAME);

                getActivity().finish();
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameEdit.setText(((CreateWordActivity)getActivity()).getCurrentPhotoName());
    }
}
