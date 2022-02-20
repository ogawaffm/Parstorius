package com.ogawa.parstorius;

import java.text.ParseException;
import java.text.ParsePosition;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@SuppressWarnings("unused")
// T is the type of the object to format or returned from parse, e.g. Date, LocalTime, String
// B is the type of base formatter used internally.

public abstract class Formatter<T, B, F extends Formatter<T, B, F>> {

    public static final String NULL_TEXT = "null";

    // internal formatter
    B baseFormatter;

    boolean parseCaseInsensitive;

    // parsing defaults
    T parseMissingDefault; // default on parsing an empty or blank string
    T parseNullDefault;    // default on parsing a representative or on a null-argument (which is no string instance)
    T parseErrorDefault;   // default on parsing error
    List<String> parseNullTexts; // list of null representatives, e.g. "null", "<null>", "-", "none", "."
    TRIM_MODE parseTrimMode;
    Function<String, Integer> getTrimSkipIndexFunction;
    boolean parseUntilEnd;
    
    int baseOffset; // number of chars skipped by trim 
    ParseException lastParseException;
    ParsePosition parsePosition;

    // format defaults
    String formatNullText;
    Object formatNullDefault;
    Object formatErrorDefault;

    /* ************************************************************************** */
    /* ****************************** constructors ****************************** */
    /* ************************************************************************** */

    // Text '2018-FEB-09' could not be parsed at index 5
    // "0.98..7.6.5,43E001"

    /**
     * Constructs a new formatter instance using the default pattern.
     * @param baseFormatter pattern for parsing and formatting
     */
    protected Formatter(final B baseFormatter, final boolean parseCaseInsensitive) {

        // internalize arguments
        this.parseCaseInsensitive = parseCaseInsensitive;
        this.baseFormatter = baseFormatter;

        // parse defaults
        this.parseNullDefault = null;
        this.parseMissingDefault = null;
        this.parseErrorDefault = null;
        this.parseUntilEnd = true;

        setParseNullTexts(Collections.emptyList());

        // format defaults
        this.formatNullDefault = null;
        this.formatErrorDefault = null;
        this.formatNullText = NULL_TEXT;

        parseTrimMode = TRIM_MODE.NONE;
        getTrimSkipIndexFunction = skipZeroFunction;
        baseOffset = 0;

    }

    /**
     * Initializer method to be invoked on construction or changes by setters, which need reinitialization
     */
    abstract protected F init();

    abstract public F clone();
    abstract String patternToString();

    /* ************************************************************************** */
    /* ********************************* common ********************************* */
    /* ************************************************************************** */

    /* ***************************** common getter ****************************** */

    /**
     * Returns the base formatter used for parsing and formatting
     * @return the base formatter used for parsing and formatting
     */
    abstract public B getBaseFormatter();

    /* ****************************** common logic ****************************** */

    protected F copyProperties(F sourceFormatter) {

        // internals
        this.baseOffset = sourceFormatter.baseOffset;
        this.parsePosition = sourceFormatter.getParsePosition();
        this.lastParseException = sourceFormatter.getLastParseException();

        // mode parameters
        this.setParseCaseInsensitive(sourceFormatter.getParseCaseInsensitive());
        this.setParseUntilEnd(sourceFormatter.getParseUntilEnd());
        this.setParseTrimMode(sourceFormatter.getParseTrimMode());

        // parse result values
        this.setParseMissingDefault(sourceFormatter.getParseMissingDefault());
        this.setParseNullTexts(sourceFormatter.getParseNullTexts());
        this.setParseNullDefault(sourceFormatter.getParseNullDefault());
        this.setParseErrorDefault(sourceFormatter.getParseErrorDefault());

        // format result values
        this.setFormatNullText(sourceFormatter.getFormatNullText());
        this.setFormatNullDefault(sourceFormatter.getFormatNullDefault());
        this.setFormatErrorDefault(sourceFormatter.getFormatErrorDefault());

        return (F) this;

    }

