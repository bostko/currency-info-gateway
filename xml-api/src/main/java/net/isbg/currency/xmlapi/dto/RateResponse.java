package net.isbg.currency.xmlapi.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

import java.util.List;

@JacksonXmlRootElement(localName = "rate")
public class RateResponse {

    @JacksonXmlProperty(localName = "currency")
    private final String currency;

    @JacksonXmlProperty(localName = "base")
    private final String base;

    @JacksonXmlProperty(localName = "date")
    private final String date;

    @JacksonXmlProperty(localName = "timestamp")
    private final long timestamp;

    @JacksonXmlElementWrapper(localName = "conversions")
    @JacksonXmlProperty(localName = "conversion")
    private final List<Conversion> conversions;

    public RateResponse(String currency, String base, String date, long timestamp, List<Conversion> conversions) {
        this.currency = currency;
        this.base = base;
        this.date = date;
        this.timestamp = timestamp;
        this.conversions = conversions;
    }

    public String getCurrency() { return currency; }
    public String getBase() { return base; }
    public String getDate() { return date; }
    public long getTimestamp() { return timestamp; }
    public List<Conversion> getConversions() { return conversions; }
}