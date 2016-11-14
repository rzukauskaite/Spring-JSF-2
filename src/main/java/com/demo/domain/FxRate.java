package com.demo.domain;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;
/**
 * {@link FxRate} is main xml element of the root {@link FxRates}. It provides following
 * xml elements: Tp, Dt and list of {@link CcyAmt}.
 * @author RED
 */
@XStreamAlias("FxRate")
public class FxRate {

    /**
     * Attribute, holding tp. ONly 'LT' or 'EU' are valid values.
     */
    @XStreamAlias("Tp")
    private String tp;

    /**
     * Attribute, holding date, on which services of currency data retrieval is called.
     */
    @XStreamAlias("Dt")
    private Date dt;

    /**
     * List of attributes, holding currency data, i.e.: each attribute hold currency
     * title and currency rating in comparison with EUR.
     */
    @XStreamImplicit(itemFieldName = "CcyAmt")
    private List<CcyAmt> ccyAmt = Lists.newArrayList();

    public FxRate() {}

    public FxRate(String tp, Date dt, List<CcyAmt> ccyAmt) {
        super();
        this.tp = tp;
        this.dt = dt;
        this.ccyAmt = ccyAmt;
    }

    public String getTp() {
        return tp;
    }

    public void setTp(String tp) {
        this.tp = tp;
    }

    public Date getDt() {
        return dt;
    }

    public void setDt(Date dt) {
        this.dt = dt;
    }

    public List<CcyAmt> getCcyAmt() {
        return ccyAmt;
    }

    public void setCcyAmt(List<CcyAmt> ccyAmt) {
        this.ccyAmt = ccyAmt;
    }

}