package com.demo.services.impl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;

import com.demo.domain.CcyAmt;
import com.demo.domain.FxRate;
import com.demo.domain.FxRates;
import com.demo.services.SebFxRatesRetrievalService;
import com.demo.services.XmlDeserializer;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.basic.DateConverter;
/**
 * {@link XmlDeserializer} implementation.
 * @author RED
 */
public class XmlDeserializerImpl implements XmlDeserializer {

    /**
     * Attribute, holding valid TP values, which are used to construct web services link.
     * This service link is used for data retrieval (XML document is retrieved).
     * <p><b>Note:</b> only 'LT' and 'EU' are valid values.
     */
    private List<String> tpList;

    private SebFxRatesRetrievalService sebFxRatesRetrievalService;

    /**
     * {@inheritDoc}
     * <p>Web services retrieves prefilled XML. This document is deserialized to domain and retrieved as list of {@link FxRate}.
     */
    @Override
    public List<FxRate> retrieveFxRates(String tp) throws MalformedURLException, ProtocolException, IOException {
        Assert.assertTrue("tp parameter should not be empty or null", StringUtils.isNotEmpty(tp));
        Assert.assertTrue("provided tp is not maintainned", tpList.contains(tp));

        String xmlContent = sebFxRatesRetrievalService.retrieveFxRates(tp);
        return deserialize(xmlContent);
    }

    /**
     * Deserializes xml document to JAVA domain.
     * @param xmlContent xml document, which should be deserialized
     *
     * @return list of {@link FxRate}
     */
    protected List<FxRate> deserialize(String xmlContent) {
        XStream xstream = new XStream();
        xstream.processAnnotations(FxRates.class);
        xstream.processAnnotations(FxRate.class);
        xstream.processAnnotations(CcyAmt.class);
        xstream.registerConverter(new DateConverter("yyyy-MM-dd", new String[] {}));
        FxRates fxRates = (FxRates) xstream.fromXML(xmlContent);

        return fxRates.getFxRate();
    }

    public SebFxRatesRetrievalService getSebFxRatesRetrievalService() {
        return sebFxRatesRetrievalService;
    }

    public void setSebFxRatesRetrievalService(SebFxRatesRetrievalService sebFxRatesRetrievalService) {
        this.sebFxRatesRetrievalService = sebFxRatesRetrievalService;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<String> getTpList() {
        return tpList;
    }

    public void setTpList(List<String> tpList) {
        this.tpList = tpList;
    }

}
