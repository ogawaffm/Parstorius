package com.ogawa.parstorius;

import java.text.ParsePosition;
import java.util.Map;

@SuppressWarnings("unused")
// The constant value is the format, which is the result of parseText and is returned on format
// as its string representation
public class KeyValueFormatter<T> extends Formatter<T, Map<String, T>, KeyValueFormatter<T>> {

    private T format;

    /**
     * Constructs a new formatter instance using the default pattern.
     * @param parseCaseInsensitive flag, if the parser behaves case-insensitive
     * @param pattern pattern for parsing and formatting
     */
    protected KeyValueFormatter(final boolean parseCaseInsensitive, final Map<String, T> pattern) {
        super(pattern, parseCaseInsensitive);
    }


    @Override protected KeyValueFormatter<T> init() { return this; }

    @Override public KeyValueFormatter<T> clone() { return new KeyValueFormatter<>(parseCaseInsensitive,
        baseFormatter);
    }

    @Override String patternToString() {
        return null;
    }

    @Override public Map<String, T> getBaseFormatter() {
        return null;
    }

    @Override
    protected String formatObject(T object) {
        return format.toString();
    }

    @Override
    protected T parseText(String text, ParsePosition parsePosition) {
        return format;
    }

}
