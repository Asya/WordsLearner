
package com.example.WordsLearner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.example.WordsLearner.R;
import com.example.WordsLearner.activities.WordsListActivity;
import com.example.WordsLearner.db.WordsLearnerDataHelper;
import com.example.WordsLearner.lazyloader.ImageLoader;
import com.example.WordsLearner.model.Word;
import com.fortysevendeg.swipelistview.SwipeListView;

import java.io.File;
import java.util.List;

public class WordsListAdapter extends BaseAdapter {

    private List<Word> data;
    private Context context;
    private WordsListActivity.CloseListMenuListener closeListMenuListener;
    public ImageLoader imageLoader;


    public WordsListAdapter(Context context, List<Word> data, WordsListActivity.CloseListMenuListener closeListMenuListener) {
        this.context = context;
        this.data = data;
        this.closeListMenuListener = closeListMenuListener;
        imageLoader=new ImageLoader(context);
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
            convertView = li.inflate(R.layout.row_word_list, parent, false);
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

        imageLoader.DisplayImage(word.getImagePath(), holder.ivImage);
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

        WordsLearnerDataHelper db = new WordsLearnerDataHelper(context);
        db.deleteWord(word);

        deleteFile(word.getImagePath());
        deleteFile(word.getSoundPath());

    }

    private void deleteFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            file.delete();
        }
    }


    static class ViewHolder {
        ImageView ivImage;
        TextView tvTitle;
        Button btnEdit;
        Button btnDelete;
    }

}
