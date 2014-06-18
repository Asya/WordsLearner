package com.example.WordsLearner.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.*;

public class Utils {

    // TODO: this package does a lot of things, so I'll refactor it soon

    private static final boolean DEBUG_MODE = true;
    private static final String LOG_TAG = "Utils";

    public static final String WORDS_LEARNER_FOLDER = Environment.getExternalStorageDirectory() + File.separator + "WordsLearner";
    public static final String IMAGES_FOLDER = WORDS_LEARNER_FOLDER + File.separator + "Images";
    public static final String SOUNDS_FOLDER = WORDS_LEARNER_FOLDER + File.separator + "Sounds";

    public static final String IMAGE_EXTENTION= ".jpg";
    public static final String SOUND_EXTENTION= ".mp3";

    public static final String IMAGES_TEMP_FILE_NAME = "TempCameraImage";
    public static final String SOUND_TEMP_FILE_NAME = "TempSound";


    public static Bitmap decodeSampledBitmapFromFile(File file, int targetLongestEdge) {
        log(LOG_TAG, "Decode bitmap from file = " + file.getAbsolutePath() + " with target longest edge = " + targetLongestEdge);

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

        Utils.log(LOG_TAG, "inSampleSize for source %d, target %d is %d", imageLongestEdge, targetLongestEdge, inSampleSize);
        return inSampleSize;
    }

    public static void checkDirectory(String folder) {
        File dir = new File (folder);
        if (!dir.exists()) {
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

        ScaleInformation result = widthIsLongest ?
            new ScaleInformation(bitmapSampleSize, targetLongestEdge, (int)(imageHeight / scaleRatio)) :
            new ScaleInformation(bitmapSampleSize, (int)(imageWidth / scaleRatio), targetLongestEdge);

        Utils.log(LOG_TAG, "image %d x %d, desired %d x %d, result: %s", imageWidth, imageHeight, desiredWidth, desiredHeight, result);
        return result;
    }

    public static void copyAndResizeToScreen(String sourceImage, String outputImage, int screenWidth, int screenHeight) throws IOException {
        log(LOG_TAG, "Copy and resize to screen size source file = %s, output file = %s", sourceImage, outputImage);

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

    public static void copyFile(String inputFilePath, String outputFolder, String outputFileName) throws IOException{
        log(LOG_TAG, "Copy file. Input file = %s, output folder = %s, output file = %s",
                inputFilePath, outputFolder, outputFileName);

        InputStream in;
        OutputStream out;

        checkDirectory(outputFolder);

        in = new FileInputStream(inputFilePath);
        out = new FileOutputStream(new File(outputFolder, outputFileName).getAbsolutePath());

        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        in.close();

        out.flush();
        out.close();
    }

    public static File getCameraTempFile() throws IOException {
        checkDirectory(IMAGES_FOLDER);
        File imageFile = File.createTempFile(IMAGES_TEMP_FILE_NAME, IMAGE_EXTENTION, new File(IMAGES_FOLDER));
        return imageFile;
    }

    public static File getSoundTempFile() throws IOException {
        checkDirectory(SOUNDS_FOLDER);
        File soundFile;
        soundFile = File.createTempFile(SOUND_TEMP_FILE_NAME, SOUND_EXTENTION, new File(SOUNDS_FOLDER));
        return soundFile;
    }

    public static void deleteFile(String path) {
        log(LOG_TAG, "Delete file = " + path);

        File file = new File(path);
        if (!file.exists()) {
            file.delete();
        }
    }

    public static void log(String logTag, String log) {
        if(DEBUG_MODE) {
            Log.d(logTag, log);
        }
    }

    public static void log(String logTag, String format, Object... args) {
        if(DEBUG_MODE) {
            Log.d(logTag, String.format(format, args));
        }
    }

    /**************************************************/

    public static class ScaleInformation {
        public final int bitmapSampleSize;
        public final int width;
        public final int height;

        public ScaleInformation(int bitmapSampleSize, int width, int height) {
            this.bitmapSampleSize = bitmapSampleSize;
            this.width = width;
            this.height = height;
        }

        @Override
        public String toString() {
            return String.format("Sample size: %d, size %d x %d", bitmapSampleSize, width, height);
        }
    }
}
