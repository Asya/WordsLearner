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

        new LoadPreviewAsynk().execute();

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ChoosePhotoFragment.PICK_IMAGE && data != null && data.getData() != null) {
            getExistingImage(data);
            new LoadPreviewAsynk().execute();
        } else if (requestCode == ChoosePhotoFragment.REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            new LoadPreviewAsynk().execute();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }


    private void getExistingImage(Intent data) {
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
            File file = new File(imageFilePath);
            try {
                Utils.copyFile(file, Utils.IMAGES_FOLDER);
            }
            catch (FileNotFoundException e) {
                // TODO: better error handling
                e.printStackTrace();
            }
            catch (IOException e) {
                // TODO: better error handling
                e.printStackTrace();
            }
            ((CreateWordActivity)getActivity()).setCurrentPhotoName(file.getName());
        }
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
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private File createImageFile() throws IOException {
        File storageDir = new File(Utils.IMAGES_FOLDER);
        if (!storageDir.exists()) {
            storageDir.mkdirs();
            storageDir.createNewFile();
        }

        File image = File.createTempFile(
                "IMG_",  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        if(getActivity() != null) {
            ((CreateWordActivity)getActivity()).setCurrentPhotoName(image.getName());
        }
        return image;
    }

    class LoadPreviewAsynk extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        protected Bitmap doInBackground(Void... args) {
            Word word = ((CreateWordActivity)getActivity()).getCurrentWord();
            if(word != null && word.getImagePath() != null) {
                File imageFile = new File(Utils.IMAGES_FOLDER, word.getImagePath());
                return Utils.decodeSampledBitmapFromFile(imageFile, 150, 150);
            }
            return null;
        }

        protected void onPostExecute(Bitmap result) {
            imagePreview.setImageBitmap(result);
            progressBar.setVisibility(View.GONE);

            //when image chose enable next button
            Word word = ((CreateWordActivity)getActivity()).getCurrentWord();
            if(word != null && word.getImagePath() != null) {
                btnNext.setEnabled(true);
            }
        }
    }

}
