package santana.tebaktebakan.utilityUI;

/**
 * Created by Gujarat Santana on 30/06/15.
 */

import android.support.v7.widget.RecyclerView;

public abstract class RecyclerScrollListener extends RecyclerView.OnScrollListener {
    private static final float MINIMUM = 1f;
    private int scrollDist = 0;
    private boolean isVisible = true;

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);

        if(isVisible && scrollDist > MINIMUM) {
            hide();
            scrollDist = 0;
            isVisible = false;
        } else if(!isVisible && scrollDist < -MINIMUM) {
            show();
            scrollDist = 0;
            isVisible = true;
        }

        if((isVisible && dy > 0) || (!isVisible && dy < 0)) {
            scrollDist += dy;
        }
    }

    public abstract void show();
    public abstract void hide();
}

