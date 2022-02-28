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

package com.ogawa.parstorius.numberFormatter;

import com.ogawa.parstorius.Formatter;
import com.ogawa.parstorius.NumberFormatter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.DecimalFormat;
import java.util.List;

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
    Assertions.assertEquals(new DecimalFormat().toPattern(), f.getDecimalFormat().toPattern());
  }

  @Test
  default void constructor_trueFalse() {
    NumberFormatter<T> f;
    f = new NumberFormatter(Integer.class, true);
    Assertions.assertEquals(true, f.getParseCaseInsensitive());
    Assertions.assertEquals(new DecimalFormat().toPattern(), f.getDecimalFormat().toPattern());

    f = new NumberFormatter(Integer.class, false);
    Assertions.assertEquals(false, f.getParseCaseInsensitive());
    Assertions.assertEquals(new DecimalFormat().toPattern(), f.getDecimalFormat().toPattern());
  }

  @Test
  default void constructor_DefaultDF() {
    NumberFormatter<T> f;
    f = new NumberFormatter(Integer.class, new DecimalFormat());
    Assertions.assertEquals(false, f.getParseCaseInsensitive());
    Assertions.assertEquals(new DecimalFormat().toPattern(), f.getDecimalFormat().toPattern());
  }

  @Test
  default void constructor_DefaultDF_trueFalse() {
    NumberFormatter<T> f;
    f = new NumberFormatter(Integer.class, new DecimalFormat(), true );
    Assertions.assertEquals(true, f.getParseCaseInsensitive());
    Assertions.assertEquals(new DecimalFormat().toPattern(), f.getDecimalFormat().toPattern());

    f = new NumberFormatter(Integer.class, new DecimalFormat(), false);
    Assertions.assertEquals(false, f.getParseCaseInsensitive());
    Assertions.assertEquals(new DecimalFormat().toPattern(), f.getDecimalFormat().toPattern());
  }

}
