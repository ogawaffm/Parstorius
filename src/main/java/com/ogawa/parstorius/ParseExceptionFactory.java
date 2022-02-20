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

import java.text.ParseException;
import java.text.ParsePosition;
import java.time.format.DateTimeParseException;

public class ParseExceptionFactory {

  /**
   * Constant to indicate an unknown error offset
   */
  private static final int UNKNOWN_ERROR_OFFSET = -1;

  /**
   * Checks and returns if the passed error offset is a valid offset.
   * @param errorOffset error offset to check
   * @return Check result
   */
  private static final boolean isValidErrorIndex(int errorOffset) {
    return errorOffset >= 0;
  }

  /**
   * Returns a unique message text for a ParseException, containing the error offset (if valid).
   * @param errorOffset error offset
   * @return Error message
   */
  private static String getMessageText(int errorOffset) {
    return "Parse exception" + (isValidErrorIndex(errorOffset) ? " at index " + errorOffset : "");
  }

  /**
   * Creates a ParseException with a unique message based on the original error offset and base offset
   * and adds the root-cause-exception as its cause.
   * @param originalErrorOffset the original error index from the root cause source
   * @param baseOffset the base offset the error index is relative to
   * @param rootCauseException the root-cause-exception
   * @return
   */
  private static ParseException createParseException(int originalErrorOffset, int baseOffset,
      Exception rootCauseException) {

    int errorOffset;
    String errorMessage;

    if (isValidErrorIndex(originalErrorOffset)) {

      errorOffset = originalErrorOffset + baseOffset;
      errorMessage = getMessageText(errorOffset);

    } else {

      errorMessage = getMessageText(UNKNOWN_ERROR_OFFSET);
      errorOffset = Math.max(baseOffset, 0);

    }

    ParseException parseException = new ParseException(errorMessage, errorOffset);

    if (rootCauseException != null) {
      parseException.initCause(rootCauseException);
    }
    return parseException;
  }

  /**
   * Creates an ParseException based on a root-cause-exception.
   * @param errorOffset The error offset
   * @return created ParseException
   */
  public static ParseException createParseException(int errorOffset) {
    return createParseException(errorOffset, 0, null);
  }

  /**
   * Creates an ParseException based on a root-cause-exception.
   * @param errorOffset The local error offset
   * @param baseOffset The global offset the local error offset is relative to.
   * @return created ParseException
   */
  public static ParseException createParseException(int errorOffset, int baseOffset) {
    return createParseException(errorOffset, baseOffset, null);
  }

  /**
   * Creates an ParseException based on a root-cause-exception.
   * @param parsePosition The parsePosition with the local error offset
   * @return created ParseException
   */
  public static ParseException createParseException(ParsePosition parsePosition) {
    return createParseException(parsePosition.getErrorIndex(), 0, null);
  }

  /**
   * Creates an ParseException based on a root-cause-exception.
   * @param parsePosition The parsePosition with the local error offset
   * @param exception The root-cause-exception
   * @return created ParseException
   */
  public static ParseException createParseException(ParsePosition parsePosition, Exception exception) {
    return createParseException(parsePosition.getErrorIndex(), 0, exception);
  }

  /**
   * Creates an ParseException based on a root-cause-exception.
   * @param dateTimeParseException The root-cause-exception with local error offset
   * @return created ParseException
   */
  public static ParseException createParseException(DateTimeParseException dateTimeParseException) {
    return createParseException(dateTimeParseException.getErrorIndex(), 0, dateTimeParseException);
  }

  /**
   * Creates an ParseException based on a root-cause-exception.
   * @param dateTimeParseException The root-cause-exception with local error offset
   * @param baseOffset The global offset the local error offset is relative to.
   * @return created ParseException
   */
  public static ParseException createParseException(DateTimeParseException dateTimeParseException, int baseOffset) {
    return createParseException(dateTimeParseException.getErrorIndex(), baseOffset, dateTimeParseException);
  }

  /**
   * Creates an ParseException based on a root-cause-exception. 
   * @param parseException The root-cause-exception with local error offset
   * @return created ParseException
   */
  public static ParseException createParseException(ParseException parseException) {
    return createParseException(parseException.getErrorOffset(), 0, parseException);
  }

  /**
   * Creates an ParseException based on a root-cause-exception.
   * @param parseException The root-cause-exception with local error offset
   * @param baseOffset The global offset the local error offset is relative to.
   * @return created ParseException
   */
  public static ParseException createParseException(ParseException parseException, int baseOffset) {
    return createParseException(parseException.getErrorOffset(), baseOffset, parseException);
  }

  /**
   * Creates an ParseException based on a root-cause-exception.
   * Also used for NumberFormatException, because it does not provide any error index/offset information.
   * @param exception
   * @return created ParseException
   */
  public static ParseException createParseException(Exception exception) {
    return createParseException(UNKNOWN_ERROR_OFFSET, 0, exception);
  }

  /**
   * Creates an ParseException based on a root-cause-exception. The error offset is built considering the baseOffset.
   * Also used for NumberFormatException, because it does not provide any error index/offset information.
   * @param exception
   * @param baseOffset The global offset the local error offset is relative to.
   * @return created ParseException
   */
  public static ParseException createParseException(Exception exception, int baseOffset) {
    return createParseException(UNKNOWN_ERROR_OFFSET, baseOffset, exception);
  }

}
