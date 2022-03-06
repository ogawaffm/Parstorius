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

import java.text.ParsePosition;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
public class BooleanFormatter extends Formatter<Boolean, BooleanFormatter> {

//    public final String ONE = "1";
//    public final String ZERO = "0";
//    public final String YES = "YES";
//    public final String NO = "NO";

    public static final List<String> DEFAULT_TRUE_LIST = List.of(Boolean.TRUE.toString());
    public static final List<String> DEFAULT_FALSE_LIST = List.of(Boolean.TRUE.toString());

    public static final List<String> ONE_TRUE_LIST = List.of(Boolean.TRUE.toString());
    public static final List<String> ZERO_FALSE_LIST = List.of(Boolean.TRUE.toString());

    BiPredicate<String, String> caseFunction;
    final private ArrayList<String> trueRepresentatives = new ArrayList<String>();
    final private ArrayList<String> falseRepresentatives = new ArrayList<String>();


    /* ************************************************************************** */
    /* ****************************** constructors ****************************** */
    /* ************************************************************************** */

    public BooleanFormatter(
        List<String> trueRepresentatives, List<String> falseRepresentatives,
        boolean parseCaseInsensitive,
        PARSE_SKIP_MODE parseSkipMode, boolean parseUntilEnd) {
        super(parseCaseInsensitive, parseSkipMode, parseUntilEnd);
        trueRepresentatives = new ArrayList<String>();
        trueRepresentatives.forEach(e -> this.trueRepresentatives.add(e));
        falseRepresentatives = new ArrayList<String>();
        falseRepresentatives.forEach(e -> this.falseRepresentatives.add(e));
        init();
    }

    /* ************************************************************************** */
    /* ********************************* common ********************************* */
    /* ************************************************************************** */

    /* ****************************** common logic ****************************** */

    @Override public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("true=");
        sb.append(trueRepresentatives.stream().collect(Collectors.joining(",", "\"", "\"")));
        sb.append("false=");
        sb.append(falseRepresentatives.stream().collect(Collectors.joining(",", "\"", "\"")));
        return sb.toString();
    }

    public List<String> getTrueList() {
        return new ArrayList<>(trueRepresentatives);
    }

    public List<String> getFalseList() {
        return new ArrayList<>(falseRepresentatives);
    }

    @Override protected BooleanFormatter init() {

        if (parseCaseInsensitive) {
            caseFunction = String::equalsIgnoreCase;
        } else {
            caseFunction = String::equals;
        }

        return this;
    }

    @Override public BooleanFormatter clone() {
        return new BooleanFormatter(trueRepresentatives, falseRepresentatives,
            getParseCaseInsensitive(), getParseSkipMode(), getParseUntilEnd()).copyProperties(this);
    }

    /* ************************************************************************** */
    /* ********************************* parsing ******************************** */
    /* ************************************************************************** */

    /* ******************************* parse logic ****************************** */

    @Override
    protected Boolean parseText(String text, ParsePosition contextParsePosition) {
        Optional<String> found;
        found = find(trueRepresentatives, text);
        if (found.isPresent()) {
            return true;
        } else {
            found = find(falseRepresentatives, text);
            if (found.isPresent()) {
                return false;
            }
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
