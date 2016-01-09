package antiboring.game.controller.Session;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Gujarat Santana on 26/12/15.
 */
public class SessionStage {
    //Preference name
    private static final String PREF_NAME = "TebakanSessionStage";



    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public SessionStage(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void setKeyStageComplete(int level,String jsonStage){
        editor.putString(String.valueOf(level), jsonStage);
        editor.commit();
    }

    public String getKeyStageComplete(int level){
        return pref.getString(String.valueOf(level), "");
    }
}
