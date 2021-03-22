package com.viewlift.models.data.appcms.weather;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "DailyForecast" ,strict = false)
public class DailyForecast {

    @ElementList(name = "Day" , inline = true)
    private List<Day> Day;

    public List<Day> getDay ()
    {
        return Day;
    }

    public void setDay (List<Day> Day)
    {
        this.Day = Day;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Day = "+Day+"]";
    }
}
