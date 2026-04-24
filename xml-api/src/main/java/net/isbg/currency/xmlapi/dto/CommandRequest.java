package net.isbg.currency.xmlapi.dto;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;

@JacksonXmlRootElement(localName = "command")
public class CommandRequest {

    @JacksonXmlProperty(isAttribute = true, localName = "id")
    private String requestId;

    @JacksonXmlProperty(localName = "get")
    private GetCommand get;

    @JacksonXmlProperty(localName = "history")
    private HistoryCommand history;

    public String getRequestId() { return requestId; }
    public GetCommand getGet() { return get; }
    public HistoryCommand getHistory() { return history; }
}