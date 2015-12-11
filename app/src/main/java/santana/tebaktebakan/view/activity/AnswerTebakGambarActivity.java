package santana.tebaktebakan.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import butterknife.Bind;
import butterknife.ButterKnife;
import santana.tebaktebakan.R;
import santana.tebaktebakan.common.ApplicationConstants;
import santana.tebaktebakan.controller.UIManager.LogicInterfaceManager;
import santana.tebaktebakan.controller.tebakanManager.Tebakan;


/**
 * Created by Gujarat Santana on 01/11/15.
 */
public class AnswerTebakGambarActivity extends AppCompatActivity {


    @Bind(R.id.btnBack) LinearLayout btnBack;
    @Bind(R.id.btnCek) ImageView btnCek;
    @Bind(R.id.btnHelp) ImageView btnHelp;
    @Bind(R.id.btnShare) ImageView btnShare;
    @Bind(R.id.TebakGambar) ImageView TebakGambar;
    @Bind(R.id.layoutAnim) LinearLayout layoutAnim;

    

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_answer_tebak_gambar);

        //initial Interface
        initUI();
        // initial for action button or widget
        initAction();
    }

    private void initUI() {
        ButterKnife.bind(this);

        // set size imageVIew for TebakGambar
        Tebakan.getInstance().setSizeImageView(this, TebakGambar);

        //set Effect on widget
        LogicInterfaceManager.getInstance().setOnClickEffect(this, btnBack);
        LogicInterfaceManager.getInstance().setOnClickEffect(this, btnCek);
        LogicInterfaceManager.getInstance().setOnClickEffect(this, btnHelp);
        LogicInterfaceManager.getInstance().setOnClickEffect(this, btnShare);

        Intent intent= getIntent();
        if(intent!=null){
            Tebakan.getInstance().loadImageToImageView2(TebakGambar, intent.getExtras().getString(ApplicationConstants.imageUrl),getApplicationContext());
        }

    }


    private void initAction() {
        LogicInterfaceManager.getInstance().backAction(this, btnBack);

        btnCek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YoYo.with(Techniques.Shake).playOn(findViewById(R.id.layoutAnim));
                setRotateAnimation(btnCek, 45);


            }
        });

    }

    public void setRotateAnimation(final ImageView imageView,int degree){
        RotateAnimation rotate = new RotateAnimation(0f, degree,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);

        imageView.setDrawingCacheEnabled(true);
        rotate.setDuration(300);
        rotate.setInterpolator(getApplicationContext(), android.R.anim.linear_interpolator);
        rotate.setFillBefore(true);
        rotate.setFillAfter(true);
        imageView.startAnimation(rotate);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                setRotateAnimation(imageView, -45);
            }
        }, 1500);

    }
}
