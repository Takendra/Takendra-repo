package com.viewlift.tv.views.fragment;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.utility.Utils;
import com.viewlift.utils.CommonUtils;

import rx.functions.Action1;

/**
 * Created by anas.azeem on 9/13/2017.
 * Owned by ViewLift, NYC
 */

public class PreviewDialogFragment extends AbsDialogFragment {

    public static final String DIALOG_HEIGHT_KEY = "dialog_height_key";
    public static final String DIALOG_WIDTH_KEY = "dialog_width_key";
    public static final String DIALOG_TITLE_KEY = "dialog_title_key";
    public static final String DIALOG_MESSAGE_KEY = "dialog_message_key";
    public static final String DIALOG_TITLE_SIZE_KEY = "dialog_title_size_key";
    public static final String DIALOG_MESSAGE__SIZE_KEY = "dialog_message_size_key";
    public static final String DIALOG_TITLE_TEXT_COLOR_KEY = "dialog_title_text_color_key";
    public static final String DIALOG_MESSAGE_TEXT_COLOR_KEY = "dialog_message_text_color_key";
    public static final String DIALOG_POSITIVE_BUTTON_TEXT_KEY = "dialog_positive_button_text_key";
    public static final String DIALOG_NEGATIVE_BUTTON_TEXT_KEY = "dialog_negative_button_text_key";
    public static final String DIALOG_NEUTRAL_BUTTON_TEXT_KEY = "dialog_neutral_button_text_key";
    public static final String DIALOG_EXTRA_BUTTON_TEXT_KEY = "dialog_extra_button_text_key";
    public static final String DIALOG_TITLE_VISIBILITY_KEY = "dialog_title_visibility_key";
    public static final String DIALOG_MESSAGE_VISIBILITY_KEY = "dialog_message_visibility_key";
    public static final String DIALOG_POSITIVE_BUTTON_VISIBILITY_KEY
            = "dialog_positive_button_visibility_key";
    public static final String DIALOG_NEGATIVE_BUTTON_VISIBILITY_KEY
            = "dialog_negative_button_visibility_key";
    private Action1<String> onPositiveButtonClicked;
    private Action1<String> onNegativeButtonClicked;
    private Action1<String> onNeutralButtonClicked;
    private Action1<String> onExtraButtonClicked;
    private Action1<String> onBackKeyListener;

    private boolean isFocusOnPositiveButton,isFocusOnNegativeButton,isFocusOnNeutralButton;
    private Button positiveButton;
    private Button negativeButton;
    private Button neutralButton;
    private Button extraButton;
    private ImageView videoImage;


    public PreviewDialogFragment() {
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }

    public static PreviewDialogFragment newInstance(Bundle bundle) {
        PreviewDialogFragment fragment = new PreviewDialogFragment();
        fragment.setArguments(bundle);

        return fragment;
    }


    public void setOnPositiveButtonClicked (Action1<String> onPositiveButtonClicked){
        this.onPositiveButtonClicked = onPositiveButtonClicked;
    }

    public void setOnNegativeButtonClicked (Action1<String> onNegativeButtonClicked){
        this.onNegativeButtonClicked = onNegativeButtonClicked;
    }

    public void setOnNeutralButtonClicked (Action1<String> onNeutralButtonClicked){
        this.onNeutralButtonClicked = onNeutralButtonClicked;
    }

    public void setOnExtraButtonClicked (Action1<String> onExtraButtonClicked){
        this.onExtraButtonClicked = onExtraButtonClicked;
    }

