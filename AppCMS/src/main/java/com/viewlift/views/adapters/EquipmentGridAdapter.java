package com.viewlift.views.adapters;

import android.graphics.Color;
import android.graphics.Typeface;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.viewlift.models.data.appcms.EquipmentElement;
import com.viewlift.R;
import com.viewlift.models.data.appcms.ui.AppCMSUIKeyType;
import com.viewlift.presenters.AppCMSPresenter;

import java.util.List;

public class EquipmentGridAdapter extends RecyclerView.Adapter<EquipmentGridAdapter.ListItemViewHolder> {

    private List<EquipmentElement> list;
    private static AppCMSUIKeyType componentKey;
    private static AppCMSPresenter appCMSPresenter;

    public EquipmentGridAdapter(List<EquipmentElement> list, AppCMSUIKeyType componentKey, AppCMSPresenter appCMSPresenter) {
        this.list = list;
        EquipmentGridAdapter.componentKey = componentKey;
        EquipmentGridAdapter.appCMSPresenter = appCMSPresenter;
    }

    @Override
    public ListItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (componentKey == AppCMSUIKeyType.PAGE_VIDEO_DETAIL_EQUIPMENT_NEEDED_LIST_KEY) {
            return new ListItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.neou_video_detail_page_item, parent, false));
        } else {
            return new ListItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.equipment_single_view_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(ListItemViewHolder holder, int position) {
        if (componentKey == AppCMSUIKeyType.PAGE_VIDEO_DETAIL_EQUIPMENT_NEEDED_LIST_KEY) {
            holder.itemName.setText(list.get(position).getName());

        /*    if (position == 0) {
                holder.itemName.setTextColor(ContextCompat.getColor(holder.itemName.getContext(), R.color.colorGrayLight));
                holder.itemName.setTextSize(10);
                holder.imageView.setVisibility(View.GONE);
            } else {*/
            holder.imageView.setImageResource(list.get(position).getImage());
            holder.itemName.setTextColor(ContextCompat.getColor(holder.itemName.getContext(), R.color.color_white));

            if (list.get(position).getEquipment_needed() != null
                    && !TextUtils.isEmpty(list.get(position).getEquipment_needed())) {
                Glide.with(holder.itemView)
                        .load(list.get(position).getEquipment_needed())
                        .into(holder.imageView);
                holder.imageView.setVisibility(View.VISIBLE);
            } else {

                holder.imageView.setVisibility(View.GONE);
            }
            if (list.get(position).getName().contains("No equipment")) {
            } else if (list.get(position).isRequired()) {
                holder.itemName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_required, 0);
            } else {
                holder.itemName.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_optional, 0);
            }

            holder.itemName.setCompoundDrawablePadding(10);
//            }
            holder.itemName.setGravity(Gravity.LEFT);
        } else {
            holder.itemName.setText(list.get(position).getName());
            holder.imageView.setImageResource(list.get(position).getImage());
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ListItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView itemName;
        ImageView imageView, brandView;
        LinearLayout layoutItem;


        ListItemViewHolder(View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.equipment_name);
            imageView = itemView.findViewById(R.id.equipment_icon);
            try {
                Typeface face = Typeface.createFromAsset(itemView.getContext().getAssets(),
                        itemView.getContext().getString(R.string.montserrat_regular_ttf));
                itemName.setTypeface(face);
            } catch (Exception e) {
                e.printStackTrace();
            }
            itemName.setTextSize(12);
            if (componentKey != AppCMSUIKeyType.PAGE_VIDEO_DETAIL_EQUIPMENT_NEEDED_LIST_KEY) {
                itemView.setTag("notSelected");
                itemView.setOnClickListener(this);
                layoutItem = itemView.findViewById(R.id.layoutItem);
            /*    if (BaseView.isTablet(itemView.getContext())) {
                    GridLayoutManager.LayoutParams layoutParams = (GridLayoutManager.LayoutParams) layoutItem.getLayoutParams();
                    layoutParams.height = 140;
                    layoutParams.width = 170;
                    layoutItem.setLayoutParams(layoutParams);
                    itemName.setTextSize(10);
                }*/
            }
        }

        @Override
        public void onClick(View view) {
            if (view.getTag().equals("selected")) {
                view.setBackgroundColor(Color.parseColor("#00ffffff"));
                view.setTag("notSelected");
            } else {
                view.setBackgroundColor(Color.parseColor("#55ffffff"));
                view.setTag("selected");
            }
        }
    }
}

