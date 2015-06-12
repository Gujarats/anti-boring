package santana.tebaktebakan.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import santana.tebaktebakan.R;
import santana.tebaktebakan.adapter.TebakanListAdapter;

/**
 * Created by Gujarat Santana on 12/06/15.
 */
public class TebakanListActivity extends AppCompatActivity {

    private ListView listView;
    private TebakanListAdapter tebakanListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tebakan_list_layout_activity);
        initUI();
    }

    private void initUI(){
        tebakanListAdapter = new TebakanListAdapter();
        listView = (ListView)findViewById(R.id.ListTebakan);
        listView.setAdapter(tebakanListAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
