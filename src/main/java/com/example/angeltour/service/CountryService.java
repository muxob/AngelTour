package com.example.angeltour.service;

import com.example.angeltour.model.Country;

public interface CountryService {
    Country fetchCountry(String countryCode);
}
