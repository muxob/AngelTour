package com.example.angeltour;

import com.example.angeltour.model.*;
import com.example.angeltour.service.CountryService;
import com.example.angeltour.service.CurrencyRatesService;
import com.example.angeltour.service.TourService;
import com.example.angeltour.service.impl.TourServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@SpringBootTest
class AngelTourApplicationTests {

	private static final String[] bgNeighbors = {"GRC", "MKD", "ROU", "SRB", "TUR"};

	@Mock
	private CountryService countryService;

	@Mock
	private CurrencyRatesService currencyRatesService;

	@InjectMocks
	private TourService tourService = new TourServiceImpl();

	@BeforeEach
	void setMockOutput() {
		Country bgrCountry = new Country();
		bgrCountry.setBorders(bgNeighbors);
		when(countryService.fetchCountry("BGR"))
				.thenReturn(bgrCountry);

		Country grcCountry = new Country();
		grcCountry.setName("Greece");
		grcCountry.setCurrencies(new String[] {"EUR"});
		when(countryService.fetchCountry("GRC"))
				.thenReturn(grcCountry);

		Country mkdCountry = new Country();
		mkdCountry.setName("Republic of Macedonia");
		mkdCountry.setCurrencies(new String[] {"MKD"});
		when(countryService.fetchCountry("MKD"))
				.thenReturn(mkdCountry);

		Country rouCountry = new Country();
		rouCountry.setName("Romania");
		rouCountry.setCurrencies(new String[] {"RON"});
		when(countryService.fetchCountry("ROU"))
				.thenReturn(rouCountry);

		Country srbCountry = new Country();
		srbCountry.setName("Serbia");
		srbCountry.setCurrencies(new String[] {"RSD"});
		when(countryService.fetchCountry("SRB"))
				.thenReturn(srbCountry);

		Country turCountry = new Country();
		turCountry.setName("Turkey");
		turCountry.setCurrencies(new String[] {"TRY"});
		when(countryService.fetchCountry("TUR"))
				.thenReturn(turCountry);


		Map<String, Float> rates  = new HashMap<String, Float>() {{
			put("TRY", 6.5897f);
			put("RON", 4.7663f);
		}};
		when(currencyRatesService.getCurrencyRates("EUR"))
				.thenReturn(rates);
	}

	@DisplayName("Test BG Tour")
	@Test
	void testBgTour() {
		CountryMoney turCost = new CountryMoney("TUR", "Turkey",
				new Money(1317.9401f, "TRY"));
		CountryMoney rouCost = new CountryMoney("ROU", "Romania",
				new Money(953.26f, "RON"));

		TourBudget tourBudget = new TourBudget("BGR", 100f, 1200f, "EUR");
		Tour tour = tourService.tourNeighborCountries(tourBudget);
		assertEquals(bgNeighbors, tour.getNeighbors());
		assertEquals(2, tour.getTourCounts());
		assertEquals(200, tour.getLeftover().getAmount());
		assertEquals(5, tour.getNeighborCountryMoney().size());
		assertTrue(tour.getNeighborCountryMoney().contains(turCost), "Incorrect cost for Turkey");
		assertTrue(tour.getNeighborCountryMoney().contains(rouCost), "Incorrect cost for Romania");
	}

}
