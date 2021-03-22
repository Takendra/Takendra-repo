package com.viewlift.models.data.appcms.weather;


import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.Root;

@Root(name = "Day" ,strict = false)
public class Day {

    @Attribute(name = "ValidDateUtc" , required = false)
    private String ValidDateUtc;

    @Attribute(name = "WndDirCardinalNight" , required = false)
    private String WndDirCardinalNight;

    @Attribute(name = "PhraseNight" , required = false)
    private String PhraseNight;

    @Attribute(name = "WndDirDegrNight" , required = false)
    private String WndDirDegrNight;

    @Attribute(name = "HiTempF" , required = false)
    private String HiTempF;

    @Attribute(name = "IconCode" , required = false)
    private String IconCode;

    @Attribute(name = "SkyCodeNight" , required = false)
    private String SkyCodeNight;

    @Attribute(name = "PhraseDay" , required = false)
    private String PhraseDay;

    @Attribute(name = "DayNum" , required = false)
    private String DayNum;

    @Attribute(name = "MoonPhase" , required = false)
    private String MoonPhase;

    @Attribute(name = "SnowAmtIn" , required = false)
    private String SnowAmtIn;

    @Attribute(name = "RelHumidity" , required = false)
    private String RelHumidity;

    @Attribute(name = "HiTempC" , required = false)
    private String HiTempC;

    @Attribute(name = "WndSpdKnNight" , required = false)
    private String WndSpdKnNight;

    @Attribute(name = "IconCodeNight" , required = false)
    private String IconCodeNight;

    @Attribute(name = "SnowChance" , required = false)
    private String SnowChance;

    @Attribute(name = "PhraseNightC" , required = false)
    private String PhraseNightC;

    @Attribute(name = "WndSpdMphNight" , required = false)
    private String WndSpdMphNight;

    @Attribute(name = "PrecipChanceNight" , required = false)
    private String PrecipChanceNight;

    @Attribute(name = "Moonset" , required = false)
    private String Moonset;

    @Attribute(name = "RelHumidityNight" , required = false)
    private String RelHumidityNight;

    @Attribute(name = "PrecipChance" , required = false)
    private String PrecipChance;

    @Attribute(name = "PhraseDayC" , required = false)
    private String PhraseDayC;

    @Attribute(name = "CloudCoverage" , required = false)
    private String CloudCoverage;

    @Attribute(name = "WndDirDegr" , required = false)
    private String WndDirDegr;

    @Attribute(name = "Moonrise" , required = false)
    private String Moonrise;

    @Attribute(name = "UvDescr" , required = false)
    private String UvDescr;

    @Attribute(name = "DayOfWk" , required = false)
    private String DayOfWk;

    @Attribute(name = "IconCodeDay" , required = false)
    private String IconCodeDay;

    @Attribute(name = "SkyText" , required = false)
    private String SkyText;

    @Attribute(name = "WndDirCardinal" , required = false)
    private String WndDirCardinal;

    @Attribute(name = "ValidDateLocal" , required = false)
    private String ValidDateLocal;

    @Attribute(name = "LoTempF" , required = false)
    private String LoTempF;

    @Attribute(name = "LoTempC" , required = false)
    private String LoTempC;

    @Attribute(name = "Sunset" , required = false)
    private String Sunset;

    @Attribute(name = "SnowAmtCm" , required = false)
    private String SnowAmtCm;

    @Attribute(name = "SkyTextDay" , required = false)
    private String SkyTextDay;

    @Attribute(name = "WndSpdKmNight" , required = false)
    private String WndSpdKmNight;

    @Attribute(name = "UvIdx" , required = false)
    private String UvIdx;

    @Attribute(name = "SkyCode" , required = false)
    private String SkyCode;

    @Attribute(name = "Sunrise" , required = false)
    private String Sunrise;

    @Attribute(name = "WndSpdKn" , required = false)
    private String WndSpdKn;

    @Attribute(name = "WndSpdKm" , required = false)
    private String WndSpdKm;

