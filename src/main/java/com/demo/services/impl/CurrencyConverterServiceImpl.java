package com.demo.services.impl;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.demo.services.CurrencyConverterService;
import com.demo.services.CurrencyResolverService;
/**
 * {@link CurrencyConverterService} implementation.
 * @author RED
 */
public class CurrencyConverterServiceImpl implements CurrencyConverterService {

    private static final Logger LOG = LoggerFactory.getLogger(CurrencyConverterServiceImpl.class.getName());

    /**
     * Attribute indicates currency, from which required amount should be converted.
     * <p><b>Note:</b> it indicates user's selection in UI.
     */
    private String currencyFrom;
    /**
     * Attribute indicates currency, to which required amount should be converted.
     * <p><b>Note:</b> it indicates user's selection in UI.
     */
    private String currencyTo;
    /**
     * Attribute indicates amount, which should be converted.
     * <p><b>Note:</b> it indicates user's entered amount value in UI.
     */
    private String amountString;
    /**
     * Attribute indicates amount, which should be converted.
     * <p><b>Note:</b> it indicates user's entered amount value as a number.
     */
    private BigDecimal amount;
    /**
     * Attribute indicates conversion result.
     * <p><b>Note:</b> it is a result, calculated during currencies conversion.
     */
    private BigDecimal result;
    /**
     * Attribute indicates conversion result.
     * <p><b>Note:</b> it is a result, calculated during currencies conversion, rounded up.
     */
    private BigDecimal roundedResult;
    /**
     * Attribute holding warning messages, if user entered/selected values are incorrect.
     * <p><b>Note:</b> user entered/selected values are incorrect if validation
     * ({@link CurrencyConverterService#validate()}) fails.
     */
    private List<String> messages = Lists.newArrayList();

