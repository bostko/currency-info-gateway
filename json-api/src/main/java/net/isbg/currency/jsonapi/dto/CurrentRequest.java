package net.isbg.currency.jsonapi.dto;

import java.time.Instant;

public record  CurrentRequest(String requestId, Instant timestamp, String client, String currency) {}