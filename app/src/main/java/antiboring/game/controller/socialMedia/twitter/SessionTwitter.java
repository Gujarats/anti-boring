package antiboring.game.controller.socialMedia.twitter;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Gujarat Santana on 31/12/15.
 */
public class SessionTwitter {
    private static final String PREF_NAME = "TwitterSession";

    /**
     * key for preference
     */
    //api token
    private static final String isLogin = "isLogin";
    private static final String PREF_KEY_OAUTH_SECRET = "PREF_KEY_OAUTH_SECRET";
    private static final String PREF_KEY_OAUTH_TOKEN= "PREF_KEY_OAUTH_TOKEN";

    // Shared Preferences
    SharedPreferences pref;
    // Editor for Shared preferences
    SharedPreferences.Editor editor;
    // Context
    Context _context;
    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Constructor
    public SessionTwitter(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public boolean isLogin(){
        return pref.getBoolean(isLogin, false);
    }

    public void setLogin(boolean isLogin){
        editor.putBoolean(this.isLogin, isLogin);
        editor.commit();
    }

    public String getPrefKeyOauthSecret(){
        return pref.getString(PREF_KEY_OAUTH_SECRET, "");
    }

    public void setPrefKeyOauthSecret(String Secret){
        editor.putString(PREF_KEY_OAUTH_SECRET, Secret);
        editor.commit();
    }

    public String getPrefKeyOauthToken(){
        return pref.getString(PREF_KEY_OAUTH_TOKEN,"");
    }

    public void setPrefKeyOauthToken(String token){
        editor.putString(PREF_KEY_OAUTH_TOKEN,token);
        editor.commit();
    }
}
