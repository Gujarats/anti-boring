package santana.tebaktebakan.session;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Gujarat Santana on 12/06/15.
 */
public class SessionManager {
    //Preference name
    private static final String PREF_NAME = "TebakanPref";

    /**
     * key for preference
     */
    //api token
    private static final String KEY_TOKEN = "KEY_TOKEN";
    // uid user
    private static final String KEY_UID = "KEY_UID";
    private static final String KEY_IS_GCM_REGISTERED_TO_SERVER = "KEY_IS_GCM_REGISTERED_TO_SERVER";
    private static final String KEY_gcm = "KEY_gcm";
    private static final String KEY_AppVersion = "KEY_AppVersion";
    private static final String KEY_EMAIL = "KEY_EMAIL";
    private static final String KEY_PASS = "KEY_PASS";
    private static final String KEY_USERNAME = "KEY_USERNAME";
    private static final String KEY_LOGGIN = "KEY_LOGGIN";
    private static final String KEY_HINT = "KEY_HINT";
    private static final String KEY_POINT = "KEY_POINT";
    private static final String KEY__IdTebakan = "KEY__IdTebakan";
    private static final String KEY_LEVEL = "KEY_LEVEL";
    private static final String MODE_USER= "MODE_USER";

    /**
     * twitter sessio
     */
    private static final String PREF_KEY_OAUTH_TOKEN = "oauth_token";
    private static final String PREF_KEY_OAUTH_SECRET = "oauth_token_secret";
    private static final String PREF_KEY_TWITTER_LOGIN = "is_twitter_loggedin";
    private static final String PREF_USER_NAME = "twitter_user_name";


    // uri gambar
    private static final String KEY_URI = "KEY_URI";

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

    public boolean getModeUser(){
        return pref.getBoolean(MODE_USER,false);
    }

    public void setModeUser(boolean bool){
        editor.putBoolean(MODE_USER,bool);
        editor.commit();
    }

    public boolean getLoginTwitter(){
        return pref.getBoolean(PREF_KEY_TWITTER_LOGIN,false);
    }

    public void setLoginTwitter(boolean bol){
        editor.putBoolean(PREF_KEY_TWITTER_LOGIN,bol);
        editor.commit();
    }

    public void saveTwitterInfodong(String token, String tokenSecret, boolean bool, String username) {
        editor.putString(PREF_KEY_OAUTH_TOKEN, token);
        editor.putString(PREF_KEY_OAUTH_SECRET, tokenSecret);
        editor.putBoolean(PREF_KEY_TWITTER_LOGIN, bool);
        editor.putString(PREF_USER_NAME, username);
        editor.commit();
    }

    public String getPrefKeyOauthToken(){
        return pref.getString(PREF_KEY_OAUTH_TOKEN,"");
    }

    public void setPrefKeyOauthToken(String token){
        editor.putString(PREF_KEY_OAUTH_TOKEN,token);
        editor.commit();
    }

    public String getPrefKeyOauthSecret(){
        return pref.getString(PREF_KEY_OAUTH_SECRET,"");
    }

    public void setPrefKeyOauthSecret(String Secret){
        editor.putString(PREF_KEY_OAUTH_SECRET,Secret);
        editor.commit();
    }

    public boolean isLoggin(){
        return pref.getBoolean(KEY_LOGGIN,false);
    }

    public void setLoggin(boolean loggin){
        editor.putBoolean(KEY_LOGGIN,loggin);
        editor.commit();
    }

    public String getKeyUri(){
        return pref.getString(KEY_URI, "");
    }

    public void setKeyUri(String path){
        editor.putString(KEY_URI, path);
        editor.commit();
    }

    public String getLevel(){
        return pref.getString(KEY_LEVEL, "");
    }

    public void setLevel(String path){
        editor.putString(KEY_LEVEL, path);
        editor.commit();
    }

    public String getIdTebakan(){
        return pref.getString(KEY__IdTebakan, "");
    }

    public void setIdTebakan(String path){
        editor.putString(KEY__IdTebakan, path);
        editor.commit();
    }

    public int getCoins(){
        return pref.getInt(KEY_POINT, 0);
    }

    public void setCoins(int jumlahHint){
        editor.putInt(KEY_POINT, jumlahHint);
        editor.commit();
    }

    public int getHint(){
        return pref.getInt(KEY_HINT, 0);
    }

    public void setHint(int jumlahHint){
        editor.putInt(KEY_HINT,jumlahHint);
        editor.commit();
    }

    public String getUsername(){
        return pref.getString(KEY_USERNAME,"");
    }

    public void setUsername(String username){
        editor.putString(KEY_USERNAME,username);
        editor.commit();
    }

    public String getPassword(){
        return pref.getString(KEY_PASS,"");
    }

    public void setPassword(String Pass){
        editor.putString(KEY_PASS,Pass);
        editor.commit();
    }

    public String getEmail(){
        return pref.getString(KEY_EMAIL,"");
    }

    public void setEmail(String email){
        editor.putString(KEY_EMAIL,email);
        editor.commit();
    }

    public int getAppVersion(){
        return pref.getInt(KEY_AppVersion, Integer.MIN_VALUE);
    }

    public void setAppVersion(int appVersion){
        editor.putInt(KEY_AppVersion,appVersion);
        editor.commit();
    }

    public String getUidUser(){
        return pref.getString(KEY_UID,"");
    }

    //uid user from server
    public void setUidUser(String uid){
        editor.putString(KEY_UID,uid);
        editor.commit();
    }

    //token
    public String getToken(){
        return pref.getString(KEY_TOKEN,"");
    }

    public void setToken(String token){
        editor.putString(KEY_TOKEN,token);
        editor.commit();
    }

    public void setIsGcmRegistered(boolean value){
        editor.putBoolean(KEY_IS_GCM_REGISTERED_TO_SERVER, value);
        editor.commit();
    }

    public boolean getIsGCmRegistered(){
        return pref.getBoolean(KEY_IS_GCM_REGISTERED_TO_SERVER,false);
    }

    public String getGcmID(){
        return pref.getString(KEY_gcm, "");
    }

    public void setGcmID(String gcmID){
        editor.putString(KEY_gcm, gcmID);
        editor.commit();
    }

    public void clearAllSession(){
        editor.clear();
        editor.commit();
    }
}

