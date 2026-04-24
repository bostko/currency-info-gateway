package net.isbg.currency.xmlapi.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class HistoryCommand {

    @JacksonXmlProperty(isAttribute = true, localName = "consumer")
    private String consumer;

    @JacksonXmlProperty(isAttribute = true, localName = "currency")
    private String currency;

    @JacksonXmlProperty(isAttribute = true, localName = "period")
    private int period;

    public String getConsumer() { return consumer; }
    public String getCurrency() { return currency; }
    public int getPeriod() { return period; }
}