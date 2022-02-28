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
 * This enum describes the basis on which a formatting result was determined.
 */
enum FORMAT_RESULT_CAUSE {
  /**
   * The object to format was null and the defaultOnFormatNull was taken to format.
   */
  FORMAT_ON_NULL,
  /**
   * The formatting of the object caused an error and the defaultOnFormatError was taken to format.
   */
  ERROR,
  /**
   * THE formatting of the object was successful and the respective text for the object was returned.
   */
  OBJECT
}
