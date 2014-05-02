package com.example.WordsLearner.db;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import com.example.WordsLearner.model.Word;

import java.util.LinkedList;
import java.util.List;

public class WordsLearnerDataHelper extends SQLiteOpenHelper {

    private static final String LOG_TAG = "WordsLearnerDB: ";

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "WordsLearnerDB";

    private static final String TABLE_WORDS = "words";
    private static final String KEY_ID = "id";
    private static final String KEY_IMAGE_PATH = "imagePath";
    private static final String KEY_SOUND_PATH = "soundPath";
    private static final String KEY_NAME = "name";

    private static final String[] WORDS_COLUMNS = {KEY_ID, KEY_IMAGE_PATH, KEY_SOUND_PATH, KEY_NAME};

    private String CREATE_WORDS_TABLE = "CREATE TABLE " + TABLE_WORDS + " ( " +
            KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            KEY_IMAGE_PATH + " TEXT, "+
            KEY_SOUND_PATH + " TEXT, "+
            KEY_NAME + " TEXT )";

    public WordsLearnerDataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WORDS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void addWord(Word word){
        Log.d(LOG_TAG + "addWord", word.toString());

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IMAGE_PATH, word.getImagePath());
        values.put(KEY_SOUND_PATH, word.getSoundPath());
        values.put(KEY_NAME, word.getName());

        db.insert(TABLE_WORDS, null, values);

        db.close();
    }

    public List<Word> getAllWords() {
        List<Word> words = new LinkedList<Word>();
        String query = "SELECT  * FROM " + TABLE_WORDS;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                int id = Integer.parseInt(cursor.getString(0));
                String imagePath = cursor.getString(1);
                String soundPath = cursor.getString(2);
                String name = cursor.getString(3);

                Word word = new Word(id, imagePath, soundPath, name);
                words.add(word);
            } while (cursor.moveToNext());
        }

        Log.d(LOG_TAG + "getAllWords", words.toString());

        return words;
    }

    public int updateWord(Word word) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_IMAGE_PATH, word.getImagePath());
        values.put(KEY_SOUND_PATH, word.getSoundPath());
        values.put(KEY_NAME, word.getName());

        int i = db.update(TABLE_WORDS, values, KEY_ID+" = ?", new String[] { String.valueOf(word.getId()) });
        db.close();

        return i;
    }

    public void deleteWord(Word word) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WORDS, KEY_ID+" = ?", new String[] { String.valueOf(word.getId()) });
        db.close();

        Log.d(LOG_TAG + "deleteWord", word.toString());
    }
}