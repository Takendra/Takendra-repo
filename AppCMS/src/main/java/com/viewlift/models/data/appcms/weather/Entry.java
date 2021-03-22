package com.viewlift.models.data.appcms.weather;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "entry" ,strict = false)
public class Entry {

    @Element(name = "summary" , required = false)
    private String summary;

    @Element(name = "id" , required = false)
    private String id;

   // private Category category;

    @Element(name = "title" , required = false)
    private String title;

    @Element(name = "updated" , required = false)
    private String updated;

    @Element(name = "pubdate" , required = false)
    private String pubdate;

  //  private Link link;

    public String getSummary ()
    {
        return summary;
    }

    public void setSummary (String summary)
    {
        this.summary = summary;
    }

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

   /* public Category getCategory ()
    {
        return category;
    }

    public void setCategory (Category category)
    {
        this.category = category;
    }*/

    public String getTitle ()
    {
        return title;
    }

    public void setTitle (String title)
    {
        this.title = title;
    }

    public String getUpdated ()
    {
        return updated;
    }

    public void setUpdated (String updated)
    {
        this.updated = updated;
    }

    public String getPubdate ()
    {
        return pubdate;
    }

    public void setPubdate (String pubdate)
    {
        this.pubdate = pubdate;
    }

   /* public Link getLink ()
    {
        return link;
    }

    public void setLink (Link link)
    {
        this.link = link;
    }
*/
    @Override
    public String toString()
    {
        return "ClassPojo [summary = "+summary+", id = "+id+", title = "+title+", updated = "+updated+", pubdate = "+pubdate+", link = "+"]";
    }
}
