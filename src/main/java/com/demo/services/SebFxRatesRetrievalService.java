package com.demo.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
/**
 * Service for XML data retrieval from https://www.lb.lt/webservices/FxRates/FxRates.asmx/getCurrentFxRates?tp=<tp>.
 * @author RED
 */
public interface SebFxRatesRetrievalService {
    /**
     * Retrieves XML data from and converts to String.
     * @param tp used to build {@link URL}. Only 'LT' and 'EU' are maintained
     * @return XML content as String
     */
    public String retrieveFxRates(String tp) throws MalformedURLException, ProtocolException, IOException;
}
