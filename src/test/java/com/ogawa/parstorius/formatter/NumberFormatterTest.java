package com.ogawa.parstorius.formatter;

import com.ogawa.parstorius.Formatter;
import com.ogawa.parstorius.FormatterFactory;
import com.ogawa.parstorius.NumberFormatter;
import com.ogawa.parstorius.numberFormatter.NumberFormatterConstructorTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.util.List;

public class NumberFormatterTest extends FormatterTest<Integer, DecimalFormat, NumberFormatter<Integer>> implements
    NumberFormatterConstructorTest,
    FormatterConstructorDefaultsTest {

  static List<String> booleanTexts = List.of("wahr", "falsch", "-1", "0");

  NumberFormatterTest() {
    super(new NumberFormatter(Integer.class, new DecimalFormat()),
        Integer.valueOf(1), new DecimalFormat(), new DecimalFormat());
  }

  @Override public List<Formatter> getConstructed() { return NumberFormatterConstructorTest.super.getConstructed(); }

  @Test
  @DisplayName("parse with case-sensitivity")
  void testParseCaseInsensitivity() {
    NumberFormatter f = new NumberFormatter(Integer.class, new DecimalFormat());

    f.setFormatErrorDefault(-1);

    f.setParseCaseInsensitive(true);
    Assertions.assertEquals(32, f.parse("3,2E1"));
    Assertions.assertEquals(32, f.parse("3,2E1"));


    f.setParseCaseInsensitive(false);
    Assertions.assertEquals(32, f.parse("3,2e1"));
    Assertions.assertEquals(32, f.parse("3,2E1"));
  }

  @Test
  @DisplayName("parse with custom format")
  void parseWithCustomFormat() {
    NumberFormatter f = new NumberFormatter(Integer.class, new DecimalFormat());

    Assertions.assertEquals(null, f.parse("true"));
    Assertions.assertEquals(null, f.parse("false"));

    Assertions.assertEquals(Boolean.FALSE, f.parse("falsch"));
    Assertions.assertEquals(Boolean.TRUE, f.parse("wahr"));

    Assertions.assertEquals(Boolean.FALSE, f.parse("0"));
    Assertions.assertEquals(Boolean.TRUE, f.parse("-1"));

    f.setParseCaseInsensitive(true);
    Assertions.assertEquals(Boolean.FALSE, f.parse("faLSCh"));
    Assertions.assertEquals(null, f.parse("trUe"));
  }

  @Test
   void test() {
    NumberFormatter<Integer> f = new NumberFormatter<Integer>(Integer.class, new DecimalFormat());
    Object o =  f.setParseCaseInsensitive(false);
    System.out.println(o.getClass().getSimpleName());
  }

  @Test
  @DisplayName("parse (null-handling)")
  void parseWithNulls() {
    NumberFormatter f = null; // FormatterFactory.createIntegerFormatter(null, null);

    f.setParseNullTexts(List.of("N/A", "Nothing"));

    // case-sensitive

    // with null as null default (which is the default on instance construction)
    f.setParseNullDefault(null);
    Assertions.assertEquals(null, f.parse(null));
    Assertions.assertEquals(null, f.parse("N/A"));
    Assertions.assertEquals(true, f.parse("true"));

    // with false as null default
    f.setParseNullDefault(1);
    Assertions.assertEquals(false, f.parse(null));
    Assertions.assertEquals(false, f.parse("N/A"));
    Assertions.assertEquals(null, f.parse("cause error"));

    // with true as null default
    f.setParseNullDefault(1);
    Assertions.assertEquals(true, f.parse(null));
    Assertions.assertEquals(true, f.parse("Nothing"));
    Assertions.assertEquals(null, f.parse("n/a"));
    Assertions.assertEquals(null, f.parse("cause error"));

    // case-insensitive

    f.setParseCaseInsensitive(true);
    f.setParseNullDefault(1);

    f.setParseNullDefault(1);
    Assertions.assertEquals(false, f.parse(null));
    Assertions.assertEquals(false, f.parse("n/A"));

    f.setParseNullDefault(1);
    Assertions.assertEquals(true, f.parse(null));
    Assertions.assertEquals(true, f.parse("NothING"));
    Assertions.assertEquals(null, f.parse("cause error"));
  }

  @Test
  @DisplayName("parse (error-handling)")
  void parseWithErrors() {
    NumberFormatter f = new NumberFormatter(Integer.class, new DecimalFormat());

    f.setParseErrorDefault(1);
    f.setParseNullDefault(1); // set different default for null
    Assertions.assertEquals(false, f.parse(null));
    Assertions.assertEquals(true, f.parse("cause error"));

    f.setParseNullDefault(1);
    f.setParseErrorDefault(1); // set different default for null
    Assertions.assertEquals(true, f.parse(null));
    Assertions.assertEquals(false, f.parse("cause error"));
  }

  @Test
  @DisplayName("parse (missing-handling)")
  void parseWithMissing() {
    NumberFormatter f = new NumberFormatter(Integer.class, new DecimalFormat());

    f.setParseMissingDefault(1);
    f.setParseErrorDefault(1); // set different default for null
    f.setParseNullDefault(1); // set different default for null
    Assertions.assertEquals(false, f.parse(null));
    Assertions.assertEquals(false, f.parse("cause error"));
    Assertions.assertEquals(true, f.parse(""));
    Assertions.assertEquals(true, f.parse("    "));

    f.setParseMissingDefault(1);
    f.setParseErrorDefault(1); // set different default for null
    f.setParseNullDefault(1); // set different default for null
    Assertions.assertEquals(true, f.parse(null));
    Assertions.assertEquals(true, f.parse("cause error"));
    Assertions.assertEquals(false, f.parse(""));
    Assertions.assertEquals(false, f.parse("    "));

  }


  @DisplayName("parse (with all args)")
  void parseWithAllArgs() {
    NumberFormatter f = new NumberFormatter(Integer.class, new DecimalFormat());

    f.setParseMissingDefault(null);
    f.setParseErrorDefault(1);
    f.setParseNullDefault(1);


    Assertions.assertEquals(false, f.parse(null));
    Assertions.assertEquals(true, f.parse("cause error"));

    f.setParseNullDefault(1);
    f.setParseErrorDefault(1); // set different default for null
    Assertions.assertEquals(true, f.parse(null));
    Assertions.assertEquals(false, f.parse("cause error"));
  }
}
