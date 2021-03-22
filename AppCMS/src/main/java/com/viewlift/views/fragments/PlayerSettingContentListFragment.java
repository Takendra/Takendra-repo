package com.viewlift.views.fragments;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.viewlift.R;
import com.viewlift.views.adapters.AppCMSDownloadRadioAdapter;

@SuppressLint("ValidFragment")
public class PlayerSettingContentListFragment extends Fragment {

    RecyclerView settingList;
    AppCMSDownloadRadioAdapter listViewAdapter;

    public static PlayerSettingContentListFragment newInstance(AppCMSDownloadRadioAdapter adapter) {
        PlayerSettingContentListFragment fragment = new PlayerSettingContentListFragment();
        fragment.listViewAdapter = adapter;
        fragment.setRetainInstance(true);
        return fragment;
    }

//    @SuppressLint("ValidFragment")
//    public PlayerSettingContentListFragment(AppCMSDownloadRadioAdapter adapter){
//        listViewAdapter = adapter;
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View rootView = inflater.inflate(R.layout.fragment_player_setting_items, container, false);

        settingList = rootView.findViewById(R.id.player_setting_content_list);

        if (listViewAdapter != null) {
            settingList.setAdapter(listViewAdapter);
        }
        settingList.setBackgroundColor(Color.TRANSPARENT/*appCMSPresenter.getGeneralBackgroundColor()*/);
        settingList.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,
                false));

        RecyclerView.OnItemTouchListener mScrollTouchListener = new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                int action = e.getAction();
                switch (action) {
                    case MotionEvent.ACTION_MOVE:
                        rv.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        };

        ((RecyclerView) settingList).addOnItemTouchListener(mScrollTouchListener);

        return rootView;
    }
}
