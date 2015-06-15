package santana.tebaktebakan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import santana.tebaktebakan.R;
import santana.tebaktebakan.adapter.TebakanListAdapter;
import santana.tebaktebakan.session.SessionManager;

/**
 * Created by Gujarat Santana on 12/06/15.
 */
public class TebakanListActivity extends AppCompatActivity {
    SessionManager sessionManager;

    //listview and adapter
    private ListView listView;
    private TebakanListAdapter tebakanListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_tebakan_list_activity);
        sessionManager = new SessionManager(getApplicationContext());
        initUI();
    }

    /**
     * before ini the ui check whether user is registered or loggon
     */
    private void initUI(){
        if(sessionManager.getUidUser().isEmpty() || sessionManager.getToken().isEmpty()){
            sessionManager.clearAllSession();
            Intent intent = new Intent(this,RegisterActivity.class);
            finish();
            startActivity(intent);

        }

        tebakanListAdapter = new TebakanListAdapter(getApplicationContext(),this,R.layout.item_list_tebakan);
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
