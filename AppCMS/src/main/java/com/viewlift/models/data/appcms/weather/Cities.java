package com.viewlift.models.data.appcms.weather;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

@Root(name = "Cities" , strict = false)
 public class Cities {

    @ElementList(name = "City" , inline = true)
    private List<City> City;

    public List<City> getCity ()
    {
        return City;
    }

    public void setCity (List<City> City)
    {
        this.City = City;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [City = "+City+"]";
    }


 /*  @Element
    private City City;

    public  City getCity ()
    {
        return City;
    }

    public void setCity ( City City)
    {
        this.City = City;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [City = "+City+"]";
    }*/
}
