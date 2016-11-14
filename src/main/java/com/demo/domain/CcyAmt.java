package com.demo.domain;

import java.math.BigDecimal;

import com.thoughtworks.xstream.annotations.XStreamAlias;
/**
 * {@link CcyAmt} is one of xml elements of the root {@link FxRate}. It provides following
 * xml elements: Ccy and Amt.
 * @author RED
 */
@XStreamAlias("CcyAmt")
public class CcyAmt {

    /**
     * Attribute, holding currency title.
     */
    @XStreamAlias("Ccy")
    private String ccy;

    /**
     * Attribute, holding currency rating.
     */
    @XStreamAlias("Amt")
    private BigDecimal amt;

    public CcyAmt() {}

    public CcyAmt(String ccy, BigDecimal amt) {
        super();
        this.ccy = ccy;
        this.amt = amt;
    }

    public String getCcy() {
        return ccy;
    }

    public void setCcy(String ccy) {
        this.ccy = ccy;
    }

    public BigDecimal getAmt() {
        return amt;
    }

    public void setAmt(BigDecimal amt) {
        this.amt = amt;
    }
}
