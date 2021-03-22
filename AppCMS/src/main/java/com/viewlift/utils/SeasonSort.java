package com.viewlift.utils;

import com.viewlift.models.data.appcms.api.Season_;

import java.util.Comparator;

public class SeasonSort implements Comparator<Season_> {
    public int compare(Season_ season_a, Season_ season_b)
    {
        return season_a.getTitle().compareTo(season_b.getTitle());
    }
}
