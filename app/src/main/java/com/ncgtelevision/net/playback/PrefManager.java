package com.ncgtelevision.net.playback;

import android.content.Context;
import android.content.SharedPreferences;

public class PrefManager {

    Context context;

     PrefManager(Context context)
    {
        this.context=context;
    }

    public void saveVideoId(String videoId){
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("videoId",videoId);
        editor.apply();
    }

    public String getVideoId(){
         SharedPreferences sharedPreferences = context.getSharedPreferences("data",Context.MODE_PRIVATE);
         return  sharedPreferences.getString("videoId","");
    }

}