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

import com.ogawa.parstorius.Formatter;
import com.ogawa.parstorius.NumberFormatter;
import com.ogawa.parstorius.PARSE_RESULT_CAUSE;
import com.ogawa.parstorius.TRIM_MODE;
import com.ogawa.parstorius.numberFormatter.NumberFormatterConstructorTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;

public class NumberFormatterTest extends FormatterTest<Integer, NumberFormatter<Integer>> implements
    NumberFormatterConstructorTest,
    FormatterConstructorDefaultsTest {

  NumberFormatterTest() {
    super(new NumberFormatter(Integer.class, new DecimalFormat()), Integer.valueOf(1));
  }

  @Override public List<Formatter> getConstructed() { return NumberFormatterConstructorTest.super.getConstructed(); }


  private void setParseDefaults(NumberFormatter f) {
    f.setParseOfNullDefault(PARSE_TEST.PARSE_OF_NULL_DEFAULT.getDefault());
    f.setParseMissingDefault(PARSE_TEST.PARSE_MISSING_DEFAULT.getDefault());
    f.setParseNullTextDefault(PARSE_TEST.PARSE_NULL_TEXT_DEFAULT.getDefault());
    f.setParseErrorDefault(PARSE_TEST.PARSE_ERROR_DEFAULT.getDefault());
  }

  private void resetParseDefaults(NumberFormatter f) {
    f.setParseOfNullDefault(null);
    f.setParseMissingDefault(null);
    f.setParseNullTextDefault(null);
    f.setParseErrorDefault(null);
  }

  @Test
  @DisplayName("parse valid value")
  void testParseValidValues() {
    NumberFormatter f = new NumberFormatter(Integer.class, new DecimalFormat());

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
    NumberFormatter f = new NumberFormatter(Integer.class, new DecimalFormat());
    DecimalFormatSymbols dfs = new DecimalFormatSymbols();

    dfs.setExponentSeparator("x10^");
    DecimalFormat df = new DecimalFormat(new DecimalFormat().toPattern(), dfs);
    NumberFormatter fx10 = new NumberFormatter(Integer.class, df);

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
    NumberFormatter f = new NumberFormatter(Integer.class, new DecimalFormat());

    f.setParseNullTexts(List.of("N/A", "Nothing"));

    resetParseDefaults(f);

    // case-sensitive

    // with null as null default (which is the default on instance construction)
    f.setParseNullTextDefault(null);

    Assertions.assertEquals(null, f.parse(null));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.PARSE_OF_NULL, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(null, f.parse("n/A"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.ERROR, f.getLastParseResultCause());
    Assertions.assertNotNull(f.getExceptionOnParsing());
    Assertions.assertNotEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(null, f.parse("N/A"));
    Assertions.assertEquals(PARSE_RESULT_CAUSE.NULL_AS_TEXT, f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    setParseDefaults(f);
    Assertions.assertEquals(PARSE_TEST.PARSE_OF_NULL_DEFAULT.getDefault(), f.parse(null));
    Assertions.assertEquals(PARSE_TEST.PARSE_OF_NULL_DEFAULT.getResultCause(), f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(PARSE_TEST.PARSE_NULL_TEXT_DEFAULT.getDefault(), f.parse("N/A"));
    Assertions.assertEquals(PARSE_TEST.PARSE_NULL_TEXT_DEFAULT.getResultCause(), f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(PARSE_TEST.PARSE_NULL_TEXT_DEFAULT.getDefault(), f.parse("Nothing"));
    Assertions.assertEquals(PARSE_TEST.PARSE_NULL_TEXT_DEFAULT.getResultCause(), f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(PARSE_TEST.PARSE_ERROR_DEFAULT.getDefault(), f.parse("nothing"));
    Assertions.assertEquals(PARSE_TEST.PARSE_ERROR_DEFAULT.getResultCause(), f.getLastParseResultCause());
    Assertions.assertNotNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    // case-insensitive
    f.setParseCaseInsensitive(false);
    Assertions.assertEquals(PARSE_TEST.PARSE_NULL_TEXT_DEFAULT.getDefault(), f.parse("Nothing"));
    Assertions.assertEquals(PARSE_TEST.PARSE_NULL_TEXT_DEFAULT.getResultCause(), f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(PARSE_TEST.PARSE_ERROR_DEFAULT.getDefault(), f.parse("nothing"));
    Assertions.assertEquals(PARSE_TEST.PARSE_ERROR_DEFAULT.getResultCause(), f.getLastParseResultCause());
    Assertions.assertNotNull(f.getExceptionOnParsing());
    Assertions.assertNotEquals(-1, f.getLastParsePosition().getErrorIndex());

    // case-sensitive
    f.setParseCaseInsensitive(true);
    Assertions.assertEquals(PARSE_TEST.PARSE_NULL_TEXT_DEFAULT.getDefault(), f.parse("Nothing"));
    Assertions.assertEquals(PARSE_TEST.PARSE_NULL_TEXT_DEFAULT.getResultCause(), f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(PARSE_TEST.PARSE_NULL_TEXT_DEFAULT.getDefault(), f.parse("nothing"));
    Assertions.assertEquals(PARSE_TEST.PARSE_NULL_TEXT_DEFAULT.getResultCause(), f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

  }

  @Test
  @DisplayName("parse (error-handling)")
  void parseWithErrors() {
    NumberFormatter f = new NumberFormatter(Integer.class, new DecimalFormat());

    f.setParseNullTexts(List.of("nope"));
    setParseDefaults(f);

    Assertions.assertEquals(PARSE_TEST.PARSE_ERROR_DEFAULT.getDefault(), f.parse("null"));
    Assertions.assertEquals(PARSE_TEST.PARSE_ERROR_DEFAULT.getResultCause(), f.getLastParseResultCause());
    Assertions.assertNotNull(f.getExceptionOnParsing());
    Assertions.assertNotEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(PARSE_TEST.PARSE_ERROR_DEFAULT.getDefault(), f.parse("cause error"));
    Assertions.assertEquals(PARSE_TEST.PARSE_ERROR_DEFAULT.getResultCause(), f.getLastParseResultCause());
    Assertions.assertNotNull(f.getExceptionOnParsing());
    Assertions.assertNotEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(3, f.parse("3"));
    Assertions.assertEquals(PARSE_TEST.PARSE_TEXT_VALUE.getResultCause(), f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(PARSE_TEST.PARSE_NULL_TEXT_DEFAULT.getDefault(), f.parse("nope"));
    Assertions.assertEquals(PARSE_TEST.PARSE_NULL_TEXT_DEFAULT.getResultCause(), f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

    Assertions.assertEquals(PARSE_TEST.PARSE_OF_NULL_DEFAULT.getDefault(), f.parse(null));
    Assertions.assertEquals(PARSE_TEST.PARSE_OF_NULL_DEFAULT.getResultCause(), f.getLastParseResultCause());
    Assertions.assertNull(f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());

  }

  void checkNoError(NumberFormatter f) {
    Assertions.assertEquals(null, f.getExceptionOnParsing());
    Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());
  }

  void checkFullParse(NumberFormatter f, String text) {
    Assertions.assertEquals(text.length(), f.getLastParsePosition().getIndex());
  }

  void testParseDefaultResult(NumberFormatter f, String text, PARSE_TEST parseTest) {

    Assertions.assertEquals(parseTest.getDefault(), f.parse(text));
    Assertions.assertEquals(parseTest.getResultCause(), f.getLastParseResultCause());

    if (parseTest == PARSE_TEST.PARSE_ERROR_DEFAULT) {
      Assertions.assertNotNull(f.getExceptionOnParsing());
      Assertions.assertNotEquals(-1, f.getLastParsePosition().getErrorIndex());
    } else {
      Assertions.assertNull(f.getExceptionOnParsing());
      Assertions.assertEquals(-1, f.getLastParsePosition().getErrorIndex());
    }
  }

  @Test
  @DisplayName("parse (missing-handling)")
  void parseWithMissing() {
    NumberFormatter f = new NumberFormatter(Integer.class, new DecimalFormat());
    String text;

    setParseDefaults(f);

    /* **** parsing with default trim mode **** */

    // missing default result
    text = "";
    testParseDefaultResult(f, text, PARSE_TEST.PARSE_MISSING_DEFAULT);
    checkFullParse(f, text);

    // error default result
    text = " ";
    testParseDefaultResult(f, text, PARSE_TEST.PARSE_ERROR_DEFAULT);
    checkFullParse(f, text);

    /* **** parsing with default trim mode (explicitly set) **** */

    f.setParseTrimMode(TRIM_MODE.NONE);

    // error default result
    text = "";
    testParseDefaultResult(f, text, PARSE_TEST.PARSE_MISSING_DEFAULT);
    checkFullParse(f, text);

    // error default result
    text = " ";
    testParseDefaultResult(f, text, PARSE_TEST.PARSE_ERROR_DEFAULT);
    checkFullParse(f, text);

    /* **** parsing with all really trimming/splitting modes which will led to missing default result **** */

    f.setParseTrimMode(TRIM_MODE.TRIM);
    text = "";
    testParseDefaultResult(f, text, PARSE_TEST.PARSE_MISSING_DEFAULT);
    checkFullParse(f, text);

    text = " ";
    testParseDefaultResult(f, text, PARSE_TEST.PARSE_MISSING_DEFAULT);
    checkFullParse(f, text);

    f.setParseTrimMode(TRIM_MODE.TRIM_LEADING);
    text = "";
    testParseDefaultResult(f, text, PARSE_TEST.PARSE_MISSING_DEFAULT);
    checkFullParse(f, text);

    text = " ";
    testParseDefaultResult(f, text, PARSE_TEST.PARSE_MISSING_DEFAULT);
    checkFullParse(f, text);

    f.setParseTrimMode(TRIM_MODE.TRIM_TRAILING);
    text = "";
    testParseDefaultResult(f, text, PARSE_TEST.PARSE_MISSING_DEFAULT);
    checkFullParse(f, text);

    text = " ";
    testParseDefaultResult(f, text, PARSE_TEST.PARSE_MISSING_DEFAULT);
    checkFullParse(f, text);


    f.setParseTrimMode(TRIM_MODE.STRIP);
    text = "";
    testParseDefaultResult(f, text, PARSE_TEST.PARSE_MISSING_DEFAULT);
    checkFullParse(f, text);

    text = " ";
    testParseDefaultResult(f, text, PARSE_TEST.PARSE_MISSING_DEFAULT);
    checkFullParse(f, text);

    f.setParseTrimMode(TRIM_MODE.STRIP_LEADING);
    text = "";
    testParseDefaultResult(f, text, PARSE_TEST.PARSE_MISSING_DEFAULT);
    checkFullParse(f, text);

    text = " ";
    testParseDefaultResult(f, text, PARSE_TEST.PARSE_MISSING_DEFAULT);
    checkFullParse(f, text);

    f.setParseTrimMode(TRIM_MODE.STRIP_TRAILING);
    text = "";
    testParseDefaultResult(f, text, PARSE_TEST.PARSE_MISSING_DEFAULT);
    checkFullParse(f, text);

    text = " ";
    testParseDefaultResult(f, text, PARSE_TEST.PARSE_MISSING_DEFAULT);
    checkFullParse(f, text);

  }


  @DisplayName("parse (with all args)")
  void parseWithAllArgs() {
    NumberFormatter f = new NumberFormatter(Integer.class, new DecimalFormat());

    f.setParseMissingDefault(null);
    f.setParseErrorDefault(1);
    f.setParseNullTextDefault(1);

    Assertions.assertEquals(false, f.parse(null));
    Assertions.assertEquals(true, f.parse("cause error"));

    f.setParseNullTextDefault(1);
    f.setParseErrorDefault(1); // set different default for null
    Assertions.assertEquals(true, f.parse(null));
    Assertions.assertEquals(false, f.parse("cause error"));
  }
}
