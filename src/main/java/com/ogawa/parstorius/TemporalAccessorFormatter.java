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

import java.text.ParsePosition;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.OffsetTime;
import java.time.Year;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.chrono.HijrahDate;
import java.time.chrono.JapaneseDate;
import java.time.chrono.MinguoDate;
import java.time.chrono.ThaiBuddhistDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.TemporalAccessor;
import java.util.Objects;
import java.util.function.Function;

// T can be one of: LocalDate, LocalDateTime, LocalTime, ZonedDateTime.
//                  The implementation is prepared but not tested for HijrahDate, Instant, JapaneseDate, MinguoDate,
//                  OffsetDateTime, OffsetTime, ThaiBuddhistDate, Year, YearMonth

public class TemporalAccessorFormatter<T extends TemporalAccessor>
    extends Formatter<T, DateTimeFormatter, TemporalAccessorFormatter<T>> {

    final private Class<T> classT;
    // reference to BigDecimal method to cast to T (used by cast())
    private final Function<TemporalAccessor, ?> castMethod;

    /* ************************************************************************** */
    /* ****************************** constructors ****************************** */
    /* ************************************************************************** */

    public TemporalAccessorFormatter(Class<T> temporalAccessorClassT, DateTimeFormatter dateTimeFormatter) {
        super(dateTimeFormatter, true);

        Objects.requireNonNull(temporalAccessorClassT, "temporalAccessorClassT");

        this.classT = temporalAccessorClassT;

        castMethod = getCastMethod(temporalAccessorClassT);

        if (castMethod == null) {
            throw new IllegalArgumentException(temporalAccessorClassT.getName() + " not supported");
        }

        init();

    }

    /* ************************************************************************** */
    /* ********************************* common ****************** */
    /* ************************************************************************** */

    /* ***************************** common setter ****************************** */

    @Override public DateTimeFormatter getBaseFormatter() {
        return new DateTimeFormatterBuilder().append(baseFormatter).toFormatter();
    }

    /* ****************************** common logic ****************************** */

    @Override String patternToString() {
        return baseFormatter.toFormat().toString();
    }

    @Override protected TemporalAccessorFormatter<T> init() {

        DateTimeFormatterBuilder dateTimeFormatterBuilder = new DateTimeFormatterBuilder();

        if (parseCaseInsensitive) {
            dateTimeFormatterBuilder.parseCaseInsensitive();
        }

        dateTimeFormatterBuilder.parseLenient().append(baseFormatter);
        return this;

    }

    @Override protected TemporalAccessorFormatter<T> copyProperties(TemporalAccessorFormatter<T> sourceFormatter) {
        return super.copyProperties(sourceFormatter);
    }

    @Override public TemporalAccessorFormatter<T> clone() {
        return new TemporalAccessorFormatter<>(classT, baseFormatter).copyProperties(this);
    }

    @SuppressWarnings("unchecked")
    private T cast(TemporalAccessor temporalAccessor) {
        return (T) castMethod.apply(temporalAccessor);
    }

    private Function<TemporalAccessor, Object> getCastMethod(Class<T> temporalAccessorClassT) {

        if (temporalAccessorClassT.equals(LocalDate.class)) {
            return LocalDate::from;
        } else if (temporalAccessorClassT.equals(LocalTime.class)) {
            return LocalTime::from;
        } else if (temporalAccessorClassT.equals(LocalDateTime.class)) {
            return LocalDateTime::from;
        } else if (temporalAccessorClassT.equals(ZonedDateTime.class)) {
            return ZonedDateTime::from;
        } else if (temporalAccessorClassT.equals(OffsetTime.class)) {
            return OffsetTime::from;
        } else if (temporalAccessorClassT.equals(OffsetDateTime.class)) {
            return OffsetDateTime::from;
        } else if (temporalAccessorClassT.equals(Year.class)) {
            return Year::from;
        } else if (temporalAccessorClassT.equals(YearMonth.class)) {
            return YearMonth::from;
        } else if (temporalAccessorClassT.equals(HijrahDate.class)) {
            return HijrahDate::from;
        } else if (temporalAccessorClassT.equals(MinguoDate.class)) {
            return MinguoDate::from;
        } else if (temporalAccessorClassT.equals(JapaneseDate.class)) {
            return JapaneseDate::from;
        } else if (temporalAccessorClassT.equals(ThaiBuddhistDate.class)) {
            return ThaiBuddhistDate::from;
        } else if (temporalAccessorClassT.equals(Instant.class)) {
            return Instant::from;
        } else {
            return null;
        }
    }

    /* ************************************************************************** */
    /* ********************************* parsing ******************************** */
    /* ************************************************************************** */

    /* ******************************* parse logic ****************************** */

    @Override
    public T parseText(String text, ParsePosition parsePosition) {

        TemporalAccessor result =  baseFormatter.parse(text, parsePosition);

        return result != null ? cast(result) : null;

    }

    /* ************************************************************************** */
    /* ******************************* formatting ******************************* */
    /* ************************************************************************** */

    /* ****************************** format logic ****************************** */

    @Override protected String formatObject(final T object) {
        return baseFormatter.format(object);
    }

}

