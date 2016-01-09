package antiboring.game.controller.Session;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Gujarat Santana on 12/06/15.
 */
public class SessionManager {
    //Preference name
    private static final String PREF_NAME = "TebakanSessionPlay";

    /**
     * key for preference
     */
    //api token
    private static final String KEY_USER_PLAYED = "KEY_USER_PLAYED";



    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public SessionManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setKeyUserPlayed(boolean isFirsPlay){
        editor.putBoolean(KEY_USER_PLAYED,isFirsPlay);
        editor.commit();
    }

    public boolean isUserPlayed(){
        return pref.getBoolean(KEY_USER_PLAYED,false);
    }


    public void clearAllSession(){
        editor.clear();
        editor.commit();
    }
}

