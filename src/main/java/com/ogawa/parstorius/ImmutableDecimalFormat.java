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

import java.math.RoundingMode;
import java.text.AttributedCharacterIterator;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.util.Currency;
import java.util.Objects;

public class ImmutableDecimalFormat extends DecimalFormat {

  // backing/wrapped DecimalFormat instance
    DecimalFormat backingDecimalFormat;
    ImmutableDecimalFormatSymbols immutableDecimalFormatSymbols;

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormat is a DecimalFormat-wrapper.
   * @exception UnsupportedOperationException
   */
  public ImmutableDecimalFormat() {
    throw new UnsupportedOperationException();
  }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormat is a DecimalFormat-wrapper.
   * @param pattern a non-localized pattern string.
   * @exception NullPointerException if <code>pattern</code> is null
   * @exception UnsupportedOperationException if <code>pattern</code> is not null
   */
  public ImmutableDecimalFormat(String pattern) {
    Objects.requireNonNull(pattern);
    throw new UnsupportedOperationException();
  }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormat is a DecimalFormat-wrapper.
   * @param pattern a non-localized pattern string
   * @param symbols the set of symbols to be used
   * @exception NullPointerException if any of the given arguments is null
   * @exception UnsupportedOperationException if all arguments are not null
   */
  public ImmutableDecimalFormat (String pattern, DecimalFormatSymbols symbols) {
    Objects.requireNonNull(pattern);
    Objects.requireNonNull(symbols);
    throw new UnsupportedOperationException();
  }

  /**
   * Creates an ImmutableDecimalFormat wrapping the passed DecimalFormat-instance
   * @param backingDecimalFormat the decimal format to be wrapped
   * @exception NullPointerException if <code>decimalFormat</code> is null
   */
  private ImmutableDecimalFormat (DecimalFormat backingDecimalFormat) {
    Objects.requireNonNull(backingDecimalFormat);
    this.backingDecimalFormat = backingDecimalFormat;
    this.immutableDecimalFormatSymbols =
        ImmutableDecimalFormatSymbols.of(backingDecimalFormat.getDecimalFormatSymbols());
  }

  /**
   * Factory to creates an ImmutableDecimalFormatSymbols of the passed DecimalFormatSymbols instance.
   * @param decimalFormat DecimalFormatSymbols instance to be wrapped to an immutable DecimalFormatSymbols
   */
  public static final ImmutableDecimalFormat of(DecimalFormat decimalFormat) {
    return new ImmutableDecimalFormat(decimalFormat);
  }

  /**
   * Returns a clone of the wrapped DecimalFormat of the passed ImmutableDecimalFormat-instance
   * @param immutableDecimalFormat
   * @return Clone of the wrapped DecimalFormat-instance
   */
  public final DecimalFormat getWrappedDecimalFormatClone(ImmutableDecimalFormat immutableDecimalFormat) {
    return (DecimalFormat) immutableDecimalFormat.backingDecimalFormat.clone();
  }

  @Override
  public Object clone() { return backingDecimalFormat.clone(); }

  @Override
  public boolean equals(Object obj) {
    return (! (obj instanceof ImmutableDecimalFormat)) ? false : backingDecimalFormat.equals(obj);
  }

  @Override
  public int hashCode() { return backingDecimalFormat.hashCode(); }

  @Override
  public StringBuffer format(double number, StringBuffer result, FieldPosition fieldPosition)
    { throw new UnsupportedOperationException(); }

  @Override
  public StringBuffer format(long number, StringBuffer result, FieldPosition fieldPosition)
    { throw new UnsupportedOperationException(); }

  @Override
  public AttributedCharacterIterator formatToCharacterIterator(Object obj)
    { throw new UnsupportedOperationException(); }

  @Override
  public Number parse(String text, ParsePosition pos) { return null;}

  /**
   * Returns an immutable instance of the wrapped DecimalFormat-instances DecimalFormatSymbols
   * @return
   */
  @Override
  public DecimalFormatSymbols getDecimalFormatSymbols() { return immutableDecimalFormatSymbols; }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormat is immutable.
   */
  @Override
  public void setDecimalFormatSymbols(DecimalFormatSymbols newSymbols) { throw new UnsupportedOperationException(); }

