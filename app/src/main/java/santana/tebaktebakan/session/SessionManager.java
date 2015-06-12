package santana.tebaktebakan.session;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Gujarat Santana on 12/06/15.
 */
public class SessionManager {
    //Preference name
    private static final String PREF_NAME = "TebakanPref";
    //api token
    private static final String KEY_TOKEN = "KEY_TOKEN";
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

    public String getToken(){
        return pref.getString(KEY_TOKEN,"");
    }

    public void setToken(String token){
        editor.putString(KEY_TOKEN,token);
        editor.commit();
    }




}