    /**
     * Checks if the passed collection contains the passed string. The check takes parse case-sensitivity into account.
     * @param collection collection to check against, if str is contained
     * @param str string to check, if it is contained in collection
     * @return boolean result of the contains-check
     */
    boolean contains(Collection<String> collection, String str) {
        if (parseCaseInsensitive) {
            return collection.stream().anyMatch(str::equalsIgnoreCase);
        } else {
            return collection.stream().anyMatch(str::equals);
        }
    }

    /**
     * Returns if the passed text has no value. This base implementation works fine for all, but
     * not for strings, for which one or more spaces is also a valid (non-missing) value
     * @param text text to check, if it is representing a missing value
     * @return boolean value, if the passed text is representing a missing value
     */
    /* ***** do not make static! Could be overridden in subclasses, e.g. for a string parser *****/
    protected boolean valueIsMissing(String text) {
        switch (parseTrimMode) {
        case NONE:
        default:
            return text.isEmpty();
        case TRIM:
        case TRIM_LEADING:
        case TRIM_TRAILING:
            return -1 != StringUtil.getFirstNonSpaceIndex(text, 0);
        case STRIP:
        case STRIP_LEADING:
        case STRIP_TRAILING:
            return -1 != StringUtil.getFirstNonWhiteSpaceIndex(text, 0);
        }

    }

    /**
     * Human-readable override ot toString() for debugging purposes
     * @return Human-readable string
     */
    @Override
    @SuppressWarnings("all") // for 'StringBuilder' can be replaced with 'String'
    public String toString() {
        return new StringBuilder()
            .append(getClass().getSimpleName())
            .append(" for ")
            .append(" using format ")
            .append(patternToString())
            .append(" parsing case-")
            .append(parseCaseInsensitive ? "insensitive" : "sensitive")
            .toString()
            ;
    }

    /* ************************************************************************** */
    /* ********************************* parsing ******************************** */
    /* ************************************************************************** */

    /* ****************************** parse setter ****************************** */

    /**
     * Sets if the parser is case-insensitive.
     * @param parseCaseInsensitive new value for the parse case-insensitive property
     * @return the formatter instance
     */
    public F setParseCaseInsensitive(final boolean parseCaseInsensitive) {
        this.parseCaseInsensitive = parseCaseInsensitive;
        return init();
    }

    /**
     * Sets the value to be returned on parsing a text with found null representative
     * (e.g. "null", "<null>", "-", ".", "none" - set using setParseNullTexts)
     * or on parsing on null argument instead of a string instance.
     * @param parseNullDefault value to be returned, on parsing of a null-text or a text with a null representative
     * @return the formatter instance
     */
    @SuppressWarnings("unchecked")
    public F setParseNullDefault(final T parseNullDefault) {
        this.parseNullDefault = parseNullDefault;
        return (F) this;
    }

    /**
     * Sets the values representing null (e.g. "null", "<null>", "-", ".", "none") in the string to be parsed.
     * Null itself is not an allowed value in the list.
     * @return the formatter instance
     */
    @SuppressWarnings("unchecked")
    public F setParseNullTexts(final List<String> parseNullTexts
    ) {
        this.parseNullTexts = List.copyOf(Objects.requireNonNullElse(parseNullTexts, Collections.EMPTY_LIST));
        return (F) this;
    }

    /**
     * Sets the value to be returned on parsing a text with a no value (missing value)
     * @param parseMissingDefault default returned on a missing value
     * @return the formatter instance
     */
    @SuppressWarnings("unchecked")
    public F setParseMissingDefault(final T parseMissingDefault) {
        this.parseMissingDefault = parseMissingDefault;
        return (F) this;
    }

    /**
     * Sets the value to be returned when a parse error occurs.
     * @param parseErrorDefault default returned on parse errors
     * @return the formatter instance
     */
    @SuppressWarnings("unchecked")
    public F setParseErrorDefault(final T parseErrorDefault) {
        this.parseErrorDefault = parseErrorDefault;
        return (F) this;
    }

