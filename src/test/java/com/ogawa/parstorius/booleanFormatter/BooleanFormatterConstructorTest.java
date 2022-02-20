package com.ogawa.parstorius.booleanFormatter;

import com.ogawa.parstorius.BooleanFormatter;
import com.ogawa.parstorius.Formatter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.ogawa.parstorius.BooleanFormatter.DEFAULT_PATTERN;

public interface BooleanFormatterConstructorTest {

  static List<String> booleanTexts = List.of("wahr", "falsch", "-1", "0");

  default List<Formatter> getConstructed() {
    final List<Formatter> formatters = List.of(
        new BooleanFormatter(),
        new BooleanFormatter(true),
        new BooleanFormatter(false),
        new BooleanFormatter(true, null),
        new BooleanFormatter(null),
        new BooleanFormatter(booleanTexts),
        new BooleanFormatter(false, booleanTexts)
    );
    return formatters;
  }

  @Test
  default void constructor() {
    BooleanFormatter f;
    f = new BooleanFormatter();
    Assertions.assertEquals(false, f.getParseCaseInsensitive());
    Assertions.assertEquals(DEFAULT_PATTERN, f.getBaseFormatter());
  }

  @Test
  default void constructor_true() {
    BooleanFormatter f;
    f = new BooleanFormatter(true);
    Assertions.assertEquals(true, f.getParseCaseInsensitive());
    Assertions.assertEquals(DEFAULT_PATTERN, f.getBaseFormatter());
  }

  @Test
  default void constructor_false() {
    BooleanFormatter f;
    f = new BooleanFormatter(false);
    Assertions.assertEquals(false, f.getParseCaseInsensitive());
    Assertions.assertEquals(DEFAULT_PATTERN, f.getBaseFormatter());
  }

  static List<String> germanBooleans = List.of("wahr", "falsch");

  @Test
  default void constructor_true_List() {
    BooleanFormatter f;
    f = new BooleanFormatter(true, null);
    Assertions.assertEquals(true, f.getParseCaseInsensitive());
    Assertions.assertEquals(DEFAULT_PATTERN, f.getBaseFormatter());

    f = new BooleanFormatter(true, germanBooleans);
    Assertions.assertEquals(true, f.getParseCaseInsensitive());
    Assertions.assertEquals(germanBooleans, f.getBaseFormatter());
  }

  @Test
  default void constructor_false_List() {
    BooleanFormatter f;
    f = new BooleanFormatter(false, null);
    Assertions.assertEquals(false, f.getParseCaseInsensitive());
    Assertions.assertEquals(DEFAULT_PATTERN, f.getBaseFormatter());

    f = new BooleanFormatter(false, germanBooleans);
    Assertions.assertEquals(false, f.getParseCaseInsensitive());
    Assertions.assertEquals(germanBooleans, f.getBaseFormatter());
  }

}