    public void setOnBackKeyListener(Action1<String> onBackKeyListener){
       this.onBackKeyListener = onBackKeyListener;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View mView = inflater.inflate(R.layout.fragment_preview_dialog, container, false);

        AppCMSPresenter appCMSPresenter = ((AppCMSApplication) getActivity().getApplication())
                .getAppCMSPresenterComponent()
                .appCMSPresenter();

        String backGroundColor = appCMSPresenter.getAppBackgroundColor();
        mView.findViewById(R.id.fragment_clear_overlay).setBackgroundColor(Color.parseColor(backGroundColor));
        mView.findViewById(R.id.fragment_clear_overlay).setFocusable(false);
        /*Bind Views*/
        videoImage = (ImageView) mView.findViewById(R.id.video_image);
        negativeButton = (Button) mView.findViewById(R.id.btn_cancel);
        positiveButton = (Button) mView.findViewById(R.id.btn_yes);
        neutralButton = (Button) mView.findViewById(R.id.btn_neutral);
        extraButton = (Button) mView.findViewById(R.id.btn_extra);
        TextView tvTitle = (TextView) mView.findViewById(R.id.text_overlay_title);
        tvTitle.setFocusable(false);
        tvTitle.setEnabled(false);
        tvTitle.setClickable(false);
        tvTitle.setTextColor(Color.parseColor(appCMSPresenter.getAppTextColor()));
        TextView tvDescription = (TextView) mView.findViewById(R.id.text_overlay_description);
        TextView tvLoginHeader = (TextView) mView.findViewById(R.id.text_login);
        ScrollView scrollView = (ScrollView) mView.findViewById(R.id.scrollview);

        /*Request focus on the description */
        //positiveButton.requestFocus();
        Bundle arguments = getArguments();
        String title = arguments.getString(DIALOG_TITLE_KEY, null);
        String description = arguments.getString(DIALOG_MESSAGE_KEY, null);
        String textColor = arguments.getString(DIALOG_MESSAGE_TEXT_COLOR_KEY, null);
        String positiveBtnText = arguments.getString(DIALOG_POSITIVE_BUTTON_TEXT_KEY,null);
        String negativeBtnText = arguments.getString(DIALOG_NEGATIVE_BUTTON_TEXT_KEY, null);
        String neutralBtnText = arguments.getString(DIALOG_NEUTRAL_BUTTON_TEXT_KEY, null);
        String extraBtnText = arguments.getString(DIALOG_EXTRA_BUTTON_TEXT_KEY, null);
        float messageSize = arguments.getFloat(DIALOG_MESSAGE__SIZE_KEY);

        positiveButton.setText(positiveBtnText);
        negativeButton.setText(negativeBtnText);
        neutralButton.setText(neutralBtnText);
        extraButton.setText(extraBtnText);
        tvDescription.setTextSize(TypedValue.COMPLEX_UNIT_SP, messageSize);
        tvLoginHeader.setTextSize(TypedValue.COMPLEX_UNIT_SP, messageSize);

        if (description == null) {
            throw new RuntimeException("Description is null");
        }

        if (!TextUtils.isEmpty(title)) {
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }
        textColor = appCMSPresenter.getAppTextColor();
        String desc_text = getString(R.string.text_with_color,
                Integer.toHexString(Color.parseColor(textColor)).substring(2),
                description);

        /*if (!TextUtils.isEmpty(title)) {
            tvTitle.setText(title);
        }*/
        tvDescription.setText(Html.fromHtml(desc_text));
        tvLoginHeader.setText(appCMSPresenter.getLocalisedStrings().getHaveAccountText());

        Component componentTitle = new Component();
        componentTitle.setFontFamily(getString(R.string.app_cms_page_font_family_key));
        componentTitle.setFontWeight(getString(R.string.app_cms_page_font_semibold_key));
        tvTitle.setTypeface(Utils.getTypeFace(getActivity(), appCMSPresenter, componentTitle));

        Component component = new Component();
        component.setFontFamily(getString(R.string.app_cms_page_font_family_key));
        tvDescription.setTypeface(Utils.getTypeFace(getActivity(), appCMSPresenter, component));
        tvLoginHeader.setTypeface(Utils.getTypeFace(getActivity(), appCMSPresenter, component));
        //tvDescription.setTextSize(R.dimen.text_ovelay_dialog_desc_font_size);

        Component btnComponent1 = new Component();
        btnComponent1.setFontFamily(getString(R.string.app_cms_page_font_family_key));
        btnComponent1.setFontWeight(getString(R.string.app_cms_page_font_semibold_key));
        btnComponent1.setCornerRadius(4);
        btnComponent1.setBorderColor(CommonUtils.getColor(getActivity(), Integer.toHexString(ContextCompat.getColor(getActivity(),
                R.color.btn_color_with_opacity))));
        btnComponent1.setBorderWidth(4);

        neutralButton.setBackground(Utils.setButtonBackgroundSelector(getActivity(),
                Color.parseColor(Utils.getFocusColor(getActivity(), appCMSPresenter)),
                btnComponent1,
                appCMSPresenter));

        neutralButton.setTextColor(Utils.getButtonTextColorDrawable(
                CommonUtils.getColor(getActivity(), Integer.toHexString(ContextCompat.getColor(getActivity(),
                        R.color.btn_color_with_opacity))),
                CommonUtils.getColor(getActivity(), Integer.toHexString(ContextCompat.getColor(getActivity(),
                        android.R.color.white))),appCMSPresenter
        ));


        neutralButton.setTypeface(Utils.getTypeFace(getActivity(), appCMSPresenter, btnComponent1));

        negativeButton.setBackground(Utils.setButtonBackgroundSelector(getActivity(),
                Color.parseColor(Utils.getFocusColor(getActivity(), appCMSPresenter)),
                btnComponent1,
                appCMSPresenter));

        negativeButton.setTextColor(Utils.getButtonTextColorDrawable(
                CommonUtils.getColor(getActivity(), Integer.toHexString(ContextCompat.getColor(getActivity(),
                        R.color.btn_color_with_opacity))),
                CommonUtils.getColor(getActivity(), Integer.toHexString(ContextCompat.getColor(getActivity(),
                        android.R.color.white))),appCMSPresenter
        ));


        negativeButton.setTypeface(Utils.getTypeFace(getActivity(), appCMSPresenter, btnComponent1));

        positiveButton.setBackground(Utils.setButtonBackgroundSelector(getActivity(),
                Color.parseColor(Utils.getFocusColor(getActivity(), appCMSPresenter)),
                btnComponent1,
                appCMSPresenter));

        positiveButton.setTextColor(Utils.getButtonTextColorDrawable(
                CommonUtils.getColor(getActivity(), Integer.toHexString(ContextCompat.getColor(getActivity(),
                        R.color.btn_color_with_opacity))),
                CommonUtils.getColor(getActivity(), Integer.toHexString(ContextCompat.getColor(getActivity(),
                        android.R.color.white))),appCMSPresenter
        ));


        positiveButton.setTypeface(Utils.getTypeFace(getActivity(), appCMSPresenter, btnComponent1));

        extraButton.setBackground(Utils.setButtonBackgroundSelector(getActivity(),
                Color.parseColor(Utils.getFocusColor(getActivity(), appCMSPresenter)),
                btnComponent1,
                appCMSPresenter));

        extraButton.setTextColor(Utils.getButtonTextColorDrawable(
                CommonUtils.getColor(getActivity(), Integer.toHexString(ContextCompat.getColor(getActivity(),
                        R.color.btn_color_with_opacity))),
                CommonUtils.getColor(getActivity(), Integer.toHexString(ContextCompat.getColor(getActivity(),
                        android.R.color.white))),appCMSPresenter
        ));

        extraButton.setTypeface(Utils.getTypeFace(getActivity(), appCMSPresenter, btnComponent1));

        //positiveButton.requestFocus();

        if(TextUtils.isEmpty(positiveBtnText)){
            positiveButton.setVisibility(View.GONE);
            tvLoginHeader.setVisibility(View.GONE);
        }

        if(TextUtils.isEmpty(negativeBtnText)){
            negativeButton.setVisibility(View.GONE);
        }

        if(TextUtils.isEmpty(neutralBtnText)){
            neutralButton.setVisibility(View.GONE);
        }

        if(TextUtils.isEmpty(extraBtnText)){
            extraButton.setVisibility(View.GONE);
        }
        /*Set click listener*/
        negativeButton.setOnClickListener(v -> {
            if(null != onNegativeButtonClicked) {
                onNegativeButtonClicked.call("");
            }
            dismiss();
        });


        positiveButton.setOnClickListener(v -> {
            if(null != onPositiveButtonClicked) {
                onPositiveButtonClicked.call("");
            }
            dismiss();
        });

        neutralButton.setOnClickListener(v -> {
            if(null != onNeutralButtonClicked) {
                onNeutralButtonClicked.call("");
            }
            dismiss();
        });

        extraButton.setOnClickListener(v -> {
            if(null != onExtraButtonClicked) {
                onExtraButtonClicked.call("");
            }
            dismiss();
        });

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == KeyEvent.ACTION_DOWN){
                    if(null != onBackKeyListener)
                        onBackKeyListener.call("");
                }
                return false;
            }
        });

        loadVideoImage(videoImage,appCMSPresenter);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Bundle bundle = new Bundle();
        /*int width = getArguments().getInt(DIALOG_WIDTH_KEY);
        int height = getArguments().getInt(DIALOG_HEIGHT_KEY);
        bundle.putInt(getString(R.string.tv_dialog_width_key), width);
        bundle.putInt(getString(R.string.tv_dialog_height_key), height);*/

        super.onActivityCreated(bundle);
    }


    @Override
    public void onPause() {
        super.onPause();
        isFocusOnPositiveButton = false;
        isFocusOnNegativeButton = false;
        isFocusOnNeutralButton = false;

        if (positiveButton != null && positiveButton.hasFocus()){
            isFocusOnPositiveButton = true;
        }else if (negativeButton != null && negativeButton.hasFocus()){
            isFocusOnNegativeButton = true;
        } else if (neutralButton != null && neutralButton.hasFocus()){
            isFocusOnNeutralButton = true;
        }
        Log.d("ANSA onPause" , "isFocusOnPositiveButton = "+isFocusOnPositiveButton);
    }

    @Override
    public void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> {
            if (isVisible() && isAdded()) {
                Log.d("ANSA onResume" , "isFocusOnPositiveButton = "+isFocusOnPositiveButton);
                /*if (isFocusOnPositiveButton) {
                    positiveButton.requestFocus();
                } else if(isFocusOnNegativeButton) {
                    negativeButton.requestFocus();
                }else if(isFocusOnNeutralButton) {
                    neutralButton.requestFocus();
                }*/
            }
        }, 100);
    }

    private void loadVideoImage(ImageView imageView,AppCMSPresenter appCMSPresenter){
       // moduleAPI.getContentData().get(moduleAPI.getItemPosition()).getGist().getVideoImageUrl()
        String videoImageUrl = "";
        if(appCMSPresenter.getModuleApi() != null &&
                appCMSPresenter.getModuleApi().getContentData() != null &&
                appCMSPresenter.getModuleApi().getContentData().size() >0 &&
                appCMSPresenter.getModuleApi().getContentData().get(0) != null &&
                appCMSPresenter.getModuleApi().getContentData().get(0).getGist() != null &&
                appCMSPresenter.getModuleApi().getContentData().get(0).getGist().getVideoImageUrl() != null){
            videoImageUrl = appCMSPresenter.getModuleApi().getContentData().get(0).getGist().getVideoImageUrl();
        }
        /*if(CommonUtils.contentDatum != null){
            videoImageUrl = CommonUtils.contentDatum.getGist().getVideoImageUrl();
        }*/
         Glide.with(getContext())
                .load(videoImageUrl)
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                        .error(ContextCompat.getDrawable(getContext(), R.drawable.video_image_placeholder))
                        .placeholder(ContextCompat.getDrawable(getContext(), R.drawable.video_image_placeholder)))
                .into(imageView);
    }
}
