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
