package santana.tebaktebakan.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import santana.tebaktebakan.R;
import santana.tebaktebakan.session.SessionLevel;
import view.CostumFont;

/**
 * Created by AdrianEkaFikri on 11/1/2015.
 */
public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {

    public static int lvl;
    private Context context;
    private Activity activity;
    private int layout;
    private SessionLevel sessionLevel;

    public MainActivityAdapter(Context context, Activity activity, int layout) {
        this.context = context;
        this.activity = activity;
        this.layout = layout;
        sessionLevel = new SessionLevel(context);
        sessionLevel.setLevel(0);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        if(position==0){
            sessionLevel.setLevel(0);
        }
        lvl = sessionLevel.getLevel();
        System.out.println("LVL " + lvl);

        holder.txtLevel.setText(lvl);

        sessionLevel.setLevel(lvl);

    }

    @Override
    public int getItemCount() {
        return 40;
    }

    private int increment() {
        lvl = lvl + 1;
        return lvl;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public view.CostumFont txtLevel;
        public ImageView bintang1, bintang2;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtLevel = (CostumFont) view.findViewById(R.id.txtLvl);
            bintang1 = (ImageView) view.findViewById(R.id.bintang1);
            bintang2 = (ImageView) view.findViewById(R.id.bintang2);

        }

    }


}
