package com.ogawa.parstorius;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParsePosition;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;

@SuppressWarnings("unused")

/*
 * Supported Number types for T are;
 * Default rounding mode is {@link RoundingMode#HALF_UP}
 */

// T can be one of Byte, Short, Integer, Long, Float, Double, BigInteger, BigDecimal
public class NumberFormatter<T extends Number> extends Formatter<T, DecimalFormat, NumberFormatter<T>> {

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

    public NumberFormatter(Class<T> numberClassT, DecimalFormat decimalFormat) {
        this(numberClassT, decimalFormat, true);
    }


    public NumberFormatter(Class<T> numberClassT, DecimalFormat decimalFormat, boolean parseCaseInsensitive) {
        super(decimalFormat, parseCaseInsensitive);

        Objects.requireNonNull(numberClassT, "numberClassT");
        Objects.requireNonNull(numberClassT, "decimalFormat");

        // decouple passed decimal format from original to avoid any interference
        this.decimalFormat = (DecimalFormat) decimalFormat.clone();
        this.numberClassT = numberClassT;

        castMethod = getCastMethod(numberClassT);

        if (castMethod == null) {
            throw new IllegalArgumentException(numberClassT.getName() + " not supported");
        }

    }

    /* ************************************************************************** */
    /* ********************************* common ********************************* */
    /* ************************************************************************** */

    /* ****************************** common logic ****************************** */

    @SuppressWarnings("unchecked")
    private T cast(BigDecimal bigDecimal) {
        T result = (T) castMethod.apply(bigDecimal);
        return result;
    }

    private Function<BigDecimal, ? extends Number> getCastMethod(Class<T> numberClassT) {

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

    @Override String patternToString() {
        return decimalFormat.toString();
    }

    @Override public DecimalFormat getBaseFormatter() {
        return (DecimalFormat) decimalFormat.clone();
    }

    @Override protected NumberFormatter<T> init() {
        return this;
    }

    @Override protected NumberFormatter<T> copyProperties(NumberFormatter<T> sourceFormatter) {
        return super.copyProperties(sourceFormatter).setParseRoundingMode(this.getParseRoundingMode());
    }

    // instantiates a new Formatter with same local and formatPattern
    @Override public NumberFormatter<T> clone() {
        return new NumberFormatter<>(numberClassT, baseFormatter).copyProperties(this);
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
        Pattern p = Pattern.compile(
            "^" + ".".repeat(parsePosition) +
                "\\d(" + Pattern.quote(decimalFormat.getDecimalFormatSymbols().getExponentSeparator()) + ")\\d",
            Pattern.CASE_INSENSITIVE);

        return p.matcher(text).replaceFirst(decimalFormat.getDecimalFormatSymbols().getExponentSeparator());

    }

    @Override
    protected T parseText(String text, ParsePosition parsePosition) throws Exception {

        Number result;

        if (parseCaseInsensitive) {
            String stringToParse = replaceExponentCaseMismatch(text, parsePosition.getIndex());
            result = decimalFormat.parse(stringToParse, parsePosition);
        } else {
            result = decimalFormat.parse(text, parsePosition);
        }


        return result != null ? cast((BigDecimal) result) : null;

    }

    /* ************************************************************************** */
    /* ******************************* formatting ******************************* */
    /* ************************************************************************** */

    /* ****************************** format logic ****************************** */

    @Override
    protected String formatObject(final T object) {
        return decimalFormat.format(object);
    }

    /* ************************************************************************** */
    /* ****************************** static stuff ****************************** */
    /* ************************************************************************** */

    /**
     * Returns the decimal separator e.g. , or . for the default locale
     * @return decimal separator
     */
    public DecimalFormatSymbols getDecimalFormatSymbols() {
        return DecimalFormatSymbols.getInstance();
    }

    /**
     * Returns the decimal separator e.g. , or . for the passed locale
     * @param locale local for which the decimal separator shall be determined
     * @return decimal separator
     */
    public DecimalFormatSymbols getDecimalFormatSymbols(Locale locale) {
        return DecimalFormatSymbols.getInstance(locale);
    }

}
