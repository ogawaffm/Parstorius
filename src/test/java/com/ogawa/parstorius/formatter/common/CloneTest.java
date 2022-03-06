/*
 * Copyright (c) 2020-2020-2022 Kai Bächle
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

package com.ogawa.parstorius.formatter.common;

import com.ogawa.parstorius.Formatter;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public interface CloneTest <T, F extends Formatter<T, F>> {

  abstract Formatter<T, F> getCloneTestFormatter();
  abstract Formatter<T, F> getComplementaryCloneTestFormatter();

  @Test
  @DisplayName("clone")
  default void cloneAndCompare() {

    Formatter<T, F> f;

    f = getCloneTestFormatter();
    compareBaseProps(f, f.clone());
    compareExtendedProps(f, f.clone());

    f = getComplementaryCloneTestFormatter();
    compareBaseProps(f, f.clone());
    compareExtendedProps(f, f.clone());


  }
  @Test
  abstract void compareExtendedProps(Formatter<T, F> f, Formatter<T, F> fc);

  @Test
  default void compareBaseProps(Formatter<T, F> f, Formatter<T, F> fc) {

    /* ***** test ***** */

    // state
    Assertions.assertEquals(f.getExceptionOnParsing(), fc.getExceptionOnParsing());
    Assertions.assertEquals(f.getLastParsePosition(), fc.getLastParsePosition());

    // mode
    Assertions.assertEquals(f.getParseCaseInsensitive(), fc.getParseCaseInsensitive());
    Assertions.assertEquals(f.getParseSkipMode(), fc.getParseSkipMode());

    // parse values
    Assertions.assertEquals(f.getParseMissingDefault(), fc.getParseMissingDefault());
    Assertions.assertEquals(f.getParseNullTextDefault(), fc.getParseNullTextDefault());
    Assertions.assertEquals(f.getParseNullTexts(), fc.getParseNullTexts());
    Assertions.assertEquals(f.getParseErrorDefault(), fc.getParseErrorDefault());

    // format values
    Assertions.assertEquals(f.getFormatNullText(), fc.getFormatNullText());
    Assertions.assertEquals(f.getFormatErrorDefault(), fc.getFormatErrorDefault());
    Assertions.assertEquals(f.getFormatNullDefault(), fc.getFormatNullDefault());

  }

}
