package net.isbg.currency.xmlapi.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Getter;

import java.util.List;

@JacksonXmlRootElement(localName = "command")
@Getter
public class CommandResponse {

    @JacksonXmlProperty(isAttribute = true, localName = "id")
    private final String requestId;

    @JacksonXmlProperty(isAttribute = true, localName = "consumer")
    private final String consumer;

    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "rate")
    private final List<RateResponse> rates;

    public CommandResponse(String requestId, String consumer, List<RateResponse> rates) {
        this.requestId = requestId;
        this.consumer = consumer;
        this.rates = rates;
    }
}