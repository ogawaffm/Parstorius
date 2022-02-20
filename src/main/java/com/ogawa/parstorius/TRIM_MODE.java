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

import java.util.function.UnaryOperator;

public enum TRIM_MODE implements UnaryOperator<String> {

  NONE(String::toString),
  TRIM_TRAILING(s -> StringUtil.trimTrailing(s)),
  TRIM_LEADING(s -> StringUtil.trimLeading(s)),
  TRIM(String::toString),
  STRIP_LEADING(String::stripLeading),
  STRIP_TRAILING(String::stripTrailing),
  STRIP(String::strip);

  private UnaryOperator<String> unaryOperator;

  TRIM_MODE(final UnaryOperator<String> unaryOperator) { this.unaryOperator = unaryOperator; }

  @Override public String apply(String s) { return unaryOperator.apply(s); }

}
