package com.example.angeltour.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@AllArgsConstructor
@Builder
@Getter
@ToString
public class Tour {
    private String countryName;
    private String[] neighbors;
    private int tourCounts;
    private Money leftover;
    private List<CountryMoney> neighborCountryMoney;
}
