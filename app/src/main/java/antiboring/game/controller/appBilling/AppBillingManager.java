package antiboring.game.controller.appBilling;

import android.app.Activity;
import android.app.AlertDialog;
import android.util.Log;

import antiboring.game.R;
import antiboring.game.controller.appBilling.util.IabBroadcastReceiver;
import antiboring.game.controller.appBilling.util.IabHelper;
import antiboring.game.controller.appBilling.util.IabResult;

/**
 * Created by Gujarat Santana on 01/01/16.
 */
public class AppBillingManager {

    // list item to buy
    static final String SKU_PREMIUM_MONTHLY = "premium_monthly";
    static final String SKU_COINS = "coins";
    private static final String TAG = "AppBillingManager";
    // The helper object
    IabHelper mHelper;

    // Provides purchase notification while this app is running
    IabBroadcastReceiver mBroadcastReceiver;
    private Activity activity;

    public AppBillingManager(){

    }

    public void initBilling(final Activity activity){
        this.activity = activity;
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(activity, activity.getResources().getString(R.string.apa));

        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);

        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain(activity,"Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // Important: Dynamically register for broadcast messages about updated purchases.
                // We register the receiver here instead of as a <receiver> in the Manifest
                // because we always call getPurchases() at startup, so therefore we can ignore
                // any broadcasts sent while the app isn't running.
                // Note: registering this listener in an Activity is a bad idea, but is done here
                // because this is a SAMPLE. Regardless, the receiver must be registered after
                // IabHelper is setup, but before first call to getPurchases().
//                mBroadcastReceiver = new IabBroadcastReceiver(MainActivity.this);
//                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
//                registerReceiver(mBroadcastReceiver, broadcastFilter);
//
//                // IAB is fully set up. Now, let's get an inventory of stuff we own.
//                Log.d(TAG, "Setup successful. Querying inventory.");
//                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });
    }



    private void complain(Activity activity,String message) {
        Log.e(TAG, "**** TrivialDrive Error: " + message);
        alert(activity,"Error: " + message);
    }

    private void alert(Activity activity,String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(activity);
        bld.setMessage(message);
        bld.setNeutralButton("OK", null);
        Log.d(TAG, "Showing alert dialog: " + message);
        bld.create().show();
    }
}
