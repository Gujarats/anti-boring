package antiboring.game.controller.appBilling;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Gujarat Santana on 09/01/16.
 */
public class SessionPremiumUser {

    //Preference name
    private static final String PREF_NAME = "TebakanSessionPremium";

    /**
     * key for preference
     */
    //api token
    private static final String KEY_PREMIUM_USER = "KEY_PREMIUM_USER";



    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public SessionPremiumUser(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public boolean isPremiumUser(){
        return pref.getBoolean(KEY_PREMIUM_USER,false);
    }

    public void setPremiumUser(boolean isFirsPlay){
        editor.putBoolean(KEY_PREMIUM_USER,isFirsPlay);
        editor.commit();
    }

    public void clearAllSession(){
        editor.clear();
        editor.commit();
    }
}
