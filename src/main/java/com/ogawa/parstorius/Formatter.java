package com.ogawa.parstorius;

import java.text.ParseException;
import java.text.ParsePosition;
import java.time.format.DateTimeParseException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Abstract super class of all parsers with the main logic of a stateful, controlled formatter/parser.
 * @param <T> T is the type of the object to format or returned from parse, e.g. Date, LocalTime, String
 * @param <F> B is the type of base formatter used internally.
 */
@SuppressWarnings("unused")

public abstract class Formatter<T, F extends Formatter<T, F>> {

    public static final String NULL_TEXT = "null";

    boolean parseCaseInsensitive;

    // default when null was passed to parse instead of a text
    T parseOfNullDefault;

    // default on parsing an empty or blank string
    T parseMissingDefault;

    // default on parsing a representative or on a null-argument (which is no string instance)
    T parseNullTextDefault;

    // default on parsing error
    T parseErrorDefault;

    // list of null representatives, e.g. "null", "<null>", "-", "none", "."
    List<String> parseNullTexts;
    PARSE_SKIP_MODE parseSkipMode;
    boolean parseUntilEnd;
    PARSE_RESULT_CAUSE lastParseResultCause = null;

    // Exceptions are private, because subclasses shall raise it, but not set it
    private ParseException exceptionOnParsing;
    ParsePosition parsePosition;

    // format defaults
    String formatNullText;
    Object formatNullDefault;
    Object formatErrorDefault;
    FORMAT_RESULT_CAUSE lastFormatResultCause = null;

    // Exceptions are private, because subclasses shall raise it, but not set it
    private Exception exceptionOnFormatting;


    /* ************************************************************************** */
    /* ****************************** constructors ****************************** */
    /* ************************************************************************** */

    // Text '2018-FEB-09' could not be parsed at index 5
    // "0.98..7.6.5,43E001"

    /**
     * Constructs a new formatter
     * @param parseCaseInsensitive flag, if this formatter parses case-insensitive
     */
    protected Formatter(final boolean parseCaseInsensitive, final PARSE_SKIP_MODE parseSkipMode, boolean parseUntilEnd) {

        // internalize arguments
        this.parseCaseInsensitive = parseCaseInsensitive;

        // parse defaults
        this.parseOfNullDefault = null;
        this.parseNullTextDefault = null;
        this.parseMissingDefault = null;
        this.parseErrorDefault = null;
        this.parseUntilEnd = parseUntilEnd;
        this.parsePosition = new ParsePosition(0);

        setParseNullTexts(Collections.emptyList());

        // format defaults
        this.formatNullDefault = null;
        this.formatErrorDefault = null;
        this.formatNullText = NULL_TEXT;

        this.parseSkipMode = parseSkipMode;

    }

    /**
     * Initializer method to be invoked on construction or changes by setters, which need reinitialization
     */
    abstract protected F init();

    /**
     * Abstract clone method to be implemented by all subclasses.
     * @return the clone
     */
    abstract public F clone();

    /* ************************************************************************** */
    /* ********************************* common ********************************* */
    /* ************************************************************************** */

    /* ***************************** common getter ****************************** */

    /* ****************************** common logic ****************************** */

    /**
     * Copies all properties from the passed sourceFormatter to this instance for cloning
     * @param sourceFormatter source formatter to copy properties from
     * @return this
     */
    @SuppressWarnings("unchecked")
    protected F copyProperties(F sourceFormatter) {

        // internals
        this.parsePosition = sourceFormatter.getLastParsePosition();
        this.exceptionOnParsing = sourceFormatter.getExceptionOnParsing();
        this.exceptionOnFormatting = sourceFormatter.getExceptionOnFormatting();
        this.lastParseResultCause = sourceFormatter.lastParseResultCause;
        this.lastFormatResultCause = sourceFormatter.lastFormatResultCause;

        // mode parameters
        this.parseCaseInsensitive = sourceFormatter.getParseCaseInsensitive();
        this.setParseUntilEnd(sourceFormatter.getParseUntilEnd());
        this.setParseSkipMode(sourceFormatter.getParseSkipMode());

        // parse result values
        this.setParseNullTexts(sourceFormatter.getParseNullTexts());
        this.setParseOfNullDefault(sourceFormatter.getParseOfNullDefault());
        this.setParseNullTextDefault(sourceFormatter.getParseNullTextDefault());
        this.setParseMissingDefault(sourceFormatter.getParseMissingDefault());
        this.setParseErrorDefault(sourceFormatter.getParseErrorDefault());

        // format result values
        this.setFormatNullText(sourceFormatter.getFormatNullText());
        this.setFormatNullDefault(sourceFormatter.getFormatNullDefault());
        this.setFormatErrorDefault(sourceFormatter.getFormatErrorDefault());

        return (F) this;

    }

