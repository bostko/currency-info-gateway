package net.isbg.currency.xmlapi.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class GetCommand {

    @JacksonXmlProperty(isAttribute = true, localName = "consumer")
    private String consumer;

    @JacksonXmlProperty(localName = "currency")
    private String currency;

    public String getConsumer() { return consumer; }
    public String getCurrency() { return currency; }
}