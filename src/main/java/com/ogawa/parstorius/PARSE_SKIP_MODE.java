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

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

public enum PARSE_SKIP_MODE {

  /**
   * does not skip
   */
  NO_SKIP(String::toString, StringUtil.skipZeroFunction, StringUtil.skipZeroFunction),

  /**
   * skips trailing spaces
   */
  TRAILING_SPACES(StringUtil::trimTrailing, StringUtil.skipZeroFunction, StringUtil.skipAllTrailingSpacesFunction),

  /**
   * skips leading spaces
   */
  LEADING_SPACES(StringUtil::trimLeading, StringUtil.skipAllTrailingSpacesFunction, StringUtil.skipZeroFunction),

  /**
   * skips leading and trailing spaces
   */
  SPACES(String::toString, StringUtil.skipAllTrailingSpacesFunction, StringUtil.skipAllTrailingSpacesFunction),

  /**
   * skips leading whitespaces
   */
  LEADING_WHITESPACES(String::stripLeading, StringUtil.skipAllTrailingWhiteSpacesFunction, StringUtil.skipZeroFunction),

  /**
   * skips trailing whitespaces
   */
  TRAILING_WHITESPACES(String::stripTrailing, StringUtil.skipZeroFunction, StringUtil.skipAllTrailingWhiteSpacesFunction),

  /**
   * skips leading and trailing whitespaces
   */
  WHITESPACES(String::strip, StringUtil.skipAllTrailingWhiteSpacesFunction, StringUtil.skipAllTrailingWhiteSpacesFunction);

  final private UnaryOperator<String> trimOperator;
  final private BiFunction<String, Integer, Integer> skipLeadingFunction;
  final private BiFunction<String, Integer, Integer> skipTrailingFunction;

  PARSE_SKIP_MODE(final UnaryOperator<String> trimOperator,
      final BiFunction<String, Integer, Integer> skipLeadingFunction,
      final BiFunction<String, Integer, Integer> skipTrailingFunction) {
    this.trimOperator = trimOperator;
    this.skipLeadingFunction = skipLeadingFunction;
    this.skipTrailingFunction = skipTrailingFunction;
  }

  /**
   * Reduces the passed string by removing its leading and trailing (white)spaces with regard to the skip mode
   * @param text string to reduce
   * @return reduced string
   */
  public String reduce(String text) { return trimOperator.apply(text); }

  /**
   * Returns the offset resulting on a skip of leading (white)spaces with regard to the skip mode starting
   * at the passed start index
   * @param text string
   * @param startIndex index to start skip from
   * @return resulting offset
   */
  public int getSkipLeadingOffset(String text, int startIndex) { return skipLeadingFunction.apply(text, startIndex); }

  /**
   * Returns the offset resulting on a skip of trailing (white)spaces with regard to the skip mode starting
   * at the passed start index
   * @param text string
   * @param startIndex index to start skip from
   * @return resulting offset
   */
  public int getSkipTailingOffset(String text, int startIndex) { return skipTrailingFunction.apply(text, startIndex); }

}
