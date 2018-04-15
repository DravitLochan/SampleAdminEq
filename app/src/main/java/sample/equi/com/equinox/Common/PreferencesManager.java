package sample.equi.com.equinox.Common;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by DravitLochan on 15-04-2018.
 */

public class PreferencesManager {

    private SharedPreferences sharedPreferences;
    private static String FIRST_TIME = "first time";
    private SharedPreferences.Editor editor;
    private Context context;

    public PreferencesManager(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences("Equinox", 0);
        this.editor = sharedPreferences.edit();
    }

    public boolean getIsFirstTime(){
        return sharedPreferences.getBoolean(FIRST_TIME, true);
    }

    public void setFirstTime(Boolean value){
        editor.putBoolean(FIRST_TIME, value);
        editor.apply();
    }
}
