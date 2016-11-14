package com.demo.services.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.eq;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.demo.domain.FxRate;
import com.demo.services.SebFxRatesRetrievalService;
import com.google.common.collect.Lists;

import src.test.java.utils.FileReaderUtils;

/**
 * Unit tests for {@link XmlDeserializerImpl}.
 * 
 * @author RED
 */
public class XmlDeserializerImplTest {

    private static final String STR_LT = "LT";
    private static final String STR_EU = "EU";

    private XmlDeserializerImpl service;

    private DateTime dateTime;

    @Mock
    private SebFxRatesRetrievalService sebFxratesRetrievalService;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        service = new XmlDeserializerImpl();
        service.setSebFxRatesRetrievalService(sebFxratesRetrievalService);
        service.setTpList(Lists.newArrayList(STR_EU, STR_LT));

        dateTime = new DateTime(2016, 11, 4, 2, 0);
    }

    @Test
    public void shouldRetrieveFxRatesForTpEU() throws MalformedURLException, ProtocolException, IOException {
        // given
        String xmlContent = FileReaderUtils.readFile("D:/seb-demo-app/src/test/resources/testDataForEU.xml");
        given(sebFxratesRetrievalService.retrieveFxRates(eq("EU"))).willReturn(xmlContent);

        // when
        List<FxRate> results = service.retrieveFxRates(STR_EU);

        // then
        assertTrue("Retrieved list should not be empty", CollectionUtils.isNotEmpty(results));
        assertThat("First list element should not be empty", results.get(0), notNullValue());
        assertThat("Incorrect Tp retrieved", ((FxRate) results.get(0)).getTp(), equalTo(STR_EU));
        assertThat("Incorrect Dt retrieved", ((FxRate) results.get(0)).getDt(), equalTo(dateTime.toDate()));
        assertThat("Incorrect Ccy retrieved", ((FxRate) results.get(0)).getCcyAmt().get(0).getCcy(), equalTo("EUR"));
        assertThat("Incorrect Amt retrieved", ((FxRate) results.get(0)).getCcyAmt().get(0).getAmt(),
                equalTo(BigDecimal.ONE));
        assertThat("Incorrect Ccy retrieved", ((FxRate) results.get(0)).getCcyAmt().get(1).getCcy(), equalTo("AUD"));
        assertThat("Incorrect Amt retrieved", ((FxRate) results.get(0)).getCcyAmt().get(1).getAmt(),
                equalTo(BigDecimal.valueOf(1.4438)));
        assertThat("Second list element should not be empty", results.get(1), notNullValue());
        assertThat("Incorrect Tp retrieved", ((FxRate) results.get(1)).getTp(), equalTo(STR_EU));
        assertThat("Incorrect Dt retrieved", ((FxRate) results.get(1)).getDt(), equalTo(dateTime.toDate()));
        assertThat("Incorrect Ccy retrieved", ((FxRate) results.get(1)).getCcyAmt().get(0).getCcy(), equalTo("EUR"));
        assertThat("Incorrect Amt retrieved", ((FxRate) results.get(1)).getCcyAmt().get(0).getAmt(),
                equalTo(BigDecimal.ONE));
        assertThat("Incorrect Ccy retrieved", ((FxRate) results.get(1)).getCcyAmt().get(1).getCcy(), equalTo("BGN"));
        assertThat("Incorrect Amt retrieved", ((FxRate) results.get(1)).getCcyAmt().get(1).getAmt(),
                equalTo(BigDecimal.valueOf(1.9558)));
    }

    @Test
    public void shouldRetrieveFxRatesForTpLT() throws MalformedURLException, ProtocolException, IOException {
        // given
        String xmlContent = FileReaderUtils.readFile("D:/seb-demo-app/src/test/resources/testDataForLT.xml");
        given(sebFxratesRetrievalService.retrieveFxRates(eq("LT"))).willReturn(xmlContent);

        // when
        List<FxRate> results = service.retrieveFxRates(STR_LT);

        // then
        assertTrue("Retrieved currency list should not be empty", CollectionUtils.isNotEmpty(results));
        assertThat("First list element should not be empty", results.get(0), notNullValue());
        assertThat("Incorrect Tp retrieved", ((FxRate) results.get(0)).getTp(), equalTo(STR_EU));
        assertThat("Incorrect Dt retrieved", ((FxRate) results.get(0)).getDt(), equalTo(dateTime.toDate()));
        assertThat("Incorrect currency (which should be converted from) retrieved",
                ((FxRate) results.get(0)).getCcyAmt().get(0).getCcy(), equalTo("EUR"));
        assertThat("Incorrect amount (which should be converted from) retrieved",
                ((FxRate) results.get(0)).getCcyAmt().get(0).getAmt(), equalTo(BigDecimal.ONE));
        assertThat("Incorrect currency (which should be converted to) retrieved",
                ((FxRate) results.get(0)).getCcyAmt().get(1).getCcy(), equalTo("AED"));
        assertThat("Incorrect amount (which should be converted to) retrieved",
                ((FxRate) results.get(0)).getCcyAmt().get(1).getAmt(), equalTo(BigDecimal.valueOf(4.01165)));
        assertThat("Second list element should not be empty", results.get(1), notNullValue());
        assertThat("Incorrect Tp retrieved", ((FxRate) results.get(1)).getTp(), equalTo(STR_EU));
        assertThat("Incorrect Dt retrieved", ((FxRate) results.get(1)).getDt(), equalTo(dateTime.toDate()));
        assertThat("Incorrect currency (which should be converted from) retrieved",
                ((FxRate) results.get(1)).getCcyAmt().get(0).getCcy(), equalTo("EUR"));
        assertThat("Incorrect amount (which should be converted from) retrieved",
                ((FxRate) results.get(1)).getCcyAmt().get(0).getAmt(), equalTo(BigDecimal.ONE));
        assertThat("Incorrect currency (which should be converted to) retrieved",
                ((FxRate) results.get(1)).getCcyAmt().get(1).getCcy(), equalTo("AFN"));
        assertThat("Incorrect amount (which should be converted to) retrieved",
                ((FxRate) results.get(1)).getCcyAmt().get(1).getAmt(), equalTo(BigDecimal.valueOf(72.15073)));
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowExceptionWhenTpDoNotExist() throws MalformedURLException, ProtocolException, IOException {
        // when
        service.retrieveFxRates("EU1");
    }

    @Test(expected = AssertionError.class)
    public void shouldThrowExceptionWhenTpNull() throws MalformedURLException, ProtocolException, IOException {
        // when
        service.retrieveFxRates(null);
    }
}
