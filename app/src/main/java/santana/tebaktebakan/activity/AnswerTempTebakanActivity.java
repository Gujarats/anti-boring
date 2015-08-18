package santana.tebaktebakan.activity;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import santana.tebaktebakan.AppController;
import santana.tebaktebakan.R;
import santana.tebaktebakan.common.ApplicationConstants;
import santana.tebaktebakan.common.ConnectionDetector;
import santana.tebaktebakan.common.ServerConstants;
import santana.tebaktebakan.object.TebakanObject;
import santana.tebaktebakan.requestNetwork.CostumRequestString;
import santana.tebaktebakan.requestNetwork.VolleyImageView;
import santana.tebaktebakan.session.SessionManager;
import santana.tebaktebakan.storageMemory.SavingFile;

/**
 * Created by Gujarat Santana on 18/08/15.
 */
public class AnswerTempTebakanActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener{

    protected AppCompatEditText AnswerTebakan;
    protected AppCompatTextView TextTebakan;
    protected VolleyImageView gambarTebakan;
    protected ProgressBar Loading;
    protected AppCompatDialog dialog;
    protected Menu menu;
    protected LinearLayout layout2;
    protected ImageView life1,life2,life3;

    //time varibel UI
    protected AppCompatTextView Waktu,CoinSaya;

    /*
    coundown variable
     */
    private CountDownTimer countDownTimer;

    /*
    variabel pointer gambar
     */
    private int tebakanPointer = 0;
    private TebakanObject tebakanObject;

    /*list tebakan */
    private List<TebakanObject> tebakanObjects;


    /*test buat hint*/
    private String tempTebakan = "Pejalan Kaki";
    private String _idTebakan = "";
    private String _idUser, textTebakan, gambarUrl, gcmID, kunciTebakan;
    private SessionManager sessionManager;
    private int WidthPhone;
    //image Loader
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    //saving file id
    private SavingFile savingFile;


    //id brow
    private JSONObject valuesIdChats;
    private boolean userAlreadyAnswer = true;
    private boolean userFailedAnswer = true;

    /*logic timer for dialog hint*/
    private long dismissHint;
    private int hintShow;

    /*
    logic succes request to server
     */
    private int requestServer=0;

    /**
     * logic connection detector
     */
    private int retryConnect = 0;
    private ConnectionDetector cd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        connection detector
         */
        cd = new ConnectionDetector(getApplicationContext());

        dismissHint =30000;
        setContentView(R.layout.layout_answer_tebakan_activity);
        sessionManager = new SessionManager(getApplicationContext());
        tebakanObject = new TebakanObject();
        hintShow = sessionManager.getHint();
        /**
         * clear list
         */
        if(tebakanObjects!=null){
            tebakanObjects.clear();
        }else{
            tebakanObjects = new ArrayList<TebakanObject>();
        }

        /**
         * make request to get list tebakan
         */
        if(cd.isConnectingToInternet() && sessionManager.getIdTebakan().isEmpty()){
//            Log.d("first","first");
            requestApi(ServerConstants.ShowAlltebakanTemp);
            requestServer = ApplicationConstants.loadGambar;
        } else if(cd.isConnectingToInternet() && !sessionManager.getIdTebakan().isEmpty()){
//            Log.d("first","second");
            requestApi(ServerConstants.showTebakanPagingTemp);
            requestServer = ApplicationConstants.loadGambar;
        } else{
            Toast.makeText(AnswerTempTebakanActivity.this, "Please Check Your Connection", Toast.LENGTH_SHORT).show();
        }

