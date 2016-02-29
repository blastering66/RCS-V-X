package id.tech.verificareolx;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * Created by macbook on 2/16/16.
 */
public class Olx_Activity_History_TabSlider extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_tabslider);

        ActionBar ac = getSupportActionBar();
        ac.setDisplayHomeAsUpEnabled(true);
        ac.setTitle("History");

        FragmentTransaction t = getSupportFragmentManager().beginTransaction();
//		TestSlidingFragment fragment = new TestSlidingFragment();
        Olx_SlidingTabsFragment fragment = new Olx_SlidingTabsFragment();
        t.replace(R.id.content_fragment	, fragment);
        t.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