  @Override
  public String getPositivePrefix () { return backingDecimalFormat.getPositivePrefix(); }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormat is immutable.
   */
  @Override
  public void setPositivePrefix (String newValue) { throw new UnsupportedOperationException(); }

  @Override
  public String getNegativePrefix () { return backingDecimalFormat.getNegativePrefix(); }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormat is immutable.
   */
  @Override
  public void setNegativePrefix (String newValue) { throw new UnsupportedOperationException(); }

  @Override
  public String getPositiveSuffix () { return backingDecimalFormat.getPositiveSuffix(); }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormat is immutable.
   */
  @Override
  public void setPositiveSuffix (String newValue) { throw new UnsupportedOperationException(); }

  @Override
  public String getNegativeSuffix () { return backingDecimalFormat.getNegativeSuffix(); }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormat is immutable.
   */
  @Override
  public void setNegativeSuffix (String newValue) { throw new UnsupportedOperationException(); }

  @Override
  public int getMultiplier () { return backingDecimalFormat.getMultiplier(); }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormat is immutable.
   */
  @Override
  public void setMultiplier (int newValue) { throw new UnsupportedOperationException(); }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormat is immutable.
   */
  @Override
  public void setGroupingUsed(boolean newValue) { throw new UnsupportedOperationException(); }

  @Override
  public int getGroupingSize () { return backingDecimalFormat.getGroupingSize(); }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormat is immutable.
   */
  @Override
  public void setGroupingSize (int newValue) { throw new UnsupportedOperationException(); }

  @Override
  public boolean isDecimalSeparatorAlwaysShown() { return backingDecimalFormat.isDecimalSeparatorAlwaysShown(); }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormat is immutable.
   */
  @Override
  public void setDecimalSeparatorAlwaysShown(boolean newValue) { throw new UnsupportedOperationException(); }

  @Override
  public boolean isParseBigDecimal() { return backingDecimalFormat.isParseBigDecimal(); }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormat is immutable.
   */
  @Override
  public void setParseBigDecimal(boolean newValue) { throw new UnsupportedOperationException(); }

  @Override
  public String toPattern() { return backingDecimalFormat.toPattern(); }

  @Override
  public String toLocalizedPattern() { return backingDecimalFormat.toLocalizedPattern(); }

  @Override
  public void applyPattern(String pattern) { throw new UnsupportedOperationException(); }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormat is immutable.
   */
  @Override
  public void applyLocalizedPattern(String pattern) { throw new UnsupportedOperationException(); }

  @Override
  public int getMinimumIntegerDigits() { return backingDecimalFormat.getMinimumIntegerDigits(); }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormat is immutable.
   */
  @Override
  public void setMinimumIntegerDigits(int newValue) { throw new UnsupportedOperationException(); }

  @Override
  public int getMaximumIntegerDigits() { return backingDecimalFormat.getMaximumIntegerDigits(); }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormat is immutable.
   */
  @Override
  public void setMaximumIntegerDigits(int newValue) { throw new UnsupportedOperationException(); }

  @Override
  public int getMaximumFractionDigits() { return backingDecimalFormat.getMaximumFractionDigits(); }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormat is immutable.
   */
  @Override
  public void setMaximumFractionDigits(int newValue) { throw new UnsupportedOperationException(); }

  @Override
  public int getMinimumFractionDigits() { return backingDecimalFormat.getMinimumFractionDigits(); }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormat is immutable.
   */
  @Override
  public void setMinimumFractionDigits(int newValue) { throw new UnsupportedOperationException(); }

  @Override
  public Currency getCurrency() { return backingDecimalFormat.getCurrency(); }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormat is immutable.
   */
  @Override
  public void setCurrency(Currency currency) { throw new UnsupportedOperationException(); }

  @Override
  public RoundingMode getRoundingMode() { return backingDecimalFormat.getRoundingMode(); }

  /**
   * Throws an UnsupportedOperationException since ImmutableDecimalFormat is immutable.
   */
  @Override
  public void setRoundingMode(RoundingMode roundingMode) { throw new UnsupportedOperationException(); }

}
