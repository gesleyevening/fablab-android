package de.fau.cs.mad.fablab.android;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import de.fau.cs.mad.fablab.android.cart.Cart;
import de.fau.cs.mad.fablab.android.cart.CartActivity;
import de.fau.cs.mad.fablab.android.cart.CheckoutActivity;
import de.fau.cs.mad.fablab.android.navdrawer.AppbarDrawerInclude;
import de.fau.cs.mad.fablab.android.productsearch.AutoCompleteHelper;
import de.fau.cs.mad.fablab.android.productsearch.ProductSearchActivity;
import de.fau.cs.mad.fablab.android.ui.NewsActivity;

public class MainActivity extends ActionBarActivity {
    // Navigation Drawer
    private AppbarDrawerInclude appbarDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appbarDrawer = AppbarDrawerInclude.getInstance(this);
        appbarDrawer.create();


        //Load Autocompleteionwords
        AutoCompleteHelper.getInstance().loadProductNames(this);


        // init db and cart - always do this on app start
        Cart.MYCART.init(getApplication());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        appbarDrawer.createMenu(menu);
        appbarDrawer.startTimer();
        return true;
    }

    @Override
    public void onPause() {
        super.onPause();
        appbarDrawer.stopTimer();
    }

    @Override
    public void onResume() {
        super.onResume();
       appbarDrawer.startTimer();

        //Load Autocompleteionwords
        AutoCompleteHelper.getInstance().loadProductNames(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_opened) {
            appbarDrawer.updateOpenState(item);
            Toast appbar_opened_toast = Toast.makeText(this, appbarDrawer.openedMessage, Toast.LENGTH_SHORT);
            appbar_opened_toast.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void startBarcodeScanner(View view) {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Intent intent = new Intent(this, BarcodeScannerActivity.class);
            startActivity(intent);
        }
    }

    public void showBasket(View view){
        Intent intent = new Intent(this, CartActivity.class);
        startActivity(intent);
    }

    public void startProductSearch(View view) {
        Intent intent = new Intent(this, ProductSearchActivity.class);
        startActivity(intent);
    }

    public void startNewsActivity(View view)
    {
        Intent intent = new Intent(this, NewsActivity.class);
        startActivity(intent);
    }

    public void startCheckout(View view) {
        Intent intent = new Intent(this, CheckoutActivity.class);
        startActivity(intent);
    }
}
