package com.ogawa.parstorius;

import java.util.Objects;

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
    Objects.checkIndex(startIndex, text.length());
    for(int index = startIndex; index >= 0; index--) {
      if (text.charAt(index) != '\u0020') return index;
    }
    return -1;
  }

  /**
   * Completes with the missing String-method trimLeading analog {@link String#stripLeading()}
   * @param text to trim left blanks off
   * @return
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
   * @return
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
    Objects.checkIndex(startIndex, text.length());
    for(int index = startIndex; index >= 0; index--) {
      if (! Character.isWhitespace(text.charAt(index))) return index;
    }
    return -1;
  }


}