    @Attribute(name = "SkyTextNight" , required = false)
    private String SkyTextNight;

    @Attribute(name = "WndSpdMph" , required = false)
    private String WndSpdMph;

    @Attribute(name = "MoonPhaseText" , required = false)
    private String MoonPhaseText;

    @Attribute(name = "ShortPhrase" , required = false)
    private String ShortPhrase;

    @Attribute(name = "SkyCodeDay" , required = false)
    private String SkyCodeDay;

    @Attribute(name = "PrecipChanceDay" , required = false)
    private String PrecipChanceDay;

    @Attribute(name = "UvWarn" , required = false)
    private String UvWarn;

    public String getValidDateUtc ()
    {
        return ValidDateUtc;
    }

    public void setValidDateUtc (String ValidDateUtc)
    {
        this.ValidDateUtc = ValidDateUtc;
    }

    public String getWndDirCardinalNight ()
    {
        return WndDirCardinalNight;
    }

    public void setWndDirCardinalNight (String WndDirCardinalNight)
    {
        this.WndDirCardinalNight = WndDirCardinalNight;
    }

    public String getPhraseNight ()
    {
        return PhraseNight;
    }

    public void setPhraseNight (String PhraseNight)
    {
        this.PhraseNight = PhraseNight;
    }

    public String getWndDirDegrNight ()
    {
        return WndDirDegrNight;
    }

    public void setWndDirDegrNight (String WndDirDegrNight)
    {
        this.WndDirDegrNight = WndDirDegrNight;
    }

    public String getHiTempF ()
    {
        return HiTempF;
    }

    public void setHiTempF (String HiTempF)
    {
        this.HiTempF = HiTempF;
    }

    public String getIconCode ()
    {
        return IconCode;
    }

    public void setIconCode (String IconCode)
    {
        this.IconCode = IconCode;
    }

    public String getSkyCodeNight ()
    {
        return SkyCodeNight;
    }

    public void setSkyCodeNight (String SkyCodeNight)
    {
        this.SkyCodeNight = SkyCodeNight;
    }

    public String getPhraseDay ()
    {
        return PhraseDay;
    }

    public void setPhraseDay (String PhraseDay)
    {
        this.PhraseDay = PhraseDay;
    }

    public String getDayNum ()
    {
        return DayNum;
    }

    public void setDayNum (String DayNum)
    {
        this.DayNum = DayNum;
    }

    public String getMoonPhase ()
    {
        return MoonPhase;
    }

    public void setMoonPhase (String MoonPhase)
    {
        this.MoonPhase = MoonPhase;
    }

    public String getSnowAmtIn ()
    {
        return SnowAmtIn;
    }

    public void setSnowAmtIn (String SnowAmtIn)
    {
        this.SnowAmtIn = SnowAmtIn;
    }

    public String getRelHumidity ()
    {
        return RelHumidity;
    }

    public void setRelHumidity (String RelHumidity)
    {
        this.RelHumidity = RelHumidity;
    }

    public String getHiTempC ()
    {
        return HiTempC;
    }

    public void setHiTempC (String HiTempC)
    {
        this.HiTempC = HiTempC;
    }

    public String getWndSpdKnNight ()
    {
        return WndSpdKnNight;
    }

    public void setWndSpdKnNight (String WndSpdKnNight)
    {
        this.WndSpdKnNight = WndSpdKnNight;
    }

    public String getIconCodeNight ()
    {
        return IconCodeNight;
    }

    public void setIconCodeNight (String IconCodeNight)
    {
        this.IconCodeNight = IconCodeNight;
    }

    public String getSnowChance ()
    {
        return SnowChance;
    }

    public void setSnowChance (String SnowChance)
    {
        this.SnowChance = SnowChance;
    }

    public String getPhraseNightC ()
    {
        return PhraseNightC;
    }

    public void setPhraseNightC (String PhraseNightC)
    {
        this.PhraseNightC = PhraseNightC;
    }

    public String getWndSpdMphNight ()
    {
        return WndSpdMphNight;
    }

