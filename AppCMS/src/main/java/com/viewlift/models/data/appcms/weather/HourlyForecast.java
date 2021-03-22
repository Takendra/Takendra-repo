package com.viewlift.models.data.appcms.weather;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "HourlyForecast" , strict = false)
public class HourlyForecast {

    @ElementList(name = "Hour" , inline = true)
    private List<Hour> Hour;

    public List<Hour> getHour ()
    {
        return Hour;
    }

    public void setHour (List<Hour> Hour)
    {
        this.Hour = Hour;
    }


}
