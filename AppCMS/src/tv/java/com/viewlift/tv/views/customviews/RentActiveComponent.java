package com.viewlift.tv.views.customviews;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.viewlift.R;
import com.viewlift.models.data.appcms.api.ContentDatum;
import com.viewlift.presenters.AppCMSPresenter;
import com.viewlift.tv.utility.Utils;
import com.viewlift.views.customviews.CustomTypefaceSpan;

import java.util.Date;

public class RentActiveComponent extends RelativeLayout {

    private final Context context;
    private final AppCMSPresenter appCMSPresenter;
    private final ContentDatum contentDatum;
    private TextView textRentalPeriod;
    private long rentExpirationDate;

    public RentActiveComponent(Context context, AppCMSPresenter appCMSPresenter, ContentDatum contentDatum) {
        super(context);
        this.context = context;
        this.appCMSPresenter = appCMSPresenter;
        this.contentDatum = contentDatum;
        init();
    }

    private void init() {
        this.setLayoutParams(new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.setGravity(Gravity.CENTER);

        View separator = new View(context);
        LayoutParams separatorParams = new LayoutParams(2, LayoutParams.MATCH_PARENT);
        separator.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        separator.setLayoutParams(separatorParams);
        this.addView(separator);

        TextView textThankYou = new TextView(context);
        LayoutParams textThankYouParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textThankYouParams.addRule(RIGHT_OF, separator.getId());
        textThankYouParams.setMargins(15, 0, 0, 0);
        textThankYou.setLayoutParams(textThankYouParams);
        textThankYou.setTextSize(15f);
        textThankYou.setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
        textThankYou.setText(appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.thank_you)));
        textThankYou.setTypeface(Utils.getSpecificTypeface(
                context,
                appCMSPresenter,
                context.getString(R.string.app_cms_page_font_bold_key)));
        this.addView(textThankYou);

        textRentalPeriod = new TextView(context);
        LayoutParams textRentalPeriodParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textRentalPeriodParams.addRule(RIGHT_OF, textThankYou.getId());
        textRentalPeriodParams.addRule(BELOW, textThankYou.getId());
        textRentalPeriodParams.setMargins(15, 50, 0, 0);
        textRentalPeriod.setLayoutParams(textRentalPeriodParams);
        textRentalPeriod.setTextSize(10f);
        textRentalPeriod.setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
        textRentalPeriod.setTypeface(Utils.getSpecificTypeface(
                context,
                appCMSPresenter,
                context.getString(R.string.app_cms_page_font_regular_key)));
        textRentalPeriod.setAlpha(0.9f);
        this.addView(textRentalPeriod);

        TextView textEmail = new TextView(context);
        LayoutParams textEmailParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        textEmailParams.addRule(RIGHT_OF, separator.getId());
        textEmailParams.addRule(BELOW, textRentalPeriod.getId());
        textEmailParams.setMargins(15, 125, 0, 0);
        textEmail.setLayoutParams(textEmailParams);
        textEmail.setTextSize(10f);
        textEmail.setTextColor(appCMSPresenter.getBrandPrimaryCtaTextColor());
        String text = appCMSPresenter.getLanguageResourcesFile().getUIresource(context.getString(R.string.email_recipt_text));
        textEmail.setText(text  + appCMSPresenter.getLoggedInUserEmail());
        textEmail.setTypeface(Utils.getSpecificTypeface(
                context,
                appCMSPresenter,
                context.getString(R.string.app_cms_page_font_regular_key)));
        textEmail.setAlpha(0.9f);
        this.addView(textEmail);
    }

    public void setRentExpirationMillis(long rentExpirationDate) {
        this.rentExpirationDate = rentExpirationDate;

        float diff = rentExpirationDate - System.currentTimeMillis();
        float days = (diff / (1000 * 60 * 60 * 24));

        String text;
        if (days > 1) {
            text = appCMSPresenter.getLanguageResourcesFile().getStringValue(context.getString(R.string.rental_period_activation_other),
                    contentDatum.getGist().getTitle(),
                    DateFormat.format("dd-MMM-yyyy HH:mm", new Date(rentExpirationDate)).toString());
          //  text = "Your rental period for " + contentDatum.getGist().getTitle() + " is active until " + DateFormat.format("dd-MMM-yyyy HH:mm", new Date(rentExpirationDate));
        } else if (days == 1) {
            text = appCMSPresenter.getLanguageResourcesFile().getStringValue(
                    context.getString(R.string.rental_period_activation_tomorrow),
                    DateFormat.format("HH:mm", new Date(rentExpirationDate)).toString());
            //text = "Your rental period for " + contentDatum.getGist().getTitle() + " is active until " + "tomorrow at " + DateFormat.format("HH:mm", new Date(rentExpirationDate));
        } else {

            text = appCMSPresenter.getLanguageResourcesFile().getStringValue(
                    context.getString(R.string.rental_period_activation_today),
                    DateFormat.format("HH:mm", new Date(rentExpirationDate)).toString());

           // text = "Your rental period for " + contentDatum.getGist().getTitle() + " is active until " + "today at " + DateFormat.format("HH:mm", new Date(rentExpirationDate));
        }

        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
        spannableStringBuilder.setSpan(
                new CustomTypefaceSpan("",
                        Utils.getSpecificTypeface(
                                context,
                                appCMSPresenter,
                                context.getString(R.string.app_cms_page_font_bold_key))),
                text.indexOf(contentDatum.getGist().getTitle()),
                (text.indexOf(contentDatum.getGist().getTitle()) +
                        contentDatum.getGist().getTitle().length()),
                Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textRentalPeriod.setText(spannableStringBuilder);
    }
}
