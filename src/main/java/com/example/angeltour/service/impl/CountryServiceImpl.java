package com.example.angeltour.service.impl;

import com.example.angeltour.TourException;
import com.example.angeltour.model.Country;
import com.example.angeltour.service.CountryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class CountryServiceImpl implements CountryService {

    private static final String X_RAPIDAPI_HOST = "x-rapidapi-host";
    private static final String X_RAPIDAPI_KEY = "x-rapidapi-key";

    private static final Logger logger = LoggerFactory.getLogger(CountryServiceImpl.class);

    @Value("${countries-api.fetch-country}")
    private String countriesApiFetchCountry;

    private final WebClient webClient;

    public CountryServiceImpl(WebClient.Builder webClientBuilder,
                              @Value("${countries-api.base-url}") String countriesApiBaseUrl,
                              @Value("${countries-api.header.host}") String countriesApiHost,
                              @Value("${countries-api.header.key}") String countriesApiKey)
    {
        webClient = webClientBuilder.baseUrl(countriesApiBaseUrl)
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader(X_RAPIDAPI_HOST, countriesApiHost)
                .defaultHeader(X_RAPIDAPI_KEY, countriesApiKey)
                .build();
    }

    @Override
    public Country fetchCountry(String countryCode) {
        return webClient.get().uri(countriesApiFetchCountry, countryCode)
                .retrieve().bodyToMono(Country.class)
                .doOnError(t -> {
                    logger.error(t.toString(), t);
                    throw new TourException("API error.");
                })
                .block();
    }
}
