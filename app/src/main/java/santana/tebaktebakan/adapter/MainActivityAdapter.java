package santana.tebaktebakan.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import santana.tebaktebakan.R;
import santana.tebaktebakan.session.SessionLevel;

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
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layout, parent, false);
        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.txtLevel.setText(String.valueOf(position + 1));

    }

    @Override
    public int getItemCount() {
        return 40;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public AppCompatTextView txtLevel;
        public ImageView bintang1, bintang2;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            txtLevel = (AppCompatTextView) view.findViewById(R.id.txtLvl);
            bintang1 = (ImageView) view.findViewById(R.id.bintang1);
            bintang2 = (ImageView) view.findViewById(R.id.bintang2);

        }

    }


}
