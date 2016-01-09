package antiboring.game.controller.appBilling;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import com.android.vending.billing.IInAppBillingService;

import java.util.ArrayList;
import java.util.List;

import antiboring.game.controller.appBilling.util.IabBroadcastReceiver;
import antiboring.game.controller.appBilling.util.IabHelper;
import antiboring.game.controller.appBilling.util.IabResult;
import antiboring.game.controller.appBilling.util.Inventory;
import antiboring.game.controller.appBilling.util.Purchase;

/**
 * Created by Gujarat Santana on 01/01/16.
 */
public class AppBillingManager {

    // list item to buy
    static final String SKU_PREMIUM= "premium";
    static final String SKU_COINS_HOT_OFFER = "hot_offer";
    static final String SKU_COINS_REGULAR = "regular";
    static final String SKU_COINS_DOUBLE_REGULAR = "double_regular";
    static final String SKU_COINS_AWESOMEPACK = "awesome_pack";
    static final String SKU_COINS_BEST_OFFER= "best_offer";
    private static final String TAG = "AppBillingManager";
    public static AppBillingManager instance;
    // The helper object
    IabHelper mHelper;
    // Provides purchase notification while this app is running
    private IabBroadcastReceiver mBroadcastReceiver;
    private IInAppBillingService mService;
    private ServiceConnection mServiceConn;
    private Activity activity;
    // Called when consumption is complete
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener = new IabHelper.OnConsumeFinishedListener() {
        public void onConsumeFinished(Purchase purchase, IabResult result) {
            Log.d(TAG, "Consumption finished. Purchase: " + purchase + ", result: " + result);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            // We know this is the "gas" sku because it's the only one we consume,
            // so we don't check which sku was consumed. If you have more than one
            // sku, you probably should check...
            if (result.isSuccess()) {
                // successfully consumed, so we apply the effects of the item in our
                // game world's logic, which in our case means filling the gas tank a bit
                Log.d(TAG, "Consumption successful. Provisioning.");
            }
            else {
                complain(activity,"Error while consuming: " + result);
            }
            Log.d(TAG, "End consumption flow.");
        }
    };
    // Callback for when a purchase is finished
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            Log.d(TAG, "Purchase finished: " + result + ", purchase: " + purchase);

            // if we were disposed of in the meantime, quit.
            if (mHelper == null) return;

            if (result.isFailure()) {
                complain(activity,"Error purchasing: " + result);
                return;
            }
            if (!verifyDeveloperPayload(purchase)) {
                complain(activity,"Error purchasing. Authenticity verification failed.");
                return;
            }

            Log.d(TAG, "Purchase successful.");


            if (purchase.getSku().equals(SKU_PREMIUM)) {
                // bought the premium user
                Log.d(TAG, "Purchase is premium upgrade. Congratulating user.");
                alert(activity, "Thank you for upgrading to premium!");
            }else {
                // bought coins
                Log.d(TAG, "Purchase is gas. Starting gas consumption.");
                mHelper.consumeAsync(purchase, mConsumeFinishedListener);
            }
        }
    };

    private AppBillingManager(){}

    public static AppBillingManager getInstance() {
        if (instance == null) instance = new AppBillingManager();
        return instance;
    }

