package com.demo.services.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Maps;
import com.demo.services.CurrencyResolverService;

/**
 * Unit tests for {@link CurrencyConverterServiceImpl}.
 * 
 * @author RED
 */
public class CurrencyConverterServiceImplTest {

    private static final String MSG_AMOUNT_NOT_A_NUMBER = "\"Amount\" attribute is not a number";
    private static final String MSG_AMOUNT_REQUIRED = "\"Amount\" attribute is required";
    private static final String MSG_CONVERT_FROM_REQUIRED = "\"Convert From\" attribute is required";
    private static final String MSG_CONVERT_TO_REQUIRED = "\"Convert To\" attribute is required";

    private CurrencyConverterServiceImpl service;

    @Mock
    private CurrencyResolverService currencyResolverService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new CurrencyConverterServiceImpl();
        service.setCurrencyResolverService(currencyResolverService);
    }

    @Test
    public void shouldConvertFromEUR() {
        // given
        Map<String, BigDecimal> currenciesMap = Maps.newHashMap();
        currenciesMap.put("EUR", BigDecimal.ONE);
        currenciesMap.put("AUD", BigDecimal.valueOf(1.4438));

        service.setCurrencyFrom("EUR");
        service.setCurrencyTo("AUD");
        service.setAmountString("1");

        given(currencyResolverService.retrieveCurrenciesMap()).willReturn(currenciesMap);

        BigDecimal expectedResult = BigDecimal.valueOf(1).divide(BigDecimal.valueOf(1.4438), MathContext.DECIMAL128);
        BigDecimal expectedRoundedResult = expectedResult.setScale(4, RoundingMode.UP);

        // when
        BigDecimal result = service.convert();

        // then
        assertThat("Incorrect result retrieved", result, equalTo(expectedResult));
        assertThat("Incorrect rounded result retrieved", service.getRoundedResult(), equalTo(expectedRoundedResult));

    }

    @Test
    public void shouldConvertToEUR() {
        // given
        Map<String, BigDecimal> currenciesMap = Maps.newHashMap();
        currenciesMap.put("EUR", BigDecimal.ONE);
        currenciesMap.put("AUD", BigDecimal.valueOf(1.4438));

        service.setCurrencyFrom("AUD");
        service.setCurrencyTo("EUR");
        service.setAmountString("1");

        given(currencyResolverService.retrieveCurrenciesMap()).willReturn(currenciesMap);

        BigDecimal expectedResult = BigDecimal.valueOf(1).multiply(BigDecimal.valueOf(1.4438));
        BigDecimal expectedRoundedResult = expectedResult.setScale(4, RoundingMode.UP);

        // when
        BigDecimal result = service.convert();

        // then
        assertThat("Incorrect result retrieved", result, equalTo(expectedResult));
        assertThat("Incorrect rounded result retrieved", service.getRoundedResult(), equalTo(expectedRoundedResult));

    }

    @Test
    public void shouldConvertCurrencies() {
        // given
        Map<String, BigDecimal> currenciesMap = Maps.newHashMap();
        currenciesMap.put("BGN", BigDecimal.valueOf(1.9558));
        currenciesMap.put("AUD", BigDecimal.valueOf(1.4438));

        service.setCurrencyFrom("AUD");
        service.setCurrencyTo("BGN");
        service.setAmountString("1");

        given(currencyResolverService.retrieveCurrenciesMap()).willReturn(currenciesMap);

        BigDecimal expectedResult = BigDecimal.valueOf(1).multiply(BigDecimal.valueOf(1.4438))
                .divide(BigDecimal.valueOf(1.9558), MathContext.DECIMAL128);
        BigDecimal expectedRoundedResult = expectedResult.setScale(4, RoundingMode.UP);

        // when
        BigDecimal result = service.convert();

        // then
        assertThat("Incorrect result retrieved", result, equalTo(expectedResult));
        assertThat("Incorrect rounded result retrieved", service.getRoundedResult(), equalTo(expectedRoundedResult));

    }

    @Test
    public void shouldNotConvertWhenAmountIsNotValid() {
        // given
        Map<String, BigDecimal> currenciesMap = Maps.newHashMap();
        currenciesMap.put("BGN", BigDecimal.valueOf(1.9558));
        currenciesMap.put("AUD", BigDecimal.valueOf(1.4438));

        service.setCurrencyFrom("AUD");
        service.setCurrencyTo("BGN");
        service.setAmountString("a");

        given(currencyResolverService.retrieveCurrenciesMap()).willReturn(currenciesMap);

        // when
        BigDecimal result = service.convert();

        // then
        assertThat("Incorrect result retrieved", result, equalTo(null));
        assertThat("Incorrect warning messages quantity retrieved", service.getMessages().size(), equalTo(1));
        assertThat("Incorrect warning message retrieved", service.getMessages().get(0), equalTo(MSG_AMOUNT_NOT_A_NUMBER));
    }

    @Test
    public void shouldNotConvertWhenAmountNotEntered() {
        // given
        Map<String, BigDecimal> currenciesMap = Maps.newHashMap();
        currenciesMap.put("BGN", BigDecimal.valueOf(1.9558));
        currenciesMap.put("AUD", BigDecimal.valueOf(1.4438));

        service.setCurrencyFrom("AUD");
        service.setCurrencyTo("BGN");
        service.setAmountString("");

        given(currencyResolverService.retrieveCurrenciesMap()).willReturn(currenciesMap);

        // when
        BigDecimal result = service.convert();

        // then
        assertThat("Incorrect result retrieved", result, equalTo(null));
        assertThat("Incorrect warning messages quantity retrieved", service.getMessages().size(), equalTo(1));
        assertThat("Incorrect warning message retrieved", service.getMessages().get(0), equalTo(MSG_AMOUNT_REQUIRED));
    }

    @Test
    public void shouldNotConvertWhenCurrencyFromNotSelected() {
        // given
        Map<String, BigDecimal> currenciesMap = Maps.newHashMap();
        currenciesMap.put("BGN", BigDecimal.valueOf(1.9558));
        currenciesMap.put("AUD", BigDecimal.valueOf(1.4438));

        service.setCurrencyFrom("");
        service.setCurrencyTo("BGN");
        service.setAmountString("1");

        given(currencyResolverService.retrieveCurrenciesMap()).willReturn(currenciesMap);

        // when
        BigDecimal result = service.convert();

        // then
        assertThat("Incorrect result retrieved", result, equalTo(null));
        assertThat("Incorrect warning messages quantity retrieved", service.getMessages().size(), equalTo(1));
        assertThat("Incorrect warning message retrieved", service.getMessages().get(0), equalTo(MSG_CONVERT_FROM_REQUIRED));
    }

    @Test
    public void shouldNotConvertWhenNoDataProvided() {
        // given
        Map<String, BigDecimal> currenciesMap = Maps.newHashMap();
        currenciesMap.put("BGN", BigDecimal.valueOf(1.9558));
        currenciesMap.put("AUD", BigDecimal.valueOf(1.4438));

        service.setCurrencyFrom("");
        service.setCurrencyTo("");
        service.setAmountString("");

        given(currencyResolverService.retrieveCurrenciesMap()).willReturn(currenciesMap);

        // when
        BigDecimal result = service.convert();

        // then
        assertThat("Incorrect result retrieved", result, equalTo(null));
        assertThat("Incorrect warning messages quantity retrieved", service.getMessages().size(), equalTo(3));
        assertTrue("Incorrect warning message retrieved", service.getMessages().contains(MSG_AMOUNT_REQUIRED));
        assertTrue("Incorrect warning message retrieved", service.getMessages().contains(MSG_CONVERT_FROM_REQUIRED));
        assertTrue("Incorrect warning message retrieved", service.getMessages().contains(MSG_CONVERT_TO_REQUIRED));
    }

    @Test
    public void shouldNotConvertWhenCurrencyToNotSelected() {
        // given
        Map<String, BigDecimal> currenciesMap = Maps.newHashMap();
        currenciesMap.put("BGN", BigDecimal.valueOf(1.9558));
        currenciesMap.put("AUD", BigDecimal.valueOf(1.4438));

        service.setCurrencyFrom("AUD");
        service.setCurrencyTo("");
        service.setAmountString("1");

        given(currencyResolverService.retrieveCurrenciesMap()).willReturn(currenciesMap);

        // when
        BigDecimal result = service.convert();

        // then
        assertThat("Incorrect result retrieved", result, equalTo(null));
        assertThat("Incorrect warning messages quantity retrieved", service.getMessages().size(), equalTo(1));
        assertThat("Incorrect warning message retrieved", service.getMessages().get(0), equalTo(MSG_CONVERT_TO_REQUIRED));
    }
}
