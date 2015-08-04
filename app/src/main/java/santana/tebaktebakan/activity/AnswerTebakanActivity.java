package santana.tebaktebakan.activity;

import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
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

    private String _idTebakan, _idUser, textTebakan, gambarUrl, gcmID, kunciTebakan;
    private SessionManager sessionManager;
    private int WidthPhone;
    //image Loader
    private ImageLoader imageLoader = AppController.getInstance().getImageLoader();


    //di brow
    private JSONObject valuesIdChats;
    private boolean userIsExistinGroup = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

    private void initUi() {
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
         the id which registered on the server saved in file wit JsonObject format
         */

        try {
            SavingFile savingFile = new SavingFile(getApplicationContext(), ApplicationConstants._listChatBrow, null);
            String idChats = savingFile.readFromFile();
            Log.i("isi", idChats);
            if (idChats != null && !idChats.equals("")) {
                valuesIdChats = new JSONObject(idChats);
                if (valuesIdChats.getBoolean(_idTebakan)) {
                    // it will catch the Json Exception if there is no data in json file
                    userIsExistinGroup = true;
                    Log.d("idcaht", "true");
                } else {
//                    user id registered to group
                    userIsExistinGroup = false;
                    Log.d("idcaht", "false");
                }
            } else {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put(_idTebakan, true);
                savingFile.writeToFile(jsonObject.toString());
            }

        } catch (NullPointerException ex) {
            ex.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
            //user is not registered ro Group
            userIsExistinGroup = false;
            Log.d("idcaht", "false");
        }
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
                if(userIsExistinGroup){
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
                     * request penambahan point untuk yang menjawab dengan benar.
                     */
                    Map<String, String> mParams = new HashMap<String, String>();
                    mParams.put(ServerConstants.mParams_idUser, sessionManager.getUidUser());
                    mParams.put(ServerConstants.mParamsToken, sessionManager.getToken());
                    CostumRequestString myReq = new CostumRequestString(Request.Method.POST, ServerConstants.jawabanBenar, mParams, AnswerTebakanActivity.this, AnswerTebakanActivity.this);
                    myReq.setRetryPolicy(new DefaultRetryPolicy(5000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                    AppController.getInstance().addToRequestQueue(myReq);

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
                //Wrong answer give minus point if zero still zero
                Toast.makeText(getApplicationContext(), "Wrong Answer", Toast.LENGTH_LONG).show();
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
