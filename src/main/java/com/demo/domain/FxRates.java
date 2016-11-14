package com.demo.domain;

import java.util.List;

import com.google.common.collect.Lists;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * {@link FxRates} is root of xml file. It provides list of {@link FxRate} xml elements.
 * @author RED
 */
@XStreamAlias("FxRates")
public class FxRates {

    /**
     * List of attributes, holding currencies data.
     */
    @XStreamImplicit(itemFieldName = "FxRate")
    private List<FxRate> fxRate = Lists.newArrayList();

    public FxRates() {}

    public FxRates(List<FxRate> fxRate) {
        super();
        this.fxRate = fxRate;
    }

    public List<FxRate> getFxRate() {
        return fxRate;
    }

    public void setFxRate(List<FxRate> fxRate) {
        this.fxRate = fxRate;
    }
}
