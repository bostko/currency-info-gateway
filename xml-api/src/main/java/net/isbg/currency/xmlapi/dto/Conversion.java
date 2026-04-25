package net.isbg.currency.xmlapi.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class Conversion {

    @JacksonXmlProperty(isAttribute = true, localName = "currency")
    private final String currency;

    @JacksonXmlProperty(isAttribute = true, localName = "rate")
    private final double rate;

    public Conversion(String currency, double rate) {
        this.currency = currency;
        this.rate = rate;
    }

    public String getCurrency() { return currency; }
    public double getRate() { return rate; }
}
