package santana.tebaktebakan.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

import santana.tebaktebakan.object.TebakanObject;

/**
 * Created by Gujarat Santana on 12/06/15.
 */
public class TebakanListAdapter extends BaseAdapter {

    private List<TebakanObject> tebakanObjects;

    public TebakanListAdapter(){
        tebakanObjects = new ArrayList<TebakanObject>();
    }
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
