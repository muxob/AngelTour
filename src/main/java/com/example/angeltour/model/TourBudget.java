package com.example.angeltour.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@AllArgsConstructor
@Builder
@Getter
@ToString
public class TourBudget {
    private String homeCountryCode;
    private float budgetPerCountry;
    private float totalBudget;
    private String currency;
}
