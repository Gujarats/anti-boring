package santana.tebaktebakan.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Gujarat Santana on 15/06/15.
 */
public class ApplicationConstants {

    //intent service from activity
    public static final String FromActivity = "FromActivity";
    public static final String ImageVisibiliy = "no_data";



    public static final String SENDER_ID_GCM = "390993870271";


    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }
}
