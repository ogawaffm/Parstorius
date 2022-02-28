/*
 * Copyright (c) 2020-2022 Kai Bächle
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.ogawa.parstorius;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.ParsePosition;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unused")

/*
 * Supported Number types for T are;
 * Default rounding mode is {@link RoundingMode#HALF_UP}
 */

/**
 * Since this formatter is based on java.text.DecimalFormat it can deal with
 * 1.234E1,  1.234E001,
 * 1.234E-1, 1.234E-001
 * but not with 1.234E+1, because signed exponents are not allowed.
 */

// T can be one of Byte, Short, Integer, Long, Float, Double, BigInteger, BigDecimal
public class NumberFormatter<T extends Number> extends Formatter<T, NumberFormatter<T>> {

    // final, because it is characteristic for the formatter it should not be changeable
    final private Class<T> numberClassT;
    // reference to BigDecimal method to cast to T (used by cast())
    final private Function<BigDecimal, ?> castMethod;
    final private DecimalFormat decimalFormat;

    /* ************************************************************************** */
    /* ****************************** constructors ****************************** */
    /* ************************************************************************** */

    public NumberFormatter(Class<T> numberClassT) {
        this(numberClassT, new DecimalFormat());
    }

    public NumberFormatter(Class<T> numberClassT, boolean parseCaseInsensitive) {
        this(numberClassT, new DecimalFormat(), parseCaseInsensitive);
    }

    public NumberFormatter(Class<T> numberClassT, DecimalFormat decimalFormat) {
        // use case-sensitive mode because DecimalFormat does not know about case-insensitivity
        this(numberClassT, decimalFormat, false);
    }

    public NumberFormatter(Class<T> numberClassT, DecimalFormat decimalFormat, boolean parseCaseInsensitive) {
        super(parseCaseInsensitive);

        Objects.requireNonNull(numberClassT, "numberClassT");
        Objects.requireNonNull(numberClassT, "decimalFormat");

        // decouple passed decimal format from original to avoid any interferences
        this.numberClassT = numberClassT;
        this.decimalFormat = (DecimalFormat) decimalFormat.clone();
        this.decimalFormat.setParseBigDecimal(true);

        castMethod = getCastMethod(numberClassT);

        if (castMethod == null) {
            throw new IllegalArgumentException(numberClassT.getName() + " not supported");
        }

    }

    /* ************************************************************************** */
    /* ********************************* common ********************************* */
    /* ************************************************************************** */

    /* ***************************** common getter ****************************** */

    /**
     * Returns a clone of the used DecimalFormat
     * @return internal DecimalFormat
     */
    public DecimalFormat getDecimalFormat() {
        return (DecimalFormat) decimalFormat.clone();
    }

    /* ****************************** common logic ****************************** */

    // TODO
    public String toString() {
        return new StringBuilder()
            .append(getClass().getSimpleName())
            .append(" for ")
            .append(" using format ")
            .append(decimalFormat.toPattern())
            .append(" parsing case-")
            .append(parseCaseInsensitive ? "insensitive" : "sensitive")
            .toString()
        ;
    }


    @SuppressWarnings("unchecked")
    private T cast(BigDecimal bigDecimal) {
        T result = (T) castMethod.apply(bigDecimal);
        return result;
    }

    static private Function<BigDecimal, ? extends Number> getCastMethod(Class numberClassT) {

        // integral primitives
        if (numberClassT.equals(Byte.class)) {
            return BigDecimal::byteValueExact;
        } else if (numberClassT.equals(Short.class)) {
            return BigDecimal::shortValueExact;
        } else if (numberClassT.equals(Integer.class)) {
            return BigDecimal::intValueExact;
        } else if (numberClassT.equals(Long.class)) {
            return BigDecimal::longValueExact;

            // float primitives
        } else if (numberClassT.equals(Float.class)) {
            return BigDecimal::floatValue;
        } else if (numberClassT.equals(Double.class)) {
            return BigDecimal::doubleValue;

            // big data types
        } else if (numberClassT.equals(BigInteger.class)) {
            return BigDecimal::toBigIntegerExact;
        } else if (numberClassT.equals(BigDecimal.class)) {
            return (Function<BigDecimal, BigDecimal>) bigDecimal -> bigDecimal;
        } else {
            // not supported
            return null;
        }

    }

    @Override protected NumberFormatter<T> init() {
        return this;
    }

    @Override protected NumberFormatter<T> copyProperties(NumberFormatter<T> sourceFormatter) {
        return super.copyProperties(sourceFormatter).setParseRoundingMode(this.getParseRoundingMode());
    }

    // instantiates a new Formatter with same local and formatPattern
    @Override public NumberFormatter<T> clone() {
        return new NumberFormatter<>(numberClassT, decimalFormat).copyProperties(this);
    }

    /* ************************************************************************** */
    /* ********************************* parsing ******************************** */
    /* ************************************************************************** */

    /* ****************************** parse setter ****************************** */

    /***
     * Set the rounding mode for parsing
     * @param parseRoundingMode Rounding mode for parsing larger numbers into smaller data type
     * @return the current rounding mode for parsing
     */
    public NumberFormatter<T> setParseRoundingMode(RoundingMode parseRoundingMode) {
        decimalFormat.setRoundingMode(parseRoundingMode);
        return init();
    }

    /* ****************************** parse getter ****************************** */

    /**
     * Returns the rounding mode
     * @return the rounding mode
     */
    public RoundingMode getParseRoundingMode() {
        return decimalFormat.getRoundingMode();
    }

    /* ******************************* parse logic ****************************** */

    private String replaceExponentCaseMismatch(String text, int parsePosition) {
        Pattern p = Pattern.compile("(" +
            ".".repeat(parsePosition) +
                ".*?\\d)(" + Pattern.quote(decimalFormat.getDecimalFormatSymbols().getExponentSeparator()) + ")(-?\\d.*)",
            Pattern.CASE_INSENSITIVE);

        Matcher matcher = p.matcher(text);
        if (matcher.find()) {
            return matcher.group(1) + decimalFormat.getDecimalFormatSymbols().getExponentSeparator() + matcher.group(3);
        } else {
            return text;
        }
//        return p.matcher(text).replaceFirst(decimalFormat.getDecimalFormatSymbols().getExponentSeparator());

    }

    @Override
    protected T parseText(String text, ParsePosition contextParsePosition) throws Exception {

        Number result;

        if (parseCaseInsensitive) {
            String stringToParse = replaceExponentCaseMismatch(text, contextParsePosition.getIndex());
            result = decimalFormat.parse(stringToParse, contextParsePosition);
        } else {
            result = decimalFormat.parse(text, contextParsePosition);
        }

        if (contextParsePosition.getErrorIndex() != -1) {
            throw ParseExceptionFactory.createParseException(contextParsePosition);
        } else {
            return cast((BigDecimal) result);
        }

//        return result != null ? cast((BigDecimal) result) : null;

    }

    /* ************************************************************************** */
    /* ******************************* formatting ******************************* */
    /* ************************************************************************** */

    /* ****************************** format logic ****************************** */

    @Override
    protected String formatObject(final T object) {
        return decimalFormat.format(object);
    }

}
