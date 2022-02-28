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
import com.ogawa.parstorius.TRIM_MODE;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

import static com.ogawa.parstorius.Formatter.NULL_TEXT;

public abstract class FormatterTest <T, F extends Formatter<T, F>> {

  final private Formatter<T, F> baseFormatter;
  private Formatter<T, F> formatter;
  final private T t;

  public FormatterTest(Formatter<T, F> formatter, T t) {
    this.baseFormatter = formatter;
    this.t = t;
  }

  @BeforeEach
  void init() {
    formatter = baseFormatter.clone();
  }

  @Nested
  @DisplayName("format properties")
  class FormatProperties {

    @Test
    void getSetParseCaseInsensitive() {
      Assertions.assertEquals(true, formatter.setParseCaseInsensitive(true).getParseCaseInsensitive());
      Assertions.assertEquals(false, formatter.setParseCaseInsensitive(false).getParseCaseInsensitive());
    }

    @Test
    void getSetParseNullTexts() {
      List<String> nullTexts = List.of("null", "NULL", ".");
      Assertions.assertEquals(List.of(), formatter.setParseNullTexts(null).getParseNullTexts());
      Assertions.assertEquals(nullTexts, formatter.setParseNullTexts(nullTexts).getParseNullTexts());
    }

    @Test
    void getSetParseNullDefault() {
      Assertions.assertEquals(null, formatter.setParseNullTextDefault(null).getParseNullTextDefault());
      Assertions.assertEquals(t, formatter.setParseNullTextDefault(t).getParseNullTextDefault());
    }

    @Test
    void getSetParseMissingDefault() {
      Assertions.assertEquals(null, formatter.setParseMissingDefault(null).getParseMissingDefault());
      Assertions.assertEquals(t, formatter.setParseMissingDefault(t).getParseMissingDefault());
    }

    @Test
    void getSetParseErrorDefault() {
      Assertions.assertEquals(null, formatter.setParseErrorDefault(null).getParseErrorDefault());
      Assertions.assertEquals(t, formatter.setParseErrorDefault(t).getParseErrorDefault());
    }

    @Test
    void getSetPreParseOperation() {
      // formatter.setPreParseOperation(null);
    }

  }

  @Nested
  @DisplayName("parse properties")
  class ParseProperties {

    @Test
    void getSetFormatNullText() {
      Assertions.assertEquals("fred", formatter.setFormatNullText("fred").getFormatNullText());
      Assertions.assertEquals(NULL_TEXT, formatter.setFormatNullText(null).getFormatNullText());
    }

    @Test
    void getSetFormatNullDefault() {
      Assertions.assertEquals(null, formatter.setFormatNullDefault(null).getFormatNullDefault());
      Assertions.assertEquals("fred", formatter.setFormatNullDefault("fred").getFormatNullDefault());
    }

    @Test
    void getSetFormatErrorDefault() {
      Assertions.assertEquals(null, formatter.setFormatErrorDefault(null).getFormatErrorDefault());
      Assertions.assertEquals("Error", formatter.setFormatErrorDefault("Error").getFormatErrorDefault());
    }

    @Test
    void getCharacteristics() {
      DecimalFormat decimalFormat = new DecimalFormat("'fred was here'");

      NumberFormatter<Integer> f = new NumberFormatter<>(Integer.class, decimalFormat);

      DecimalFormat df = f.getDecimalFormat();

      Assertions.assertEquals(decimalFormat.toPattern(), df.toPattern());

      // since a new instance of the decimal format used for construction is expected, test it
      df.setRoundingMode(RoundingMode.UNNECESSARY);

      Assertions.assertNotEquals(decimalFormat.getRoundingMode(), df.getRoundingMode());
    }

    @Test
    void cloneTest() {

      DecimalFormat decimalFormat = new DecimalFormat("'fred was here'");

      NumberFormatter<Integer> f = new NumberFormatter<>(Integer.class, decimalFormat);

      /* *****  set values different from defaults **** */

      // mode
      f.setParseCaseInsensitive(true);
      f.setParseUntilEnd(false);
      f.setParseTrimMode(TRIM_MODE.STRIP_LEADING);
      f.setParseRoundingMode(RoundingMode.CEILING);

      // parsing values
      f.setParseMissingDefault(100);
      f.setParseNullTextDefault(101);
      f.setParseErrorDefault(102);
      f.setParseNullTexts(List.of("null", "."));

      // formatting values
      f.setFormatNullText("nothing");
      f.setFormatNullDefault(200);
      f.setFormatErrorDefault(201);

      NumberFormatter<Integer> fc = f.clone();

      /* ***** test ***** */

      // state
      Assertions.assertEquals(f.getExceptionOnParsing(), fc.getExceptionOnParsing());
      Assertions.assertEquals(f.getLastParsePosition(), fc.getLastParsePosition());

      // mode
      Assertions.assertEquals(f.getParseCaseInsensitive(), fc.getParseCaseInsensitive());
      Assertions.assertEquals(f.getParseTrimMode(), fc.getParseTrimMode());

      // parse values
      Assertions.assertEquals(f.getParseMissingDefault(), fc.getParseMissingDefault());
      Assertions.assertEquals(f.getParseNullTextDefault(), fc.getParseNullTextDefault());
      Assertions.assertEquals(f.getParseNullTexts(), fc.getParseNullTexts());
      Assertions.assertEquals(f.getParseErrorDefault(), fc.getParseErrorDefault());

      // format values
      Assertions.assertEquals(f.getFormatNullText(), fc.getFormatNullText());
      Assertions.assertEquals(f.getFormatErrorDefault(), fc.getFormatErrorDefault());
      Assertions.assertEquals(f.getFormatNullDefault(), fc.getFormatNullDefault());

      // test if clone is really a clone and not the copy source instance to be used for cloning
      fc.setParseCaseInsensitive(! f.getParseCaseInsensitive());
      Assertions.assertNotEquals(f.getParseCaseInsensitive(), fc.getParseCaseInsensitive());

    }

  }
}