    private ParsePosition resetParsePosition(ParsePosition parsePosition) {
        parsePosition.setIndex(0);
        parsePosition.setErrorIndex(-1);
        return parsePosition;
    }

    private ParsePosition moveParsePosition(ParsePosition parsePosition, int move) {
        parsePosition.setIndex(parsePosition.getIndex() + move);
        return parsePosition;
    }

    private ParsePosition cloneParsePosition(ParsePosition sourceParsePosition) {
        ParsePosition clonedParsePosition = new ParsePosition(sourceParsePosition.getIndex());
        clonedParsePosition.setErrorIndex(sourceParsePosition.getErrorIndex());
        return clonedParsePosition;
    }

        /**
         * Checks if the passed collection find the passed string. The check takes parse case-sensitivity into account.
         * @param collection collection to check against, if str is contained
         * @param str string to check, if it is contained in collection
         * @return boolean result of the find-check
         */
    Optional<String> find(Collection<String> collection, final String str) {
        if (parseCaseInsensitive) {
            return collection.stream().filter(str::equalsIgnoreCase).findFirst();
        } else {
            return collection.stream().filter(str::equals).findFirst();
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
        switch (parseSkipMode) {
            case NO_SKIP:
            default:
                return text.isEmpty();
            case SPACES:
            case LEADING_SPACES:
            case TRAILING_SPACES:
                return -1 == StringUtil.getFirstNonSpaceIndex(text, 0);
            case WHITESPACES:
            case LEADING_WHITESPACES:
            case TRAILING_WHITESPACES:
                return -1 == StringUtil.getFirstNonWhiteSpaceIndex(text, 0);
        }

    }

    /* ************************************************************************** */
    /* ********************************* parsing ******************************** */
    /* ************************************************************************** */

    /* ****************************** parse setter ****************************** */

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
     * Sets the value to be returned when null was passed to parse instead of a text
     * @param parseOfNullDefault default returned if null was passed to parse instead of a text
     * @return the formatter instance
     */
    @SuppressWarnings("unchecked")
    public F setParseOfNullDefault(final T parseOfNullDefault) {
        this.parseOfNullDefault = parseOfNullDefault;
        return  (F) this;
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
     * Sets the value to be returned on parsing a text with found null representative
     * (e.g. "null", "<null>", "-", ".", "none" - set using setParseNullTexts)
     * or on parsing on null argument instead of a string instance.
     * @param parseNullTextDefault value to be returned, on parsing of a null-text or a text with a null representative
     * @return the formatter instance
     */
    @SuppressWarnings("unchecked")
    public F setParseNullTextDefault(final T parseNullTextDefault) {
        this.parseNullTextDefault = parseNullTextDefault;
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
     * Sets mode to be used to skip leading and trailing (white)spaces parsing a text. Using an adequate parseSkipMode
     * can achieve a more lenient parsing.
     * @param parseSkipMode skip mode for parsing
     * @return the formatter instance
     */
    @SuppressWarnings("unchecked")
    public F setParseSkipMode(final PARSE_SKIP_MODE parseSkipMode) {
        this.parseSkipMode = parseSkipMode;
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
     * Returns a copy of the last parse position. Before the first parse or on the attempt to parse null
     * instead of a text a parse position with an index of 0 and error index of -1 is returned, because
     * parsing itself did not start. A parse position with error index > -1 indicates a parse error.
     * @return parse position
     */
    public ParsePosition getLastParsePosition() {
        return cloneParsePosition(parsePosition);
    }

    /**
     * Returns the last parse result cause or null before the first parse.
     * @return last parse result cause
     */
    public PARSE_RESULT_CAUSE getLastParseResultCause() { return lastParseResultCause; }

    /**
     * Returns the mode used to skip leading and trailing (white)spaces before/after parsing a text.
     * @return skip mode for parsing
     */
    public PARSE_SKIP_MODE getParseSkipMode() {
        return this.parseSkipMode;
    }

    /**
     * Returns, if the formatter parsed the text case-insensitive
     * @return boolean parse-case-insensitive property
     */
    public boolean getParseCaseInsensitive() { return parseCaseInsensitive; }

    /**
     * Returns a list of strings of which each is a valid textual representative of null
     * @return List of textual null representatives
     */
    public List<String> getParseNullTexts() { return parseNullTexts; }

    /**
     * Returns the default which is returned if null is passed to parse instead of a text
     * @return default value on a passed null to parse
     */
    public T getParseOfNullDefault() {
        return parseOfNullDefault;
    }

    /**
     * Return the default value returned on parsing a null-text
     * @return the default value for null
     */
    public T getParseNullTextDefault() { return parseNullTextDefault; }

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
     * Returns the ParseException of the last parsing or null if no such exception was raised. Calling this method
     * will not reset the ParseException of the last parsing. Each parse will set its respective return value.
     * If no exception occurred or null was passed to parse null is returned.
     * @return ParseException of the last parsing or null.
     */
    public ParseException getExceptionOnParsing() { return exceptionOnParsing; }

    /* ******************************* parse logic ****************************** */

    /**
     * Parses the text and returns an instance of t or null to indicate an error. This is the core method
     * for paring, which has to be provided by all formatters. It is guaranteed that no null is passed to it.
     * On error the implementing method can raise any exception
     * @param text (non-null)
     * @return instance of T or null in case of an error
     */
    protected abstract T parseText(final String text, ParsePosition parsePosition) throws Exception;

    private T buildResult(final String text,
        final T result, final PARSE_RESULT_CAUSE parseResultCause, final ParseException parseException,
        final ParsePosition contextParsePosition) {

        lastParseResultCause = parseResultCause;
        exceptionOnParsing = parseException;

        // parsing until end expected but parsing stopped before end
        if (parseUntilEnd && text != null && parsePosition.getIndex() < text.length()) {

            // move over tailing (white)spaces with respect to the parse skip mode
            parsePosition.setIndex(parseSkipMode.getSkipTailingOffset(text, parsePosition.getIndex()));

            // still not beyond end?
            if (parsePosition.getIndex() < text.length()) {
                // yes, there were none-(white)spaces found -> ERROR
                parsePosition.setErrorIndex(parsePosition.getIndex());
                exceptionOnParsing = ParseExceptionFactory.createParseException(parsePosition);
                lastParseResultCause = PARSE_RESULT_CAUSE.ERROR;
            }
        }

        contextParsePosition.setIndex(parsePosition.getIndex());
        contextParsePosition.setErrorIndex(parsePosition.getErrorIndex());

        return result;
    }

    /**
     * Initializes the parse state of the formatter to an error free state
     * @param startParsePosition parse position to be initialized
     */
    private void initParseState(ParsePosition startParsePosition) {

        // copy start parse position, but signal an error-free state
        parsePosition.setIndex(startParsePosition.getIndex());
        parsePosition.setErrorIndex(-1);

        // reset exception before parse (because parseText could set it)
        exceptionOnParsing = null;

    }

    /**
     * Parses the passed text and returns the resulting value of type T. In case of an error null is returned.
     * @param text text to be parsed. Null is not allowed, since a return value of null indicates an error.
     * @param contextParsePosition position to start from and parse position receiving the new (error) position
     * @return Instance of T representing the parsed value
     */
    final public T parse(final String text, ParsePosition contextParsePosition,
        T parseOfNullDefault, T parseMissingDefault, T parseNullDefault, T parseErrorDefault) {

        // set error free state at start
        initParseState(contextParsePosition);

        if (text == null) {
            return buildResult(
                null, parseOfNullDefault, PARSE_RESULT_CAUSE.PARSE_OF_NULL, null, contextParsePosition);
        }

        if (text == text) {
            System.out.print("");
        }

        // Calculate and save the number of chars skipped by application of the skip mode
        parsePosition.setIndex(parseSkipMode.getSkipLeadingOffset(text, parsePosition.getIndex()));

        // skipped whole text?
        if (parsePosition.getIndex() == text.length()) {

            // yes, this is a missing value case
            return buildResult(text, parseMissingDefault, PARSE_RESULT_CAUSE.MISSING_VALUE,
                null, resetParsePosition(parsePosition));

        } else {

            // search for a text representing NULL
            Optional<String> foundNullText = find(parseNullTexts, text.substring(parsePosition.getIndex()));

            if (foundNullText.isPresent()) {

                moveParsePosition(parsePosition, foundNullText.get().length());

                return buildResult(text, parseNullDefault, PARSE_RESULT_CAUSE.NULL_AS_TEXT,
                    null, resetParsePosition(parsePosition));

            } else {

              T result = null;
                try {
                    result = parseText(text, parsePosition);

                } catch (ParseException caughtParseException) {
                    exceptionOnParsing = ParseExceptionFactory.createParseException(caughtParseException);

                } catch (DateTimeParseException caughtDateTimeParseException) {
                    exceptionOnParsing = ParseExceptionFactory.createParseException(caughtDateTimeParseException);

                } catch (Exception exception) {
                    exceptionOnParsing = ParseExceptionFactory.createParseException(exception);
                    parsePosition.setErrorIndex(parsePosition.getIndex());
                }

                // caught an exception here or was it set in parseText?
                if (exceptionOnParsing != null) {
                    return buildResult(text, parseErrorDefault, PARSE_RESULT_CAUSE.ERROR,
                        exceptionOnParsing, parsePosition);
                } else {
                    return buildResult(text, result, PARSE_RESULT_CAUSE.TEXT_VALUE, null, parsePosition);
                }

            }
        }
    }

    /**
     * Parses the passed text and returns the resulting value of type T. In case of an error null is returned.
     * @param text text to be parsed. Null is not allowed, since a return value of null indicates an error.
     * @return Instance of T representing the parsed value
     */
    final public T parse(final String text,
        T parseOfNullDefault, T parseMissingDefault, T parseNullDefault, T parseErrorDefault) {
        return parse(text, new ParsePosition(0),
            parseOfNullDefault, parseMissingDefault, parseNullDefault, parseErrorDefault);
    }

    /**
     * Parses the passed text using the defaults of
     * {@link #getParseMissingDefault()}, {@link #getParseNullTextDefault()} and {@link #getParseErrorDefault()}.
     * @param text text to parse
     * @return Instance of T representing the parsed value
     */
    final public T parse(final String text) {
        return parse(text, new ParsePosition(0),
            parseOfNullDefault, parseMissingDefault, parseNullTextDefault, parseErrorDefault);
    }

    /**
     * Parses the passed text using the defaults of
     * {@link #getParseMissingDefault()}, {@link #getParseNullTextDefault()} and {@link #getParseErrorDefault()}.
     * @param text text to parse
     * @param parsePosition position to start at
     * @return Instance of T representing the parsed value
     */
    final public T parse(final String text, ParsePosition parsePosition) {
        return parse(text, parsePosition,
            parseOfNullDefault, parseMissingDefault, parseNullTextDefault, parseErrorDefault);
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

    /**
     * Returns the text representing null
     * @return Text for null
     */
    public String getFormatNullText() { return formatNullText; }

    /**
     * Returns the default (substitute) object to be used to format when trying to format null
     * @return default object for null
     */
    public Object getFormatNullDefault() { return formatNullDefault; }

    /**
     * Returns the default (substitute) object to be used to format when an error occurred
     * @return default object in case of an error
     */
    public Object getFormatErrorDefault() { return formatErrorDefault; }

    /**
     * Returns the last format result cause or null before the first format.
     * @return last format result cause
     */
    public FORMAT_RESULT_CAUSE getLastFormatResultCause() { return lastFormatResultCause; }

    /**
     * Returns the Exception of the last formatting or null if no such exception was raised. Calling this method
     * will not reset the Exception of the last parse. Each formatting will set its respective return value.
     * @return Exception of the last formatting or null.
     */
    public Exception getExceptionOnFormatting() { return exceptionOnFormatting; }

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
                lastFormatResultCause = FORMAT_RESULT_CAUSE.FORMAT_ON_NULL;
                return getFormatNullText();
            } else {
                String result = formatObject((T) object);
                if (result == null) {
                    lastFormatResultCause = FORMAT_RESULT_CAUSE.FORMAT_ON_NULL;
                    return getFormatNullText();
                } else {
                    lastFormatResultCause = FORMAT_RESULT_CAUSE.OBJECT;
                    return result;
                }
            }

        } catch (ClassCastException e) {
            // object is not of type T, so its string representation is asked for
            lastFormatResultCause = FORMAT_RESULT_CAUSE.ERROR;
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
                lastFormatResultCause = FORMAT_RESULT_CAUSE.OBJECT;
                return result;
            } else {
                lastFormatResultCause = FORMAT_RESULT_CAUSE.ERROR;
                return formatObj(defaultOnFormatError);
            }
        } else {
            // no, work with default for null
            lastFormatResultCause = FORMAT_RESULT_CAUSE.FORMAT_ON_NULL;
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