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
import java.util.Map;

@SuppressWarnings("unused")
// The constant value is the format, which is the result of parseText and is returned on format
// as its string representation
public class KeyValueFormatter<T> extends Formatter<T, KeyValueFormatter<T>> {

    private T format;

    /**
     * Constructs a new formatter instance using the default pattern.
     * @param parseCaseInsensitive flag, if the parser behaves case-insensitive
     * @param pattern pattern for parsing and formatting
     */
    protected KeyValueFormatter(final boolean parseCaseInsensitive, final Map<String, T> pattern) {
        super(parseCaseInsensitive);
    }

    @Override protected KeyValueFormatter<T> init() { return this; }

    @Override public KeyValueFormatter<T> clone() { return new KeyValueFormatter<>(parseCaseInsensitive, null);
    }

    public Map<String, T> getKeyValueMap() {
        return null;
    }

    @Override
    protected String formatObject(T object) {
        return format.toString();
    }

    @Override
    protected T parseText(String text, ParsePosition contextParsePosition) {
        return format;
    }

}