    /**
     * Sets the mode of trimming to be applied on the text immediately before parsing. Leading and trailing
     * (white) spaces result in errors which are represented in the returned parseErrorDefault. Using an
     * adequate parseTrimMode can achieve a more lenient parsing.
     * @param parseTrimMode Function getting a string, applying an operation and returning a string
     * @return the formatter instance
     */
    @SuppressWarnings("unchecked")
    public F setParseTrimMode(final TRIM_MODE parseTrimMode) {
        this.parseTrimMode = parseTrimMode;
        this.getTrimSkipIndexFunction = getTrimSkipOffsetFunction();
        return (F) this;
    }

    /**
     * Sets if the parser is parses until the end of the text.
     * @param parseUntilEnd new value for the parse until end property
     * @return the formatter instance
     */
    public F setParseUntilEnd(final boolean parseUntilEnd) {
        this.parseUntilEnd = parseUntilEnd;
        return init();
    }

    /* ****************************** parse getter ****************************** */

    /**
     * Returns a copy of the current parse position
     * @return parse position
     */
    public ParsePosition getParsePosition() {
        ParsePosition clonedParsePosition = new ParsePosition(this.parsePosition.getIndex());
        clonedParsePosition.setErrorIndex(clonedParsePosition.getErrorIndex());
        return clonedParsePosition;
    }

    /**
     * Returns the parse trim mode
     * @return trim mode
     */
    public TRIM_MODE getParseTrimMode() {
        return this.parseTrimMode;
    }

    /**
     * Returns, if the formatter parsed the text case-insensitive
     * @return boolean parse-case-insensitive property
     */
    public boolean getParseCaseInsensitive() { return parseCaseInsensitive; }

    /**
     * Return the default value returned on parsing a null-text
     * @return the default value for null
     */
    public T getParseNullDefault() { return parseNullDefault; }

    /**
     * Returns a list of strings of which each is a valid textual representative of null
     * @return List of textual null representatives
     */
    public List<String> getParseNullTexts() { return parseNullTexts; }

    /**
     * Returns the default value returned on parsing a text which represents a missing-value
     * @return the default for missing values
     */
    public T getParseMissingDefault() { return parseMissingDefault; }

    /**
     * Returns the default value returned on parsing if an error occurred
     * @return the default value for errors
     */
    public T getParseErrorDefault() { return parseErrorDefault; }

    /**
     * Returns, if the formatter parses until the end of the text
     * @return boolean parse-complete property
     */
    public boolean getParseUntilEnd() { return parseUntilEnd; }

    /**
     * Returns the ParseException of the last parse or null if no such exception was raised. Calling this method
     * will not reset the ParseException of the last parse. Each parse will set its respective return value.
     * @return ParseException of the last parse or null.
     */
    public ParseException getLastParseException() { return lastParseException; }

    /* ******************************* parse logic ****************************** */

    /**
     * Parses the text and returns an instance of t or null to indicate an error. This is the core method
     * for paring, which has to be provided by all formatters. It is guaranteed that no null is passed to it.
     * On error the implementing method can raise any exception.
     * @param text (non-null)
     * @return instance of T or null in case of an error
     */
    protected abstract T parseText(final String text, ParsePosition parsePosition) throws Exception;

    final private static Function<String, Integer> skipZeroFunction = (s -> 0);

    final private static Function<String, Integer> skipAllTrailingSpacesFunction =
        (text -> Math.max(0, StringUtil.getFirstNonSpaceIndex(text, 0))
    );
    final private static Function<String, Integer> skipAllTrailingWhiteSpacesFunction =
        (text -> Math.max(0, StringUtil.getFirstNonWhiteSpaceIndex(text, 0))
    );
    protected Function<String, Integer> getTrimSkipOffsetFunction() {

        switch (parseTrimMode) {
            case NONE:
            case TRIM_TRAILING:
            case STRIP_TRAILING:
            default:
                return skipZeroFunction;

            case TRIM:
            case TRIM_LEADING:
                return skipAllTrailingSpacesFunction;

            case STRIP:
            case STRIP_LEADING:
                return skipAllTrailingWhiteSpacesFunction;
        }
    }

