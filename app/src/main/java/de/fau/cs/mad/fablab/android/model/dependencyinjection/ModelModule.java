package de.fau.cs.mad.fablab.android.model.dependencyinjection;

import dagger.Module;
import dagger.Provides;
import de.fau.cs.mad.fablab.android.actionbar.ActionBar;
import de.fau.cs.mad.fablab.android.model.CartModel;
import de.fau.cs.mad.fablab.android.model.ICalModel;
import de.fau.cs.mad.fablab.android.model.NewsModel;
import de.fau.cs.mad.fablab.android.model.ProductModel;
import de.fau.cs.mad.fablab.android.model.StorageFragment;
import de.fau.cs.mad.fablab.android.view.floatingbutton.FloatingFablabButton;
import de.fau.cs.mad.fablab.android.view.fragments.ICalAndNewsFragment;
import de.fau.cs.mad.fablab.android.view.fragments.barcodescanner.BarcodeScannerFragment;
import de.fau.cs.mad.fablab.android.view.fragments.cart.AddToCartDialogFragment;
import de.fau.cs.mad.fablab.android.view.fragments.cart.CartSlidingUpPanel;
import de.fau.cs.mad.fablab.android.view.fragments.checkout.CheckoutFragment;
import de.fau.cs.mad.fablab.android.view.fragments.checkout.QrCodeScannerFragment;
import de.fau.cs.mad.fablab.android.view.fragments.icals.ICalDetailsDialogFragment;
import de.fau.cs.mad.fablab.android.view.fragments.icals.ICalFragment;
import de.fau.cs.mad.fablab.android.view.fragments.news.NewsDetailsDialogFragment;
import de.fau.cs.mad.fablab.android.view.fragments.news.NewsFragment;
import de.fau.cs.mad.fablab.android.view.fragments.productsearch.ProductSearchFragment;
import de.fau.cs.mad.fablab.android.view.navdrawer.NavigationDrawer;

@SuppressWarnings("unused")
@Module(
        injects = {
                ActionBar.class, AddToCartDialogFragment.class, BarcodeScannerFragment.class,
                CartSlidingUpPanel.class, CheckoutFragment.class, FloatingFablabButton.class,
                ICalAndNewsFragment.class, ICalDetailsDialogFragment.class, ICalFragment.class,
                NavigationDrawer.class, NewsDetailsDialogFragment.class, NewsFragment.class,
                ProductSearchFragment.class, QrCodeScannerFragment.class
        })
public class ModelModule {
    private final StorageFragment mStorageFragment;

    public ModelModule(StorageFragment storageFragment) {
        mStorageFragment = storageFragment;
    }

    @Provides
    NewsModel provideNewsModel() {
        return mStorageFragment.getNewsModel();
    }

    @Provides
    ICalModel provideICalModel() {
        return mStorageFragment.getICalModel();
    }

    @Provides
    CartModel provideCartModel() {
        return mStorageFragment.getCartModel();
    }

    @Provides
    ProductModel provideProductModel() {
        return mStorageFragment.getProductModel();
    }
}