        initUi();
        initHint();
    }

    @Override
    public void onBackPressed() {
    }

    private void initUi() {
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
//        setSupportActionBar(toolbar);

        AnswerTebakan = (AppCompatEditText) findViewById(R.id.TextAnswer_Answer);
        TextTebakan = (AppCompatTextView) findViewById(R.id.TextTebakan_Answer);
        gambarTebakan = (VolleyImageView) findViewById(R.id.GambarTebakan_Answer);
        Loading = (ProgressBar) findViewById(R.id.Loading);
        layout2 = (LinearLayout)findViewById(R.id.layout2);

        //waktu dan coin
        Waktu = (AppCompatTextView)findViewById(R.id.Waktu);
        CoinSaya= (AppCompatTextView)findViewById(R.id.coinSaya);
        CoinSaya.setText(String.valueOf(sessionManager.getCoins()));

        //life point
        life1 = (ImageView)findViewById(R.id.life1);
        life2 = (ImageView)findViewById(R.id.life2);
        life3 = (ImageView)findViewById(R.id.life3);

        dialog = new AppCompatDialog(AnswerTempTebakanActivity.this);

        /**
         * setVisibility of The UI
         */
        LoadingUI();

        /**
         * init the image size in phone and image loader
         */
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;

         /*get widht of the phone*/
        if (currentapiVersion >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            /**
             * merah di bawah memang sengaja, buat api 13 keatas
             * itu warning doank
             */
            display.getSize(size);
            WidthPhone = size.x;
        } else {
            Display display = getWindowManager().getDefaultDisplay();
            WidthPhone = display.getWidth();  // deprecated
        }
        gambarTebakan.getLayoutParams().height = WidthPhone;
        gambarTebakan.requestLayout();

        /**
         * listener for done key keyboard
         */
        AnswerTebakan.setOnEditorActionListener(new AppCompatTextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    //do something
                    AnswerTebakan();
                }
                return false;
            }
        });
    }

    private void setValueForTheUI(final TebakanObject tebakanObject){
        /**
         * set variabel
         */
        _idTebakan = tebakanObject.get_idTebakan();
        _idUser = tebakanObject.get_idUser();
        textTebakan = tebakanObject.getTextTebakan();
        kunciTebakan = tebakanObject.getKunciTebakan();
        gambarUrl = tebakanObject.getUrlGambarTebakan();
        gcmID = tebakanObject.getGcmID();

        /*
        session
         */
        sessionManager.setIdTebakan(_idTebakan);


        /**
         * get callback from downloading image from volley
         * and begin countDown
         */

        if(this.gambarTebakan!=null){
//            this.gambarTebakan.destroyDrawingCache();
            this.gambarTebakan.setResponseObserver(new VolleyImageView.ResponseObserver() {
                @Override
                public void onError() {
                    Log.d("gambar", "ErrorLoading");
                    setValueForTheUI(tebakanObjects.get(tebakanPointer));
                    if (countDownTimer != null)
                        countDownTimer.cancel();
                }

                @Override
                public void onSuccess() {
                    if (countDownTimer != null)
                        countDownTimer.cancel();

                    /**
                     * begin countDown
                     */
                    CountDown();
                }
            });
            imageLoader.get(tebakanObject.getUrlGambarTebakan(), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    if (response.getBitmap() != null) {
//                        Log.d("gambar","loaded");
                        gambarTebakan.setImageBitmap(response.getBitmap());
                        gambarTebakan.setImageUrl(gambarUrl, imageLoader);

                        /**
                         * setVisibility of The UI
                         */
                        LoadingFinish();
                    }

                }

                @Override
                public void onErrorResponse(VolleyError error) {
//                    Log.d("gambar","failed");
                    countDownTimer.cancel();
                    setValueForTheUI(tebakanObjects.get(tebakanPointer));
                }
            });