    /**
     * Parses the passed text and returns the resulting value of type T. In case of an error null is returned.
     * @param text text to be parsed. Null is not allowed, since a return value of null indicates an error.
     * @param parsePosition position to start at
     * @return Instance of T representing the parsed value
     */
    final public T parse(final String text, ParsePosition parsePosition,
        T parseMissingDefault, T parseNullDefault, T parseErrorDefault) {

        if (text == null) {
            return parseNullDefault;
        }

        // Calculate and save the number of chars skipped by application of the trim mode
        baseOffset = getTrimSkipIndexFunction.apply(text);

        // Trim just once to focus on the essential part of the string. Working with indexes does not really work out,
        // since building of substrings is foreseeable necessary. Doing so also simplifies the algorithm.
        String trimmedText = parseTrimMode.apply(text);
        trimmedText = text;

        if (valueIsMissing(trimmedText)) {

            return parseMissingDefault;

        } else if (contains(parseNullTexts, trimmedText.substring(baseOffset))) {

            return parseNullDefault;

        } else {

            T result = null;
            try {
                result = parseText(trimmedText, parsePosition);
                lastParseException = null;

            } catch (ParseException caughtParseException) {
                lastParseException = ParseExceptionFactory.createParseException(caughtParseException, baseOffset);

            } catch (DateTimeParseException caughtDateTimeParseException) {
                lastParseException = ParseExceptionFactory.createParseException(caughtDateTimeParseException, baseOffset);

            } catch (Exception exception) {
                lastParseException = ParseExceptionFactory.createParseException(exception, baseOffset);
            }

            return result != null ? result : parseErrorDefault;
        }
    }

    /**
     * Parses the passed text and returns the resulting value of type T. In case of an error null is returned.
     * @param text text to be parsed. Null is not allowed, since a return value of null indicates an error.
     * @return Instance of T representing the parsed value
     */
    final public T parse(final String text, T parseMissingDefault, T parseNullDefault, T parseErrorDefault) {
        return parse(text, new ParsePosition(0), parseMissingDefault, parseNullDefault, parseErrorDefault);
    }

    /**
     * Parses the passed text using the defaults of
     * {@link #getParseMissingDefault()}, {@link #getParseNullDefault()} and {@link #getParseErrorDefault()}.
     * @param text text to parse
     * @return Instance of T representing the parsed value
     */
    final public T parse(final String text) {
        return parse(text, new ParsePosition(0), parseMissingDefault, parseNullDefault, parseErrorDefault);
    }

    /**
     * Parses the passed text using the defaults of
     * {@link #getParseMissingDefault()}, {@link #getParseNullDefault()} and {@link #getParseErrorDefault()}.
     * @param text text to parse
     * @param parsePosition position to start at
     * @return Instance of T representing the parsed value
     */
    final public T parse(final String text, ParsePosition parsePosition) {
        return parse(text, parsePosition, parseMissingDefault, parseNullDefault, parseErrorDefault);
    }


    /* ************************************************************************** */
    /* ******************************* formatting ******************************* */
    /* ************************************************************************** */

    /* ***************************** format setter ****************************** */

    /**
     * Sets the value to be used when formatting a null value. If formatNullDefault is of type T it is formatted
     * otherwise the result of its .toString() method is used. If it is null getFormatNullText() is used.
     * @param formatNullDefault object representing null
     * @return the formatter instance
     */
    @SuppressWarnings("unchecked")
    public F setFormatNullDefault(final Object formatNullDefault) {
        this.formatNullDefault = formatNullDefault;
        return (F) this;
    }

