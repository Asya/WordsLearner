package com.example.WordsLearner.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.*;

public class Utils {

    public static final String WORDS_LEARNER_FOLDER = Environment.getExternalStorageDirectory() + File.separator + "WordsLearner";
    public static final String IMAGES_FOLDER = WORDS_LEARNER_FOLDER + File.separator + "Images";
    public static final String SOUNDS_FOLDER = WORDS_LEARNER_FOLDER + File.separator + "Sounds";

    public static Bitmap decodeSampledBitmapFromFile(File file, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(file.getAbsolutePath(), options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(file.getAbsolutePath(), options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    || (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize++;
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

    public static void copyFile(File file, String outputPath) throws IOException{
        InputStream in;
        OutputStream out;

        String inputFile = file.getName();

        Utils.checkDirectory(Utils.IMAGES_FOLDER);

        in = new FileInputStream(file.getPath());
        out = new FileOutputStream(new File(outputPath, inputFile).getPath());

        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
        in.close();

        // write the output file
        out.flush();
        out.close();
    }
}