    private CurrencyResolverService currencyResolverService;

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal convert() {
        LOG.debug("[convert] method called");
        if (BooleanUtils.isFalse(validate())) {
            return null;
        }
        Map<String, BigDecimal> currencies = currencyResolverService.retrieveCurrenciesMap();

        if (StringUtils.equals(this.currencyFrom, "EUR")) {
            BigDecimal currencyRate = currencies.get(currencyTo);
            setResult(convertCurrencyFromEUR(currencyRate, amount));
            setRoundedResult(roundBigDecimal(result));
            return getResult();
        }

        if (StringUtils.equals(this.currencyTo, "EUR")) {
            BigDecimal currencyRate = currencies.get(currencyFrom);
            setResult(convertCurrencyToEUR(currencyRate, amount));
            setRoundedResult(roundBigDecimal(result));
            return getResult();
        }

        // else some currency should be converted to another currency
        BigDecimal rate1 = currencies.get(currencyFrom);
        BigDecimal rate2 = currencies.get(currencyTo);
        setResult(convertCurrencies(rate1, rate2, amount));
        setRoundedResult(roundBigDecimal(result));
        return getResult();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean validate() {
        LOG.debug("[validate] method called");
        clearMessages();

        if (isValueAssigned(currencyFrom, "Convert From")) {
            setCurrencyFrom(currencyFrom);
        }
        if (isValueAssigned(currencyTo, "Convert To")) {
            setCurrencyTo(currencyTo);
        }
        if (isValueAssigned(amountString, "Amount") && isNumber(amountString, "Amount")) {
            setAmount(new BigDecimal(amountString));
        }

        return CollectionUtils.isEmpty(messages);
    }

    /**
     * Checks if parameter has assigned value.
     * @param value parameter, which should be checks if contains value assigned
     * @param label parameters name, which will be used to construct a warning message if value is not assigned
     * @return {@code TRUE} if value is assigned, {@code FALSE} otherwise
     */
    protected boolean isValueAssigned(Object value, String label) {
        if (StringUtils.isNotEmpty((String)value)) {
            return true;
        }
        // otherwise attribute is not selected/entered
        messages.add(MessageFormat.format("\"{0}\" attribute is required", label));
        return false;
    }

    /**
     * Checks if passed parameter is a number.
     * @param value parameter, which should be checks if it is a number
     * @param label parameters name, which will be used to construct a warning message if value is not assigned
     * @return {@code TRUE} if parameter is a number, {@code FALSE} otherwise
     */
    protected boolean isNumber(String value, String label) {
        if (value.matches("^[0-9.;]+$")) {
            return true;
        }
        // otherwise attribute is not a number
        messages.add(MessageFormat.format("\"{0}\" attribute is not a number", label));
        return false;
    }

    /**
     * Converts amount to EUR.
     * <p> i.e.: user wants to convert 20 AUD to EUR.
     * If 1.4438 AUD = 1 EUR, then 20 AUD = x EUR.
     * So x = 20 AUD * 1 EUR / 1.4438 (indicates AUD currency rate) = 13.8523 EUR
     *
     * @param rate currency rate matches 1 EUR
     * @param amount that should be converted to EUR
     * @return amount converted to EUR
     */
    protected BigDecimal convertCurrencyToEUR(BigDecimal rate, BigDecimal amount) {
        LOG.debug(MessageFormat.format("[convertCurrencyFromEUR] parameters: currency rate (from): {0}, amount: {1}", rate, amount));
        return amount.multiply(rate);
    }

    /**
     * Converts amount from EUR to the required currency.
     * <p> i.e.: user wants to convert 20 EUR to AUD.
     * If 1.4438 AUD = 1 EUR, then x AUD = 20 EUR.
     * So x = 20 EUR * 1.4438 (indicates AUD currency rate) / 1 EUR = 28.876 AUD
     *
     * @param rate currency rate matches 1 EUR
     * @param amount that should be converted to EUR
     * @return amount converted to EUR
     */
    protected BigDecimal convertCurrencyFromEUR(BigDecimal rate, BigDecimal amount) {
        LOG.debug(MessageFormat.format("[convertCurrencyFromEUR] parameters: currency rate (to): {0}, amount: {1}", rate, amount));
        return amount.divide(rate, MathContext.DECIMAL128);
    }

    /**
     * Converts currency to another currency.
     * <p> i.e.: user wants to convert 20 AUD to BGN.
     * If 1.4438 AUD = 1 EUR and 1.9558 BGN = 1 EUR, then 20 AUD = x BGN.
     * So x = 20 EUR * 1.4438 (indicates AUD currency rate) / 1.9558 BGN (indicates BGN currency rate) = 14.7643 BGN
     * @param rate1 first currency rate, i.e. convert from
     * @param rate2 second currency rate, i.e. convert to
     * @param amount amount that should be converted
     * @return converted amount
     */
    protected BigDecimal convertCurrencies(BigDecimal rate1, BigDecimal rate2, BigDecimal amount) {
        LOG.debug(MessageFormat.format("[convertCurrencies] parameters: currency rate (from): {0}, currency rate (to): {1}, amount: {2}", rate1, rate2, amount));
        return convertCurrencyFromEUR(rate2, convertCurrencyToEUR(rate1, amount));
    }

    /**
     * Rounds amount up to following format: xx.xxxx.
     * @param amount value, which should be rounded
     * @return rounded amount
     */
    protected BigDecimal roundBigDecimal(BigDecimal amount) {
        return amount != null ? amount.setScale(4, RoundingMode.UP) : amount;
    }

    public CurrencyResolverService getCurrencyResolverService() {
        return currencyResolverService;
    }

    public void setCurrencyResolverService(CurrencyResolverService currencyResolverService) {
        this.currencyResolverService = currencyResolverService;
    }

    public BigDecimal getResult() {
        return result;
    }

    public void setResult(BigDecimal result) {
        this.result = result;
    }

    public String getCurrencyFrom() {
        return currencyFrom;
    }

    public void setCurrencyFrom(String currency) {
        this.currencyFrom = currency;
    }

    public String getCurrencyTo() {
        return currencyTo;
    }

    public void setCurrencyTo(String currency) {
        this.currencyTo = currency;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
          this.amount = amount;
      }

    public List<String> getMessages() {
        return messages;
    }

    public void setMessages(List<String> messages) {
        this.messages = messages;
    }

    public BigDecimal getRoundedResult() {
        return roundedResult;
    }

    public void setRoundedResult(BigDecimal result) {
        this.roundedResult = result;
    }

    public String getAmountString() {
        return amountString;
    }

    public void setAmountString(String amount) {
        this.amountString = amount;
    }

    public void clearMessages() {
        messages.clear();
    }
}
