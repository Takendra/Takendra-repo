package com.viewlift.tv.views.fragment;


import androidx.fragment.app.Fragment;

/**
 * Created by nitin.tyagi on 7/24/2017.
 */

public abstract class BaseFragment extends Fragment implements AppCmsSubNavigationFragment.OnSubNavigationVisibilityListener {
    public abstract boolean isSubNavigationVisible();
    public abstract boolean isSubNavExist();
}
