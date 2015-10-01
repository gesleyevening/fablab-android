package de.fau.cs.mad.fablab.android.view.fragments.news;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.activities.MainActivity;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;

public class NewsDetailsDialogFragment extends BaseDialogFragment
        implements NewsDetailsDialogViewModel.Listener {
    @Bind(R.id.news_dialog_title)
    TextView title_tv;
    @Bind(R.id.news_dialog_webview)
    WebView webView;
    @Bind(R.id.news_dialog_image)
    ImageView image_iv;
    @Bind(R.id.news_dialog_link_tv)
    WebView link_tv;

    @Inject
    NewsDetailsDialogViewModel mViewModel;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.setListener(this);
        mViewModel.initialize(getArguments());

        new ViewCommandBinding().bind(image_iv, mViewModel.getImageClickCommand());

        title_tv.setText(mViewModel.getNews().getTitle());

        String stylesheet = "<link rel=\"stylesheet\" type=\"text/css\" href=\"news_dialog_stylesheet.css\" /> ";
        String htmlData = stylesheet + mViewModel.getNews().getDescription();

        webView.loadDataWithBaseURL("file:///android_asset/", htmlData, "text/html", "UTF-8", null);
        if (mViewModel.getNews().getLinkToPreviewImage() != null) {
            Picasso.with(image_iv.getContext()).load(mViewModel.getNews().getLinkToPreviewImage()).into(image_iv);
        } else {
            Picasso.with(image_iv.getContext()).load(R.drawable.news_nopicture).fit().into(image_iv);
        }

        String link = stylesheet +
                "<html><body class=\"link\"><a href=" +  mViewModel.getNews().getLink() + ">" + mViewModel.getNews().getLink() + "</a></body></html>";
        link_tv.loadDataWithBaseURL("file:///android_asset/", link, "text/html", "UTF-8", null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        return inflater.inflate(R.layout.fragment_news_dialog, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        setDisplayOptions(MainActivity.DISPLAY_LOGO | MainActivity.DISPLAY_TIME);
    }

    @Override
    public void onImageClicked() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(mViewModel.getNews().getLinkToPreviewImage()));
        getActivity().startActivity(intent);
    }
}
