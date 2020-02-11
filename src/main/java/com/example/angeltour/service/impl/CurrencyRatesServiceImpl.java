package com.example.angeltour.service.impl;

import com.example.angeltour.model.CurrencyRates;
import com.example.angeltour.service.CurrencyRatesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class CurrencyRatesServiceImpl implements CurrencyRatesService {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyRatesServiceImpl.class);

    @Value("${rates-api.latest-rates}")
    private String ratesApiLatestRates;

    private final WebClient webClient;

    public CurrencyRatesServiceImpl(WebClient.Builder webClientBuilder,
                                    @Value("${rates-api.base-url}") String ratesApiBaseUrl)
    {
        webClient = webClientBuilder.baseUrl(ratesApiBaseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }

    @Override
    public Map<String, Float> getCurrencyRates(String baseCurrencyCode) {
        return webClient.get().uri(ratesApiLatestRates, baseCurrencyCode)
                .retrieve().bodyToMono(CurrencyRates.class)
                .doOnError(t -> logger.warn(t.toString(), t))
                .onErrorReturn(new CurrencyRates())
                .map(CurrencyRates::getRates)
                .block();
    }
}
