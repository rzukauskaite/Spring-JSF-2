package com.demo.services.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.demo.domain.CcyAmt;
import com.demo.domain.FxRate;
import com.demo.services.CurrencyResolverService;
import com.demo.services.XmlDeserializer;
import com.google.common.collect.Lists;
/**
 * Unit tests for {@link CurrencyResolverService}.
 * @author RED
 */
public class CurrencyResolverServiceImplTest {

    private CurrencyResolverServiceImpl service;

    @Mock
    private XmlDeserializer xmlDeserializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new CurrencyResolverServiceImpl();
        service.setXmlDeserializer(xmlDeserializer);
        given(xmlDeserializer.getTpList()).willReturn(Lists.newArrayList("LT", "EU"));
    }

    @Test
    public void shouldResolveCurrencies() throws MalformedURLException, ProtocolException, IOException {
        // given
        FxRate fxRate1 = createFxRate("AUD", BigDecimal.valueOf(1.4438));
        FxRate fxRate2 = createFxRate("BGN", BigDecimal.valueOf(1.9558));
        given(xmlDeserializer.retrieveFxRates("EU")).willReturn(Lists.newArrayList(fxRate1, fxRate2));

        // when
        Set<String> currencies = service.resolveCurrencies().keySet();

        // then
        assertThat(currencies.size(), equalTo(3));
        assertTrue(currencies.contains("EUR"));
        assertTrue(currencies.contains("AUD"));
        assertTrue(currencies.contains("BGN"));
    }

    @Test
    public void shouldNotResolveCurrenciesForEmptyList() throws MalformedURLException, ProtocolException, IOException {
        // given
        given(xmlDeserializer.retrieveFxRates("EU")).willReturn(Lists.<FxRate>newArrayList());

        // when
        Set<String> currencies = service.resolveCurrencies().keySet();

        // then
        assertTrue(CollectionUtils.isEmpty(currencies));
    }

    @Test
    public void shouldNotResolveCurrenciesForNull() throws MalformedURLException, ProtocolException, IOException {
        // given
        given(xmlDeserializer.retrieveFxRates("EU")).willReturn(null);

        // when
        Set<String> currencies = service.resolveCurrencies().keySet();

        // then
        assertTrue(CollectionUtils.isEmpty(currencies));
    }

    /**
     * Creates instance of {@link FxRate}.
     * @param currency currency
     * @param amount currency rate
     * @return prefilled instance of {@link FxRate}
     */
    private FxRate createFxRate(String currency, BigDecimal amount) {
        DateTime dt = new DateTime();
        CcyAmt ccyAmt = new CcyAmt(currency, amount);

        return new FxRate("EU", dt.toDate(), Lists.newArrayList(new CcyAmt("EUR", BigDecimal.ONE), ccyAmt));
    }
}
