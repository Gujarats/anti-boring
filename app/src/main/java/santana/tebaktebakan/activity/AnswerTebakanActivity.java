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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import santana.tebaktebakan.AppController;
import santana.tebaktebakan.R;
import santana.tebaktebakan.common.ApplicationConstants;
import santana.tebaktebakan.common.ServerConstants;
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
    protected AppCompatDialog dialog;
    protected Menu menu;
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

    public static int countWords(String s){

        int wordCount = 0;

        boolean word = false;
        int endOfLine = s.length() - 1;

        for (int i = 0; i < s.length(); i++) {
            // if the char is a letter, word = true.
            if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
                word = true;
                // if char isn't a letter and there have been letters before,
                // counter goes up.
            } else if (!Character.isLetter(s.charAt(i)) && word) {
                wordCount++;
                word = false;
                // last word of String; if it doesn't end with a non letter, it
                // wouldn't count without this.
            } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
                wordCount++;
            }
        }
        return wordCount;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dismissHint =30000;
        setContentView(R.layout.layout_answer_tebakan_activity);
        sessionManager = new SessionManager(getApplicationContext());
        /***
         * get value from intent
         */
        Bundle intent = getIntent().getExtras();
        if (intent.getString(ApplicationConstants.FromActivity).equals(ApplicationConstants.TebakanListActivity)) {
            _idTebakan = intent.getString(ApplicationConstants._idTebakan);
            _idUser = intent.getString(ApplicationConstants._idUserTebakan);
            textTebakan = intent.getString(ApplicationConstants.textTebakan);
            kunciTebakan = intent.getString(ApplicationConstants.kunciTebakan);
            gambarUrl = intent.getString(ApplicationConstants.imageUrl);
            gcmID = intent.getString(ApplicationConstants.gcmID);

            Log.d("tebakan : ", _idTebakan);
        }
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
        dialog = new AppCompatDialog(AnswerTebakanActivity.this);

        //set value
        TextTebakan.setText(textTebakan);
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
        gambarTebakan.setImageUrl(gambarUrl, imageLoader);

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.answer_activity, menu);
        this.menu=menu;
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu1) {
        new CountDownTimer(30000, 1000) {//CountDownTimer(edittext1.getText()+edittext2.getText()) also parse it to long

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
                AnswerTebakanActivity.this.finish();

            }
        }.start();
        return super.onPrepareOptionsMenu(menu1);

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
                            dialog.dismiss();
                            finish();
                        }
                    });
                    dialog.show();

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
                 * dialog hint tidak akan terbuka jia kurang dari 4 detik atau sama
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

                new CountDownTimer(4000, 1000) {//CountDownTimer(edittext1.getText()+edittext2.getText()) also parse it to long

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
        hintShow=hintShow-1;
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
        Toast.makeText(getApplicationContext(), "Wooww, Error man", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onResponse(String response) {
        Log.d("response", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString(ServerConstants.statusBeckend).equalsIgnoreCase(ServerConstants.statusBeckendOk)) {

            } else {
                switch (jsonObject.getInt(ServerConstants.resultType)) {
                    case ServerConstants.ErrorEmptyParams:
                        Toast.makeText(getApplicationContext(), "Oops Sorry, Somethings Wrong", Toast.LENGTH_LONG).show();
                        break;
                    case ServerConstants.ErrorDataNotFound:
                        Toast.makeText(getApplicationContext(), "Oops Sorry, Somethings Wrong", Toast.LENGTH_LONG).show();
                        break;
                    case ServerConstants.ErrorQuery:
                        Toast.makeText(getApplicationContext(), "Oops Sorry, Somethings Wrong", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
