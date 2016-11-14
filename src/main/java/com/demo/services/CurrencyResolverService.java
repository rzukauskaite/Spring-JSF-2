package com.demo.services;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.Map;

import com.demo.Bean;
/**
 * Service for currencies resolving/retrieval.
 * @author RED
 */
public interface CurrencyResolverService {

    /**
     * Constructs map, where {@code key} is currency and {@code value} currency rate.
     * This map is initiated on application start up from {@link Bean#init()}. Since
     * currencies map is initiated once, it values should be reached by using
     * {@link CurrencyResolverService#retrieveCurrenciesMap()} method.
     * 
     * <p><b>Note:</b> tp = EU is chosen. TP list is initialized via Spring beans.
     * @return constructed map
     */
    public Map<String, BigDecimal> resolveCurrencies() throws MalformedURLException, ProtocolException, IOException;

    /**
     * Retrieves resolved currencies map, where {@code key} is currency and {@code value} currency rate.
     * @return constructed map
     */
    public Map<String, BigDecimal> retrieveCurrenciesMap();
}
