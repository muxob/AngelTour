package com.example.angeltour.controller;

import com.example.angeltour.TourException;
import com.example.angeltour.model.Tour;
import com.example.angeltour.model.TourBudget;
import com.example.angeltour.service.TourService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TourApi {

    private static final Logger logger = LoggerFactory.getLogger(TourApi.class);

    @Autowired
    TourService tourService;

    @GetMapping(value = "/v1/tour-api", produces = MediaType.APPLICATION_JSON_VALUE)
    ResponseEntity<Tour> tour(@RequestParam("homeCountryCode") String homeCountryCode,
                              @RequestParam("budgetPerCountry") float budgetPerCountry,
                              @RequestParam("totalBudget") float totalBudget,
                              @RequestParam("currency") String currency)
    {
        try {
            return ResponseEntity.ok(tourService.tourNeighborCountries(
                    TourBudget.builder()
                            .homeCountryCode(homeCountryCode)
                            .budgetPerCountry(budgetPerCountry)
                            .totalBudget(totalBudget)
                            .currency(currency)
                            .build()
            ));
        } catch (TourException te) {
            logger.warn(te.getMessage(), te);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
