package com.example.WordsLearner.fragments;

import android.app.Fragment;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.example.WordsLearner.R;
import com.example.WordsLearner.activities.CreateWordActivity;
import com.example.WordsLearner.adapters.CreateWordPagerAdapter;
import com.example.WordsLearner.model.Word;
import com.example.WordsLearner.utils.Utils;

import java.io.IOException;

public class RecordSoundFragment extends Fragment {

    private final static String LOG_TAG = "RecordSoundFragment";

    private MediaRecorder mRecorder = null;
    private MediaPlayer mPlayer = null;

    private Button nextBtn;
    private Button listenBtn;
    private Chronometer chronometer;
    private ProgressBar progressBar;
    private ImageView recordIsOn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_record_sound, container, false);

        Button recordBtn = (Button)rootView.findViewById(R.id.btn_record);
        listenBtn = (Button)rootView.findViewById(R.id.btn_listen);
        nextBtn = (Button)rootView.findViewById(R.id.btn_next);
        chronometer = (Chronometer)rootView.findViewById(R.id.chronometer);
        progressBar = (ProgressBar)rootView.findViewById(R.id.progress);
        recordIsOn = (ImageView)rootView.findViewById(R.id.img_record_on);

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
            setNextAndListenButtonsEnabled();
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

        chronometer.stop();
    }

    /**************************************************/

    private void startRecording() {
        Utils.checkDirectory(Utils.SOUNDS_FOLDER);
        String soundName = null;
        try {
            soundName = Utils.getSoundTempFile().getAbsolutePath();
            ((CreateWordActivity)getActivity()).setSoundTempFilePath(soundName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        progressBar.setVisibility(View.VISIBLE);

        Utils.log(LOG_TAG, "Creating recorder with file " + soundName);
        mRecorder = new MediaRecorder();
        mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mRecorder.setOutputFile(soundName);
        mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            mRecorder.prepare();
        } catch (IOException e) {
            Log.e(LOG_TAG, "start recording failed");
            e.printStackTrace();
        }

        mRecorder.start();
        Utils.log(LOG_TAG, "Start recording into file = " + soundName);

        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

        progressBar.setVisibility(View.INVISIBLE);
        recordIsOn.setVisibility(View.VISIBLE);
    }

    private void stopRecording() {
        Utils.log(LOG_TAG, "Stop recording");
        mRecorder.stop();
        mRecorder.release();
        mRecorder = null;
        setNextAndListenButtonsEnabled();
        chronometer.stop();
        recordIsOn.setVisibility(View.INVISIBLE);
    }

    private void setNextAndListenButtonsEnabled() {
            nextBtn.setEnabled(true);
            listenBtn.setEnabled(true);
    }

    private void startPlaying() {
        String soundName = ((CreateWordActivity)getActivity()).getSoundTempFilePath();
        Utils.log(LOG_TAG, "Start playing file = " + soundName);

        mPlayer = new MediaPlayer();
        try {
            mPlayer.setDataSource(soundName);
            mPlayer.prepare();
            mPlayer.start();
        } catch (IOException e) {
            Log.e(LOG_TAG, "playback failed");
        }
    }
}
