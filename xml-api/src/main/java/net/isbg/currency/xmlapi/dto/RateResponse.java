package net.isbg.currency.xmlapi.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;

@JacksonXmlRootElement(localName = "rate")
@Getter
@EqualsAndHashCode
public class RateResponse {

    @JacksonXmlProperty(localName = "currency")
    private final String currency;

    @JacksonXmlProperty(localName = "date")
    private final String date;

    @JacksonXmlProperty(localName = "timestamp")
    private final long timestamp;

    @JacksonXmlElementWrapper(localName = "conversions")
    @JacksonXmlProperty(localName = "conversion")
    private final List<Conversion> conversions;

    public RateResponse(String currency, String date, long timestamp, List<Conversion> conversions) {
        this.currency = currency;
        this.date = date;
        this.timestamp = timestamp;
        this.conversions = conversions;
    }
}