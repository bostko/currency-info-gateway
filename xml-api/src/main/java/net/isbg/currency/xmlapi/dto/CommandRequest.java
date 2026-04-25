package net.isbg.currency.xmlapi.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@JacksonXmlRootElement(localName = "command")
@EqualsAndHashCode
@Getter
public class CommandRequest {

    @JacksonXmlProperty(isAttribute = true, localName = "id")
    private String requestId;

    @JacksonXmlProperty(localName = "get")
    private GetCommand get;

    @JacksonXmlProperty(localName = "history")
    private HistoryCommand history;
}