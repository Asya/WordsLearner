package com.example.WordsLearner.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.WordsLearner.R;
import com.example.WordsLearner.utils.PreferencesManager;
import com.example.WordsLearner.utils.Utils;

public class PasscodeActivity extends Activity {

    private final static String LOG_TAG = "PasscodeActivity";

    private final static int PASSCODE_LENGTH = 4;

    private TextView title;
    private Button btnNext;
    private EditText invisible_edit_text;
    private TextView editPass1;
    private TextView editPass2;
    private TextView editPass3;
    private TextView editPass4;
    private TextView pleaseSetPasscode;

    private PreferencesManager prefs;
    private String passcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);

        prefs = new PreferencesManager(this);
        passcode = prefs.getPasscode();

        title = (TextView)findViewById(R.id.text);
        title.setText(R.string.passcode);

        btnNext = (Button)findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String enterdPasscode = invisible_edit_text.getText().toString();

                if(enterdPasscode.length() < PASSCODE_LENGTH) {
                    Toast.makeText(PasscodeActivity.this, R.string.short_passcode, Toast.LENGTH_SHORT).show();
                    Utils.log(LOG_TAG, "Passcode < 4 digit lenght");
                    return;
                }

                hideKeyboard();

                if(passcode == null) {
                    setPasscode(invisible_edit_text.getText().toString());
                }

                if (passCodeCorrect()) {
                    Utils.log(LOG_TAG, "Passcode correct");
                    startActivity(new Intent(PasscodeActivity.this, WordsListActivity.class));
                    finish();
                } else {
                    Toast.makeText(PasscodeActivity.this, R.string.incorrect_passcode, Toast.LENGTH_SHORT).show();
                    Utils.log(LOG_TAG, "Passcode incorrect");
                }
            }
        });

        invisible_edit_text = (EditText)findViewById(R.id.edit_passcode_invivsible);
        invisible_edit_text.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId== EditorInfo.IME_ACTION_DONE)
                {
                    btnNext.performClick();
                }
                return false;
            }
        });
        invisible_edit_text.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                switch (s.length()) {
                    case 0:
                        editPass2.setEnabled(false);
                        editPass2.setText("");
                        editPass1.setText("_");
                        break;
                    case 1:
                        editPass3.setEnabled(false);
                        editPass2.setEnabled(true);
                        editPass3.setText("");
                        editPass2.setText("_");
                        editPass1.setText(s.charAt(0)+"");
                        break;
                    case 2:
                        editPass4.setEnabled(false);
                        editPass3.setEnabled(true);
                        editPass4.setText("");
                        editPass3.setText("_");
                        editPass2.setText(s.charAt(1)+"");
                        break;
                    case 3:
                        editPass4.setEnabled(true);
                        editPass4.setText("_");
                        editPass3.setText(s.charAt(2)+"");
                        btnNext.setEnabled(false);
                        break;
                    case 4:
                        editPass4.setText(s.charAt(3) + "");
                        btnNext.setEnabled(true);
                        break;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        editPass1 = (TextView)findViewById(R.id.edit_pass1);
        editPass2 = (TextView)findViewById(R.id.edit_pass2);
        editPass3 = (TextView)findViewById(R.id.edit_pass3);
        editPass4 = (TextView)findViewById(R.id.edit_pass4);

        pleaseSetPasscode = (TextView)findViewById(R.id.please_set_passcode);
        if(passcode != null) {
            pleaseSetPasscode.setVisibility(View.GONE);
        }

        findViewById(R.id.layout_passcode).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showKeyboard();
            }
        });

        setTypeface();
    }

    @Override
    protected void onResume() {
        super.onResume();
        showKeyboard();
    }

    @Override
    protected void onPause() {
        super.onPause();
        hideKeyboard();
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
        pleaseSetPasscode.setTypeface(typeFace);
    }

    private boolean passCodeCorrect() {
        return invisible_edit_text.getText().toString().equals(passcode);
    }

    private void setPasscode(String passcode) {
        this.passcode = passcode;
        prefs.setPasscode(passcode);
    }

    private void hideKeyboard() {
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(invisible_edit_text.getWindowToken(), 0);
    }

    private void showKeyboard() {
        ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                .toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }
}
