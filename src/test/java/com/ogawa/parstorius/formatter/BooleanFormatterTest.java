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

import com.ogawa.parstorius.BooleanFormatter;
import com.ogawa.parstorius.Formatter;
import com.ogawa.parstorius.booleanFormatter.BooleanFormatterConstructorTest;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static com.ogawa.parstorius.BooleanFormatter.DEFAULT_PATTERN;

public class BooleanFormatterTest extends FormatterTest<Boolean, List<String>, BooleanFormatter> implements
    BooleanFormatterConstructorTest,
    FormatterConstructorDefaultsTest {

  static List<String> booleanTexts = List.of("wahr", "falsch", "-1", "0");

  BooleanFormatterTest() {
    super(new BooleanFormatter(), Boolean.FALSE, booleanTexts, DEFAULT_PATTERN);
  }

  @Override public List<Formatter> getConstructed() { return BooleanFormatterConstructorTest.super.getConstructed(); }

  @Test
  @DisplayName("parse with case-sensitivity")
  void testParseCaseInsensitivity() {
    BooleanFormatter f = new BooleanFormatter();

    Assertions.assertEquals(Boolean.TRUE, f.parse("true"));
    Assertions.assertEquals(Boolean.FALSE, f.parse("false"));

    Assertions.assertEquals(null, f.parse("fALSe"));
    Assertions.assertEquals(null, f.parse("fred"));

    f.setParseCaseInsensitive(true);
    Assertions.assertEquals(Boolean.FALSE, f.parse("fALSe"));
    Assertions.assertEquals(Boolean.TRUE, f.parse("trUe"));
  }

  @Test
  @DisplayName("parse with custom format")
  void parseWithCustomFormat() {
    BooleanFormatter f = new BooleanFormatter(booleanTexts);

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
  @DisplayName("parse (null-handling)")
  void parseWithNulls() {
    BooleanFormatter f = new BooleanFormatter();

    f.setParseNullTexts(List.of("N/A", "Nothing"));

    // case-sensitive

    // with null as null default (which is the default on instance construction)
    f.setParseNullDefault(null);
    Assertions.assertEquals(null, f.parse(null));
    Assertions.assertEquals(null, f.parse("N/A"));
    Assertions.assertEquals(true, f.parse("true"));

    // with false as null default
    f.setParseNullDefault(false);
    Assertions.assertEquals(false, f.parse(null));
    Assertions.assertEquals(false, f.parse("N/A"));
    Assertions.assertEquals(null, f.parse("cause error"));

    // with true as null default
    f.setParseNullDefault(true);
    Assertions.assertEquals(true, f.parse(null));
    Assertions.assertEquals(true, f.parse("Nothing"));
    Assertions.assertEquals(null, f.parse("n/a"));
    Assertions.assertEquals(null, f.parse("cause error"));

    // case-insensitive

    f.setParseCaseInsensitive(true);
    f.setParseNullDefault(false);

    f.setParseNullDefault(false);
    Assertions.assertEquals(false, f.parse(null));
    Assertions.assertEquals(false, f.parse("n/A"));

    f.setParseNullDefault(true);
    Assertions.assertEquals(true, f.parse(null));
    Assertions.assertEquals(true, f.parse("NothING"));
    Assertions.assertEquals(null, f.parse("cause error"));
  }

  @Test
  @DisplayName("parse (error-handling)")
  void parseWithErrors() {
    BooleanFormatter f = new BooleanFormatter();

    f.setParseErrorDefault(true);
    f.setParseNullDefault(false); // set different default for null
    Assertions.assertEquals(false, f.parse(null));
    Assertions.assertEquals(true, f.parse("cause error"));

    f.setParseNullDefault(true);
    f.setParseErrorDefault(false); // set different default for null
    Assertions.assertEquals(true, f.parse(null));
    Assertions.assertEquals(false, f.parse("cause error"));
  }

  @Test
  @DisplayName("parse (missing-handling)")
  void parseWithMissing() {
    BooleanFormatter f = new BooleanFormatter();

    f.setParseMissingDefault(true);
    f.setParseErrorDefault(false); // set different default for null
    f.setParseNullDefault(false); // set different default for null
    Assertions.assertEquals(false, f.parse(null));
    Assertions.assertEquals(false, f.parse("cause error"));
    Assertions.assertEquals(true, f.parse(""));
    Assertions.assertEquals(true, f.parse("    "));

    f.setParseMissingDefault(false);
    f.setParseErrorDefault(true); // set different default for null
    f.setParseNullDefault(true); // set different default for null
    Assertions.assertEquals(true, f.parse(null));
    Assertions.assertEquals(true, f.parse("cause error"));
    Assertions.assertEquals(false, f.parse(""));
    Assertions.assertEquals(false, f.parse("    "));

  }


  @DisplayName("parse (with all args)")
  void parseWithAllArgs() {
    BooleanFormatter f = new BooleanFormatter();

    f.setParseMissingDefault(null);
    f.setParseErrorDefault(true);
    f.setParseNullDefault(false);


    Assertions.assertEquals(false, f.parse(null));
    Assertions.assertEquals(true, f.parse("cause error"));

    f.setParseNullDefault(true);
    f.setParseErrorDefault(false); // set different default for null
    Assertions.assertEquals(true, f.parse(null));
    Assertions.assertEquals(false, f.parse("cause error"));
  }
}
