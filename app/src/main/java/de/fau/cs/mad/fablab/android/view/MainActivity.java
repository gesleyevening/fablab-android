package de.fau.cs.mad.fablab.android.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.ButterKnife;
import dagger.ObjectGraph;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.actionbar.ActionBar;
import de.fau.cs.mad.fablab.android.eventbus.NavigationEvent;
import de.fau.cs.mad.fablab.android.model.StorageFragment;
import de.fau.cs.mad.fablab.android.model.dependencyinjection.ModelModule;
import de.fau.cs.mad.fablab.android.navdrawer.NavigationDrawer;
import de.fau.cs.mad.fablab.android.view.dependencyinjection.ActivityModule;
import de.fau.cs.mad.fablab.android.view.floatingbutton.FloatingFablabButton;
import de.fau.cs.mad.fablab.android.view.fragments.news.NewsFragment;
import de.greenrobot.event.EventBus;

public class MainActivity extends AppCompatActivity {

    private ObjectGraph mObjectGraph;

    FloatingFablabButton fablabButton;
    NavigationDrawer navigationDrawer;
    ActionBar actionBar;

    EventBus eventbus = EventBus.getDefault();

    public void inject(Object object) {
        mObjectGraph.inject(object);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        StorageFragment storageFragment = (StorageFragment) getSupportFragmentManager()
                .findFragmentByTag("storage");
        if (storageFragment == null) {
            storageFragment = new StorageFragment();
            getSupportFragmentManager().beginTransaction().add(storageFragment, "storage").commit();
        }

        mObjectGraph = ObjectGraph.create(new ActivityModule(this), new ModelModule(storageFragment));

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, new NewsFragment()).commit();
        }

        eventbus.register(this);
    }

    private void navigate(int itemId) {
        NewsFragment newsfragment = new NewsFragment();
        switch(itemId) {
            case R.id.drawer_item_news:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, newsfragment).commit();
                break;
            case R.id.drawer_item_productsearch:
                // Test ... einfach Fragment mal loeschen
                getSupportFragmentManager().beginTransaction().remove(getSupportFragmentManager().findFragmentById(R.id.fragment_container)).commit();
                break;
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        actionBar = new ActionBar(this, findViewById(android.R.id.content));
        fablabButton = new FloatingFablabButton(this, findViewById(android.R.id.content));
        navigationDrawer = new NavigationDrawer(this, findViewById(android.R.id.content));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    public void onEvent(NavigationEvent event) {
        navigate(event.getitemId());
    }
}
