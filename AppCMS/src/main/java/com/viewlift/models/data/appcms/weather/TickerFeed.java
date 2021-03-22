package com.viewlift.models.data.appcms.weather;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name = "feed" , strict = false)
public class TickerFeed {

    @Element(name = "id" , required = false)
    private String id;

   // private Author author;

    @Element(name = "title" , required = false)
    private String title;

    @Element(name = "updated" , required = false)
    private String updated;

    //private Link link;

    @Element(name = "entry")
    private Entry entry;

    @Element(name = "subtitle" , required = false)
    private String subtitle;

    @Element(name = "xmlns" , required = false)
    private String xmlns;

    public String getId ()
    {
        return id;
    }

    public void setId (String id)
    {
        this.id = id;
    }

  /*  public Author getAuthor ()
    {
        return author;
    }

    public void setAuthor (Author author)
    {
        this.author = author;
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

   /* public Link getLink ()
    {
        return link;
    }

    public void setLink (Link link)
    {
        this.link = link;
    }*/

    public Entry getEntry ()
    {
        return entry;
    }

    public void setEntry (Entry entry)
    {
        this.entry = entry;
    }

    public String getSubtitle ()
    {
        return subtitle;
    }

    public void setSubtitle (String subtitle)
    {
        this.subtitle = subtitle;
    }

    public String getXmlns ()
    {
        return xmlns;
    }

    public void setXmlns (String xmlns)
    {
        this.xmlns = xmlns;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [id = "+id+", title = "+title+", updated = "+updated+", entry = "+entry+", subtitle = "+subtitle+", xmlns = "+xmlns+"]";
    }
}