//            this.gambarTebakan.setImageUrl(tebakanObject.getUrlGambarTebakan(),imageLoader);
            this.TextTebakan.setText(tebakanObject.getTextTebakan());
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.answer_activity, menu);
//        this.menu=menu;
//        return true;
//    }
//
//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu1) {
//        this.menu.findItem(R.id.Coins).setTitle(String.valueOf(sessionManager.getCoins()));
//        return super.onPrepareOptionsMenu(menu1);
//
//    }

    private void CountDown(){
        countDownTimer = new CountDownTimer(30000, 1000) {//CountDownTimer(edittext1.getText()+edittext2.getText()) also parse it to long

            public void onTick(long millisUntilFinished) {
                long count = millisUntilFinished/1000;
//                menu.findItem(R.id.Count).setTitle(String.valueOf(count));
                Waktu.setText(String.valueOf(count));
                dismissHint = millisUntilFinished;
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {

                dialog.setContentView(R.layout.dialog_out_of_time);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                AppCompatTextView text2 = (AppCompatTextView) dialog.findViewById(R.id.dialog_text);
                text2.setText(R.string.dialog_out_of_time);
                text2.setTextColor(getResources().getColor(R.color.primary));
                //coins
                AppCompatTextView coins = (AppCompatTextView) dialog.findViewById(R.id.coins);
                coins.setText(String.valueOf(sessionManager.getCoins()));

                AppCompatButton button2 = (AppCompatButton) dialog.findViewById(R.id.dialog_try_again);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        /*
                        set visibily
                         */
                        LoadingUI();

                        /*
                        algoritma untuk next image
                         */
                        setValueForTheUI(tebakanObjects.get(tebakanPointer));

                        /*
                        begin again count down
                         */
                        if(countDownTimer!=null)
                            countDownTimer.cancel();
                        CountDown();

                        /**
                         * request minus point tray again
                         */
//                        sessionManager.setCoins(sessionManager.getCoins()-30);
                        setCoin(-30);

//                        requestApi(ServerConstants.tryAgain);
//                        requestServer = ApplicationConstants.tryAgain;
                        dialog.dismiss();
                    }
                });

                ((AppCompatButton)dialog.findViewById(R.id.dialog_next)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*
                        set visibiliy
                         */

                        LoadingUI();
                        /*
                        algoritma untuk next image
                         */
                        setValueForTheUI(NextTebakanObjectList());

                        /*
                        begin count down
                         */
                        if (countDownTimer != null)
                            countDownTimer.cancel();
                        CountDown();

                        /**
                         * request minus point next krn tidak berhasil menjawab pertanyaan dengan benar
                         */
//                        requestApi(ServerConstants.next);
//                        requestServer = ApplicationConstants.next;
//                        sessionManager.setCoins(sessionManager.getCoins()-50);
                        setCoin(-50);
                        dialog.dismiss();
                    }
                });
                dialog.show();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

            }
        }.start();
    }

    private void setCoin(int point){
        sessionManager.setCoins(sessionManager.getCoins()+point);
        CoinSaya.setText(String.valueOf(sessionManager.getCoins()));
    }


    private void AnswerTebakan() {
        String answerTemp = AnswerTebakan.getText().toString();
        if (!answerTemp.trim().isEmpty()) {
            if (answerTemp.equalsIgnoreCase(kunciTebakan)) {
                //Answer is right give point and send messege to user who has tebakan
//                Toast.makeText(getApplicationContext(), "Yeaahhh You Got Points !!!", Toast.LENGTH_LONG).show();

                /**
                 * making reques for the right answer
                 */
                //plus point 150
//                sessionManager.setCoins(sessionManager.getCoins()+150);
                setCoin(150);
//                requestApi(ServerConstants.jawabanBenar);
//                requestServer = ApplicationConstants.jawabanBenar;


                /**
                 * show dialog for winner
                 */

                dialog.setContentView(R.layout.dialog_correct_answer);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                AppCompatTextView text = (AppCompatTextView) dialog.findViewById(R.id.dialog_text);
                text.setText(R.string.dialog_answer_correct);
                text.setTextColor(getResources().getColor(R.color.primary));
                ((AppCompatButton) dialog.findViewById(R.id.dialog_try_again)).setVisibility(View.GONE);
                AppCompatButton button = (AppCompatButton) dialog.findViewById(R.id.dialog_ok);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        LoadingUI();

                            /*
                            algoritma untuk next image
                             */
                        if(tebakanPointer<=tebakanObjects.size()-1){
                            NextTebakanRequest();
                        }else{
                            setValueForTheUI(NextTebakanObjectList());
                        }


                        if(countDownTimer!=null)
                            countDownTimer.cancel();
                        dialog.dismiss();
                    }
                });
                    /*
                    cancel timer before showing out of time dialog
                     */
                countDownTimer.cancel();

                dialog.show();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

                    /*
                    set editText to null
                     */
                AnswerTebakan.setText("");


            } else {

                //Wrong answer give minus point if zero still zero
//                Toast.makeText(getApplicationContext(), "Wrong Answer", Toast.LENGTH_LONG).show();
                dialog.setContentView(R.layout.dialog_correct_answer);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                AppCompatTextView text = (AppCompatTextView) dialog.findViewById(R.id.dialog_text);
                text.setText(R.string.dialog_answer_incorrect);
                text.setTextColor(getResources().getColor(R.color.primary_red));
                AppCompatButton button = (AppCompatButton) dialog.findViewById(R.id.dialog_ok);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /**
                         * next kurangi point 50 krn tidak berhasil menjawab perntanyaan
                         */

                        if(sessionManager.getCoins()==0){
                            Toast.makeText(AnswerTempTebakanActivity.this, "Please Share to Social Media To get 1000 coins", Toast.LENGTH_SHORT).show();
                        }else{
                            //minus 50 coin
//                            sessionManager.setCoins(sessionManager.getCoins()-50);
                            setCoin(-50);
                            /*
                                algoritma next Image
                             */
                            if(tebakanPointer<=tebakanObjects.size()-1){
                                NextTebakanRequest();
                            }else{
                                setValueForTheUI(NextTebakanObjectList());
                            }

                            if(countDownTimer!=null)
                                countDownTimer.cancel();
                            dialog.dismiss();
                        }


                    }
                });
                button.setTextColor(getResources().getColor(R.color.primary_red));

                /*
                try again
                 */
                AppCompatButton tryAgain = (AppCompatButton) dialog.findViewById(R.id.dialog_try_again);
                tryAgain.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /**
                         * next kurangi point 30 krn tidak berhasil menjawab perntanyaan
                         */
                        if(sessionManager.getCoins()==0){
                            Toast.makeText(AnswerTempTebakanActivity.this, "Please Share to Social Media To get 1000 coins", Toast.LENGTH_SHORT).show();
                        }else{
                            if(countDownTimer!=null)
                                countDownTimer.cancel();
                            CountDown();
//                            sessionManager.setCoins(sessionManager.getCoins()-30);
                            setCoin(-30);
                            dialog.dismiss();
                        }

                    }
                });

                dialog.show();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);


                /**
                 * minus point untuk user yg gagal menjawab pertanyaan
                 */

