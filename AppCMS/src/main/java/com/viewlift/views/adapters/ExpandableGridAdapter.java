package com.viewlift.views.adapters;

import android.content.Context;
import android.graphics.drawable.PaintDrawable;
import android.os.Build;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ConceptData;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.models.data.appcms.api.Person;
import com.viewlift.models.data.appcms.ui.page.Component;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.views.customviews.ViewCreator;
import com.viewlift.views.customviews.expendablerecyclerview.ExpandableRecyclerViewAdapter;
import com.viewlift.views.customviews.expendablerecyclerview.models.ExpandableGroup;
import com.viewlift.views.customviews.expendablerecyclerview.viewholders.ChildViewHolder;
import com.viewlift.views.customviews.expendablerecyclerview.viewholders.GroupViewHolder;

import java.util.List;

import static android.view.animation.Animation.RELATIVE_TO_SELF;

public class ExpandableGridAdapter extends ExpandableRecyclerViewAdapter<ExpandableGridAdapter.ItemViewHolder, ExpandableGridAdapter.SubItemViewHolder> {
    private AppCMSPresenter appCMSPresenter;
    private Component component;
    private ContentDatum data;
    private Context context;

    public ExpandableGridAdapter(List<? extends ExpandableGroup> groups, Component component, AppCMSPresenter appCMSPresenter, ContentDatum data, Context context) {
        super(groups);
        this.component = component;
        this.appCMSPresenter = appCMSPresenter;
        this.data = data;
        this.context = context;
    }

    @Override
    public ItemViewHolder onCreateGroupViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_recyclerview_item, parent, false);
        return new ItemViewHolder(v);
    }

    @Override
    public SubItemViewHolder onCreateChildViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.expandable_recyclerview_sub_item, parent, false);
        return new SubItemViewHolder(v);
    }

    @Override
    public void onBindChildViewHolder(SubItemViewHolder holder, int flatPosition, ExpandableGroup group, int childIndex) {
        final Person persons = ((ConceptData) group).getItems().get(childIndex);
        holder.bind(persons,group,flatPosition);
        //holder.bind(flatPosition);
    }

    @Override
    public void onBindGroupViewHolder(ItemViewHolder holder, int flatPosition, ExpandableGroup group) {
        holder.setGroupName(group);
        //holder.bind(flatPosition);
    }


    //******************************************************************
    public class ItemViewHolder extends GroupViewHolder {
        private TextView itemTitle;
        private ImageView arrow;

        public ItemViewHolder(View itemView) {
            super(itemView);

            itemTitle = itemView.findViewById(R.id.item_title);
            arrow = itemView.findViewById(R.id.arrow);
            ViewCreator.setTypeFace(context, appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), new Component(), itemTitle);
        }

        @Override
        public void expand() {
            animateExpand();
        }

        @Override
        public void collapse() {
            animateCollapse();
        }

        public void setGroupName(ExpandableGroup group) {
            itemTitle.setText(group.getTitle());
        }

        private void animateExpand() {
            RotateAnimation rotate =
                    new RotateAnimation(360, 180, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            arrow.setAnimation(rotate);
        }

        private void animateCollapse() {
            RotateAnimation rotate =
                    new RotateAnimation(180, 360, RELATIVE_TO_SELF, 0.5f, RELATIVE_TO_SELF, 0.5f);
            rotate.setDuration(300);
            rotate.setFillAfter(true);
            arrow.setAnimation(rotate);
        }
    }

    //******************************************************************

    public class SubItemViewHolder extends ChildViewHolder {
        private TextView subItemTitle, subItemDescription;
        private ImageView instagram, facebook, twitter;

        public SubItemViewHolder(View itemView) {
            super(itemView);
            subItemDescription = itemView.findViewById(R.id.sub_item_description);
            subItemTitle = itemView.findViewById(R.id.sub_item_title);
            instagram = itemView.findViewById(R.id.instagram);
            twitter = itemView.findViewById(R.id.twitter);
            facebook = itemView.findViewById(R.id.facebook);
            Component component=new Component();
            ViewCreator.setTypeFace(context, appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, subItemDescription);
            component.setFontWeight(context.getString(R.string.app_cms_page_font_bold_key));
            ViewCreator.setTypeFace(context, appCMSPresenter, appCMSPresenter.getJsonValueKeyMap(), component, subItemTitle);

            //initViews();
        }
        public void bind(Person persons, ExpandableGroup group, int parentPosition) {

            if(parentPosition == 1){
                subItemDescription.setText(data.getGist().getDescription());
                subItemTitle.setVisibility(View.GONE);
                instagram.setVisibility(View.GONE);
                twitter.setVisibility(View.GONE);
                facebook.setVisibility(View.GONE);
            }else{
                subItemTitle.setVisibility(View.VISIBLE);
                subItemDescription.setVisibility(View.VISIBLE);
                instagram.setVisibility(persons.getGist() == null
                        || persons.getGist().getInstagramUrl() == null ? View.GONE : View.VISIBLE);

                twitter.setVisibility(persons.getGist() == null
                        || persons.getGist().getTwitterUrl() == null ? View.GONE : View.VISIBLE);

                facebook.setVisibility(persons.getGist() == null
                        || persons.getGist().getFacebookUrl() == null ? View.GONE : View.VISIBLE);

                handleOnClick(persons);

                String trainerName = "";
                if ( persons != null
                        && persons.getGist() != null
                        && persons.getGist().getFirstName() != null
                        && !TextUtils.isEmpty(persons.getGist().getFirstName())
                        && persons.getGist().getLastName() != null
                        && !TextUtils.isEmpty(persons.getGist().getLastName())) {

                    trainerName = persons.getGist().getFirstName();

                    if (persons.getGist().getLastName() != null) {
                        trainerName = trainerName + " " + persons.getGist().getLastName();
                    }
                }
                subItemTitle.setText(trainerName);

                String instructorDetails = null;
                if (persons != null
                        && persons.getGist().getBody() != null) {
                    instructorDetails = persons.getGist().getBody();
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        subItemDescription.setText(Html.fromHtml(instructorDetails));
                    } else {
                        subItemDescription.setText(Html.fromHtml(instructorDetails, Html.FROM_HTML_MODE_LEGACY));
                    }
                }

            }
        }

        private void handleOnClick(Person persons) {

            PaintDrawable gdDefault = new PaintDrawable(ContextCompat.getColor(itemView.getContext(), R.color.color_white));
            gdDefault.setCornerRadius(15f);
            instagram.setBackground(gdDefault);

            instagram.setOnClickListener(v -> {
                if (persons != null
                        && persons.getGist() != null
                        && persons.getGist().getInstagramUrl() != null)
                    appCMSPresenter.openChromeTab(persons.getGist().getInstagramUrl());
            });

            twitter.setBackground(gdDefault);
            twitter.setOnClickListener(v -> {
                if ( persons != null
                        && persons.getGist() != null
                        && persons.getGist().getTwitterUrl() != null)
                    appCMSPresenter.openChromeTab(persons.getGist().getTwitterUrl());
            });

            facebook.setBackground(gdDefault);
            facebook.setOnClickListener(v -> {
                if ( persons != null
                        && persons.getGist() != null
                        && persons.getGist().getFacebookUrl() != null)
                    appCMSPresenter.openChromeTab(persons.getGist().getFacebookUrl());

            });
        }
    }

    //******************************************************************
}

