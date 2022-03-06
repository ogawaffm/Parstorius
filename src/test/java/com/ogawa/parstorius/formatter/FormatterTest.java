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
import com.ogawa.parstorius.formatter.parsing.FormatterParseDefaultsTester;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static com.ogawa.parstorius.Formatter.NULL_TEXT;

public abstract class FormatterTest <T, F extends Formatter<T, F>>
  implements
    FormatterConstructorArgsTest<T, F>,
    FormatterParseDefaultsTester<T, F>
{

  @Nested
  @DisplayName("Parser Functionality")
  class ParseProperties {

    @Test
    void getSetFormatNullText() {
      Formatter<T, F> f = createDefaultFormatter();
      Assertions.assertEquals("fred", f.setFormatNullText("fred").getFormatNullText());
      Assertions.assertEquals(NULL_TEXT, f.setFormatNullText(null).getFormatNullText());
    }

  }

  @Nested
  @DisplayName("Formatter Functionality")
  class FormatProperties {

  }


}
