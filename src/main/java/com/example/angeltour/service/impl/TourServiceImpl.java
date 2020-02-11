package com.example.angeltour.service.impl;

import com.example.angeltour.TourException;
import com.example.angeltour.model.*;
import com.example.angeltour.service.CountryService;
import com.example.angeltour.service.CurrencyRatesService;
import com.example.angeltour.service.TourService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class TourServiceImpl implements TourService {

    private static final Logger logger = LoggerFactory.getLogger(TourServiceImpl.class);

    @Autowired
    private CountryService countryService;

    @Autowired
    private CurrencyRatesService currencyRatesService;

    @Override
    public Tour tourNeighborCountries(TourBudget tourBudget) {
        validateInput(tourBudget);

        Country homeCountry = countryService.fetchCountry(tourBudget.getHomeCountryCode());
        String[] neighbors = homeCountry.getBorders();
        int tourCounts;
        float leftover;
        List<CountryMoney> neighborCountryMoney = new ArrayList<>();

        if (neighbors.length > 0) {
            float singleTour = tourBudget.getBudgetPerCountry() * neighbors.length;
            tourCounts = (int)(tourBudget.getTotalBudget() / singleTour);
            leftover = tourBudget.getTotalBudget() % singleTour;
            if (tourCounts > 0) {
                neighborCountryMoney = getNeighborCountryMoney(
                        neighbors,
                        tourBudget.getBudgetPerCountry() * tourCounts,
                        tourBudget.getCurrency()
                );
            }
        } else {
            tourCounts = 0;
            leftover = tourBudget.getTotalBudget();
        }

        return Tour.builder()
                .neighbors(neighbors)
                .tourCounts(tourCounts)
                .leftover(new Money(leftover, tourBudget.getCurrency()))
                .neighborCountryMoney(neighborCountryMoney)
                .build();
    }

    private void validateInput(TourBudget tourBudget) {
        if (tourBudget == null) {
            throw new TourException("No budget.");
        }

        if (StringUtils.isEmpty(tourBudget.getHomeCountryCode())) {
            throw new TourException("No home country.");
        }

        if (tourBudget.getBudgetPerCountry() <= 0) {
            throw new TourException("No budget per country.");
        }

        if (tourBudget.getTotalBudget() <= 0) {
            throw new TourException("No total budget.");
        }

        if (StringUtils.isEmpty(tourBudget.getCurrency())) {
            throw new TourException("No currency.");
        }
    }

    private List<CountryMoney> getNeighborCountryMoney(String[] neighbors, float amountInBaseCurrency,
                                                       String baseCurrency)
    {
        Map<String, Float> rates = currencyRatesService.getCurrencyRates(baseCurrency);
        List<CountryMoney> neighborCountryMoney = new ArrayList<>();
        for (String neighborCode : neighbors) {
            Money countryCost = new Money(amountInBaseCurrency, baseCurrency);
            Country neighborCountry = countryService.fetchCountry(neighborCode);
            try {
                String[] neighborCurrencies = neighborCountry.getCurrencies();
                if (neighborCurrencies != null && neighborCurrencies.length > 0) {
                    String neighborCurrency = neighborCurrencies[0];
                    Float neighborCurrencyRate = rates.get(neighborCurrency);
                    if (neighborCurrencyRate != null) {
                        countryCost = new Money(amountInBaseCurrency * neighborCurrencyRate, neighborCurrency);
                    }
                }
            } catch (Exception e) {
                logger.warn("Get currency rates error", e);
            }

            neighborCountryMoney.add(CountryMoney.builder()
                    .countryCode(neighborCode)
                    .countryName(neighborCountry.getName())
                    .tourMoney(countryCost)
                    .build());
        }
        return neighborCountryMoney;
    }
}
