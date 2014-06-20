package com.example.WordsLearner.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.WordsLearner.R;
import com.example.WordsLearner.utils.Utils;

public class PasscodeActivity extends Activity {

    private final static String LOG_TAG = "PasscodeActivity";

    private TextView title;
    private Button btnNext;
    private EditText editPass1;
    private EditText editPass2;
    private EditText editPass3;
    private EditText editPass4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);

        title = (TextView)findViewById(R.id.text);
        title.setText(R.string.passcode);

        btnNext = (Button)findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.log(LOG_TAG, "Passcode correct");
                startActivity(new Intent(PasscodeActivity.this, WordsListActivity.class));
            }
        });

        editPass1 = (EditText)findViewById(R.id.edit_pass1);
        editPass1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_UP) {
                    if(keyCode != KeyEvent.KEYCODE_DEL) {
                        editPass2.setEnabled(true);
                        editPass2.requestFocus();
                    }
                }
                return false;
            }
        });

        editPass2 = (EditText)findViewById(R.id.edit_pass2);
        editPass2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_UP) {
                    if(keyCode == KeyEvent.KEYCODE_DEL && editPass2.getText().length() == 0) {
                        editPass1.requestFocus();
                        editPass1.setText("");
                        editPass2.setEnabled(false);
                    } else if(editPass2.getText().length() > 0) {
                        editPass3.requestFocus();
                        editPass3.setEnabled(true);
                    }
                }
                return false;
            }
        });

        editPass3 = (EditText)findViewById(R.id.edit_pass3);
        editPass3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_UP) {
                    if(keyCode == KeyEvent.KEYCODE_DEL && editPass3.getText().length() == 0) {
                        editPass2.requestFocus();
                        editPass2.setText("");
                        editPass3.setEnabled(false);
                    } else if(editPass3.getText().length() > 0) {
                        editPass4.requestFocus();
                        editPass4.setEnabled(true);
                    }
                }
                return false;
            }
        });

        editPass4 = (EditText)findViewById(R.id.edit_pass4);
        editPass4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getAction() == KeyEvent.ACTION_UP) {
                    if(keyCode == KeyEvent.KEYCODE_DEL && editPass4.getText().length() == 0) {
                        editPass3.requestFocus();
                        editPass3.setText("");
                        editPass4.setEnabled(false);
                    }
                }
                return false;
            }
        });

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        setTypeface();
    }

    /**************************************************/

    private void setTypeface() {
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/chalk.ttf");
        title.setTypeface(typeFace);
        btnNext.setTypeface(typeFace);
        editPass1.setTypeface(typeFace);
        editPass2.setTypeface(typeFace);
        editPass3.setTypeface(typeFace);
        editPass4.setTypeface(typeFace);
    }
}
