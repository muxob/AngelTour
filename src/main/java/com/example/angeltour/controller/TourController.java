package com.example.angeltour.controller;

import com.example.angeltour.model.Tour;
import com.example.angeltour.model.TourBudget;
import com.example.angeltour.service.TourService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.security.Principal;

@Controller
public class TourController {

    @Autowired
    TourService tourService;

    @GetMapping("/tour")
    public String tour(@RequestParam("homeCountryCode") String homeCountryCode,
                       @RequestParam("budgetPerCountry") float budgetPerCountry,
                       @RequestParam("totalBudget") float totalBudget,
                       @RequestParam("currency") String currency,
                       Model model, Principal principal)
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

        String username = "Angel";
        if (OAuth2AuthenticationToken.class.isAssignableFrom(principal.getClass())
            && ((OAuth2AuthenticationToken) principal).getPrincipal() != null
            && !StringUtils.isEmpty(((OAuth2AuthenticationToken) principal).getPrincipal().getAttribute("name")))
        {
            username = ((OAuth2AuthenticationToken) principal).getPrincipal().getAttribute("name");
        }
        model.addAttribute("username", username);
        return "tour";
    }
}
