package com.viewlift.views.dialog;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.adapters.ListSelectionDialogAdapter;
import com.viewlift.views.customviews.ViewCreator;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.functions.Action1;

public class ListSelectionDialog extends DialogFragment {
    @Inject
    AppCMSPresenter appCMSPresenter;

    @BindView(R.id.dialogTitle)
    TextView dialogTitle;
    @BindView(R.id.listSlection)
    RecyclerView listSlectionView;


    Action1<String> selectedValue;
    ListSelectionDialogAdapter adapter = new ListSelectionDialogAdapter();

    public ListSelectionDialog() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_list_selection, container);
        ButterKnife.bind(this, view);
        ((AppCMSApplication) this.getActivity().getApplication()).getAppCMSPresenterComponent().inject(this);
        Component component = new Component();
        ViewCreator.setTypeFace(getActivity(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, dialogTitle);
        dialogTitle.setText(getArguments().getString("title"));
        listSlectionView.setLayoutManager(new LinearLayoutManager(getActivity(),
                LinearLayoutManager.VERTICAL,
                false));
        listSlectionView.setAdapter(adapter);
        return view;
    }

    public void setSelectedValue(Action1<String> selectedValue) {
        this.selectedValue = selectedValue;
        adapter.setData(getArguments().getStringArrayList("content"), selectedValue);
    }

}
