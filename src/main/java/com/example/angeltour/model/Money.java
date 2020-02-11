package com.example.angeltour.model;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@EqualsAndHashCode
@ToString
public class Money {
    private float amount;
    private String currencyCode;
}
