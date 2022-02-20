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

import java.util.Locale;
import java.util.Objects;

public abstract class LocalizedFormatter<T, B, F extends LocalizedFormatter<T, B, F>>
    extends Formatter<T, B, F> {

  Locale locale;

  /* ************************************************************************** */
  /* ****************************** constructors ****************************** */
  /* ************************************************************************** */

  protected LocalizedFormatter(final Locale locale, final B baseFormatter) {
    super(baseFormatter, false);
    // internalize arguments
    this.locale = Objects.requireNonNullElse(locale,Locale.getDefault());
  }

  /* ************************************************************************** */
  /* ********************************* common ********************************* */
  /* ************************************************************************** */

  /* ***************************** common setter ****************************** */

  /**
   * Sets the locale of the formatter. Null is the systems default locale.
   * @param locale the locale to be used for formatting and parsing
   * @return the formatter instance
   */
  public LocalizedFormatter <T, B, F> setLocale(final Locale locale) {
    this.locale = Objects.requireNonNullElse(locale, Locale.getDefault());
    init();
    return this;
  }

  /* ***************************** common getter ****************************** */

  /**
   * Returns the locale used for parsing and formatting
   * @return the locale used for parsing and formatting
   */
  public Locale getLocale() { return locale; }


  /* ****************************** common logic ****************************** */

  @Override public String toString() {
    return super.toString() + " with locale " + locale.toString();
  }

}
