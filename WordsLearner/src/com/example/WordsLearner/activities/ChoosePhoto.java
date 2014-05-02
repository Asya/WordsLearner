package com.example.WordsLearner.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.example.WordsLearner.R;
import com.example.WordsLearner.db.WordsLearnerDataHelper;
import com.example.WordsLearner.model.Word;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ChoosePhoto extends Activity {

    private static final int PICK_IMAGE = 2;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    public static final String WORDS_FOLDER = Environment.getExternalStorageDirectory() + File.separator + "WordsLearner";

    private static String mCurrentPhotoPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);

            Button btnSelect = (Button) rootView.findViewById(R.id.btn_select);
            btnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    getActivity().startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                }
            });

            Button btnCamera = (Button) rootView.findViewById(R.id.btn_camera);
            btnCamera.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dispatchTakePictureIntent();
                }
            });
            return rootView;
        }

        private void dispatchTakePictureIntent() {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            // Ensure that there's a camera activity to handle the intent
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                // Create the File where the photo should go
                File photoFile = null;
                try {
                    photoFile = createImageFile();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                // Continue only if the File was successfully created
                if (photoFile != null) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                    getActivity().startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }

        private File createImageFile() throws IOException {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = new File(Environment.getExternalStorageDirectory() + File.separator + "WordsLearner");
            if (!storageDir.exists()) {
                storageDir.mkdirs();
                storageDir.createNewFile();
            }

            File image = File.createTempFile(
                    imageFileName,  /* prefix */
                    ".jpg",         /* suffix */
                    storageDir      /* directory */
            );
            mCurrentPhotoPath = image.getAbsolutePath();
            return image;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_IMAGE && data != null && data.getData() != null) {
            Uri _uri = data.getData();

            //User had pick an image.
            Cursor cursor = getContentResolver().query(_uri, new String[]{android.provider.MediaStore.Images.ImageColumns.DATA}, null, null, null);
            cursor.moveToFirst();

            //Link to the image
            final String imageFilePath = cursor.getString(0);
            cursor.close();

            if (imageFilePath == null || "".equals(imageFilePath)) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("This image does not exists or you have no permessions to use it")
                        .setTitle("Error");
                AlertDialog dialog = builder.create();
                dialog.show();
            } else {
                saveToDB(imageFilePath);
            }
        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            saveMediaEntry(mCurrentPhotoPath, getContentResolver());
                saveToDB(mCurrentPhotoPath);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private Uri saveMediaEntry(String imagePath, ContentResolver contentResolver) {
        ContentValues v = new ContentValues();
        v.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        File f = new File(imagePath);
        v.put(MediaStore.Images.Media.SIZE, f.length());
        v.put("_data", imagePath);
        return contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, v);
    }

    private void saveToDB(String imagePath) {
        WordsLearnerDataHelper db = new WordsLearnerDataHelper(this);
        db.addWord(new Word(imagePath, null, null));
    }
}
