package net.isbg.currency.jsonapi.service;

import net.isbg.currency.jsonapi.dto.CurrencyRateResponse;
import net.isbg.currency.jsonapi.dto.CurrentRequest;
import net.isbg.currency.jsonapi.dto.HistoryRequest;

import java.util.List;

public interface CurrencyService {
    CurrencyRateResponse getCurrent(CurrentRequest request);
    List<CurrencyRateResponse> getHistory(HistoryRequest request);
}