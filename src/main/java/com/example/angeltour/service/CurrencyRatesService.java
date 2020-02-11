package com.example.angeltour.service;

import java.util.Map;

public interface CurrencyRatesService {
    Map<String, Float> getCurrencyRates(String baseCurrencyCode);
}
