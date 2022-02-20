package com.ogawa.parstorius.numberFormatter;

import com.ogawa.parstorius.Formatter;
import com.ogawa.parstorius.NumberFormatter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.util.List;

import static com.ogawa.parstorius.BooleanFormatter.DEFAULT_PATTERN;

public interface NumberFormatterConstructorTest<T extends Number> {

  static List<String> booleanTexts = List.of("wahr", "falsch", "-1", "0");

  default List<Formatter> getConstructed() {
    DecimalFormat decimalFormat = new DecimalFormat();
    final List<Formatter> formatters = List.of(
        new NumberFormatter(Integer.class, decimalFormat, true),
        new NumberFormatter(Integer.class, decimalFormat, false)
    );
    return formatters;
  }

  @Test
  default void constructor() {
    NumberFormatter<T> f;
    f = new NumberFormatter(Integer.class);
    Assertions.assertEquals(false, f.getParseCaseInsensitive());
    Assertions.assertEquals(DEFAULT_PATTERN, f.getBaseFormatter());
  }

  @Test
  default void constructor_true() {
    NumberFormatter<T> f;
    f = new NumberFormatter(Integer.class);
    Assertions.assertEquals(true, f.getParseCaseInsensitive());
    Assertions.assertEquals(DEFAULT_PATTERN, f.getBaseFormatter());
  }

  @Test
  default void constructor_false() {
    NumberFormatter<T> f;
    f = new NumberFormatter(Integer.class);
    Assertions.assertEquals(false, f.getParseCaseInsensitive());
    Assertions.assertEquals(DEFAULT_PATTERN, f.getBaseFormatter());
  }

  static List<String> germanBooleans = List.of("wahr", "falsch");

  @Test
  default void constructor_true_List() {
    NumberFormatter<T> f;
    f = new NumberFormatter(Integer.class);
    Assertions.assertEquals(true, f.getParseCaseInsensitive());
    Assertions.assertEquals(DEFAULT_PATTERN, f.getBaseFormatter());

    f = new NumberFormatter(Integer.class);
    Assertions.assertEquals(true, f.getParseCaseInsensitive());
    Assertions.assertEquals(germanBooleans, f.getBaseFormatter());
  }

  @Test
  default void constructor_false_List() {
    NumberFormatter<T> f;
    f = new NumberFormatter(Integer.class);
    Assertions.assertEquals(false, f.getParseCaseInsensitive());
    Assertions.assertEquals(DEFAULT_PATTERN, f.getBaseFormatter());

    f = new NumberFormatter(Integer.class);
    Assertions.assertEquals(false, f.getParseCaseInsensitive());
    Assertions.assertEquals(germanBooleans, f.getBaseFormatter());
  }

}
