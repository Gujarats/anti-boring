package antiboring.game.controller.Session;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Gujarat Santana on 21/12/15.
 */
public class SessionStars {
    private static final String PREF_NAME = "TebakanStars";

    /**
     * key for preference
     */
    private static final String KEY_STARS = "KEY_STARS";
    private static final String KEY_LEVEL = "KEY_LEVEL";
    private static final String KEY_LEVEL_JSON = "KEY_LEVEL_JSON";

    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public SessionStars(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public String getKeyLevelJson(){
        return pref.getString(KEY_LEVEL_JSON, "");
    }

    public void setKeyLevelJson(String keyLevelJson){
        editor.putString(KEY_LEVEL_JSON, keyLevelJson);
        editor.commit();
    }


    public int getKeyStars(){
        return pref.getInt(KEY_STARS,0);
    }

    public void setKeyStars(int stars){
        editor.putInt(KEY_STARS,stars);
        editor.commit();
    }

    public int getKeyLevel(){
        return pref.getInt(KEY_LEVEL,1);
    }

    public void setKeyLevel(int level){
        editor.putInt(KEY_LEVEL,level);
        editor.commit();
    }




}
