package de.fau.cs.mad.fablab.android.view.fragments.news;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.pedrogomez.renderers.Renderer;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;

public class NewsViewModelRenderer extends Renderer<NewsViewModel> {
    @Bind(R.id.title_news_entry)
    TextView title_tv;
    @Bind(R.id.text_news_entry)
    TextView text_tv;
    @Bind(R.id.icon_news_entry)
    ImageView icon_iv;

    @Override
    protected void setUpView(View view) {
        ButterKnife.bind(this, view);
    }

    @Override
    protected void hookListeners(View view) {

    }

    @Override
    protected View inflate(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        return layoutInflater.inflate(R.layout.news_entry, viewGroup, false);
    }

    @Override
    public void render() {
        NewsViewModel viewModel = getContent();

        new ViewCommandBinding().bind(getRootView(), viewModel.getShowDialogCommand());

        title_tv.setText(viewModel.getTitle());
        text_tv.setText(viewModel.getDescriptionShort());

        if (viewModel.getLinkToPreviewImage() != null) {
            Picasso.with(icon_iv.getContext()).load(viewModel.getLinkToPreviewImage()).fit()
                    .centerCrop().into(icon_iv);
        } else {
            Picasso.with(icon_iv.getContext()).load(R.drawable.news_nopicture).fit().into(icon_iv);
        }
    }
}
