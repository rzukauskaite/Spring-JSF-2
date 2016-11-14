package com.demo.services;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.List;

import com.demo.domain.FxRate;
/**
 * Deserialize XML document to JAVA.
 * @author RED
 */
public interface XmlDeserializer {

    /**
     * Retrieves list of {@link FxRate}.
     * @param tp TP value
     * @return list of {@link FxRate}
     */
    public List<FxRate> retrieveFxRates(String tp) throws MalformedURLException, ProtocolException, IOException;

    /**
     * Retrieves valid TP values list, which are used to construct web services link.
     * This service link is used for data retrieval (XML document is retrieved).
     * <p><b>Note:</b> only 'LT' and 'EU' are valid values.
     *
     * @return valid to values list
     */
    public List<String> getTpList();
}
