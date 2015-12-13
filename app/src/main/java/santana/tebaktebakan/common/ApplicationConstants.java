package santana.tebaktebakan.common;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Gujarat Santana on 15/06/15.
 */
public class ApplicationConstants {

    //request to server
    public static final int loadGambar = 0;
    public static final int jawabanSalah = 1;
    public static final int jawabanBenar = 2;
    public static final int next = 3;
    public static final int tryAgain = 4;

    //admob
    public static final String AdmobKey = "ca-app-pub-7786975749587909/8076194272";


    //intent service from activity
    public static final String FromActivity = "FromActivity";
    public static final String ImageVisibiliy = "no_data";


    //important data to answer activity
    public static final String imageUrl = "ImageUrl";
    public static final String jawabanTebakan = "jawabanTebakan";
    public static final String tebakanKata = "tebakanKata";
    public static final String gcmID = "gcmID";
    public static final String _idUserTebakan = "_idUserTebakan";
    public static final String _idTebakan = "_idTebakan";

    //id tebakan
    public static final String _listChatBrow = "_listChatBrow.txt";
    public static final String _listChatBrow2 = "_listChatBrow2.txt";




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
