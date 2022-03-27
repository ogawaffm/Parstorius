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
import com.ogawa.parstorius.NumberFormatter;

public class IntegerFormatterTest extends NumberFormatterTest<Integer> {

    IntegerFormatterTest() {
        super(Integer.class, Integer.class::cast);
    }

    @Override public Formatter<Integer, NumberFormatter<Integer>> getCloneTestFormatter() {
        return null;
    }

    @Override public Formatter<Integer, NumberFormatter<Integer>> getComplementaryCloneTestFormatter() {
        return null;
    }

    @Override public void compareExtendedProps(final Formatter<Integer, NumberFormatter<Integer>> f,
        final Formatter<Integer, NumberFormatter<Integer>> fc) {

    }

}
