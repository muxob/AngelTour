package com.example.angeltour.model;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class CountryMoney {
    private String countryCode;
    private String countryName;
    private Money tourMoney;
}
