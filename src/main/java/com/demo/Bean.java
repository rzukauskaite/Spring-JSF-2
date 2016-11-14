package com.demo;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;

import com.demo.services.CurrencyConverterService;
import com.demo.services.CurrencyResolverService;
/**
 * Managed bean.
 * @author RED
 */
public class Bean {

    private CurrencyConverterService currencyConverterService;
    private CurrencyResolverService currencyResolverService;

    /**
     * Initialization method, which is invoked after all necessary properties on
     * the bean have been set.
     */
    public void init() throws MalformedURLException, ProtocolException, IOException {
        currencyResolverService.resolveCurrencies();
    }

    public CurrencyConverterService getCurrencyConverterService() {
        return currencyConverterService;
    }

    public void setCurrencyConverterService(CurrencyConverterService currencyConverterService) {
        this.currencyConverterService = currencyConverterService;
    }

    public CurrencyResolverService getCurrencyResolverService() {
        return currencyResolverService;
    }

    public void setCurrencyResolverService(CurrencyResolverService currencyResolverService) {
        this.currencyResolverService = currencyResolverService;
    }
}
