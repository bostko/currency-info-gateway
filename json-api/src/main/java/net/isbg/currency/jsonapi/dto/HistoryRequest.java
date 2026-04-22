package net.isbg.currency.jsonapi.dto;

public record HistoryRequest(String requestId, long timestamp, String client, String currency, int period) {}