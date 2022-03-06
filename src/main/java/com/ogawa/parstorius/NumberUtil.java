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

package com.ogawa.parstorius;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.Function;

public class NumberUtil {

  static public Function<BigDecimal, ? extends Number> getBigDecimalCaster(Class numberClassT) {

    // integral primitives
    if (numberClassT.equals(Byte.class)) {
      return BigDecimal::byteValueExact;
    } else if (numberClassT.equals(Short.class)) {
      return BigDecimal::shortValueExact;
    } else if (numberClassT.equals(Integer.class)) {
      return BigDecimal::intValueExact;
    } else if (numberClassT.equals(Long.class)) {
      return BigDecimal::longValueExact;

      // float primitives
    } else if (numberClassT.equals(Float.class)) {
      return BigDecimal::floatValue;
    } else if (numberClassT.equals(Double.class)) {
      return BigDecimal::doubleValue;

      // big data types
    } else if (numberClassT.equals(BigInteger.class)) {
      return BigDecimal::toBigIntegerExact;
    } else if (numberClassT.equals(BigDecimal.class)) {
      return (Function<BigDecimal, BigDecimal>) bigDecimal -> bigDecimal;
    } else {
      // not supported
      return null;
    }

  }


}
