package com.example.WordsLearner.fragments;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.WordsLearner.R;
import com.example.WordsLearner.activities.CreateWordActivity;
import com.example.WordsLearner.adapters.CreateWordPagerAdapter;
import com.example.WordsLearner.model.Word;
import com.example.WordsLearner.utils.Utils;

import java.io.IOException;

public class RecordSoundFragment extends Fragment {

    private final static String LOG_TAG = "RecordSound";

    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;

    private Button nextBtn;
    private Button listenBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_record_sound, container, false);

        Button recordBtn = (Button)rootView.findViewById(R.id.btn_record);
        listenBtn = (Button)rootView.findViewById(R.id.btn_listen);
        nextBtn = (Button)rootView.findViewById(R.id.btn_next);

        recordBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    startRecording();
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    stopRecording();
                }
                return true;
            }
        });

        listenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlaying();
            }
        });

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((CreateWordActivity) getActivity()).goToNextStep(CreateWordPagerAdapter.FRAGMENT_NAME);
            }
        });

        Word word = ((CreateWordActivity)getActivity()).getCurrentWord();
        if(word != null && word.getSoundPath() != null) {
            setNextListenButtonsEnabled();
        }
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mRecorder != null) {
            mRecorder.release();
            mRecorder = null;
        }

        if (mPlayer != null) {
            mPlayer.release();
            mPlayer = null;
        }
    }

    /**************************************************/

    private void startRecording() {
        Utils.checkDirectory(Utils.SOUNDS_FOLDER);
        String soundName = null;
        try {
            soundName = Utils.getSoundTempFile().getAbsolutePath();
            ((CreateWordActivity)getActivity()).setSoundTempFilePath(soundName);
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(soundName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
            e.printStackTrace();
        }

        mRecorder.start();
    }

    private void stopRecording() {
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        setNextListenButtonsEnabled();
    }

    private void setNextListenButtonsEnabled() {
            nextBtn.setEnabled(true);
            listenBtn.setEnabled(true);
    }

    private void startPlaying() {
        String soundName = ((CreateWordActivity)getActivity()).getSoundTempFilePath();

        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(soundName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "prepare() failed");
        }
    }
}
