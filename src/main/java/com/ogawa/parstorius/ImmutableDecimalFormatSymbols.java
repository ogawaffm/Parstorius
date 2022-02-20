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

import java.text.DecimalFormatSymbols;
import java.util.Currency;
import java.util.Locale;
import java.util.Objects;

// { throw new UnsupportedOperationException(); }
public class ImmutableDecimalFormatSymbols extends DecimalFormatSymbols {

  /**
   *  ImmutableDecimalFormatSymbols is an immutable Wrapper for an DecimalFormatSymbols instance
   */

  // backing/wrapped DecimalFormatSymbols instance
  DecimalFormatSymbols backingDecimalFormatSymbols;

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormatSymbols is a DecimalFormatSymbols-wrapper.
   * @exception UnsupportedOperationException
   */
  public ImmutableDecimalFormatSymbols() { throw new UnsupportedOperationException(); }

  /**
   * Throws a NullPointerException if <code>locale</code> is null else throws an UnsupportedOperationException
   * since ImmutableDecimalFormatSymbols is a DecimalFormatSymbols-wrapper.
   * @exception UnsupportedOperationException
   */
  public ImmutableDecimalFormatSymbols(Locale locale) {
    Objects.requireNonNull(locale); // <= as stated in the java docs of DecimalFormatSymbols
    throw new UnsupportedOperationException();
  }

  /**
   * Creates an ImmutableDecimalFormatSymbols of the passed DecimalFormatSymbols instance.
   * @param backingDecimalFormatSymbols DecimalFormatSymbols instance to be wrapped to an immutable DecimalFormatSymbols
   */
  private ImmutableDecimalFormatSymbols(DecimalFormatSymbols backingDecimalFormatSymbols) {
    this.backingDecimalFormatSymbols = backingDecimalFormatSymbols;
  }

  /**
   * Factory to creates an ImmutableDecimalFormatSymbols of the passed DecimalFormatSymbols instance.
   * @param decimalFormatSymbols DecimalFormatSymbols instance to be wrapped to an immutable DecimalFormatSymbols
   */
  public static final ImmutableDecimalFormatSymbols of(DecimalFormatSymbols decimalFormatSymbols) {
    return new ImmutableDecimalFormatSymbols(decimalFormatSymbols);
  }

  @Override
  public char getZeroDigit() {
    return backingDecimalFormatSymbols.getZeroDigit();
  }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormatSymbols is immutable.
   * @exception UnsupportedOperationException
   */
  @Override
  public void setZeroDigit(char zeroDigit) { throw new UnsupportedOperationException(); }

  @Override
  public char getGroupingSeparator() {
    return backingDecimalFormatSymbols.getGroupingSeparator();
  }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormatSymbols is immutable.
   * @exception UnsupportedOperationException
   */
  @Override
  public void setGroupingSeparator(char groupingSeparator) { throw new UnsupportedOperationException(); }

  @Override
  public char getDecimalSeparator() { return backingDecimalFormatSymbols.getDecimalSeparator(); }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormatSymbols is immutable.
   * @exception UnsupportedOperationException
   */
  @Override
  public void setDecimalSeparator(char decimalSeparator) { throw new UnsupportedOperationException(); }

  @Override
  public char getPerMill() {
    return backingDecimalFormatSymbols.getPerMill();
  }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormatSymbols is immutable.
   * @exception UnsupportedOperationException
   */
  @Override
  public void setPerMill(char perMill) { throw new UnsupportedOperationException(); }

  @Override
  public char getPercent() {
    return backingDecimalFormatSymbols.getPercent();
  }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormatSymbols is immutable.
   * @exception UnsupportedOperationException
   */
  @Override
  public void setPercent(char percent) { throw new UnsupportedOperationException(); }

  @Override
  public char getDigit() {
    return backingDecimalFormatSymbols.getDigit();
  }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormatSymbols is immutable.
   * @exception UnsupportedOperationException
   */
  @Override
  public void setDigit(char digit) { throw new UnsupportedOperationException(); }

  @Override
  public char getPatternSeparator() { return backingDecimalFormatSymbols.getPatternSeparator(); }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormatSymbols is immutable.
   * @exception UnsupportedOperationException
   */
  @Override
  public void setPatternSeparator(char patternSeparator) { throw new UnsupportedOperationException(); }

  @Override
  public String getInfinity() {
    return backingDecimalFormatSymbols.getInfinity();
  }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormatSymbols is immutable.
   * @exception UnsupportedOperationException
   */
  @Override
  public void setInfinity(String infinity) { throw new UnsupportedOperationException(); }

  @Override
  public String getNaN() {
    return backingDecimalFormatSymbols.getNaN();
  }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormatSymbols is immutable.
   * @exception UnsupportedOperationException
   */
  @Override
  public void setNaN(String NaN) { throw new UnsupportedOperationException(); }

  @Override
  public char getMinusSign() {
    return backingDecimalFormatSymbols.getMinusSign();
  }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormatSymbols is immutable.
   * @exception UnsupportedOperationException
   */
  @Override
  public void setMinusSign(char minusSign) { throw new UnsupportedOperationException(); }

  @Override
  public String getCurrencySymbol() { return backingDecimalFormatSymbols.getCurrencySymbol(); }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormatSymbols is immutable.
   * @exception UnsupportedOperationException
   */
  @Override
  public void setCurrencySymbol(String currency) { throw new UnsupportedOperationException(); }

  @Override
  public String getInternationalCurrencySymbol() { return backingDecimalFormatSymbols.getInternationalCurrencySymbol(); }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormatSymbols is immutable.
   * @exception UnsupportedOperationException
   */
  @Override
  public void setInternationalCurrencySymbol(String currencyCode) { throw new UnsupportedOperationException(); }

  @Override
  public Currency getCurrency() { return backingDecimalFormatSymbols.getCurrency(); }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormatSymbols is immutable.
   * @exception UnsupportedOperationException
   */
  @Override
  public void setCurrency(Currency currency) { throw new UnsupportedOperationException(); }

  @Override
  public char getMonetaryDecimalSeparator()
  {
    return backingDecimalFormatSymbols.getMonetaryDecimalSeparator();
  }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormatSymbols is immutable.
   * @exception UnsupportedOperationException
   */
  @Override
  public void setMonetaryDecimalSeparator(char sep) { throw new UnsupportedOperationException(); }

  @Override
  public String getExponentSeparator() { return backingDecimalFormatSymbols.getExponentSeparator(); }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormatSymbols is immutable.
   * @exception UnsupportedOperationException
   */
  @Override
  public void setExponentSeparator(String exp) { throw new UnsupportedOperationException(); }

  @Override
  public Object clone() { return backingDecimalFormatSymbols.clone(); }

  @Override
  public boolean equals(Object obj) { return backingDecimalFormatSymbols.equals(obj); }

  @Override
  public int hashCode() { return backingDecimalFormatSymbols.hashCode();}
}
