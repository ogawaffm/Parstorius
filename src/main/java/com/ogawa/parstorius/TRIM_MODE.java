package com.ogawa.parstorius;

import java.util.function.UnaryOperator;

public enum TRIM_MODE implements UnaryOperator<String> {

  NONE(String::toString),
  TRIM_TRAILING(s -> StringUtil.trimTrailing(s)),
  TRIM_LEADING(s -> StringUtil.trimLeading(s)),
  TRIM(String::toString),
  STRIP_LEADING(String::stripLeading),
  STRIP_TRAILING(String::stripTrailing),
  STRIP(String::strip);

  private UnaryOperator<String> unaryOperator;

  TRIM_MODE(final UnaryOperator<String> unaryOperator) { this.unaryOperator = unaryOperator; }

  @Override public String apply(String s) { return unaryOperator.apply(s); }

}
