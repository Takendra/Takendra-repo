package com.viewlift.models.data.appcms.weather;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "Hour" , strict = false)
public class Hour {

/*
    private String DewPtC;


    private String VisibilityKm;


    private String DewPtF;

    private String WndDirCardinal;

    private String IconCode;
*/
    @Attribute(name = "HourNum" , required = false)
    private String HourNum;

    public String getHourNum ()
    {
        return HourNum;
    }

    public void setHourNum (String HourNum)
    {
        this.HourNum = HourNum;
    }

    @Attribute(name = "DayOfWeek" , required = false)
    private String DayOfWeek;

    public String getDayOfWeek ()
    {
        return DayOfWeek;
    }

    public void setDayOfWeek (String DayOfWeek)
    {
        this.DayOfWeek = DayOfWeek;
    }


    @Attribute(name = "ReportTime" , required = false)
    private String ReportTime;

    public String getReportTime ()
    {
        return ReportTime;
    }

    public void setReportTime (String ReportTime)
    {
        this.ReportTime = ReportTime;
    }

    @Attribute(name = "PrecipChance" , required = false)
    private String PrecipChance;

    public String getPrecipChance ()
    {
        return PrecipChance;
    }

    public void setPrecipChance (String PrecipChance)
    {
        this.PrecipChance = PrecipChance;
    }

    @Attribute(name = "SkyLong" , required = false)
    private String SkyLong;

    public String getSkyLong ()
    {
        return SkyLong;
    }

    public void setSkyLong (String SkyLong)
    {
        this.SkyLong = SkyLong;
    }

    @Attribute(name = "TempF" , required = false)
    private String TempF;

    public String getTempF ()
    {
        return TempF;
    }

    public void setTempF (String TempF)
    {
        this.TempF = TempF;
    }

    @Attribute(name = "TempC" , required = false)
    private String TempC;

    public String getTempC ()
    {
        return TempC;
    }

    public void setTempC (String TempC)
    {
        this.TempC = TempC;
    }

    @Attribute(name = "SkyCode" , required = false)
    private String SkyCode;

    public String getSkyCode ()
    {
        return SkyCode;
    }

    public void setSkyCode (String SkyCode)
    {
        this.SkyCode = SkyCode;
    }


    @Attribute(name = "ValidDateUtc" , required = false)
    private String ValidDateUtc;

    public String getValidDateUtc ()
    {
        return ValidDateUtc;
    }

    public void setValidDateUtc (String ValidDateUtc)
    {
        this.ValidDateUtc = ValidDateUtc;
    }

    @Attribute(name = "ValidDateLocal" , required = false)
    private String ValidDateLocal;

    public String getValidDateLocal ()
    {
        return ValidDateLocal;
    }

    public void setValidDateLocal (String ValidDateLocal)
    {
        this.ValidDateLocal = ValidDateLocal;
    }


