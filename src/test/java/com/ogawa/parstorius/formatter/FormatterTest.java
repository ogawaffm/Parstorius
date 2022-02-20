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

public abstract class FormatterTest <T, C, F extends Formatter<T, C, F>> {

  final private Formatter<T, C, F> baseFormatter;
  private Formatter<T, C, F> formatter;
  final private T t;
  final private C f;
  final private C defaultF;
  public FormatterTest(Formatter<T, C, F> formatter, T t, C f, C defaultF) {
    this.baseFormatter = formatter;
    this.t = t;
    this.f = f;
    this.defaultF = defaultF;
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
      Assertions.assertEquals(null, formatter.setParseNullDefault(null).getParseNullDefault());
      Assertions.assertEquals(t, formatter.setParseNullDefault(t).getParseNullDefault());
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

      DecimalFormat c = f.getBaseFormatter();

      Assertions.assertEquals(decimalFormat.toPattern(), c.toPattern());

      // since a new instance of the decimal format used for construction is expected, test it
      c.setRoundingMode(RoundingMode.UNNECESSARY);

      Assertions.assertNotEquals(decimalFormat.getRoundingMode(), c.getRoundingMode());
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
      f.setParseNullDefault(101);
      f.setParseErrorDefault(102);
      f.setParseNullTexts(List.of("null", "."));

      // formatting values
      f.setFormatNullText("nothing");
      f.setFormatNullDefault(200);
      f.setFormatErrorDefault(201);

      NumberFormatter<Integer> fc = f.clone();

      /* ***** test ***** */

      // state
      Assertions.assertEquals(f.getLastParseException(), fc.getLastParseException());
      Assertions.assertEquals(f.getParsePosition(), fc.getParsePosition());

      // mode
      Assertions.assertEquals(f.getParseCaseInsensitive(), fc.getParseCaseInsensitive());
      Assertions.assertEquals(f.getParseTrimMode(), fc.getParseTrimMode());

      // parse values
      Assertions.assertEquals(f.getParseMissingDefault(), fc.getParseMissingDefault());
      Assertions.assertEquals(f.getParseNullDefault(), fc.getParseNullDefault());
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