    /**
     * Sets the value to be used when a format error occurs. If formatErrorDefault is of type T it is formatted
     * otherwise the result of its .toString() method is used. If it is null getFormatNullText() is used.
     * @param formatErrorDefault object representing the result of format if an error occurs
     * @return the formatter instance
     */
    @SuppressWarnings("unchecked")
    public F setFormatErrorDefault(final Object formatErrorDefault) {
        this.formatErrorDefault = formatErrorDefault;
        return (F) this;
    }

    /**
     * Sets the text representing null in the result of format. If formatNullText is null, "null" is taken.
     * The representation e.g. can be changed for
     * formatting towards SQL or SAS to "NULL" or "."
     * @param formatNullText text representing null in the result of format
     * @return the formatter instance
     */
    @SuppressWarnings("unchecked")
    public F setFormatNullText(final String formatNullText) {
        this.formatNullText = Objects.requireNonNullElse(formatNullText, NULL_TEXT);
        return (F) this;
    }

    /* ***************************** format getter ****************************** */

    public String getFormatNullText() { return formatNullText; }
    public Object getFormatNullDefault() { return formatNullDefault; }
    public Object getFormatErrorDefault() { return formatErrorDefault; }

    /* ****************************** format logic ****************************** */

    /**
     * Formats the passed object and returns its string representation or null if an error occurred. This is the core
     * method for formatting, which has to be provided by all formatters. It is guaranteed that no null is passed to it.
     * @param object object (non-null) to format
     * @return a string representing the formatted object
     */
    protected abstract String formatObject(final T object);

    @SuppressWarnings("all") // for:
    // Unchecked cast: 'java.lang.Object' to 'T'
    // Method invocation 'toString' may produce 'NullPointerException'
    private String formatObj(final Object object) {
        try {
            if (object == null) {
                return getFormatNullText();
            } else {
                return Objects.requireNonNullElse(formatObject((T) object), getFormatNullText());
            }

        } catch (ClassCastException e) {
            // object is not of type T, so its string representation is asked for
            return object.toString();
        }
    }

    /**
     * Formats the passed object using the defaults of
     * {@link #getFormatNullDefault()} and {@link #getFormatErrorDefault()}.
     * @param object object to format
     * @return the string representation of the formatted object
     */
    final public String format(final T object) {
        return format(object, this.formatNullDefault, this.formatErrorDefault);
    }

    /**
     * Formats the passed objectToFormat. If objectToFormat is null, formatting is based on defaultOnFormatNull.
     * If defaultOnFormatNull is of type T the formatter is applied, else its toString-representation is used.
     * If defaultOnFormatNull is null "null" is returned. In case of an error occurred formatting objectToFormat or
     * defaultOnFormatNull the formatting is based on defaultOnFormatError by the formatter if it is of type T,
     * else its toString-method is used or "null" is returned if defaultOnFormatError is null.
     * @param objectToFormat       object to format
     * @param defaultOnFormatNull  default value returned on the attempt to format null
     * @param defaultOnFormatError default value returned, if an error occurred
     * @return the formatted string representation ot the object to format or one of the defaults passed
     */
    final public String format(final T objectToFormat,
        final Object defaultOnFormatNull, final Object defaultOnFormatError) {

        String result;
        // Is objectToFormat a real instance to format?
        if (objectToFormat != null) {
            result = formatObject(objectToFormat);
            // Didn't formatting fail?
            if (result != null) {
                return result;
            } else {
                return formatObj(defaultOnFormatError);
            }
        } else {
            // no, work with default for null
            return formatObj(defaultOnFormatNull);
        }
    }

    /* ************************************************************************** */
    /* ****************************** static stuff ****************************** */
    /* ************************************************************************** */

    /**
     * Quotes the passed String with single quotes and escape all contained single quotes. For an empty string
     * an empty string is returned.
     * @param string string to be quoted to become a literal
     * @return Valid, quoted literal of string to use for DateTimeFormatter or DecimalFormatter
     */
    public static String getFormatterLiteral(String string) {
        Objects.requireNonNull(string, "string");
        return string.isEmpty() ? "" : "'" + string.replace("'", "''") + "'";
    }

}

