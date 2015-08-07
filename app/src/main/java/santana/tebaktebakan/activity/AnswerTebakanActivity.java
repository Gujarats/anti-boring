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
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

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
import santana.tebaktebakan.common.ServerConstants;
import santana.tebaktebakan.object.TebakanObject;
import santana.tebaktebakan.requestNetwork.CostumRequestString;
import santana.tebaktebakan.session.SessionManager;
import santana.tebaktebakan.storageMemory.SavingFile;

/**
 * Created by Gujarat Santana on 12/06/15.
 */
public class AnswerTebakanActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener {

    protected AppCompatEditText AnswerTebakan;
    protected AppCompatTextView TextTebakan;
    protected NetworkImageView gambarTebakan;
    protected ProgressBar Loading;
    protected AppCompatDialog dialog;
    protected Menu menu;
    protected LinearLayout layout2;

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
    private String _idTebakan, _idUser, textTebakan, gambarUrl, gcmID, kunciTebakan;
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
    private int hintShow =3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dismissHint =30000;
        setContentView(R.layout.layout_answer_tebakan_activity);
        sessionManager = new SessionManager(getApplicationContext());
        tebakanObject = new TebakanObject();
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
        Map<String,String> mParams = new HashMap<String,String>();
        mParams.put(ServerConstants.mParams_idUser,sessionManager.getUidUser());
        mParams.put(ServerConstants.mParamsToken,sessionManager.getToken());

        CostumRequestString myReq = new CostumRequestString(Request.Method.POST,ServerConstants.ShowAllTebakan,mParams,AnswerTebakanActivity.this,AnswerTebakanActivity.this);
        myReq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        AppController.getInstance().addToRequestQueue(myReq);

