package de.fau.cs.mad.fablab.android.model;

import com.j256.ormlite.dao.RuntimeExceptionDao;

import java.util.List;

import de.fau.cs.mad.fablab.android.view.fragments.productsearch.NoProductsFoundEvent;
import de.fau.cs.mad.fablab.android.view.fragments.productsearch.ProductSearchRetrofitErrorEvent;
import de.fau.cs.mad.fablab.android.viewmodel.common.ObservableArrayList;
import de.fau.cs.mad.fablab.rest.core.Product;
import de.fau.cs.mad.fablab.rest.myapi.ProductApi;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import de.greenrobot.event.EventBus;

public class ProductModel {
    private RuntimeExceptionDao<Product, String> mProductDao;
    private ProductApi mProductApi;

    private ObservableArrayList<Product> mProducts;

    public ProductModel(RuntimeExceptionDao<Product, String> productDao, ProductApi productApi) {
        mProductDao = productDao;
        mProductApi = productApi;
        mProducts = new ObservableArrayList<>();
    }

    Callback<List<Product>> productSearchCallback = new Callback<List<Product>>() {
        @Override
        public void success(List<Product> products, Response response) {
            if(products.isEmpty()) {
                EventBus.getDefault().post(new NoProductsFoundEvent());
            }
            mProducts.addAll(products);
        }

        @Override
        public void failure(RetrofitError error) {
            EventBus.getDefault().post(new ProductSearchRetrofitErrorEvent());
        }
    };

    public void searchForProduct(String searchString){
        mProducts.clear();
        mProductApi.findByName(searchString, 0, 0, productSearchCallback);
    }

    public ObservableArrayList<Product> getProducts() {
        return mProducts;
    }

    public Product findProductById(String id) {
        for (Product product : mProducts) {
            if (product.getProductId().equals(id)) {
                return product;
            }
        }

        return mProductApi.findByIdSynchronously(id);
    }

    public void persist(Product product) {
        mProductDao.createOrUpdate(product);
    }
}