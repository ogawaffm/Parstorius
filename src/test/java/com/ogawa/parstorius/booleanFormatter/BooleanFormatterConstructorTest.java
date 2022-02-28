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

package com.ogawa.parstorius.booleanFormatter;

import com.ogawa.parstorius.BooleanFormatter;
import com.ogawa.parstorius.Formatter;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.ogawa.parstorius.BooleanFormatter.DEFAULT_FALSE_LIST;
import static com.ogawa.parstorius.BooleanFormatter.DEFAULT_TRUE_LIST;

public interface BooleanFormatterConstructorTest {

  static List<String> booleanTexts = List.of("wahr", "falsch", "-1", "0");

  default List<Formatter> getConstructed() {
    final List<Formatter> formatters = List.of(
        new BooleanFormatter(),
        new BooleanFormatter(true),
        new BooleanFormatter(false),
        new BooleanFormatter(true, null, null),
        new BooleanFormatter(false, null, null),
        new BooleanFormatter(true, List.of("wahr", "-1"), List.of("falsch", "0")),
        new BooleanFormatter(false, List.of("wahr", "-1"), List.of("falsch", "0"))
    );
    return formatters;
  }

  @Test
  default void constructor() {
    BooleanFormatter f;
    f = new BooleanFormatter();
    Assertions.assertEquals(false, f.getParseCaseInsensitive());
    Assertions.assertEquals(DEFAULT_FALSE_LIST, f.getFalseList());
    Assertions.assertEquals(DEFAULT_TRUE_LIST, f.getTrueList());
  }

  @Test
  default void constructor_true() {
    BooleanFormatter f;
    f = new BooleanFormatter(true);
    Assertions.assertEquals(true, f.getParseCaseInsensitive());
    Assertions.assertEquals(DEFAULT_TRUE_LIST, f.getTrueList());
    Assertions.assertEquals(DEFAULT_FALSE_LIST, f.getFalseList());
  }

  @Test
  default void constructor_false() {
    BooleanFormatter f;
    f = new BooleanFormatter(false);
    Assertions.assertEquals(false, f.getParseCaseInsensitive());
    Assertions.assertEquals(DEFAULT_TRUE_LIST, f.getTrueList());
    Assertions.assertEquals(DEFAULT_FALSE_LIST, f.getFalseList());
  }

  static List<String> germanTrueList = List.of("wahr");
  static List<String> germanFalseList = List.of("falsch");

  @Test
  default void constructor_true_List() {
    BooleanFormatter f;
    f = new BooleanFormatter(true, null, null);
    Assertions.assertEquals(true, f.getParseCaseInsensitive());
    Assertions.assertEquals(DEFAULT_TRUE_LIST, f.getTrueList());
    Assertions.assertEquals(DEFAULT_FALSE_LIST, f.getFalseList());

    f = new BooleanFormatter(true, germanTrueList, germanFalseList);
    Assertions.assertEquals(true, f.getParseCaseInsensitive());
    Assertions.assertEquals(germanTrueList, f.getTrueList());
    Assertions.assertEquals(germanFalseList, f.getFalseList());
  }

  @Test
  default void constructor_false_List() {
    BooleanFormatter f;
    f = new BooleanFormatter(false, null, null);
    Assertions.assertEquals(false, f.getParseCaseInsensitive());
    Assertions.assertEquals(DEFAULT_TRUE_LIST, f.getTrueList());
    Assertions.assertEquals(DEFAULT_FALSE_LIST, f.getFalseList());

    f = new BooleanFormatter(false, germanTrueList, germanFalseList);
    Assertions.assertEquals(false, f.getParseCaseInsensitive());
    Assertions.assertEquals(germanTrueList, f.getTrueList());
    Assertions.assertEquals(germanFalseList, f.getFalseList());
  }

}
