package com.ogawa.parstorius;

import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.BiPredicate;

@SuppressWarnings("unused")
public class BooleanFormatter extends Formatter<Boolean, List<String>, BooleanFormatter> {

    BiPredicate<String, String> caseFunction;
    private ArrayList<String> trueRepresentatives;
    private ArrayList<String> falseRepresentatives;

    public static final List<String> DEFAULT_PATTERN = List.of(Boolean.TRUE.toString(), Boolean.FALSE.toString());

    /* ************************************************************************** */
    /* ****************************** constructors ****************************** */
    /* ************************************************************************** */

    public BooleanFormatter() {
        this(false);
    }
    public BooleanFormatter(boolean parseCaseInsensitive) {
        this(parseCaseInsensitive, null);
    }
    public BooleanFormatter(List<String> pattern) {
        this(false, pattern);
    }

    public BooleanFormatter(boolean parseCaseInsensitive, List<String> pattern) {
        super(pattern, parseCaseInsensitive);
        setParseCaseInsensitive(parseCaseInsensitive);
        init();
    }

    /* ************************************************************************** */
    /* ********************************* common ********************************* */
    /* ************************************************************************** */

    /* ****************************** common logic ****************************** */

    @Override String patternToString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < trueRepresentatives.size(); i++) {
            sb.append(trueRepresentatives.get(i)).append("/").append(falseRepresentatives.get(i));
            if (i < trueRepresentatives.size() - 1) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }

    @Override public List<String> getBaseFormatter() {
        return null;
    }

    @Override protected BooleanFormatter init() {

        baseFormatter = Objects.requireNonNullElse(baseFormatter, DEFAULT_PATTERN);

        if (this.baseFormatter.size() < 2) {
            throw new IllegalArgumentException(
                "pattern list must have at least two values. One true and one false representative.");
        }

        if ((this.baseFormatter.size() % 2) != 0) {
            throw new IllegalArgumentException(
                "pattern list must have an even size to represent true/false tuples.");
        }
        trueRepresentatives = new ArrayList<>(baseFormatter.size() / 2);
        falseRepresentatives = new ArrayList<>(baseFormatter.size() / 2);

        if (parseCaseInsensitive) {
            caseFunction = String::equalsIgnoreCase;
        } else {
            caseFunction = String::equals;
        }

        for (int i = 0; i < baseFormatter.size(); i++){
            if (i % 2 == 0) {
                trueRepresentatives.add(baseFormatter.get(i));
            } else {
                falseRepresentatives.add(baseFormatter.get(i));
            }
        }

        return this;
    }

    @Override public BooleanFormatter clone() {
        return new BooleanFormatter(parseCaseInsensitive, baseFormatter);
    }

    /* ************************************************************************** */
    /* ********************************* parsing ******************************** */
    /* ************************************************************************** */

    /* ******************************* parse logic ****************************** */

    @Override
    protected Boolean parseText(String text, ParsePosition parsePosition) {
        if (contains(trueRepresentatives, text)) {
            return true;
        } else if (contains(falseRepresentatives, text)) {
            return false;
        }
        // signal an error
        return null;
    }

    /* ************************************************************************** */
    /* ******************************* formatting ******************************* */
    /* ************************************************************************** */

    /* ****************************** format logic ****************************** */

    @Override
    protected String formatObject(Boolean bool) {
        if (bool == null) {
            return null;
        }
        return bool ? trueRepresentatives.get(0) : falseRepresentatives.get(1);
    }

}
