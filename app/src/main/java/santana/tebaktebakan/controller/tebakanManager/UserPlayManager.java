package santana.tebaktebakan.controller.tebakanManager;

import android.app.Activity;

import santana.tebaktebakan.controller.SessionManager.SessionManager;

/**
 * Created by Gujarat Santana on 25/12/15.
 */
public class UserPlayManager {
    private static UserPlayManager instance;
    private UserPlayManager() {}

    public static UserPlayManager getInstance() {
        if (instance == null) instance = new UserPlayManager();
        return instance;
    }

    public void setUserPlayed(Activity activity,boolean isPlayed){
        SessionManager sessionManager = new SessionManager(activity);
        sessionManager.setKeyUserPlayed(isPlayed);
    }

    public boolean isUserPlayed(Activity activity){
        SessionManager sessionManager = new SessionManager(activity);
        return sessionManager.isUserPlayed();
    }
}
