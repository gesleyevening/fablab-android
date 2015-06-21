package de.fau.cs.mad.fablab.android.model;

import android.os.Bundle;
import android.support.v4.app.Fragment;

/***
 * The main storage fragment which initializes all other storage parts
 */
public class StorageFragment extends Fragment {
    private NavigationDrawerStorage navigationDrawerStorage;
    private NewsStorage newsStorage;
    private ICalStorage iCalStorage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        newsStorage = new NewsStorage();
        iCalStorage = new ICalStorage();
        navigationDrawerStorage = new NavigationDrawerStorage();
    }

    public NewsStorage getNewsStorage(){
        return newsStorage;
    }

    public ICalStorage getICalStorage()
    {
        return iCalStorage;
    }

    public NavigationDrawerStorage getNavigationDrawerStorage() {
        return navigationDrawerStorage;
    }

}
