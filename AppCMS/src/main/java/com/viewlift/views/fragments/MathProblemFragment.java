package com.viewlift.views.fragments;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.binders.AppCMSBinder;
import com.viewlift.views.customviews.ViewCreator;

import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class MathProblemFragment extends Fragment {
    @Inject
    AppCMSPresenter appCMSPresenter;
    @BindView(R.id.mainLayout)
    ConstraintLayout mainLayout;
    @BindView(R.id.watchNow)
    Button watchNow;
    @BindView(R.id.mathProblem)
    TextView mathProblem;
    @BindView(R.id.continueMath)
    TextView continueMath;
    @BindView(R.id.mathHeader)
    TextView mathHeader;
    @BindView(R.id.errorMsg)
    TextView errorMsg;
    @BindView(R.id.result)
    EditText result;
    int problemResult = 0;
    private AppCMSBinder appCMSBinder;
    String[] addSub = new String[]{"Addition", "Subtraction"};
    Unbinder unbinder;
    public MathProblemFragment() {
        // Required empty public constructor
    }

    public static MathProblemFragment newInstance(Context context, AppCMSBinder appCMSBinder) {
        MathProblemFragment fragment = new MathProblemFragment();
        Bundle args = new Bundle();
        args.putBinder(context.getString(R.string.fragment_page_bundle_key), appCMSBinder);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_math_problem, container, false);
        unbinder= ButterKnife.bind(this, view);
        ((AppCMSApplication) getActivity().getApplicationContext()).getAppCMSPresenterComponent().inject(this);
        Bundle args = getArguments();
        try {
            appCMSBinder =
                    ((AppCMSBinder) args.getBinder(getContext().getString(R.string.fragment_page_bundle_key)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        ViewCreator.setTypeFace(view.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), mathHeader);
        ViewCreator.setTypeFace(view.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), watchNow);
        ViewCreator.setTypeFace(view.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), errorMsg);
        ViewCreator.setTypeFace(view.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), result);
        ViewCreator.setTypeFace(view.getContext(), appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), continueMath);
        mainLayout.setBackgroundColor(appCMSPresenter.getGeneralBackgroundColor());
        watchNow.setTextColor(appCMSPresenter.getGeneralBackgroundColor());
        watchNow.setBackgroundColor(Color.parseColor(appCMSPresenter.getAppCtaBackgroundColor()));
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        setMathProblem();
    }

    int[] randomNum = new int[2];

    private void setMathProblem() {
        int[] intArray = {0, 1};
        int idx = new Random().nextInt(intArray.length);

        Random random = new Random();
        for (int i = 0; i < 2; i++) {
            randomNum[i] = random.nextInt(100);
        }

        if (idx == 0) {
            if (randomNum[0] < randomNum[1])
                swap();
            mathProblem.setText(randomNum[0] + " - " + randomNum[1]);
            problemResult = randomNum[0] - randomNum[1];
        } else {
            mathProblem.setText(randomNum[0] + " + " + randomNum[1]);
            problemResult = randomNum[0] + randomNum[1];
        }
    }

    void swap() {
        randomNum[0] = randomNum[0] ^ randomNum[1];
        randomNum[1] = randomNum[0] ^ randomNum[1];
        randomNum[0] = randomNum[0] ^ randomNum[1];
    }

    @OnClick(R.id.watchNow)
    void watchClick() {
        if (TextUtils.isEmpty(result.getText().toString()))
            errorMsg.setVisibility(View.VISIBLE);
        else if (Integer.parseInt(result.getText().toString()) != problemResult)
            errorMsg.setVisibility(View.VISIBLE);
        else {
            errorMsg.setVisibility(View.GONE);
            appCMSPresenter.sendCloseOthersAction(null,
                    true,
                    false);
           /* appCMSPresenter.openVideoPlayerActivity(appCMSBinder.getLaunchData().getContentDatum(), appCMSBinder.getLaunchData().isTrailer(), appCMSBinder.getLaunchData().isVideoOffline(),
                    appCMSBinder.getLaunchData().getPagePath(), appCMSBinder.getLaunchData().getAction(), appCMSBinder.getLaunchData().getFilmTitle(), appCMSBinder.getLaunchData().getExtraData(), appCMSBinder.getLaunchData().isCloseLauncher(),
                    appCMSBinder.getLaunchData().getCurrentlyPlayingIndex(), appCMSBinder.getLaunchData().getRelateVideoIds(), appCMSBinder.getLaunchData().getActionType());*/
        }
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
