package net.isbg.currency.jsonapi.dto;

public record  CurrentRequest(String requestId, long timestamp, String client, String currency) {}