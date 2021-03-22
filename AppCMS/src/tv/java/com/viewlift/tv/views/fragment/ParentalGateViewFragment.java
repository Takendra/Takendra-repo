package com.viewlift.tv.views.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.viewlift.AppCMSApplication;
import com.viewlift.R;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.utility.Utils;

import java.util.Random;

public class ParentalGateViewFragment extends DialogFragment {

    private static ParentalGateViewFragment fragment;
    private AppCMSPresenter appCMSPresenter;
    private String TAG = ParentalGateViewFragment.class.getSimpleName();
    private Animals random;
    private ParentalGateViewInteractionListener parentalGateViewInteractionListener;
    private Context mContext;

    public ParentalGateViewFragment() {
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }

    public static ParentalGateViewFragment getInstance() {
        if (fragment == null) {
            fragment = new ParentalGateViewFragment();
        }
        return fragment;
    }


    @Override
    public void onResume() {
        super.onResume();
        appCMSPresenter.stopLoader();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.parental_gate_view, container, false);

        appCMSPresenter = ((AppCMSApplication) getActivity().getApplication())
                .getAppCMSPresenterComponent().appCMSPresenter();

        TextView messageTextView = view.findViewById(R.id.app_cms_parental_gate_message_view);
        messageTextView.setText(appCMSPresenter.getLocalisedStrings().getGrabGrownUpMessage());
        TextView subtextView = view.findViewById(R.id.app_cms_parental_gate_subtext_view);

        subtextView.setTypeface(Utils.getSpecificTypeface(mContext, appCMSPresenter, mContext.getString(R.string.app_cms_page_font_regular_key)));
        messageTextView.setTypeface(Utils.getSpecificTypeface(mContext, appCMSPresenter, mContext.getString(R.string.app_cms_page_font_regular_key)));

        ImageView butterflyImage = view.findViewById(R.id.app_cms_parental_gate_image_1);
        butterflyImage.setTag(Animals.BUTTERFLY);
        butterflyImage.setOnKeyListener(keyListener);

        ImageView ladybugImage = view.findViewById(R.id.app_cms_parental_gate_image_2);
        ladybugImage.setTag(Animals.LADYBUG);
        ladybugImage.setOnKeyListener(keyListener);

        ImageView owlImage = view.findViewById(R.id.app_cms_parental_gate_image_3);
        owlImage.setTag(Animals.OWL);
        owlImage.setOnKeyListener(keyListener);

        random = r.random();
        subtextView.setText(appCMSPresenter.getLocalisedStrings().getPressHoldContinue() + " " + random.name());
        return view;
    }

    private View.OnKeyListener keyListener = (v, keyCode, event) -> {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER && event.isLongPress()) {
            Animals tag = (Animals) v.getTag();
            if (tag.equals(random)) {
                System.out.println("longClickListener true: " + tag);
                if (parentalGateViewInteractionListener != null) {
                    parentalGateViewInteractionListener.onCorrectImageSelected();
                }
            } else {
                System.out.println("longClickListener false: " + tag);
            }
            return true;
        }
        return false;
    };

    private enum Animals {
        BUTTERFLY, OWL, LADYBUG
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
        if (getActivity() instanceof ParentalGateViewInteractionListener) {
            parentalGateViewInteractionListener = (ParentalGateViewInteractionListener) getActivity();
        }
    }

    private final RandomEnum<Animals> r =
            new RandomEnum<Animals>(Animals.class);


    private class RandomEnum<E extends Enum<E>> {

        private final Random RND = new Random();
        private final E[] values;

        public RandomEnum(Class<E> token) {
            values = token.getEnumConstants();
        }

        public E random() {
            return values[RND.nextInt(values.length)];
        }
    }

    public interface ParentalGateViewInteractionListener {
        void onCorrectImageSelected();
    }
}
