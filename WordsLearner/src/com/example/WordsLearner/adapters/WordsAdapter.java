
package com.example.WordsLearner.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.WordsLearner.R;
import com.example.WordsLearner.activities.ChoosePhoto;
import com.example.WordsLearner.activities.MainActivity;
import com.example.WordsLearner.db.WordsLearnerDataHelper;
import com.example.WordsLearner.model.Word;
import com.fortysevendeg.swipelistview.SwipeListView;

import java.io.File;
import java.util.List;

public class WordsAdapter extends BaseAdapter {

    private List<Word> data;
    private Context context;
    private MainActivity.CloseListMenuListener closeListMenuListener;

    public WordsAdapter(Context context, List<Word> data, MainActivity.CloseListMenuListener closeListMenuListener) {
        this.context = context;
        this.data = data;
        this.closeListMenuListener = closeListMenuListener;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Word getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Word word = getItem(position);
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = li.inflate(R.layout.word_list_row, parent, false);
            holder = new ViewHolder();
            holder.ivImage = (ImageView) convertView.findViewById(R.id.image);
            holder.tvTitle = (TextView) convertView.findViewById(R.id.name);
            holder.btnEdit = (Button) convertView.findViewById(R.id.btn_edit);
            holder.btnDelete = (Button) convertView.findViewById(R.id.btn_delete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ((SwipeListView)parent).recycle(convertView, position);

        holder.ivImage.setImageBitmap(decodeSampledBitmapFromResource(word.getImagePath(), 100, 100));
        if(word.getName() != null) {
            holder.tvTitle.setText(word.getName());
        } else {
            holder.tvTitle.setText(word.getImagePath());
        }

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO:edit word in DB
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteWord(position, word);
            }
        });

        return convertView;
    }

    private void deleteWord(int position, Word word) {
        data.remove(position);
        notifyDataSetChanged();
        closeListMenuListener.closeMenu();

        //TOOD: check if I need to do in separate thread
        WordsLearnerDataHelper db = new WordsLearnerDataHelper(context);
        db.deleteWord(word);

        if(word.getImagePath().startsWith(ChoosePhoto.WORDS_FOLDER)) {
            File file = new File(word.getImagePath());
            if (!file.exists()) {
                file.delete();
            }
        }
    }

    public static Bitmap decodeSampledBitmapFromResource(String imagePath,
                                                         int reqWidth, int reqHeight) {
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(imagePath, options);
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
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    static class ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
        Button btnEdit;
        Button btnDelete;
    }

}
