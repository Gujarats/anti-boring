package santana.tebaktebakan.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by AdrianEkaFikri on 11/1/2015.
 */
public class MainActivityAdapter extends RecyclerView.Adapter<MainActivityAdapter.ViewHolder> {

    private Context context;
    private Activity activity;
    private int layout;

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
    public void onBindViewHolder(ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 40;
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
//        public final AppCompatTextView level;
//        public final ImageView bintang1;
//        public final ImageView bintang2;
//        public final LinearLayoutCompat background;


        public ViewHolder(View view) {
            super(view);
            mView = view;
//            level = (AppCompatTextView) view.findViewById(R.id.txtLvlSedang);
//            bintang1 = (ImageView) view.findViewById(R.id.layoutBintangKecil);
//            bintang2 = (ImageView) view.findViewById(R.id.layoutBintangKecil);
//            background = (LinearLayoutCompat) view.findViewById(R.id.background);

        }

    }

}
