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
import com.ogawa.parstorius.PARSE_RESULT_CAUSE;
import com.ogawa.parstorius.PARSE_SKIP_MODE;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

public class BooleanFormatterTest extends FormatterTest<Boolean, BooleanFormatter> {

  BooleanFormatterTest() {
  }


  @Test
  @DisplayName("parse with case-sensitivity")
  void testParseCaseInsensitivity() {
    BooleanFormatter f = createDefaultFormatter();

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
    BooleanFormatter f = createDefaultFormatter();

    Assertions.assertEquals(Boolean.FALSE, f.parse("0"));
    Assertions.assertEquals(Boolean.TRUE, f.parse("1"));
    Assertions.assertEquals(Boolean.TRUE, f.parse("-1"));

    Assertions.assertEquals(Boolean.FALSE, f.parse("no"));
    Assertions.assertEquals(Boolean.TRUE, f.parse("yes"));

    Assertions.assertEquals(Boolean.FALSE, f.parse("false"));
    Assertions.assertEquals(Boolean.TRUE, f.parse("true"));


    Assertions.assertEquals(Boolean.FALSE, f.parse("falsch"));
    Assertions.assertEquals(Boolean.TRUE, f.parse("wahr"));

    Assertions.assertEquals(Boolean.FALSE, f.parse("0"));
    Assertions.assertEquals(Boolean.TRUE, f.parse("-1"));


    Assertions.assertEquals(null, f.parse("true"));
    Assertions.assertEquals(null, f.parse("false"));

    f.setParseCaseInsensitive(true);
    Assertions.assertEquals(Boolean.FALSE, f.parse("faLSCh"));
    Assertions.assertEquals(null, f.parse("trUe"));
  }

  @Test
  @DisplayName("parse (null-handling)")
  void parseWithNulls() {
    BooleanFormatter f = createDefaultFormatter();

    f.setParseNullTexts(List.of("N/A", "Nothing"));

    // case-sensitive

    // with null as null default (which is the default on instance construction)
    f.setParseNullTextDefault(null);
    Assertions.assertEquals(null, f.parse(null));
    Assertions.assertEquals(null, f.parse("N/A"));
    Assertions.assertEquals(true, f.parse("true"));

    // with false as null default
    f.setParseNullTextDefault(false);
    Assertions.assertEquals(false, f.parse(null));
    Assertions.assertEquals(false, f.parse("N/A"));
    Assertions.assertEquals(null, f.parse("cause error"));

    // with true as null default
    f.setParseNullTextDefault(true);
    Assertions.assertEquals(true, f.parse(null));
    Assertions.assertEquals(true, f.parse("Nothing"));
    Assertions.assertEquals(null, f.parse("n/a"));
    Assertions.assertEquals(null, f.parse("cause error"));

    // case-insensitive

    f.setParseCaseInsensitive(true);
    f.setParseNullTextDefault(false);

    f.setParseNullTextDefault(false);
    Assertions.assertEquals(false, f.parse(null));
    Assertions.assertEquals(false, f.parse("n/A"));

    f.setParseNullTextDefault(true);
    Assertions.assertEquals(true, f.parse(null));
    Assertions.assertEquals(true, f.parse("NothING"));
    Assertions.assertEquals(null, f.parse("cause error"));
  }

  @Test
  @DisplayName("parse (error-handling)")
  void parseWithErrors() {
    BooleanFormatter f = createDefaultFormatter();

    f.setParseErrorDefault(true);
    f.setParseNullTextDefault(false); // set different default for null
    Assertions.assertEquals(false, f.parse(null));
    Assertions.assertEquals(true, f.parse("cause error"));

    f.setParseNullTextDefault(true);
    f.setParseErrorDefault(false); // set different default for null
    Assertions.assertEquals(true, f.parse(null));
    Assertions.assertEquals(false, f.parse("cause error"));
  }

  @Test
  @DisplayName("parse (missing-handling)")
  void parseWithMissing() {
    BooleanFormatter f = createDefaultFormatter();

    f.setParseMissingDefault(true);
    f.setParseErrorDefault(false); // set different default for null
    f.setParseNullTextDefault(false); // set different default for null
    Assertions.assertEquals(false, f.parse(null));
    Assertions.assertEquals(false, f.parse("cause error"));
    Assertions.assertEquals(true, f.parse(""));
    Assertions.assertEquals(true, f.parse("    "));

    f.setParseMissingDefault(false);
    f.setParseErrorDefault(true); // set different default for null
    f.setParseNullTextDefault(true); // set different default for null
    Assertions.assertEquals(true, f.parse(null));
    Assertions.assertEquals(true, f.parse("cause error"));
    Assertions.assertEquals(false, f.parse(""));
    Assertions.assertEquals(false, f.parse("    "));

  }


  @DisplayName("parse (with all args)")
  void parseWithAllArgs() {
    BooleanFormatter f = createDefaultFormatter();

    f.setParseMissingDefault(null);
    f.setParseErrorDefault(true);
    f.setParseNullTextDefault(false);


    Assertions.assertEquals(false, f.parse(null));
    Assertions.assertEquals(true, f.parse("cause error"));

    f.setParseNullTextDefault(true);
    f.setParseErrorDefault(false); // set different default for null
    Assertions.assertEquals(true, f.parse(null));
    Assertions.assertEquals(false, f.parse("cause error"));
  }

  @Override public BooleanFormatter createDefaultFormatter() {
    return new BooleanFormatter(List.of("true", "yes", "-1", "1"), List.of("false", "no", "0"), true,
        PARSE_SKIP_MODE.NO_SKIP, true);
  }

  @Override public Boolean getTestParseDefault(final PARSE_RESULT_CAUSE formatDefault) {
    return null;
  }

  @Override public Formatter<Boolean, BooleanFormatter> getCloneTestFormatter() {
    return null;
  }

  @Override public Formatter<Boolean, BooleanFormatter> getComplementaryCloneTestFormatter() {
    return null;
  }

  @Override public void compareExtendedProps(final Formatter<Boolean, BooleanFormatter> f,
      final Formatter<Boolean, BooleanFormatter> fc) {

  }
}
