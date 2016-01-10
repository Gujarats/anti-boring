package antiboring.game.controller.Session;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Gujarat Santana on 24/12/15.
 */
public class SessionCoin {

    private static final String PREF_NAME = "TebakanSessionCoin";

    /**
     * key for preference
     */
    //api token
    private static final String KEY_COINS = "KEY_COINS";

    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public SessionCoin(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public int getCoins(){
        return pref.getInt(KEY_COINS, 170);
    }

    public void setCoins(int coins){
        editor.putInt(KEY_COINS, coins);
        editor.commit();
    }



}
