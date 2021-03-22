package com.viewlift.views.customviews.exoplayerview;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.AudioManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import com.viewlift.R;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.BaseView;

import javax.annotation.Nullable;

public class VolumeController extends RelativeLayout {

    Context mContext;
    ImageButton imageVolume;
    SeekBar seekBarVolume;

    AppCMSPresenter appCMSPresenter;
    private AudioManager audioManager = null;

    public VolumeController(Context context) {
        super(context);
    }

    public VolumeController(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public VolumeController(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public VolumeController(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public VolumeController(Context context, AppCMSPresenter appCMSPresenter, int imageRes) {
        super(context);
        mContext = context;
        this.appCMSPresenter = appCMSPresenter;
        setId(R.id.VolumeController);

        imageVolume = new ImageButton(mContext);
        imageVolume.setId(R.id.imageIconVolumeInfo);
        imageVolume.setImageResource(imageRes);
        imageVolume.setBackgroundResource(R.color.transparentColor);
       /* imageVolume.setChecked(false);
        imageVolume.setTextOff("");
        imageVolume.setTextOn("");
        imageVolume.setText("");
        //imageVolume.setBackgroundDrawable(getResources().getDrawable(R.drawable.full_screen_toggle_selector, null));
        imageVolume.setBackgroundDrawable(getResources().getDrawable(imageRes, null));*/

        LayoutParams toggleLP = new LayoutParams(BaseView.dpToPx(R.dimen.app_cms_video_controller_cc_width, getContext()), BaseView.dpToPx(R.dimen.app_cms_video_controller_cc_width, getContext()));
        toggleLP.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        toggleLP.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        toggleLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        imageVolume.setLayoutParams(toggleLP);



        seekBarVolume = new VerticalVolumeSeekBar(mContext);
        seekBarVolume.setId(R.id.seekBarVolume);




        seekBarVolume.setProgressDrawable(mContext.getDrawable(R.drawable.seekbar_volume_bg));
        seekBarVolume.setProgressTintList(ColorStateList.valueOf(appCMSPresenter.getBrandPrimaryCtaColor()));
        seekBarVolume.setThumbTintList(ColorStateList.valueOf(Color.WHITE));
        seekBarVolume.setVisibility(INVISIBLE);

        imageVolume.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (seekBarVolume.getVisibility() == VISIBLE){
                    seekBarVolume.setVisibility(INVISIBLE);
                }else if(seekBarVolume.getVisibility() == INVISIBLE){
                    seekBarVolume.setVisibility(VISIBLE);
                }
            }
        });



        int height = 300;


        LayoutParams seekBarLP = new LayoutParams( ViewGroup.LayoutParams.WRAP_CONTENT,height);

        seekBarLP.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        seekBarLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        seekBarLP.addRule(RelativeLayout.ABOVE, R.id.imageIconVolumeInfo);
        seekBarVolume.setLayoutParams(seekBarLP);

        audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);

        addView(seekBarVolume);
        addView(imageVolume);

        try
        {
            seekBarVolume.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            seekBarVolume.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));


            seekBarVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
            {
                @Override
                public void onStopTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0)
                {
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2)
                {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public void settVolumeLevel(int progress){
        seekBarVolume.setProgress(progress);

    }
    public SeekBar getSeekBarVolume(){
        return seekBarVolume;
    }

    public ImageButton getImageVolume(){
        return imageVolume;
    }



}
