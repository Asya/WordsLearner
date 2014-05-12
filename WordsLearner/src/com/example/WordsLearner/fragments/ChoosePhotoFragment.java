package com.example.WordsLearner.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import com.example.WordsLearner.R;
import com.example.WordsLearner.activities.CreateWordActivity;
import com.example.WordsLearner.adapters.CreateWordPagerAdapter;
import com.example.WordsLearner.model.Word;
import com.example.WordsLearner.utils.Utils;

import java.io.*;

public class ChoosePhotoFragment extends Fragment {

    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int PICK_IMAGE = 2;

    private Button btnNext;
    private ImageView imagePreview;
    private ProgressBar progressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_choose_photo, container, false);

        imagePreview = (ImageView) rootView.findViewById(R.id.img_preview);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress);

        Button btnSelect = (Button) rootView.findViewById(R.id.btn_select);
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, getString(R.string.select_photo)), PICK_IMAGE);
            }
        });

        Button btnCamera = (Button) rootView.findViewById(R.id.btn_camera);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dispatchTakePictureIntent();
            }
        });

        btnNext = (Button) rootView.findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((CreateWordActivity)getActivity()).goToNextStep(CreateWordPagerAdapter.FRAGMENT_SOUND);
            }
        });

        Word word = ((CreateWordActivity)getActivity()).getCurrentWord();
        if(word != null && word.getImagePath() != null) {
            new LoadPreviewAsync(new File(Utils.IMAGES_FOLDER, word.getImagePath())).execute();
        }

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ChoosePhotoFragment.PICK_IMAGE && data != null && data.getData() != null) {
            saveExistingImagePath(data);
            File imageFile = new File(((CreateWordActivity)getActivity()).getImageTempFilePath());
            new LoadPreviewAsync(imageFile).execute();
        } else if (requestCode == ChoosePhotoFragment.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            File imageFile = new File(((CreateWordActivity)getActivity()).getImageTempFilePath());
            new LoadPreviewAsync(imageFile).execute();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    /**************************************************/

    private void saveExistingImagePath(Intent data) {
        Uri _uri = data.getData();

        //User had pick an image.
        Cursor cursor = getActivity().getContentResolver().query(_uri, new String[]{android.provider.MediaStore.Images.ImageColumns.DATA}, null, null, null);
        cursor.moveToFirst();

        //Link to the image
        final String imageFilePath = cursor.getString(0);
        cursor.close();

        if (imageFilePath == null || "".equals(imageFilePath)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage(R.string.no_photo_permissions)
                    .setTitle(R.string.error);
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            ((CreateWordActivity)getActivity()).setImageTempFilePath(imageFilePath);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File tempPhotoFile = null;
            try {
                tempPhotoFile = Utils.getCameraTempFile();
                ((CreateWordActivity)getActivity()).setImageTempFilePath(tempPhotoFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
            if (tempPhotoFile != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(tempPhotoFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    /**************************************************/

    class LoadPreviewAsync extends AsyncTask<Void, Void, Bitmap> {

        private File imageFile;

        private LoadPreviewAsync(File imageFile) {
            this.imageFile = imageFile;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        protected Bitmap doInBackground(Void... args) {
            return Utils.decodeSampledBitmapFromFile(imageFile, 150);
        }

        protected void onPostExecute(Bitmap result) {
            imagePreview.setImageBitmap(result);
            progressBar.setVisibility(View.GONE);

            //when image is chosen enable next button
            btnNext.setEnabled(true);
        }
    }

}