//    public boolean isAppBillingAvailableOnDevice(){
//
//    }

    public void initBillingBuyActivity(final Context context,final Activity activity){
        this.activity = activity;
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(context, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjFVcXKbW4TvdA04tcXjeNBU3zo59OMXLHcfpwOWE9sNtEUH0H7puxZs2biaC6zTmSk2Ia8Quv30JxUn1h15emE923G/BGR1Hrj0SbNIRHpLrwIjO62RZ+cw0xpzEMrYeKMQTbDe+xrKBzEAMo436P1KbAR/+TDiYw7PhC3fKqGMfq4otGL7RVjvF6NGiTHGEz639pM2Uru46IfSUJGile/9aNGoQaOPs0xGXpVjGarEDr+LuzmoejSmZRkqLpIm/1j4woXnw74a0SuntF5R0mYxPoPtlPvsh/JvP98ppnq4McqAW22CWFOesgOLQi4Gz+y2fkNCJe8IQFiNv3jTXFQIDAQAB");

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
                    complain(activity, "Problem setting up in-app billing: " + result);
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
                mBroadcastReceiver = new IabBroadcastReceiver(new IabBroadcastReceiver.IabBroadcastListener() {
                    @Override
                    public void receivedBroadcast() {
                        Log.d(TAG, "Received broadcast notification. Querying inventory.");
                        getAvailableItemForPurchase();
                    }
                });
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                activity.registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
//                mHelper.queryInventoryAsync(new IabHelper.QueryInventoryFinishedListener() {
//                    @Override
//                    public void onQueryInventoryFinished(IabResult result, Inventory inv) {
//                        settAvailableItems(inv);
//                    }
//                });

                getAvailableItemForPurchase();
            }
        });
    }

    /**
     * we use this on main activity to get user has purchase some items,
     * and to determine if user is premium or not
     * @param context
     * @param activity
     */
    public void initBillingMainActivity(final Context context,final Activity activity){
        this.activity = activity;
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(context, "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjFVcXKbW4TvdA04tcXjeNBU3zo59OMXLHcfpwOWE9sNtEUH0H7puxZs2biaC6zTmSk2Ia8Quv30JxUn1h15emE923G/BGR1Hrj0SbNIRHpLrwIjO62RZ+cw0xpzEMrYeKMQTbDe+xrKBzEAMo436P1KbAR/+TDiYw7PhC3fKqGMfq4otGL7RVjvF6NGiTHGEz639pM2Uru46IfSUJGile/9aNGoQaOPs0xGXpVjGarEDr+LuzmoejSmZRkqLpIm/1j4woXnw74a0SuntF5R0mYxPoPtlPvsh/JvP98ppnq4McqAW22CWFOesgOLQi4Gz+y2fkNCJe8IQFiNv3jTXFQIDAQAB");

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
                mBroadcastReceiver = new IabBroadcastReceiver(new IabBroadcastReceiver.IabBroadcastListener() {
                    @Override
                    public void receivedBroadcast() {
                        Log.d(TAG, "Received broadcast notification. Querying inventory.");
                        mHelper.queryInventoryAsync(new IabHelper.QueryInventoryFinishedListener() {
                            @Override
                            public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                                checkItemUserOwned(inv);
                            }
                        });
                    }
                });
                IntentFilter broadcastFilter = new IntentFilter(IabBroadcastReceiver.ACTION);
                activity.registerReceiver(mBroadcastReceiver, broadcastFilter);

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                mHelper.queryInventoryAsync(new IabHelper.QueryInventoryFinishedListener() {
                    @Override
                    public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                        checkItemUserOwned(inv);
                    }
                });


            }
        });
    }

    private void settAvailableItems(Inventory inventory){
        boolean hot1 = inventory.hasPurchase(SKU_COINS_HOT_OFFER);
        boolean regular1 =inventory.hasPurchase(SKU_COINS_REGULAR);
        boolean premium1 = inventory.hasPurchase(SKU_PREMIUM);


        Log.i(TAG, "settAvailableItems: "+"premium "+premium1);
        Log.i(TAG, "settAvailableItems: " + "hot offer " + hot1);
        Log.i(TAG, "settAvailableItems: " + "regular " + regular1);
    }

    public boolean isUserPremium(Inventory inventory){
        try{
            Purchase premiumPurchase = inventory.getPurchase(SKU_PREMIUM);
            boolean isPremium = (premiumPurchase != null && verifyDeveloperPayload(premiumPurchase));
            return isPremium;
        }catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean isHotOffer(Inventory inventory){
        try{
            Purchase hotOfferPurchase = inventory.getPurchase(SKU_COINS_HOT_OFFER);
            boolean isPremium = (hotOfferPurchase != null && verifyDeveloperPayload(hotOfferPurchase));
            return isPremium;
        }catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean isRegular(Inventory inventory){
        try{
            Purchase regularPurchase = inventory.getPurchase(SKU_COINS_REGULAR);
            boolean isPremium = (regularPurchase != null && verifyDeveloperPayload(regularPurchase));
            return isPremium;
        }catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean isDoubleRegular(Inventory inventory){
        try{
            Purchase double_regular_purchase = inventory.getPurchase(SKU_COINS_DOUBLE_REGULAR);
            boolean isPremium = (double_regular_purchase != null && verifyDeveloperPayload(double_regular_purchase));
            return isPremium;
        }catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean isAwseomePack(Inventory inventory){
        try{
            Purchase AwesomePackPurchase = inventory.getPurchase(SKU_COINS_AWESOMEPACK);
            boolean isPremium = (AwesomePackPurchase != null && verifyDeveloperPayload(AwesomePackPurchase));
            return isPremium;
        }catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }

    public boolean isBestOffer(Inventory inventory){
        try{
            Purchase bestOfferPurchase = inventory.getPurchase(SKU_COINS_BEST_OFFER);
            boolean isPremium = (bestOfferPurchase != null && verifyDeveloperPayload(bestOfferPurchase));
            return isPremium;
        }catch (NullPointerException e){
            e.printStackTrace();
            return false;
        }
    }

    public void checkItemUserOwned(Inventory inventory){

        if(isHotOffer(inventory)){
            // user bought hot offer

        }

        if(isUserPremium(inventory)){
            // user is Premium
        }

        if(isRegular(inventory)){
            //user brought regualr
        }

        if(isDoubleRegular(inventory)){
            //user brought regualr
        }

        if(isAwseomePack(inventory)){
            //user brought regualr
        }

        if(isBestOffer(inventory)){
            //user brought regualr
        }





    }

    public void getAvailableItemForPurchase(){
        List additionalSkuList = new ArrayList();
        final List<String> items = new ArrayList<>();
        additionalSkuList.add(SKU_COINS_HOT_OFFER);
        additionalSkuList.add(SKU_PREMIUM);
        additionalSkuList.add(SKU_COINS_REGULAR);
        mHelper.queryInventoryAsync(true, additionalSkuList,
                new IabHelper.QueryInventoryFinishedListener() {
                    @Override
                    public void onQueryInventoryFinished(IabResult result, Inventory inv) {
                        if (result.isFailure()) {
                            // handle error
                            Log.i(TAG, "getAvailableItemForPurchase: Error Query");
                            return;
                        }

                        String hotOffer =
                                inv.getSkuDetails(SKU_COINS_HOT_OFFER).getPrice();
                        String premium =
                                inv.getSkuDetails(SKU_PREMIUM).getPrice();
                        String regular =
                                inv.getSkuDetails(SKU_COINS_REGULAR).getPrice();

                        items.add(hotOffer);
                        items.add(premium);
                        items.add(regular);


                        Log.i(TAG, "getAvailableItemForPurchase: " + hotOffer);
                        Log.i(TAG, "getAvailableItemForPurchase: " + premium);
                        Log.i(TAG, "getAvailableItemForPurchase: " + regular);

                    }
                });
    }

    public boolean isPremiumUser(Activity activity){
        SessionPremiumUser sessionPremiumUser = new SessionPremiumUser(activity);
        return sessionPremiumUser.isPremiumUser();
    }

    public void setPremiumUser(Activity activity,boolean isPremium){
        SessionPremiumUser sessionPremiumUser = new SessionPremiumUser(activity);
        sessionPremiumUser.setPremiumUser(isPremium);
    }


    public void bindingToAppBillingService(Activity activity){
        mServiceConn = new ServiceConnection() {
            @Override
            public void onServiceDisconnected(ComponentName name) {
                mService = null;
            }

            @Override
            public void onServiceConnected(ComponentName name,
                                           IBinder service) {
                mService = IInAppBillingService.Stub.asInterface(service);
            }
        };

        Intent serviceIntent =
                new Intent("com.android.vending.billing.InAppBillingService.BIND");
        serviceIntent.setPackage("com.android.vending");
        activity.bindService(serviceIntent, mServiceConn, Context.BIND_AUTO_CREATE);
    }

    public void onDestroy(Activity activity){
        // very important:
        if (mBroadcastReceiver != null) {
            activity.unregisterReceiver(mBroadcastReceiver);
        }

        // Destroying helper
        if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }

        // destroy mService
        if (mService != null) {
            activity.unbindService(mServiceConn);
        }
    }

    /** Verifies the developer payload of a purchase. */
    boolean verifyDeveloperPayload(Purchase p) {
        String payload = p.getDeveloperPayload();

        /*
         * TODO: verify that the developer payload of the purchase is correct. It will be
         * the same one that you sent when initiating the purchase.
         *
         * WARNING: Locally generating a random string when starting a purchase and
         * verifying it here might seem like a good approach, but this will fail in the
         * case where the user purchases an item on one device and then uses your app on
         * a different device, because on the other device you will not have access to the
         * random string you originally generated.
         *
         * So a good developer payload has these characteristics:
         *
         * 1. If two different users purchase an item, the payload is different between them,
         *    so that one user's purchase can't be replayed to another user.
         *
         * 2. The payload must be such that you can verify it even when the app wasn't the
         *    one who initiated the purchase flow (so that items purchased by the user on
         *    one device work on other devices owned by the user).
         *
         * Using your own server to store and verify developer payloads across app
         * installations is recommended.
         */

        return true;
    }

    private void complain(Activity activity,String message) {
        Log.e(TAG, "**** Anti Boring Error: " + message);
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