    /*private String SkyShort;



    private String RelHumidity;


    private String VisibilityMi;

    private String SnowChance;

    private String HeatIdxC;

    private String UvIdx;

    private String WndChillC;

    private String WndSpdKn;

    private String WndChillF;

    private String WndSpdKm;

    private String WndSpdMph;

    private String HeatIdxF;

    private String CloudCoverage;

    private String WndDirDegr;

    private String SkyMedium;

    private String UvDescr;

    private String SkyCodeExt;

    private String UvWarn;

    public String getDewPtC ()
    {
        return DewPtC;
    }

    public void setDewPtC (String DewPtC)
    {
        this.DewPtC = DewPtC;
    }



    public String getVisibilityKm ()
    {
        return VisibilityKm;
    }

    public void setVisibilityKm (String VisibilityKm)
    {
        this.VisibilityKm = VisibilityKm;
    }


    public String getDewPtF ()
    {
        return DewPtF;
    }

    public void setDewPtF (String DewPtF)
    {
        this.DewPtF = DewPtF;
    }

    public String getWndDirCardinal ()
    {
        return WndDirCardinal;
    }

    public void setWndDirCardinal (String WndDirCardinal)
    {
        this.WndDirCardinal = WndDirCardinal;
    }

    public String getIconCode ()
    {
        return IconCode;
    }

    public void setIconCode (String IconCode)
    {
        this.IconCode = IconCode;
    }

    public String getSkyShort ()
    {
        return SkyShort;
    }

    public void setSkyShort (String SkyShort)
    {
        this.SkyShort = SkyShort;
    }

    public String getRelHumidity ()
    {
        return RelHumidity;
    }

    public void setRelHumidity (String RelHumidity)
    {
        this.RelHumidity = RelHumidity;
    }


    public String getVisibilityMi ()
    {
        return VisibilityMi;
    }

    public void setVisibilityMi (String VisibilityMi)
    {
        this.VisibilityMi = VisibilityMi;
    }

    public String getSnowChance ()
    {
        return SnowChance;
    }

    public void setSnowChance (String SnowChance)
    {
        this.SnowChance = SnowChance;
    }

    public String getHeatIdxC ()
    {
        return HeatIdxC;
    }

    public void setHeatIdxC (String HeatIdxC)
    {
        this.HeatIdxC = HeatIdxC;
    }



    public String getUvIdx ()
    {
        return UvIdx;
    }

    public void setUvIdx (String UvIdx)
    {
        this.UvIdx = UvIdx;
    }


    public String getWndChillC ()
    {
        return WndChillC;
    }

    public void setWndChillC (String WndChillC)
    {
        this.WndChillC = WndChillC;
    }

    public String getWndSpdKn ()
    {
        return WndSpdKn;
    }

    public void setWndSpdKn (String WndSpdKn)
    {
        this.WndSpdKn = WndSpdKn;
    }

    public String getWndChillF ()
    {
        return WndChillF;
    }

    public void setWndChillF (String WndChillF)
    {
        this.WndChillF = WndChillF;
    }

    public String getWndSpdKm ()
    {
        return WndSpdKm;
    }

    public void setWndSpdKm (String WndSpdKm)
    {
        this.WndSpdKm = WndSpdKm;
    }

    public String getWndSpdMph ()
    {
        return WndSpdMph;
    }

    public void setWndSpdMph (String WndSpdMph)
    {
        this.WndSpdMph = WndSpdMph;
    }

    public String getHeatIdxF ()
    {
        return HeatIdxF;
    }

    public void setHeatIdxF (String HeatIdxF)
    {
        this.HeatIdxF = HeatIdxF;
    }


    public String getCloudCoverage ()
    {
        return CloudCoverage;
    }

    public void setCloudCoverage (String CloudCoverage)
    {
        this.CloudCoverage = CloudCoverage;
    }

    public String getWndDirDegr ()
    {
        return WndDirDegr;
    }

    public void setWndDirDegr (String WndDirDegr)
    {
        this.WndDirDegr = WndDirDegr;
    }

    public String getSkyMedium ()
    {
        return SkyMedium;
    }

    public void setSkyMedium (String SkyMedium)
    {
        this.SkyMedium = SkyMedium;
    }

    public String getUvDescr ()
    {
        return UvDescr;
    }

    public void setUvDescr (String UvDescr)
    {
        this.UvDescr = UvDescr;
    }

    public String getSkyCodeExt ()
    {
        return SkyCodeExt;
    }

    public void setSkyCodeExt (String SkyCodeExt)
    {
        this.SkyCodeExt = SkyCodeExt;
    }



    public String getUvWarn ()
    {
        return UvWarn;
    }

    public void setUvWarn (String UvWarn)
    {
        this.UvWarn = UvWarn;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [DewPtC = "+DewPtC+", ValidDateUtc = "+ValidDateUtc+", VisibilityKm = "+VisibilityKm+", TempC = "+TempC+", DewPtF = "+DewPtF+", WndDirCardinal = "+WndDirCardinal+", IconCode = "+IconCode+", HourNum = "+HourNum+", DayOfWeek = "+DayOfWeek+", SkyShort = "+SkyShort+", ValidDateLocal = "+ValidDateLocal+", RelHumidity = "+RelHumidity+", TempF = "+TempF+", VisibilityMi = "+VisibilityMi+", SnowChance = "+SnowChance+", HeatIdxC = "+HeatIdxC+", SkyLong = "+SkyLong+", UvIdx = "+UvIdx+", SkyCode = "+SkyCode+", WndChillC = "+WndChillC+", WndSpdKn = "+WndSpdKn+", WndChillF = "+WndChillF+", WndSpdKm = "+WndSpdKm+", WndSpdMph = "+WndSpdMph+", HeatIdxF = "+HeatIdxF+", PrecipChance = "+PrecipChance+", CloudCoverage = "+CloudCoverage+", WndDirDegr = "+WndDirDegr+", SkyMedium = "+SkyMedium+", UvDescr = "+UvDescr+", SkyCodeExt = "+SkyCodeExt+", ReportTime = "+ReportTime+", UvWarn = "+UvWarn+"]";
    }*/

}
