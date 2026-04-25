package net.isbg.currency.xmlapi.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
public class GetCommand {

    @JacksonXmlProperty(isAttribute = true, localName = "consumer")
    private String consumer;

    @JacksonXmlProperty(localName = "currency")
    private String currency;
}