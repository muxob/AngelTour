package com.example.angeltour.controller;

import com.example.angeltour.model.Tour;
import com.example.angeltour.model.TourBudget;
import com.example.angeltour.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class TourController {

    @Autowired
    TourService tourService;

    @GetMapping("/tour")
    public String tour(@RequestParam("homeCountryCode") String homeCountryCode,
                       @RequestParam("budgetPerCountry") float budgetPerCountry,
                       @RequestParam("totalBudget") float totalBudget,
                       @RequestParam("currency") String currency,
                       Model model)
    {
        Tour tour =  tourService.tourNeighborCountries(
                TourBudget.builder()
                        .homeCountryCode(homeCountryCode)
                        .budgetPerCountry(budgetPerCountry)
                        .totalBudget(totalBudget)
                        .currency(currency)
                        .build()
        );
        model.addAttribute("tour", tour);
        return "tour";
    }
}
