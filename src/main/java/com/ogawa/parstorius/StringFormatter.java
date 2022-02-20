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

import java.text.DecimalFormat;
import java.text.ParsePosition;

import static java.lang.Math.abs;

@SuppressWarnings("unused")


// T can extend Number, but must not inherit methods from AbstractObjectFormatter
public class StringFormatter extends Formatter<String, Integer, StringFormatter> {

    private DecimalFormat decimalFormat;
    String blankValueDefault;

    protected StringFormatter(boolean parseCaseInsensitive, Integer formatPattern) {
        super(formatPattern, parseCaseInsensitive);
        this.blankValueDefault = null;
    }

    @Override String patternToString() {
        return decimalFormat.toPattern();
    }

    @Override public Integer getBaseFormatter() {
        return baseFormatter;
    }

    @Override protected StringFormatter init() {
         decimalFormat = new DecimalFormat("#"); return this;
    }

    /**
     * Returns a deep copy clone
     * @return a clone of the formatter
     */
    @Override public StringFormatter clone() {
        StringFormatter clone = new StringFormatter(parseCaseInsensitive, baseFormatter);
        clone.decimalFormat = this.decimalFormat;
        clone.blankValueDefault = this.blankValueDefault;
        return clone.init();
    }

    public StringFormatter setBlankValueDefault(String blankValueDefault) {
        this.blankValueDefault = blankValueDefault;
        return this;
    }
    public String getBlankValueDefault() { return blankValueDefault; }


    @Override
    protected String formatObject(String object) {

        if (object == null) {
            return null;
        }

        // No formatting?
        if (baseFormatter == 0) {
            return object;
        }

        String result = object;

        // Is objects string representation to long for the format?
        if (result.length() > abs(baseFormatter)) {

            return null;

        }

        if (baseFormatter < 0) {

            return " ".repeat(-baseFormatter - result.length()) + result;

        } else {

            return result + " ".repeat(baseFormatter - result.length());

        }

    }

    @Override
    protected String parseText(String text, ParsePosition parsePosition) {
        return text;
    }

}
