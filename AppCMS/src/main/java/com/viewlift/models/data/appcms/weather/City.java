package com.viewlift.models.data.appcms.weather;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "City" ,strict = false)
 public class City {

    @Attribute(name = "Name" , required = false)
     private String Name;

    @Attribute(name = "Id" , required = false)
     private String Id;

    @Element(name = "HourlyForecast")
    private HourlyForecast HourlyForecast;

    @Element(name = "DailyForecast")
    private DailyForecast DailyForecast;


    public String getName ()
     {
         return Name;
     }

     public void setName (String Name)
     {
         this.Name = Name;
     }

     public String getId ()
     {
         return Id;
     }

     public void setId (String Id)
     {
         this.Id = Id;
     }

    public HourlyForecast getHourlyForecast() {
        return HourlyForecast;
    }

    public void setHourlyForecast(HourlyForecast hourlyForecast) {
        HourlyForecast = hourlyForecast;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    public com.viewlift.models.data.appcms.weather.DailyForecast getDailyForecast() {
        return DailyForecast;
    }

    public void setDailyForecast(com.viewlift.models.data.appcms.weather.DailyForecast dailyForecast) {
        DailyForecast = dailyForecast;
    }
}
