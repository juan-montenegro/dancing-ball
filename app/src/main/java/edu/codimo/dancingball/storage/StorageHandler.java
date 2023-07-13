package edu.codimo.dancingball.storage;

import android.content.Context;
import android.content.SharedPreferences;

import edu.codimo.dancingball.R;

public class StorageHandler {
    private final SharedPreferences sharedPref;

    public StorageHandler(Context context, String key) {
        this.sharedPref = context.getSharedPreferences(key,Context.MODE_PRIVATE);
    }

    public void writePref(String key, Object data){
        SharedPreferences.Editor editor = sharedPref.edit();
        if(data instanceof Boolean){
            editor.putBoolean(key, (boolean) data);
        } else if (data instanceof String) {
            editor.putString(key, (String) data);
        } else if (data instanceof Integer){
            editor.putInt(key, (int) data);
        } else {
            return;
        }
        editor.apply();
    }

    public int getInt(String key, int defValue){
        return sharedPref.getInt(key,defValue);
    }
    public String getString(String key){
        return sharedPref.getString(key,"");
    }
    public boolean getBool(String key){
        return sharedPref.getBoolean(key,false);
    }
}
