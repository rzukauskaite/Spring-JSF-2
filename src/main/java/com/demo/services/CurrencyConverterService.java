package com.demo.services;

import java.math.BigDecimal;
/**
 * Service for one currency to another conversion.
 * @author RED
 */
public interface CurrencyConverterService {

    /**
     * Converts one selected currency to another one.
     * @return counted result
     */
    public BigDecimal convert();

    /**
     * Validates if user's provided attribute values are valid.
     * @return {@code TRUE} if valid, {@code FALSE} otherwise
     */
    public boolean validate();
}
