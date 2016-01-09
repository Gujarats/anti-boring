package antiboring.game.controller.Session;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Gujarat Santana on 25/12/15.
 */
public class SessionHintDisplay {
    /**
     * key
     */
    private static final String KEY_DISPLAY_HINT = "KEY_DISPLAY_HINT";


    //Preference name
    private static final String PREF_NAME = "TebakanHintDisplay";

    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public SessionHintDisplay(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setKeyDisplayHint(int indexAnswer){
        editor.putInt(KEY_DISPLAY_HINT, indexAnswer);
        editor.commit();
    }

    public int getsetKeyDisplayHint(){
        return pref.getInt(KEY_DISPLAY_HINT,0);
    }
}


