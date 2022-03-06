/*
 * Copyright (c) 2020-2020-2022 Kai Bächle
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

package com.ogawa.parstorius.formatter.parsing;

import com.ogawa.parstorius.Formatter;
import com.ogawa.parstorius.PARSE_RESULT_CAUSE;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.List;

public interface FormatterParseDefaultsTester<T, F extends Formatter<T, F>> {

    abstract F createDefaultFormatter();

    abstract T getTestParseDefault(PARSE_RESULT_CAUSE formatDefault);

    @Test
    default void testParseDefaultsGetterSetter() {
        Formatter<T, F> f = createDefaultFormatter();
        setTestParseDefaults(f);
        Assertions.assertEquals(getTestParseDefault(PARSE_RESULT_CAUSE.PARSE_OF_NULL), f.getParseOfNullDefault());
        Assertions.assertEquals(getTestParseDefault(PARSE_RESULT_CAUSE.MISSING_VALUE), f.getParseMissingDefault());
        Assertions.assertEquals(getTestParseDefault(PARSE_RESULT_CAUSE.NULL_AS_TEXT), f.getParseNullTextDefault());
        Assertions.assertEquals(getTestParseDefault(PARSE_RESULT_CAUSE.ERROR), f.getParseErrorDefault());
    }

    @Test
    default void getSetParseNullTexts() {
        Formatter<T, F> f = createDefaultFormatter();
        List<String> nullTexts = List.of("null", "NULL", ".");
        Assertions.assertEquals(List.of(), f.setParseNullTexts(null).getParseNullTexts());
        Assertions.assertEquals(nullTexts, f.setParseNullTexts(nullTexts).getParseNullTexts());
    }


    default void setTestParseDefaults(Formatter<T, F> f) {
        f.setParseOfNullDefault(getTestParseDefault(PARSE_RESULT_CAUSE.PARSE_OF_NULL));
        f.setParseMissingDefault(getTestParseDefault(PARSE_RESULT_CAUSE.MISSING_VALUE));
        f.setParseNullTextDefault(getTestParseDefault(PARSE_RESULT_CAUSE.NULL_AS_TEXT));
        f.setParseErrorDefault(getTestParseDefault(PARSE_RESULT_CAUSE.ERROR));
    }

    default void resetParseDefaults(Formatter<T, F> f) {
        f.setParseOfNullDefault(null);
        f.setParseMissingDefault(null);
        f.setParseNullTextDefault(null);
        f.setParseErrorDefault(null);
    }

    default void testInitialParseDefaults(Formatter<T, F> justCreatedFormatter) {

        Assertions.assertNull(justCreatedFormatter.getParseOfNullDefault());
        Assertions.assertNull(justCreatedFormatter.getParseMissingDefault());
        Assertions.assertNull(justCreatedFormatter.getParseNullTextDefault());
        Assertions.assertNull(justCreatedFormatter.getParseErrorDefault());;

    }

    default void testParseDefaultResult(Formatter<T, F> f, String text, PARSE_RESULT_CAUSE parseTest) {

        Assertions.assertEquals(getTestParseDefault(parseTest), f.parse(text));
        Assertions.assertEquals(parseTest, f.getLastParseResultCause());

        if (parseTest == PARSE_RESULT_CAUSE.ERROR) {
            // check exception was saved
            Assertions.assertNotNull(f.getExceptionOnParsing());
            // check error position is indicating an error
            Assertions.assertNotEquals(-1, f.getLastParsePosition().getErrorIndex());
            // check parse position is at error position
            Assertions.assertEquals(f.getLastParsePosition().getIndex(), f.getLastParsePosition().getErrorIndex());
        } else {
            // check exception was cleared
            Assertions.assertNull(f.getExceptionOnParsing());
            // check neutral error position
            Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());
        }
    }

}