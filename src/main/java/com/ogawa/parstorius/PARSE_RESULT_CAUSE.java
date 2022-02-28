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

/**
 * This enum describes the basis on which a parsing result was determined.
 */
public enum PARSE_RESULT_CAUSE {
  /**
   * The text to parse was null and the parseOfNullDefault was returned.
   */
  PARSE_OF_NULL,
  /**
   * The text to parse had no value and the parseMissingDefault was returned.
   */
  MISSING_VALUE,
  /**
   * The text to parse represented the value of null and the parseNullDefault was returned.
   */
  NULL_AS_TEXT,
  /**
   * The parsing of the text caused an error and the parseErrorDefault was returned
   */
  ERROR,
  /**
   * THE parsing of the text was successful and the respective object for the text was returned.
   */
  TEXT_VALUE
}
