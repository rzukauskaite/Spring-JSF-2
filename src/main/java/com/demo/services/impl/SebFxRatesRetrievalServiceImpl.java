package com.demo.services.impl;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.MessageFormat;

import com.demo.services.SebFxRatesRetrievalService;
import com.demo.utils.StreamUtils;
/**
 * {@link SebFxRatesRetrievalService} implementation.
 * @author RED
 */
public class SebFxRatesRetrievalServiceImpl implements SebFxRatesRetrievalService {

    private String PATTERN_URL = "https://www.lb.lt/webservices/FxRates/FxRates.asmx/getCurrentFxRates?tp={0}";
    private String PATTERN_FAILURE_RESPONSE = "Failed : HTTP error code : {0}";

    /**
     * {@inheritDoc}
     */
    @Override
    public String retrieveFxRates(String tp) throws ProtocolException, IOException {
        URL url = new URL(MessageFormat.format(PATTERN_URL, tp));
        HttpURLConnection connection = connect(url);
        String xmlContent = StreamUtils.convertInputStreamToString(connection.getInputStream());
        connection.disconnect();
        return xmlContent;
    }

    /**
     * Connects to passed {@link URL}. If connection response code is not equal to 200
     * (i.e. successful connection response), then {@link RuntimeException} is thrown.
     * @param url link on which connection should be established
     * @return connection
     */
    protected HttpURLConnection connect(URL url) throws MalformedURLException,
            IOException, ProtocolException {
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Accept", "application/xml");

        if (connection.getResponseCode() != 200) {
            throw new RuntimeException(MessageFormat.format(PATTERN_FAILURE_RESPONSE,
                    connection.getResponseCode()));
        }

        return connection;
    }

}
