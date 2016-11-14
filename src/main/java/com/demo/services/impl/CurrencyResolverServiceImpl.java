package com.demo.services.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import com.google.common.collect.Maps;
import com.demo.domain.FxRate;
import com.demo.services.CurrencyResolverService;
import com.demo.services.XmlDeserializer;
/**
 * {@link CurrencyResolverService} implementation.
 * @author RED
 */
public class CurrencyResolverServiceImpl implements CurrencyResolverService {

    private Map<String, BigDecimal> currenciesMap = Maps.newHashMap();
    private XmlDeserializer xmlDeserializer;

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, BigDecimal> resolveCurrencies() throws MalformedURLException, ProtocolException, IOException {
        List<FxRate> fxRateList = xmlDeserializer.retrieveFxRates(xmlDeserializer.getTpList().get(1));//tp could be EU or LT
        if (CollectionUtils.isEmpty(fxRateList)) {
            return Maps.newHashMap();
        }

        for (int i = 0; i < fxRateList.size(); i++) {
            FxRate fxRate = fxRateList.get(i);
            if (fxRate == null) {
                continue;
            }
            // each element has 1st CcyAmt EUR and 2nd different. So, to not skip or duplicate EUR, add it only 
            if (i == 0) {
                currenciesMap.put(fxRate.getCcyAmt().get(0).getCcy(), fxRate.getCcyAmt().get(0).getAmt());
            }
            currenciesMap.put(fxRate.getCcyAmt().get(1).getCcy(), fxRate.getCcyAmt().get(1).getAmt());
        }
        return currenciesMap;
    }

    /**
     * Constructs map, where {@code key} is currency and {@code value} also currency.
     * This map usage: prefill drop down in UI.
     * @return constructed map
     */
    public Map<String, String> resolveCurrencyLabels() throws MalformedURLException, ProtocolException, IOException {
        if (MapUtils.isEmpty(currenciesMap)) {
            return Maps.newHashMap();
        }

        Map<String, String> currencies = Maps.newHashMap();
        for (String key : currenciesMap.keySet()) {
            currencies.put(key, key);
        }
        return currencies;
    }

    public XmlDeserializer getXmlDeserializer() {
        return xmlDeserializer;
    }

    public void setXmlDeserializer(XmlDeserializer xmlDeserializer) {
        this.xmlDeserializer = xmlDeserializer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<String, BigDecimal> retrieveCurrenciesMap() {
        return currenciesMap;
    }

}