        initUi();

    }

    @Override
    public void onBackPressed() {
    }

    private void initUi() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);

        AnswerTebakan = (AppCompatEditText) findViewById(R.id.TextAnswer_Answer);
        TextTebakan = (AppCompatTextView) findViewById(R.id.TextTebakan_Answer);
        gambarTebakan = (NetworkImageView) findViewById(R.id.GambarTebakan_Answer);
        Loading = (ProgressBar) findViewById(R.id.Loading);
        layout2 = (LinearLayout)findViewById(R.id.layout2);

        dialog = new AppCompatDialog(AnswerTebakanActivity.this);

        /**
         * setVisibility of The UI
         */
        Loading.setVisibility(View.VISIBLE);
        TextTebakan.setVisibility(View.GONE);
        gambarTebakan.setVisibility(View.GONE);
        AnswerTebakan.setVisibility(View.GONE);
        layout2.setVisibility(View.GONE);


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
//        gambarTebakan.setImageUrl(gambarUrl, imageLoader);

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


        /**
         *  checking wheter id is registered on server or not
         the id which registered on the server saved in file with JsonObject format
         */

        try {
            savingFile = new SavingFile(getApplicationContext(), ApplicationConstants._listChatBrow, null);
            /**
             * readfromfile() method adalah untuk mengambil data json id tebakan yang sudah pernah dijawab
             * readfromfile2() method adalah untuk mengambil data json id tebakan yang gagal dijawab
             *
             * writetofile() method utk menyimpan id tebakan yang berhasil dijawab
             * writetofile()2 method utk menyimpan id tebakan yg gagal dijawab
             */

            /**
             * check idTebakan yang sudah terjawab
             */
            String idChats = savingFile.readFromFile();
            Log.i("isi", idChats);
            if (idChats != null && !idChats.equals("")) {
                valuesIdChats = new JSONObject(idChats);
                if (valuesIdChats.getBoolean(_idTebakan)) {
                    // it will catch the Json Exception if there is no data in json file
                    userAlreadyAnswer = true;
                    Log.d("idTebakan", "true");
                } else {
//                    user id registered to group
                    userAlreadyAnswer = false;
                    Log.d("idTebakan", "false");
                }
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            //user is not registered ro Group
            userAlreadyAnswer = false;
            Log.d("idTebakan", "false");

        }

        /**
         * check idTebakan yang gagal terjawab
         */

            try {
                String idChats2 = savingFile.readFromFile2();
                Log.i("isi2", idChats2);
                if (idChats2 != null && !idChats2.equals("")) {

                    JSONObject valuesTebakan = new JSONObject(idChats2);
                    if (valuesTebakan.getBoolean(_idTebakan)) {
                        // it will catch the Json Exception if there is no data in json file
                        userFailedAnswer = true;
                        Log.d("idTebakan2", "true");
                    } else {
                        //                    user id registered to group
                        userFailedAnswer = false;
                        Log.d("idTebakan2", "false");
                    }
                }
            }catch (NullPointerException ex){
                ex.printStackTrace();
            }
            catch (JSONException e) {
                e.printStackTrace();
                Log.d("idTebakan2", "false");
                userFailedAnswer = false;
            }


    }

    private void setValueForTheUI(TebakanObject tebakanObject){
        /**
         * set variabel
         */
        _idTebakan = tebakanObject.get_idTebakan();
        _idUser = tebakanObject.get_idUser();
        textTebakan = tebakanObject.getTextTebakan();
        kunciTebakan = tebakanObject.getKunciTebakan();
        gambarUrl = tebakanObject.getUrlGambarTebakan();
        gcmID = tebakanObject.getGcmID();

        Log.d("Gambar", _idTebakan);

        if(this.gambarTebakan!=null){
            this.gambarTebakan.destroyDrawingCache();
            this.gambarTebakan.setImageUrl(tebakanObject.getUrlGambarTebakan(),imageLoader);
            this.TextTebakan.setText(tebakanObject.getTextTebakan());
        }


        /**
         * setVisibility of The UI
         */
        this.Loading.setVisibility(View.GONE);
        layout2.setVisibility(View.VISIBLE);
        gambarTebakan.setVisibility(View.VISIBLE);
        TextTebakan.setVisibility(View.VISIBLE);
        AnswerTebakan.setVisibility(View.VISIBLE);


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.answer_activity, menu);
        this.menu=menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu1) {
        CountDown();
        return super.onPrepareOptionsMenu(menu1);

    }

    private void CountDown(){
        countDownTimer = new CountDownTimer(30000, 1000) {//CountDownTimer(edittext1.getText()+edittext2.getText()) also parse it to long

            public void onTick(long millisUntilFinished) {
                long count = millisUntilFinished/1000;
                menu.findItem(R.id.Count).setTitle(String.valueOf(count));
                dismissHint = millisUntilFinished;
                //here you can have your logic to set text to edittext
            }

            public void onFinish() {
//                mTextField.setText("done!");
                try {
                    //read data before saving
                    String data = savingFile.readFromFile2();
                    //conver string to json and save
                    JSONObject jsonObject = new JSONObject(data);
                    jsonObject.put(_idTebakan, true);
                    savingFile.writeToFile2(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                AnswerTebakanActivity.this.finish();
                dialog.setContentView(R.layout.dialog_out_of_time);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                AppCompatTextView text2 = (AppCompatTextView) dialog.findViewById(R.id.dialog_text);
                text2.setText(R.string.dialog_answer_answered);
                text2.setTextColor(getResources().getColor(R.color.primary));
                AppCompatButton button2 = (AppCompatButton) dialog.findViewById(R.id.dialog_try_again);
                button2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        /*
                        set visibily
                         */
                        Loading.setVisibility(View.VISIBLE);
                        TextTebakan.setVisibility(View.GONE);
                        gambarTebakan.setVisibility(View.GONE);
                        AnswerTebakan.setVisibility(View.GONE);
                        layout2.setVisibility(View.GONE);

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
                        dialog.dismiss();
                    }
                });

                ((AppCompatButton)dialog.findViewById(R.id.dialog_next)).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*
                        set visibiliy
                         */
                        Loading.setVisibility(View.VISIBLE);
                        TextTebakan.setVisibility(View.GONE);
                        gambarTebakan.setVisibility(View.GONE);
                        AnswerTebakan.setVisibility(View.GONE);
                        layout2.setVisibility(View.GONE);
                        /*
                        algoritma untuk next image
                         */
                        setValueForTheUI(NextTebakanObject());

                        /*
                        begin count down
                         */
                        if (countDownTimer != null)
                            countDownTimer.cancel();
                        CountDown();
                        dialog.dismiss();
                    }
                });
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);

            }
        }.start();
    }

    private void AnswerTebakan() {
        String answerTemp = AnswerTebakan.getText().toString();
        if (!answerTemp.trim().isEmpty()) {
            if (answerTemp.equalsIgnoreCase(kunciTebakan)) {
                //Answer is right give point and send messege to user who has tebakan
//                Toast.makeText(getApplicationContext(), "Yeaahhh You Got Points !!!", Toast.LENGTH_LONG).show();
                /***
                 * chekck wheter the id has answered or not from memory
                 */
                if(userAlreadyAnswer){
                    // user sudah menjawab tebakan ini
                    /**
                     * show dialog
                     */
                    dialog.setContentView(R.layout.dialog_correct_answer);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    AppCompatTextView text2 = (AppCompatTextView) dialog.findViewById(R.id.dialog_text);
                    text2.setText(R.string.dialog_answer_answered);
                    text2.setTextColor(getResources().getColor(R.color.primary));
                    AppCompatButton button2 = (AppCompatButton) dialog.findViewById(R.id.dialog_ok);
                    button2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            finish();
                        }
                    });
                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);

                }else{
                    //user belum menjawab tebakan ini

                    /**
                     * jika user tidak berhasil menjawab pertanyaan yg sebelumnya maka point tidak maksimal
                     */
                    if(userFailedAnswer){
                        /**
                         * request untuk penambahan point tidak maksimal
                         *
                         */
                        //TODO here point yang tidak masksimal
                        Log.d("tebakBrow","point menang kurang");
                        Map<String, String> mParams = new HashMap<String, String>();
                        mParams.put(ServerConstants.mParams_idUser, sessionManager.getUidUser());
                        mParams.put(ServerConstants.mParamsToken, sessionManager.getToken());
                        CostumRequestString myReq = new CostumRequestString(Request.Method.POST, ServerConstants.jawabanBenar, mParams, AnswerTebakanActivity.this, AnswerTebakanActivity.this);
                        myReq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        AppController.getInstance().addToRequestQueue(myReq);

                    }else{
                        /**
                         * request penambahan point untuk yang menjawab dengan benar dengan point maksimal.
                         */
                        Log.d("tebakBrow","point menang maksimal");
                        Map<String, String> mParams = new HashMap<String, String>();
                        mParams.put(ServerConstants.mParams_idUser, sessionManager.getUidUser());
                        mParams.put(ServerConstants.mParamsToken, sessionManager.getToken());
                        CostumRequestString myReq = new CostumRequestString(Request.Method.POST, ServerConstants.jawabanBenar, mParams, AnswerTebakanActivity.this, AnswerTebakanActivity.this);
                        myReq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                        AppController.getInstance().addToRequestQueue(myReq);
                    }



                    /**
                     * saving idTebakan to JsonAnswered
                     */

                    try {
                        //get data before saving
                        String data  = savingFile.readFromFile();
                        //convert to json and save
                        JSONObject jsonObject = new JSONObject(data);
                        jsonObject.put(_idTebakan, true);
                        savingFile.writeToFile(jsonObject.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    /**
                     * show dialog for winner
                     */

                    dialog.setContentView(R.layout.dialog_correct_answer);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    AppCompatTextView text = (AppCompatTextView) dialog.findViewById(R.id.dialog_text);
                    text.setText(R.string.dialog_answer_correct);
                    text.setTextColor(getResources().getColor(R.color.primary));
                    AppCompatButton button = (AppCompatButton) dialog.findViewById(R.id.dialog_ok);
                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            Loading.setVisibility(View.VISIBLE);
                            TextTebakan.setVisibility(View.GONE);
                            gambarTebakan.setVisibility(View.GONE);
                            AnswerTebakan.setVisibility(View.GONE);
                            layout2.setVisibility(View.GONE);
                            //algoritma untuk next image
                            setValueForTheUI(NextTebakanObject());

                            for(int i=0;i<tebakanObjects.size();i++){
                                Log.d("Gambar Next",tebakanObjects.get(i).get_idTebakan());
                            }
                            if(countDownTimer!=null)
                                countDownTimer.cancel();
                            CountDown();
                            dialog.dismiss();
                        }
                    });
                    /*
                    cancel timer before showing out of time dialog
                     */
                    countDownTimer.cancel();

                    dialog.show();
                    dialog.setCanceledOnTouchOutside(false);





                }
            } else {

                /**
                 * saving idTebakan to Json Failed Answered
                 */

                try {
                    //read data before saving
                    String data = savingFile.readFromFile2();
                    //conver string to json and save
                    JSONObject jsonObject = new JSONObject(data);
                    jsonObject.put(_idTebakan, true);
                    savingFile.writeToFile2(jsonObject.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
                        dialog.dismiss();
                    }
                });
                button.setTextColor(getResources().getColor(R.color.primary_red));
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);

                //TODO here give minus point
                /**
                 * minus point untuk user yg gagal menjawab pertanyaan
                 */


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
        }else{
            if(dismissHint<=4000){
                /**
                 * dialog hint tidak akan terbuka jika kurang dari 4 detik atau sama
                 */
            }else{
                String hint = AlgoritmaHint(tempTebakan);
                dialog.setContentView(R.layout.dialog_correct_answer);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                AppCompatTextView text = (AppCompatTextView) dialog.findViewById(R.id.dialog_text);
                text.setText(hint);
                text.setTextColor(getResources().getColor(R.color.primary));
                AppCompatButton button = (AppCompatButton) dialog.findViewById(R.id.dialog_ok);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                button.setTextColor(getResources().getColor(R.color.primary));
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);

                new CountDownTimer(3000, 1000) {//CountDownTimer(edittext1.getText()+edittext2.getText()) also parse it to long

                    public void onTick(long millisUntilFinished) {
                        long count = millisUntilFinished/1000;
//                menu.findItem(R.id.Count).setTitle(String.valueOf(count));
                        //here you can have your logic to set text to edittext
                    }

                    public void onFinish() {
                        dialog.dismiss();
                    }
                }.start();
            }
        }
        /**
         * count show hint dialog
         */
        hintShow=hintShow-1;
    }



    private TebakanObject NextTebakanObject(){
        tebakanPointer=tebakanPointer+1;
        if(tebakanPointer==10){
            tebakanPointer=0;
            Toast.makeText(getApplicationContext(),"10 Jawaban",Toast.LENGTH_LONG).show();
        }else{
            _idTebakan = tebakanObjects.get(tebakanPointer).get_idTebakan();
            _idUser = tebakanObjects.get(tebakanPointer).get_idUser();
            textTebakan = tebakanObjects.get(tebakanPointer).getTextTebakan();
            kunciTebakan = tebakanObjects.get(tebakanPointer).getKunciTebakan();
            gambarUrl = tebakanObjects.get(tebakanPointer).getUrlGambarTebakan();
            gcmID = tebakanObjects.get(tebakanPointer).getGcmID();
            tebakanObject= tebakanObjects.get(tebakanPointer);
        }

        return tebakanObject;
    }

    private String AlgoritmaHint(String JawabanTebakan){
        String tempJawaban = "";

        String[] arrayWords = JawabanTebakan.split(" ");

        for(int i =0;i<arrayWords.length;i++){
            Log.d("word",arrayWords[i]);
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
                Log.d("loopWord",tempJawaban);
            }
        }
        return tempJawaban;
    }

    @Override
    public void onErrorResponse(VolleyError error) {
//        Log.d("Error", error.getMessage());
        Toast.makeText(getApplicationContext(), "Wooww, Error Bray", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            Log.d("response",response);
            Log.d("jumlah",String.valueOf(jsonObject.getJSONArray(ServerConstants.dataListTebakan).length()));
            if(jsonObject.getString(ServerConstants.statusBeckend).equalsIgnoreCase(ServerConstants.statusBeckendOk)){
                switch (jsonObject.getInt(ServerConstants.resultType)){
                    case ServerConstants.showTebakanResult:
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

                        setValueForTheUI(tebakanObjects.get(3));

                        break;
                    case ServerConstants.addTebakanView :
                        /**
                         * saving all the array tebkan to the list
                         */
                        JSONArray jsonArray = jsonObject.getJSONArray(ServerConstants.dataListTebakan);
                        for(int i=0;i<jsonArray.length();i++){
                            TebakanObject tebakanObject = new TebakanObject();
                            tebakanObject.set_idUser(jsonArray.getJSONObject(i).getString(ServerConstants._idUser));
                            tebakanObject.setGcmID(jsonArray.getJSONObject(i).getString(ServerConstants.gcmID));
                            tebakanObject.set_idTebakan(jsonArray.getJSONObject(i).getString(ServerConstants._idTebakan));
                            tebakanObject.setKunciTebakan(jsonArray.getJSONObject(i).getString(ServerConstants.mParamsKunciTebakan));
                            tebakanObject.setTextTebakan(jsonArray.getJSONObject(i).getString(ServerConstants.textTebakan));
                            tebakanObject.setUrlGambarTebakan(jsonArray.getJSONObject(i).getString(ServerConstants.gambarTebakan));
                            tebakanObjects.add(tebakanObject);
                        }

                        /**
                         * set value for the UI
                         */

                        break;
                    case ServerConstants.addTebakanAtas :
                        JSONArray jsonArrayAtas = jsonObject.getJSONArray(ServerConstants.dataListTebakan);
                        List<TebakanObject> tempList = new ArrayList<TebakanObject>();
                        for(int i=0;i<jsonArrayAtas.length();i++){
                            TebakanObject tebakanObject = new TebakanObject();
                            tebakanObject.set_idUser(jsonArrayAtas.getJSONObject(i).getString(ServerConstants._idUser));
                            tebakanObject.setGcmID(jsonArrayAtas.getJSONObject(i).getString(ServerConstants.gcmID));
                            tebakanObject.set_idTebakan(jsonArrayAtas.getJSONObject(i).getString(ServerConstants._idTebakan));
                            tebakanObject.setTextTebakan(jsonArrayAtas.getJSONObject(i).getString(ServerConstants.textTebakan));
                            tebakanObject.setUrlGambarTebakan(jsonArrayAtas.getJSONObject(i).getString(ServerConstants.gambarTebakan));
                            tempList.add(tebakanObject);
//                            tebakanObjects.add(tebakanObject);
                        }

                        tempList.addAll(tebakanObjects);
                        tebakanObjects.clear();
                        tebakanObjects.addAll(tempList);
                        break;
                    case ServerConstants.addITebakanDown :
                        JSONArray jsonArrayBawah = jsonObject.getJSONArray(ServerConstants.dataListTebakan);
                        for(int i=0;i<jsonArrayBawah.length();i++){
                            TebakanObject tebakanObject = new TebakanObject();
                            tebakanObject.set_idTebakan(jsonArrayBawah.getJSONObject(i).getString(ServerConstants._idTebakan));
                            tebakanObject.setTextTebakan(jsonArrayBawah.getJSONObject(i).getString(ServerConstants.textTebakan));
                            tebakanObject.setUrlGambarTebakan(jsonArrayBawah.getJSONObject(i).getString(ServerConstants.gambarTebakan));
                            tebakanObject.set_idUser(jsonArrayBawah.getJSONObject(i).getString(ServerConstants._idUser));
                            tebakanObject.setGcmID(jsonArrayBawah.getJSONObject(i).getString(ServerConstants.gcmID));
                            tebakanObjects.add(tebakanObject);
                        }
                        break;
                    default:
                        break;
                }

            }else{
                Log.d("Error", "Load Data Error");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
