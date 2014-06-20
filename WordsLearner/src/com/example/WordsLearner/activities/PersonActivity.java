package com.example.WordsLearner.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.WordsLearner.R;
import com.example.WordsLearner.model.Word;
import com.example.WordsLearner.utils.Utils;

public class PersonActivity extends Activity {

    private final static String LOG_TAG = "PersonActivity";

    private TextView title;
    private Button btnChild;
    private Button btnParent;
    private LinearLayout layoutChild;
    private LinearLayout layoutParent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person);

        title = (TextView)findViewById(R.id.text);
        title.setText(R.string.who_are_you);

        btnChild = (Button)findViewById(R.id.btn_child);
        layoutChild = (LinearLayout)findViewById(R.id.layout_child);
        layoutChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.log(LOG_TAG, "Selected child mode");
                startActivity(new Intent(PersonActivity.this, LearningActivity.class));
            }
        });

        btnParent = (Button)findViewById(R.id.btn_parent);
        layoutParent = (LinearLayout)findViewById(R.id.layout_parent);
        layoutParent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.log(LOG_TAG, "Selected parent mode");
                startActivity(new Intent(PersonActivity.this, WordsListActivity.class));
            }
        });

        setTypeface();
    }

    /**************************************************/

    private void setTypeface() {
        Typeface typeFace = Typeface.createFromAsset(getAssets(), "fonts/chalk.ttf");
        title.setTypeface(typeFace);
        btnChild.setTypeface(typeFace);
        btnParent.setTypeface(typeFace);
    }
}
