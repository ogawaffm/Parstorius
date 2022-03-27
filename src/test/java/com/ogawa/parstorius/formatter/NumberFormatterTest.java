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

package com.ogawa.parstorius.formatter;

import com.ogawa.parstorius.NumberFormatter;
import com.ogawa.parstorius.PARSE_RESULT_CAUSE;
import com.ogawa.parstorius.PARSE_SKIP_MODE;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.function.Function;

public abstract class NumberFormatterTest <T extends Number> extends FormatterTest<T, NumberFormatter<T>> {

  Function<Number, T> caster;
  Class<T> numberClassT;
  NumberFormatterTest(Class<T> numberClassT, Function<Number, T> caster) {
    this.numberClassT = numberClassT;
    this.caster = caster;
  }

  public T cast(final Number obj) {
    return caster.apply(obj);
  }

  @Override public NumberFormatter<T> createDefaultFormatter() {
    return new NumberFormatter<T>(
        numberClassT,
        new DecimalFormat(),
        false,
        PARSE_SKIP_MODE.LEADING_SPACES, false
    );
  }

  @Override public T getTestParseDefault(final PARSE_RESULT_CAUSE formatDefault) {
    // results in -1, -2 ... for integral number types and -1.2, -2.2 ... for floating point types
    return caster.apply((-formatDefault.ordinal() -1) - (-formatDefault.ordinal() -1)/10); // e.g -1.1, -2.2
  }