//                sessionManager.setCoins(sessionManager.getCoins()-100);
                setCoin(-100);

                //freeze time
                countDownTimer.cancel();
            }
        } else {
            Toast.makeText(getApplicationContext(), "Please Answer", Toast.LENGTH_LONG).show();
        }
    }



    public void HintBrow(View v){
        if(hintShow<1){
            /**
             * hint habis gan
             */
            Toast.makeText(getApplicationContext(),"Share to social Media to get Full Love",Toast.LENGTH_LONG).show();
        }else{
            if(dismissHint<=4000){
                /**
                 * dialog hint tidak akan terbuka jika kurang dari 4 detik atau sama
                 */
            }else{
                String hint = AlgoritmaHint(kunciTebakan);
                dialog.setContentView(R.layout.dialog_hint);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                AppCompatTextView text = (AppCompatTextView) dialog.findViewById(R.id.dialog_text);
                text.setText(hint);
                text.setTextColor(getResources().getColor(R.color.primary));
//                AppCompatButton button = (AppCompatButton) dialog.findViewById(R.id.dialog_ok);
//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        dialog.dismiss();
//                    }
//                });
//                button.setTextColor(getResources().getColor(R.color.primary));
                dialog.show();
                dialog.setCancelable(false);
                dialog.setCanceledOnTouchOutside(false);

                new CountDownTimer(3000, 1000) {//CountDownTimer(edittext1.getText()+edittext2.getText()) also parse it to long

                    public void onTick(long millisUntilFinished) {

                    }

                    public void onFinish() {
                        dialog.dismiss();
                    }
                }.start();
            }
        }

        if(hintShow==3){
            life3.setImageResource(R.drawable.heart_life_point2);
        }else if(hintShow==2){
            life2.setImageResource(R.drawable.heart_life_point2);
        }else if(hintShow== 1){
            life1.setImageResource(R.drawable.heart_life_point2);
        }

        /**
         * count show hint dialog
         */
        if(hintShow > 0)
            hintShow=hintShow-1;
        sessionManager.setHint(hintShow);
    }

    private void initHint(){
        switch (sessionManager.getHint()){
            case 0 :
                life3.setImageResource(R.drawable.heart_life_point2);
                life2.setImageResource(R.drawable.heart_life_point2);
                life1.setImageResource(R.drawable.heart_life_point2);
                break;
            case 1 :
                life3.setImageResource(R.drawable.heart_life_point2);
                life2.setImageResource(R.drawable.heart_life_point2);

                break;
            case 2 :
                life3.setImageResource(R.drawable.heart_life_point2);
                break;
            case 3 :
                break;
        }
    }

    private void NextTebakanRequest(){
        tebakanPointer=0;
        Log.d("Begin", "make Reques next");
        /**
         * set the ui to loading
         */
        LoadingUI();

        /**
         * begin request next tebakan
         */
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put(ServerConstants.mParams_idUser, sessionManager.getUidUser());
        mParams.put(ServerConstants.mParamsToken, sessionManager.getToken());
        mParams.put(ServerConstants.mParamsIdTebakan, sessionManager.getIdTebakan());
        mParams.put(ServerConstants.mParamCoins, String.valueOf(sessionManager.getCoins()));
//        Log.d("NextPaging1", sessionManager.getIdTebakan());
//        Log.d("NextPaging2", String.valueOf(sessionManager.getCoins()));
        CostumRequestString myReq = new CostumRequestString(Request.Method.POST, ServerConstants.showTebakanPagingTemp, mParams, Request.Priority.NORMAL,AnswerTempTebakanActivity.this, AnswerTempTebakanActivity.this);
        myReq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myReq);
    }

    private void LoadingUI(){
        Loading.setVisibility(View.VISIBLE);
        TextTebakan.setVisibility(View.GONE);
        gambarTebakan.setVisibility(View.GONE);
        AnswerTebakan.setVisibility(View.GONE);
        layout2.setVisibility(View.GONE);
    }

    private void LoadingFinish(){
        Loading.setVisibility(View.GONE);
        layout2.setVisibility(View.VISIBLE);
        gambarTebakan.setVisibility(View.VISIBLE);
        TextTebakan.setVisibility(View.VISIBLE);
        AnswerTebakan.setVisibility(View.VISIBLE);
    }


    private TebakanObject NextTebakanObjectList(){
        _idTebakan = tebakanObjects.get(tebakanPointer).get_idTebakan();
        _idUser = tebakanObjects.get(tebakanPointer).get_idUser();
        textTebakan = tebakanObjects.get(tebakanPointer).getTextTebakan();
        kunciTebakan = tebakanObjects.get(tebakanPointer).getKunciTebakan();
        gambarUrl = tebakanObjects.get(tebakanPointer).getUrlGambarTebakan();
        gcmID = tebakanObjects.get(tebakanPointer).getGcmID();
        tebakanObject= tebakanObjects.get(tebakanPointer);
        tebakanPointer=tebakanPointer+1;
        return tebakanObject;
    }

    private void requestApi(String urlApi){
        Map<String, String> mParams = new HashMap<String, String>();
        mParams.put(ServerConstants.mParams_idUser, sessionManager.getUidUser());
        mParams.put(ServerConstants.mParamsToken, sessionManager.getToken());
        mParams.put(ServerConstants.mParamsIdTebakan, sessionManager.getIdTebakan());
        mParams.put(ServerConstants.mParamCoins, String.valueOf(sessionManager.getCoins()));
//        Log.d("IDBRow", sessionManager.getIdTebakan());
        CostumRequestString myReq = new CostumRequestString(Request.Method.POST, urlApi, mParams, AnswerTempTebakanActivity.this, AnswerTempTebakanActivity.this);
        myReq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myReq);
    }

    private String AlgoritmaHint(String JawabanTebakan){
        String tempJawaban = "";

        String[] arrayWords = JawabanTebakan.split(" ");

        for(int i =0;i<arrayWords.length;i++){
            String  firstLetter = String.valueOf(arrayWords[i].charAt(0));
            String endletter = String.valueOf(arrayWords[i].charAt(arrayWords[i].length()-1));
            for(int o=0;o<arrayWords[i].length();o++){
                if(o==0){
                    tempJawaban=tempJawaban+firstLetter + " ";
                }else if(o==arrayWords[i].length()-1){
                    tempJawaban=tempJawaban+endletter+"    ";
                }else{
                    tempJawaban=tempJawaban+"_"+" ";
                }
            }
        }
        return tempJawaban;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
//        Log.d("Error", error.getMessage());
        Toast.makeText(getApplicationContext(), "Wooww, Error Bray", Toast.LENGTH_LONG).show();
        if(retryConnect<3){
            if(cd.isConnectingToInternet()){
                /**
                 * if request is failed, then make reques again, and freeze the UI
                 */
                switch (requestServer){
                    case ApplicationConstants.loadGambar :
//                        Log.d("reques","load gambar");
                        requestApi(ServerConstants.ShowAlltebakanTemp);
                        break;
                   default:
                       break;
                }
            }else{
                Toast.makeText(AnswerTempTebakanActivity.this, "Please Check Your Connectiosn", Toast.LENGTH_SHORT).show();
            }
        }
        retryConnect = retryConnect+1;
    }



    @Override
    public void onResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            Log.d("response",response);
