package santana.tebaktebakan.session;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by AdrianEkaFikri on 11/2/2015.
 */
public class SessionLevel {
    private static final String PREF_NAME = "LevelPref";
    private static final String KEY_LVL = "KEY_LVL";

    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public SessionLevel(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public int getLevel(){
        return pref.getInt(KEY_LVL,0);
    }

    public void setLevel(int lvl){
        editor.putInt(KEY_LVL, lvl);
        editor.commit();
    }
}
