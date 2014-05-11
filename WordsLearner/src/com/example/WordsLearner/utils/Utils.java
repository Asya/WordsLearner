package com.example.WordsLearner.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.*;

public class Utils {

    public static final String WORDS_LEARNER_FOLDER = Environment.getExternalStorageDirectory() + File.separator + "WordsLearner";
    public static final String IMAGES_FOLDER = WORDS_LEARNER_FOLDER + File.separator + "Images";
    public static final String SOUNDS_FOLDER = WORDS_LEARNER_FOLDER + File.separator + "Sounds";

    public static Bitmap decodeSampledBitmapFromFile(File file, int targetLongestEdge) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);
        int imageLongestEdge = Math.max(options.outWidth, options.outHeight);

        options.inSampleSize = calculateInSampleSize(imageLongestEdge, targetLongestEdge);

        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    public static int calculateInSampleSize(int imageLongestEdge, int targetLongestEdge) {
        int inSampleSize = 1;
        if (imageLongestEdge > targetLongestEdge) {

            final int halfImageLongestEdge = imageLongestEdge / 2;

            while ((halfImageLongestEdge / inSampleSize) >= targetLongestEdge) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static void checkDirectory(String folder) {
        File dir = new File (folder);
        if (!dir.exists())
        {
            dir.mkdirs();
        }
    }

    public static ScaleInformation getInSampleSizeAndOutputDimensions(int imageWidth, int imageHeight, int desiredWidth, int desiredHeight) {
        // Calculate inSampleSize
        int imageLongestEdge = imageWidth >= imageHeight ? imageWidth : imageHeight;
        boolean widthIsLongest = imageWidth >= imageHeight;
        int targetLongestEdge = desiredWidth >= desiredHeight ? desiredWidth : desiredHeight;

        int bitmapSampleSize = calculateInSampleSize(imageLongestEdge, targetLongestEdge);
        float scaleRatio = (float)imageLongestEdge / (float)targetLongestEdge;

        return widthIsLongest ?
            new ScaleInformation(bitmapSampleSize, targetLongestEdge, (int)(imageHeight / scaleRatio)) :
            new ScaleInformation(bitmapSampleSize, (int)(imageWidth / scaleRatio), targetLongestEdge);
    }

    public static void copyAndResizeToScreen(String sourceImage, String outputImage, int screenWidth, int screenHeight) throws IOException {
        // First decode with inJustDecodeBounds=true to check dimensions

        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(sourceImage, options);

        ScaleInformation scaleInfo = getInSampleSizeAndOutputDimensions(options.outWidth, options.outHeight, screenWidth, screenHeight);


        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        options.inSampleSize = scaleInfo.bitmapSampleSize;
        Bitmap bmp = BitmapFactory.decodeFile(sourceImage, options);

        // scale bitmap
        bmp = Bitmap.createScaledBitmap(bmp, scaleInfo.width, scaleInfo.height, true);

        // store into output file
        File outputFile = new File(outputImage);
        FileOutputStream out = new FileOutputStream(outputFile.getPath());


        bmp.compress(Bitmap.CompressFormat.JPEG, 80, out);
    }

    public static class ScaleInformation {
        public final int bitmapSampleSize;
        public final int width;
        public final int height;

        public ScaleInformation(int bitmapSampleSize, int width, int height) {
            this.bitmapSampleSize = bitmapSampleSize;
            this.width = width;
            this.height = height;
        }
    }
}
