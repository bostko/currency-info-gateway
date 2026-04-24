package net.isbg.currency.xmlapi.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "rate")
public class RateResponse {

    @JacksonXmlProperty(localName = "currency")
    private final String currency;

    @JacksonXmlProperty(localName = "rate")
    private final double rate;

    @JacksonXmlProperty(localName = "base")
    private final String base;

    @JacksonXmlProperty(localName = "date")
    private final String date;

    @JacksonXmlProperty(localName = "timestamp")
    private final long timestamp;

    public RateResponse(String currency, double rate, String base, String date, long timestamp) {
        this.currency = currency;
        this.rate = rate;
        this.base = base;
        this.date = date;
        this.timestamp = timestamp;
    }

    public String getCurrency() { return currency; }
    public double getRate() { return rate; }
    public String getBase() { return base; }
    public String getDate() { return date; }
    public long getTimestamp() { return timestamp; }
}