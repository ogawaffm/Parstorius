package com.ogawa.parstorius.formatter;

import com.ogawa.parstorius.Formatter;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;

public interface FormatterConstructorDefaultsTest <T, P, F extends Formatter<T, P, F>> {

  List<Formatter<T, P, F>> getConstructed();

  @Test
  @DisplayName("default of non-constructor-arguments")
  default void defaultsOfNoneConstructorArgs() {
    for(Formatter<T, P, F> f : getConstructed()) {
      assertEquals(null, f.getParseNullDefault());
      assertEquals(null, f.getParseMissingDefault());
      assertEquals(null, f.getParseErrorDefault());
      assertEquals(List.of(), f.getParseNullTexts());
    }
  }


}
