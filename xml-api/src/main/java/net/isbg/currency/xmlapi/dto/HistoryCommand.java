package net.isbg.currency.xmlapi.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Getter;

@Getter
public class HistoryCommand {

    @JacksonXmlProperty(isAttribute = true, localName = "consumer")
    private String consumer;

    @JacksonXmlProperty(isAttribute = true, localName = "currency")
    private String currency;

    @JacksonXmlProperty(isAttribute = true, localName = "period")
    private int period;
}