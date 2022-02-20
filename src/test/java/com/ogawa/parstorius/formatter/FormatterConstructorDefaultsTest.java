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

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public interface FormatterConstructorDefaultsTest <T, P, F extends Formatter<T, P, F>> {

  List<Formatter<T, P, F>> getConstructed();

  @Test
  @DisplayName("default of non-constructor-arguments")
  default void defaultsOfNoneConstructorArgs() {
    for(Formatter<T, P, F> f : getConstructed()) {
      assertEquals(null, f.getParseNullDefault());
      assertEquals(null, f.getParseMissingDefault());
      assertEquals(null, f.getParseErrorDefault());
      assertEquals(List.of(), f.getParseNullTexts());
    }
  }


}