    public void setWndSpdMphNight (String WndSpdMphNight)
    {
        this.WndSpdMphNight = WndSpdMphNight;
    }

    public String getPrecipChanceNight ()
    {
        return PrecipChanceNight;
    }

    public void setPrecipChanceNight (String PrecipChanceNight)
    {
        this.PrecipChanceNight = PrecipChanceNight;
    }

    public String getMoonset ()
    {
        return Moonset;
    }

    public void setMoonset (String Moonset)
    {
        this.Moonset = Moonset;
    }

    public String getRelHumidityNight ()
    {
        return RelHumidityNight;
    }

    public void setRelHumidityNight (String RelHumidityNight)
    {
        this.RelHumidityNight = RelHumidityNight;
    }

    public String getPrecipChance ()
    {
        return PrecipChance;
    }

    public void setPrecipChance (String PrecipChance)
    {
        this.PrecipChance = PrecipChance;
    }

    public String getPhraseDayC ()
    {
        return PhraseDayC;
    }

    public void setPhraseDayC (String PhraseDayC)
    {
        this.PhraseDayC = PhraseDayC;
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

    public String getMoonrise ()
    {
        return Moonrise;
    }

    public void setMoonrise (String Moonrise)
    {
        this.Moonrise = Moonrise;
    }

    public String getUvDescr ()
    {
        return UvDescr;
    }

    public void setUvDescr (String UvDescr)
    {
        this.UvDescr = UvDescr;
    }

    public String getDayOfWk ()
    {
        return DayOfWk;
    }

    public void setDayOfWk (String DayOfWk)
    {
        this.DayOfWk = DayOfWk;
    }

    public String getIconCodeDay ()
    {
        return IconCodeDay;
    }

    public void setIconCodeDay (String IconCodeDay)
    {
        this.IconCodeDay = IconCodeDay;
    }

    public String getSkyText ()
    {
        return SkyText;
    }

    public void setSkyText (String SkyText)
    {
        this.SkyText = SkyText;
    }

    public String getWndDirCardinal ()
    {
        return WndDirCardinal;
    }

    public void setWndDirCardinal (String WndDirCardinal)
    {
        this.WndDirCardinal = WndDirCardinal;
    }

    public String getValidDateLocal ()
    {
        return ValidDateLocal;
    }

    public void setValidDateLocal (String ValidDateLocal)
    {
        this.ValidDateLocal = ValidDateLocal;
    }

    public String getLoTempF ()
    {
        return LoTempF;
    }

    public void setLoTempF (String LoTempF)
    {
        this.LoTempF = LoTempF;
    }

    public String getLoTempC ()
    {
        return LoTempC;
    }

    public void setLoTempC (String LoTempC)
    {
        this.LoTempC = LoTempC;
    }

    public String getSunset ()
    {
        return Sunset;
    }

    public void setSunset (String Sunset)
    {
        this.Sunset = Sunset;
    }

    public String getSnowAmtCm ()
    {
        return SnowAmtCm;
    }

    public void setSnowAmtCm (String SnowAmtCm)
    {
        this.SnowAmtCm = SnowAmtCm;
    }

    public String getSkyTextDay ()
    {
        return SkyTextDay;
    }

    public void setSkyTextDay (String SkyTextDay)
    {
        this.SkyTextDay = SkyTextDay;
    }

    public String getWndSpdKmNight ()
    {
        return WndSpdKmNight;
    }

    public void setWndSpdKmNight (String WndSpdKmNight)
    {
        this.WndSpdKmNight = WndSpdKmNight;
    }

    public String getUvIdx ()
    {
        return UvIdx;
    }

    public void setUvIdx (String UvIdx)
    {
        this.UvIdx = UvIdx;
    }

    public String getSkyCode ()
    {
        return SkyCode;
    }

    public void setSkyCode (String SkyCode)
    {
        this.SkyCode = SkyCode;
    }

    public String getSunrise ()
    {
        return Sunrise;
    }

    public void setSunrise (String Sunrise)
    {
        this.Sunrise = Sunrise;
    }

    public String getWndSpdKn ()
    {
        return WndSpdKn;
    }

    public void setWndSpdKn (String WndSpdKn)
    {
        this.WndSpdKn = WndSpdKn;
    }

    public String getWndSpdKm ()
    {
        return WndSpdKm;
    }

    public void setWndSpdKm (String WndSpdKm)
    {
        this.WndSpdKm = WndSpdKm;
    }

    public String getSkyTextNight ()
    {
        return SkyTextNight;
    }

    public void setSkyTextNight (String SkyTextNight)
    {
        this.SkyTextNight = SkyTextNight;
    }

    public String getWndSpdMph ()
    {
        return WndSpdMph;
    }

    public void setWndSpdMph (String WndSpdMph)
    {
        this.WndSpdMph = WndSpdMph;
    }

    public String getMoonPhaseText ()
    {
        return MoonPhaseText;
    }

    public void setMoonPhaseText (String MoonPhaseText)
    {
        this.MoonPhaseText = MoonPhaseText;
    }

    public String getShortPhrase ()
    {
        return ShortPhrase;
    }

    public void setShortPhrase (String ShortPhrase)
    {
        this.ShortPhrase = ShortPhrase;
    }

    public String getSkyCodeDay ()
    {
        return SkyCodeDay;
    }

    public void setSkyCodeDay (String SkyCodeDay)
    {
        this.SkyCodeDay = SkyCodeDay;
    }

    public String getPrecipChanceDay ()
    {
        return PrecipChanceDay;
    }

    public void setPrecipChanceDay (String PrecipChanceDay)
    {
        this.PrecipChanceDay = PrecipChanceDay;
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
        return "ClassPojo [ValidDateUtc = "+ValidDateUtc+", WndDirCardinalNight = "+WndDirCardinalNight+", PhraseNight = "+PhraseNight+", WndDirDegrNight = "+WndDirDegrNight+", HiTempF = "+HiTempF+", IconCode = "+IconCode+", SkyCodeNight = "+SkyCodeNight+", PhraseDay = "+PhraseDay+", DayNum = "+DayNum+", MoonPhase = "+MoonPhase+", SnowAmtIn = "+SnowAmtIn+", RelHumidity = "+RelHumidity+", HiTempC = "+HiTempC+", WndSpdKnNight = "+WndSpdKnNight+", IconCodeNight = "+IconCodeNight+", SnowChance = "+SnowChance+", PhraseNightC = "+PhraseNightC+", WndSpdMphNight = "+WndSpdMphNight+", PrecipChanceNight = "+PrecipChanceNight+", Moonset = "+Moonset+", RelHumidityNight = "+RelHumidityNight+", PrecipChance = "+PrecipChance+", PhraseDayC = "+PhraseDayC+", CloudCoverage = "+CloudCoverage+", WndDirDegr = "+WndDirDegr+", Moonrise = "+Moonrise+", UvDescr = "+UvDescr+", DayOfWk = "+DayOfWk+", IconCodeDay = "+IconCodeDay+", SkyText = "+SkyText+", WndDirCardinal = "+WndDirCardinal+", ValidDateLocal = "+ValidDateLocal+", LoTempF = "+LoTempF+", LoTempC = "+LoTempC+", Sunset = "+Sunset+", SnowAmtCm = "+SnowAmtCm+", SkyTextDay = "+SkyTextDay+", WndSpdKmNight = "+WndSpdKmNight+", UvIdx = "+UvIdx+", SkyCode = "+SkyCode+", Sunrise = "+Sunrise+", WndSpdKn = "+WndSpdKn+", WndSpdKm = "+WndSpdKm+", SkyTextNight = "+SkyTextNight+", WndSpdMph = "+WndSpdMph+", MoonPhaseText = "+MoonPhaseText+", ShortPhrase = "+ShortPhrase+", SkyCodeDay = "+SkyCodeDay+", PrecipChanceDay = "+PrecipChanceDay+", UvWarn = "+UvWarn+"]";
    }

}

