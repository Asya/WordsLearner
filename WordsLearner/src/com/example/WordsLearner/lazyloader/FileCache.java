package com.example.WordsLearner.lazyloader;

import java.io.File;
import android.content.Context;
import com.example.WordsLearner.utils.Utils;

public class FileCache {
    
    private File cacheDir;
    
    public FileCache(Context context){
        //Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED))
            cacheDir=new File(Utils.WORDS_FOLDER);
        else
            cacheDir=context.getCacheDir();
        if(!cacheDir.exists())
            cacheDir.mkdirs();
    }
    
    public File getFile(String filename){
        File f = new File(cacheDir, filename);
        return f;
        
    }
    
    public void clear(){
        File[] files=cacheDir.listFiles();
        if(files==null)
            return;
        for(File f:files)
            f.delete();
    }

}