//            Log.d("jumlah",String.valueOf(jsonObject.getJSONArray(ServerConstants.dataListTebakan).length()));
            if(jsonObject.getString(ServerConstants.statusBeckend).equalsIgnoreCase(ServerConstants.statusBeckendOk)){
                switch (jsonObject.getInt(ServerConstants.resultType)){
                    case ServerConstants.dataFoundResult:
                        //algoritma to set point in local
                        setCoin(jsonObject.getInt(ServerConstants.point));
//                        sessionManager.setCoins(jsonObject.getInt(ServerConstants.point));
//                        this.menu.findItem(R.id.Coins).setTitle(String.valueOf(jsonObject.getInt(ServerConstants.point)));
                        ((AppCompatTextView)dialog.findViewById(R.id.coins)).setText(String.valueOf(jsonObject.getInt(ServerConstants.point)));
                        break;
                    case ServerConstants.showTebakanResult:
                        /**
                         * checking if the list has data, if it has remove them.
                         */
                        if(tebakanObjects!=null && tebakanObjects.size()>0){
                            tebakanObjects.clear();
                        }
                        /**
                         * saving all the array tebkan to the list
                         */
                        JSONArray jsonResult = jsonObject.getJSONArray(ServerConstants.dataListTebakan);
                        for(int i=0;i<jsonResult.length();i++){
                            TebakanObject tebakanObject = new TebakanObject();
                            tebakanObject.set_idTebakan(jsonResult.getJSONObject(i).getString(ServerConstants._idTebakan));
                            tebakanObject.setKunciTebakan(jsonResult.getJSONObject(i).getString(ServerConstants.mParamsKunciTebakan));
                            tebakanObject.setTextTebakan(jsonResult.getJSONObject(i).getString(ServerConstants.textTebakan));
                            tebakanObject.setUrlGambarTebakan(jsonResult.getJSONObject(i).getString(ServerConstants.gambarTebakan));
                            tebakanObject.set_idUser(jsonResult.getJSONObject(i).getString(ServerConstants._idUser));
                            tebakanObject.setGcmID(jsonResult.getJSONObject(i).getString(ServerConstants.gcmID));
                            tebakanObjects.add(tebakanObject);
                        }

                        /**
                         * set value for the UI
                         */

                        if(tebakanObjects.size()>0) {
                            setValueForTheUI(tebakanObjects.get(0));
                        }else{
                            Toast.makeText(AnswerTempTebakanActivity.this, "We Will add more the Content Soon", Toast.LENGTH_SHORT).show();
                        }
                        break;

                    case ServerConstants.loginResult :

                        sessionManager.setUidUser(jsonObject.getString(ServerConstants._idUser));
                        sessionManager.setToken(jsonObject.getString(ServerConstants.getTokenBeckend));
                        sessionManager.setGcmID(jsonObject.getString(ServerConstants.gcmID));
                        sessionManager.setUsername(jsonObject.getString(ServerConstants.username));
                        sessionManager.setIsGcmRegistered(true);
                        sessionManager.setAppVersion(ApplicationConstants.getAppVersion(this));
                        sessionManager.setHint(3);
                        sessionManager.setCoins(Integer.parseInt(jsonObject.getString(ServerConstants.point)));
                        break;

                    default:
                        break;
                }

            }else{
//                Log.d("Error", "Load Data Error");
                switch (jsonObject.getInt(ServerConstants.resultType)){
                    case ServerConstants.ErrorTokenExpired:
                        Toast.makeText(AnswerTempTebakanActivity.this, "Please Wait...", Toast.LENGTH_SHORT).show();
                        Map<String,String> mParams = new HashMap<String,String>();
                        mParams.put(ServerConstants.mParamsPassword, sessionManager.getPassword());
                        mParams.put(ServerConstants.mParamsEmail,sessionManager.getEmail());

                        CostumRequestString myReq = new CostumRequestString(Request.Method.POST,ServerConstants.loginTebakan,mParams,AnswerTempTebakanActivity.this,AnswerTempTebakanActivity.this);
                        myReq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        AppController.getInstance().addToRequestQueue(myReq);
                        break;
                    case ServerConstants.ErrorDataNotFound :
                        Toast.makeText(AnswerTempTebakanActivity.this, "Sorry We Couldn't find any data", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
