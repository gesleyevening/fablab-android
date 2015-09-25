package de.fau.cs.mad.fablab.android.view.fragments.projects;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;

import javax.inject.Inject;

import butterknife.Bind;
import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.view.common.binding.ViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseDialogFragment;
import de.fau.cs.mad.fablab.android.viewmodel.common.Project;

public class SaveProjectDialogFragment extends BaseDialogFragment
        implements SaveProjectDialogFragmentViewModel.Listener{

    @Bind(R.id.save_project_save_local_button)
    Button mSaveLocalButton;
    @Bind(R.id.save_project_save_github_button)
    Button mSaveGithubButton;

    @Inject
    SaveProjectDialogFragmentViewModel mViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.fragment_save_project_dialog, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Project project = (Project) getArguments().getSerializable(getResources().getString(R.string.key_project));
        mViewModel.setProject(project);

        new ViewCommandBinding().bind(mSaveLocalButton, mViewModel.getSaveProjectLocallyCommand());

        mViewModel.setListener(this);
    }

    @Override
    public void onSaveProjectLocallyClicked() {
        dismiss();
    }
}