  @Test
  @DisplayName("parse valid value")
  void testParseValidValues() {
    NumberFormatter<T> f = createDefaultFormatter();

    Assertions.assertEquals(1234567, f.parse("1234567"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.TEXT_VALUE, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());

    Assertions.assertEquals(1234567, f.parse("1.234.567"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.TEXT_VALUE, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());

    Assertions.assertEquals(1234567, f.parse("1234567,0"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.TEXT_VALUE, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());

    Assertions.assertEquals(1234567, f.parse("1,234567E6"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.TEXT_VALUE, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());

    Assertions.assertEquals(1234567, f.parse("12345670E-1"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.TEXT_VALUE, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());

    Assertions.assertEquals(3.2, f.parse("320,0E-2"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.TEXT_VALUE, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());

  }

  @Test
  @DisplayName("parse with case-sensitivity")
  void testParseCaseInsensitivity() {
    NumberFormatter<T> f = createDefaultFormatter();
    DecimalFormatSymbols dfs = new DecimalFormatSymbols();

    dfs.setExponentSeparator("x10^");
    DecimalFormat df = new DecimalFormat(new DecimalFormat().toPattern(), dfs);
    NumberFormatter<T> fx10 = createDefaultFormatter();

    f.setParseCaseInsensitive(true);

    Assertions.assertEquals(32, f.parse("3,2E1"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.TEXT_VALUE, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(f.getLastParsePosition().getErrorIndex(), -1);

    Assertions.assertEquals(32, f.parse("3,2E001"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.TEXT_VALUE, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(32, f.parse("320E-1"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.TEXT_VALUE, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(32, f.parse("320E-001"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.TEXT_VALUE, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(32, f.parse("3,2e1"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.TEXT_VALUE, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(32, f.parse("3,2e001"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.TEXT_VALUE, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(32, f.parse("320e-1"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.TEXT_VALUE, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(32, f.parse("3,2e-001"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.TEXT_VALUE, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());


    f.setParseCaseInsensitive(false);

    Assertions.assertEquals(32, f.parse("3,2E1"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.TEXT_VALUE, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(32, f.parse("3,2E001"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.TEXT_VALUE, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(32, f.parse("320-E1"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.TEXT_VALUE, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(32, f.parse("3,2E-001"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.TEXT_VALUE, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(32, f.parse("3,2e1"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.ERROR, f.getLastParseResultCause());
    Assertions.assertNotNull(f.getExceptionOnParsing());
    Assertions.assertNotEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(32, f.parse("3,2e001"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.ERROR, f.getLastParseResultCause());
    Assertions.assertNotNull(f.getExceptionOnParsing());
    Assertions.assertNotEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(32, f.parse("320e-1"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.ERROR, f.getLastParseResultCause());
    Assertions.assertNotNull(f.getExceptionOnParsing());
    Assertions.assertNotEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(32, f.parse("3,2e-001"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.ERROR, f.getLastParseResultCause());
    Assertions.assertNotNull(f.getExceptionOnParsing());
    Assertions.assertNotEquals(-1, f.getLastParsePosition().getErrorIndex());
  }

  @Test
  @DisplayName("parse (null-handling)")
  void parseWithNulls() {
    NumberFormatter<T> f = createDefaultFormatter();

    f.setParseNullTexts(List.of("N/A", "Nothing"));

    resetParseDefaults(f);

    // case-sensitive

    // with null as null default (which is the default on instance construction)
    f.setParseNullTextDefault(null);

    Assertions.assertNull(f.parse(null));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.PARSE_OF_NULL, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertNull(f.parse("n/A"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.ERROR, f.getLastParseResultCause());
    Assertions.assertNotNull(f.getExceptionOnParsing());
    Assertions.assertNotEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertNull(f.parse("N/A"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.NULL_AS_TEXT, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    setTestParseDefaults(f);
    Assertions.assertEquals(getTestParseDefault(PARSE_RESULT_CAUSE.PARSE_OF_NULL), f.parse(null));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.PARSE_OF_NULL, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(getTestParseDefault(PARSE_RESULT_CAUSE.NULL_AS_TEXT), f.parse("N/A"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.NULL_AS_TEXT, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(getTestParseDefault(PARSE_RESULT_CAUSE.NULL_AS_TEXT), f.parse("Nothing"));
    Assertions.assertEquals(getTestParseDefault(PARSE_RESULT_CAUSE.NULL_AS_TEXT), f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(getTestParseDefault(PARSE_RESULT_CAUSE.ERROR), f.parse("nothing"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.ERROR, f.getLastParseResultCause());
    Assertions.assertNotNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    // case-insensitive
    f.setParseCaseInsensitive(false);
    Assertions.assertEquals(getTestParseDefault(PARSE_RESULT_CAUSE.NULL_AS_TEXT), f.parse("Nothing"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.NULL_AS_TEXT, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(getTestParseDefault(PARSE_RESULT_CAUSE.ERROR), f.parse("nothing"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.ERROR, f.getLastParseResultCause());
    Assertions.assertNotNull(f.getExceptionOnParsing());
    Assertions.assertNotEquals(-1, f.getLastParsePosition().getErrorIndex());

    // case-sensitive
    f.setParseCaseInsensitive(true);
    Assertions.assertEquals(getTestParseDefault(PARSE_RESULT_CAUSE.NULL_AS_TEXT), f.parse("Nothing"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.NULL_AS_TEXT, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(getTestParseDefault(PARSE_RESULT_CAUSE.NULL_AS_TEXT), f.parse("nothing"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.NULL_AS_TEXT, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

  }

  @Test
  @DisplayName("parse (error-handling)")
  void parseWithErrors() {
    NumberFormatter<T> f = createDefaultFormatter();

    f.setParseNullTexts(List.of("nope"));
    setTestParseDefaults(f);

    Assertions.assertEquals(getTestParseDefault(PARSE_RESULT_CAUSE.ERROR), f.parse("null"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.ERROR, f.getLastParseResultCause());
    Assertions.assertNotNull(f.getExceptionOnParsing());
    Assertions.assertNotEquals(-1, f.getLastParsePosition().getErrorIndex());

    String text;
    text = "null";
    testParseDefaultResult(f, text, PARSE_RESULT_CAUSE.ERROR);


    Assertions.assertEquals(getTestParseDefault(PARSE_RESULT_CAUSE.ERROR), f.parse("cause error"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.ERROR, f.getLastParseResultCause());
    Assertions.assertNotNull(f.getExceptionOnParsing());
    Assertions.assertNotEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(3, f.parse("3"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.TEXT_VALUE, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(getTestParseDefault(PARSE_RESULT_CAUSE.NULL_AS_TEXT), f.parse("nope"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.NULL_AS_TEXT, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(getTestParseDefault(PARSE_RESULT_CAUSE.PARSE_OF_NULL), f.parse(null));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.PARSE_OF_NULL, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

  }

  void checkNoError(NumberFormatter<T> f) {
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());
  }

  void checkParseUntilEnd(NumberFormatter<T> f, String text) {
    Assertions.assertEquals(text.length(), f.getLastParsePosition().getIndex());
  }

  void checkParseUntilError(NumberFormatter<T> f) {
    Assertions.assertEquals(f.getLastParsePosition().getIndex(), f.getLastParsePosition().getErrorIndex());
  }


  @Test
  @DisplayName("parse (missing-handling)")
  void parseWithMissing() {
    NumberFormatter<T> f = createDefaultFormatter();
    String text;

    setTestParseDefaults(f);

    /* **** parsing with default trim mode **** */

    // missing default result
    text = "";
    testParseDefaultResult(f, text, PARSE_RESULT_CAUSE.MISSING_VALUE);
    checkParseUntilEnd(f, text);

    // error default result
    text = " ";
    testParseDefaultResult(f, text, PARSE_RESULT_CAUSE.ERROR);
    checkParseUntilEnd(f, text);

    /* **** parsing with default trim mode (explicitly set) **** */

    f.setParseSkipMode(PARSE_SKIP_MODE.NO_SKIP);

    // error default result
    text = "";
    testParseDefaultResult(f, text, PARSE_RESULT_CAUSE.MISSING_VALUE);
    checkParseUntilEnd(f, text);

    // error default result
    text = " ";
    testParseDefaultResult(f, text, PARSE_RESULT_CAUSE.ERROR);
    checkParseUntilEnd(f, text);

    /* **** parsing with all really trimming/splitting modes which will led to missing default result **** */

    f.setParseSkipMode(PARSE_SKIP_MODE.SPACES);
    text = "";
    testParseDefaultResult(f, text, PARSE_RESULT_CAUSE.MISSING_VALUE);
    checkParseUntilEnd(f, text);

    text = " ";
    testParseDefaultResult(f, text, PARSE_RESULT_CAUSE.MISSING_VALUE);
    checkParseUntilEnd(f, text);

    f.setParseSkipMode(PARSE_SKIP_MODE.LEADING_SPACES);
    text = "";
    testParseDefaultResult(f, text, PARSE_RESULT_CAUSE.MISSING_VALUE);
    checkParseUntilEnd(f, text);

    text = " ";
    testParseDefaultResult(f, text, PARSE_RESULT_CAUSE.MISSING_VALUE);
    checkParseUntilEnd(f, text);

    f.setParseSkipMode(PARSE_SKIP_MODE.TRAILING_SPACES);
    text = "";
    testParseDefaultResult(f, text, PARSE_RESULT_CAUSE.MISSING_VALUE);
    checkParseUntilEnd(f, text);

    text = " ";
    testParseDefaultResult(f, text, PARSE_RESULT_CAUSE.MISSING_VALUE);
    checkParseUntilEnd(f, text);


    f.setParseSkipMode(PARSE_SKIP_MODE.WHITESPACES);
    text = "";
    testParseDefaultResult(f, text, PARSE_RESULT_CAUSE.MISSING_VALUE);
    checkParseUntilEnd(f, text);

    text = " ";
    testParseDefaultResult(f, text, PARSE_RESULT_CAUSE.MISSING_VALUE);
    checkParseUntilEnd(f, text);

    f.setParseSkipMode(PARSE_SKIP_MODE.LEADING_WHITESPACES);
    text = "";
    testParseDefaultResult(f, text, PARSE_RESULT_CAUSE.MISSING_VALUE);
    checkParseUntilEnd(f, text);

    text = " ";
    testParseDefaultResult(f, text, PARSE_RESULT_CAUSE.MISSING_VALUE);
    checkParseUntilEnd(f, text);

    f.setParseSkipMode(PARSE_SKIP_MODE.TRAILING_WHITESPACES);
    text = "";
    testParseDefaultResult(f, text, PARSE_RESULT_CAUSE.MISSING_VALUE);
    checkParseUntilEnd(f, text);

    text = " ";
    testParseDefaultResult(f, text, PARSE_RESULT_CAUSE.MISSING_VALUE);
    checkParseUntilEnd(f, text);

  }

}
