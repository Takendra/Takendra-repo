package com.viewlift.views.adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.R;
import com.viewlift.models.data.playersettings.HLSStreamingQuality;
import com.viewlift.presenters.AppCMSPresenter;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.viewlift.presenters.AppCMSPresenter.DialogType.VIDEO_NOT_AVAILABLE_ALERT;
import static com.viewlift.presenters.AppCMSPresenter.PlatformType.TV;

/**
 * Created by sandeep.singh on 7/28/2017.
 */

public abstract class AppCMSDownloadRadioAdapter<T>
        extends RecyclerView.Adapter<AppCMSDownloadRadioAdapter.ViewHolder> {

    int tintColor;
    protected int downloadQualityPosition = 0; // Default position is 1, i.e 720p
    List<T> mItems;
    protected ItemClickListener itemClickListener;
    private Context mContext;
    private AppCMSPresenter appCMSPresenter;
    boolean isContentTvod;
    boolean isTvodHdStreaming;

    public AppCMSDownloadRadioAdapter(Context context, List<T> items, AppCMSPresenter appCMSPresenter) {
        this.mContext = context;
        this.mItems = items;
        this.appCMSPresenter = appCMSPresenter;
    }

    public AppCMSDownloadRadioAdapter(Context context, List<T> items, AppCMSPresenter appCMSPresenter, boolean isContentTvod,
                                      boolean isTvodHdStreaming) {
        this.mContext = context;
        this.mItems = items;
        this.appCMSPresenter = appCMSPresenter;
        this.isContentTvod = isContentTvod;
        this.isTvodHdStreaming = isTvodHdStreaming;

    }

    @Override
    public void onBindViewHolder(AppCMSDownloadRadioAdapter.ViewHolder viewHolder, final int i) {
        viewHolder.mRadio.setChecked(i == downloadQualityPosition);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        final View view = inflater.inflate(R.layout.download_quality_view_item, viewGroup, false);
        return new ViewHolder(view);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        if (itemClickListener != null) {
            itemClickListener.onItemClick(mItems.get(downloadQualityPosition));
        }
    }


    public interface ItemClickListener<T> {
        void onItemClick(T item);
    }

    public List<T> returnItems(){
        return mItems;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.download_quality_radio_selection)
        AppCompatRadioButton mRadio;

        @BindView(R.id.download_quality_text)
        TextView mText;

        public ViewHolder(final View inflate) {
            super(inflate);
            ButterKnife.bind(this, inflate);

            View.OnClickListener clickListener = v -> {
                downloadQualityPosition = getAdapterPosition();
                if (mItems.get(downloadQualityPosition) instanceof HLSStreamingQuality) {
                    if (((HLSStreamingQuality) mItems.get(downloadQualityPosition)).getValue() != null)
                        if (Integer.valueOf(((HLSStreamingQuality) mItems.get(downloadQualityPosition)).getIndex()) != 0) {
                            int stremaingQuality = Integer.valueOf(((HLSStreamingQuality) mItems.get(downloadQualityPosition)).getValue().replace("p", ""));
                            if (stremaingQuality >= 720) {
                                mRadio.setChecked(false);
                                if ((isContentTvod && !isTvodHdStreaming)) {
                                    appCMSPresenter.showDialog(VIDEO_NOT_AVAILABLE_ALERT, appCMSPresenter.getLocalisedStrings().getHDStreamUnavailableMsg(), false, null, null, appCMSPresenter.getLocalisedStrings().getAlertTitle());
                                    return;
                                } else if ((isContentTvod && !isTvodHdStreaming) || !appCMSPresenter.getAppPreference().isUserAllowedHDStreaming()) {
                                    if (appCMSPresenter.getPlatformType() == TV) {
                                        appCMSPresenter.openTVGenericDialog(null, appCMSPresenter.getLocalisedStrings().getPlanUpgradeText(),
                                                appCMSPresenter.getLocalisedStrings().getSubscribeNowText(), null, null, null);
                                        itemClickListener.onItemClick(null);
                                        return;
                                    } else {
                                        appCMSPresenter.showEntitlementDialog(AppCMSPresenter.DialogType.SUBSCRIPTION_PLAN_UPGRADE,
                                                () -> {
                                                }, null);
                                        return;
                                    }
                                }

                            }
                        }
                }
                notifyItemRangeChanged(0, mItems.size());
                if (mItems.size() > downloadQualityPosition && itemClickListener !=null)
                    itemClickListener.onItemClick(mItems.get(downloadQualityPosition));
            };

            //itemView.setOnClickListener(clickListener);
            mRadio.setOnClickListener(clickListener);

            if (appCMSPresenter.getPlatformType() == TV) {
                mRadio.setOnFocusChangeListener(
                        new View.OnFocusChangeListener() {
                            @Override
                            public void onFocusChange(View view, boolean b) {
                                if (b) {
                                    mRadio.setBackground(view.getContext().getDrawable(R.drawable.circle_shape));
                                } else {
                                    mRadio.setBackground(null);
                                }
                            }
                        }
                );
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (mRadio.getButtonDrawable() != null) {
                    mRadio.getButtonDrawable().setColorFilter(tintColor, PorterDuff.Mode.MULTIPLY);
                }
            } else {
                int switchOnColor = tintColor;
                ColorStateList colorStateList = new ColorStateList(
                        new int[][]{
                                new int[]{android.R.attr.state_checked},
                                new int[]{}
                        }, new int[]{
                        switchOnColor,
                        switchOnColor
                });
                mRadio.setButtonTintList(colorStateList);
            }

        }

        public AppCompatRadioButton getmRadio() {
            return mRadio;
        }

        public void setmRadio(AppCompatRadioButton mRadio) {
            this.mRadio = mRadio;
        }

        public TextView getmText() {
            return mText;
        }

        public void setmText(TextView mText) {
            this.mText = mText;
        }
    }
}
