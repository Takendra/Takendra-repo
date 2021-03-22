package com.viewlift.views.adapters;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cursoradapter.widget.CursorAdapter;

import com.viewlift.R;
import com.viewlift.presenters.AppCMSPresenter;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
 * Created by viewlift on 7/25/17.
 */

public class SearchSuggestionsAdapter extends CursorAdapter {

    private static final String TAG = "SearchSuggestionTag_";

    @BindView(R.id.search_suggestion_film_name_text)
    TextView filmTitle;

    @BindView(R.id.search_suggestion_runtime_text)
    TextView runtime;

    private final SearchableInfo searchableInfo;

    private AppCMSPresenter appCMSPresenter;

    public SearchSuggestionsAdapter(AppCMSPresenter appCMSPresenter, Context context, Cursor c, SearchableInfo searchableInfo,
                                    boolean autoRequery) {
        super(context, c, autoRequery);
        this.searchableInfo = searchableInfo;

        this.appCMSPresenter = appCMSPresenter;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        return layoutInflater.inflate(R.layout.search_suggestion, parent, false);
    }

    @Override
    @SuppressWarnings("StringBufferReplaceableByString")
    public void bindView(View view, Context context, Cursor cursor) {
        ButterKnife.bind(this, view);
        String[] searchHintResult = cursor.getString(cursor.getColumnIndex("suggest_intent_data")).split(",");
        String mediaType = searchHintResult[4];
        String songCount = searchHintResult[7];
        String episodeCount = searchHintResult[9];

        String songYear = "";
        if (searchHintResult.length >= 9 && searchHintResult[8] != null) {
            songYear = searchHintResult[8];
        }

        filmTitle.setText(cursor.getString(1));
        int runtimeAsInteger = Integer.parseInt(cursor.getString(2));


        if (runtimeAsInteger < 60 && runtimeAsInteger > 0) {
            runtime.setText(new StringBuilder().append(cursor.getString(2))
                    .append(" ")
                    .append(appCMSPresenter.getLocalisedStrings().getSecsText()).toString());
        } else if (runtimeAsInteger == 0 || runtimeAsInteger / 60 == 0) {
            if (mediaType != null && !mediaType.equalsIgnoreCase(context.getString(R.string.content_type_event))
                    && episodeCount != null)
                if (Integer.parseInt(episodeCount) > 1)
                    runtime.append(episodeCount + " " + new StringBuilder().append(appCMSPresenter.getLocalisedStrings().getEpisodesHeaderText()));
                else
                    runtime.append(episodeCount + " " + new StringBuilder().append(appCMSPresenter.getLocalisedStrings().getEpisodeText().toUpperCase()));
        } else if (runtimeAsInteger / 60 < 2) {
            runtime.setText(new StringBuilder().append(runtimeAsInteger / 60)
                    .append(" ")
                    .append(appCMSPresenter.getLocalisedStrings().getMinText()).toString());
        } else {
            runtime.setText(new StringBuilder().append(runtimeAsInteger / 60)
                    .append(" ")
                    .append(appCMSPresenter.getLocalisedStrings().getMinsText()).toString());
        }

        if (mediaType != null
                && mediaType.toLowerCase().contains(context.getString(R.string.media_type_playlist).toLowerCase())) {
            if (Integer.parseInt(songCount) > 1)
                runtime.append(songCount + " " + appCMSPresenter.getLocalisedStrings().getSongsHeaderText());
            else
                runtime.append(songCount + " " + appCMSPresenter.getLocalisedStrings().getSongText().toUpperCase());
        } else if (mediaType != null
                && mediaType.toLowerCase().contains(context.getString(R.string.media_type_audio).toLowerCase()) && !TextUtils.isEmpty(songYear)) {
            runtime.append(" | " + songYear);
        }
        if (mediaType.toLowerCase().contains(context.getString(R.string.app_cms_article_key_type).toLowerCase())
                ||mediaType.toLowerCase().contains(context.getString(R.string.app_cms_photo_gallery_key_type).toLowerCase())|| runtimeAsInteger==0) {
            runtime.setText("");
        }
    }

    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        Cursor cursor;
        String query = ((constraint == null) ? "" : constraint.toString());
        if (query != null && query.length() != 0)
            appCMSPresenter.sendSearchEvent(query);

        try {
            cursor = getSearchManagerSuggestions(searchableInfo, query, 5);
            if (cursor != null) {
                cursor.getCount();
                return cursor;
            }
        } catch (RuntimeException e) {
            //Log.w(TAG, "runQueryOnBackgroundThread: " + e.getMessage());
        }

        return super.runQueryOnBackgroundThread(constraint);
    }

    @SuppressWarnings("SameParameterValue")
    private Cursor getSearchManagerSuggestions(SearchableInfo searchable, String query, int limit) {
        if (searchable == null) {
            return null;
        }

        String authority = searchable.getSuggestAuthority();
        if (authority == null) {
            return null;
        }

        Uri.Builder uriBuilder = new Uri.Builder()
                .scheme(ContentResolver.SCHEME_CONTENT)
                .authority(authority)
                .query("")  // TODO: Remove, workaround for a bug in Uri.writeToParcel()
                .fragment("");  // TODO: Remove, workaround for a bug in Uri.writeToParcel()

        // if content path provided, insert it now
        final String contentPath = searchable.getSuggestPath();
        if (contentPath != null) {
            uriBuilder.appendEncodedPath(contentPath);
        }

        uriBuilder.appendPath(SearchManager.SUGGEST_URI_PATH_QUERY);
        String selection = searchable.getSuggestSelection();
        String[] selArgs = null;

        if (selection != null) {
            selArgs = new String[]{query};
        } else {
            uriBuilder.appendPath(query);
        }

        if (limit > 0) {
            uriBuilder.appendQueryParameter(SearchManager.SUGGEST_PARAMETER_LIMIT,
                    String.valueOf(limit));
        }

        Uri uri = uriBuilder.build();

        // finally, make the query
        return mContext.getContentResolver().query(uri, null, selection, selArgs, null);
    }
}
