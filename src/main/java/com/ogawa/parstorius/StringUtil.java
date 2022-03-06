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

import java.util.Objects;
import java.util.function.BiFunction;

public class StringUtil {

  /**
   * Returns the index of the first non-space in text starting at index startIndex searching rightward
   * @param text text to search in
   * @param startIndex index to start from
   * @return index of first non-blank or -1 if a non-blank could not to be found
   */
  public static int getFirstNonSpaceIndex(String text, int startIndex) {
    if ((startIndex < 0 || startIndex >= text.length()) && startIndex != 0) {
      throw new IllegalArgumentException("startIndex must be 0 or > 0 and < text.length()");
    }
    for(int index = startIndex; index < text.length(); index++) {
      if (text.charAt(index) != '\u0020') return index;
    }
    return -1;
  }

  /**
   * Returns the index of the last non-space in text starting at index startIndex searching leftward
   * @param text text to search in
   * @param startIndex index to start from
   * @return index of first non-blank or -1 if a non-blank could not to be found
   */
  public static int getLastNonSpaceIndex(String text, int startIndex) {
    if (startIndex >= 0 && startIndex < text.length()) {
      for (int index = startIndex; index >= 0; index--) {
        if (text.charAt(index) != '\u0020') return index;
      }
    }
    return -1;
  }

  /**
   * Completes with the missing String-method trimLeading analog {@link String#stripLeading()}
   * @param text to trim left blanks off
   * @return text without leading spaces
   */
  public static String trimLeading(String text) {
    int firstNonSpaceIndex = getFirstNonSpaceIndex(text, 0);
    if (firstNonSpaceIndex == -1) {
      return text;
    } else {
      return text.substring(firstNonSpaceIndex);
    }
  }

  /**
   * Completes with the missing String-method trimTrailing analog {@link String#stripTrailing()}
   * @param text to trim right blanks off
   * @return text without trailing spaces
   */
  public static String trimTrailing(String text) {
    int lastNonSpaceIndex = getLastNonSpaceIndex(text, 0);
    if (lastNonSpaceIndex == -1) {
      return text;
    } else {
      return text.substring(0, lastNonSpaceIndex + 1);
    }
  }

  /**
   * Returns the index of the first non-space in text starting at index startIndex searching rightward
   * @param text text to search in
   * @param startIndex index to start from
   * @return index of first non-blank or -1 if a non-blank could not to be found
   */
  public static int getFirstNonWhiteSpaceIndex(String text, int startIndex) {
    if ((startIndex < 0 || startIndex >= text.length()) && startIndex != 0) {
      throw new IllegalArgumentException("startIndex must be 0 or > 0 and < text.length()");
    }
    for(int index = startIndex; index < text.length(); index++) {
      if (! Character.isWhitespace(text.charAt(index))) return index;
    }
    return -1;
  }

  /**
   * Returns the index of the last non-space in text starting at index startIndex searching leftward
   * @param text text to search in
   * @param startIndex index to start from
   * @return index of first non-blank or -1 if a non-blank could not to be found
   */
  public static int getLastNonWhiteSpaceIndex(String text, int startIndex) {
    if (startIndex >= 0 && startIndex < text.length()) {
      for (int index = startIndex; index >= 0; index--) {
        if (! Character.isWhitespace(text.charAt(index))) return index;
      }
    }
    return -1;
  }

  static BiFunction<String, Integer, Integer> skipZeroFunction = ((str, startIndex) -> 0);

  static BiFunction<String, Integer, Integer> skipAllTrailingSpacesFunction =
      ((str, startIndex) -> Math.max(0, StringUtil.getFirstNonSpaceIndex(str, startIndex))
      );
  static BiFunction<String, Integer, Integer> skipAllTrailingWhiteSpacesFunction =
      ((str, startIndex) -> Math.max(0, StringUtil.getFirstNonWhiteSpaceIndex(str, startIndex))
      );


}
