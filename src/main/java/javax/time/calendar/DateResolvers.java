/*
 * Copyright (c) 2007, Stephen Colebourne & Michael Nascimento Santos
 *
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  * Neither the name of JSR-310 nor the names of its contributors
 *    may be used to endorse or promote products derived from this software
 *    without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
 * A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package javax.time.calendar;

import javax.time.calendar.field.DayOfMonth;
import javax.time.calendar.field.MonthOfYear;
import javax.time.calendar.field.Year;

/**
 * Provides common implementations of <code>DateResolver</code>.
 * <p>
 * DateResolvers is a utility class.
 * All resolvers returned are immutable and thread-safe.
 *
 * @author Stephen Colebourne
 */
public class DateResolvers {

    //-----------------------------------------------------------------------
    /**
     * Returns the strict resolver which does not manipulate the state
     * in any way, resulting in an exception for all invalid values.
     *
     * @return the strict resolver, never null
     */
    public static DateResolver strict() {
        return Strict.INSTANCE;
    }

    /**
     * Class implementing strict resolver.
     */
    private static class Strict implements DateResolver {
        /** The singleton instance. */
        private static final DateResolver INSTANCE = new Strict();

        /** {@inheritDoc} */
        public LocalDate resolveDate(Year year, MonthOfYear monthOfYear, DayOfMonth dayOfMonth) {
            return LocalDate.date(year, monthOfYear, dayOfMonth);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Returns the previous valid day resolver, which adjusts the date to be
     * valid by moving to the last valid day of the month.
     *
     * @return the previous valid day resolver, never null
     */
    public static DateResolver previousValid() {
        return PreviousValid.INSTANCE;
    }

    /**
     * Class implementing previousValid resolver.
     */
    private static class PreviousValid implements DateResolver {
        /** The singleton instance. */
        private static final DateResolver INSTANCE = new PreviousValid();

        /** {@inheritDoc} */
        public LocalDate resolveDate(Year year, MonthOfYear monthOfYear, DayOfMonth dayOfMonth) {
            int len = monthOfYear.lengthInDays(year);
            if (dayOfMonth.getValue() > len) {
                return LocalDate.date(year, monthOfYear, DayOfMonth.dayOfMonth(len));
            }
            return LocalDate.date(year, monthOfYear, dayOfMonth);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Returns the next valid day resolver, which adjusts the date to be
     * valid by moving to the first of the next month.
     *
     * @return the next valid day resolver, never null
     */
    public static DateResolver nextValid() {
        return NextValid.INSTANCE;
    }

    /**
     * Class implementing nextValid resolver.
     */
    private static class NextValid implements DateResolver {
        /** The singleton instance. */
        private static final DateResolver INSTANCE = new NextValid();

        /** {@inheritDoc} */
        public LocalDate resolveDate(Year year, MonthOfYear monthOfYear, DayOfMonth dayOfMonth) {
            int len = monthOfYear.lengthInDays(year);
            if (dayOfMonth.getValue() > len) {
                if (monthOfYear == MonthOfYear.DECEMBER) {
                    year = year.next();
                }
                return LocalDate.date(year, monthOfYear.next(), DayOfMonth.dayOfMonth(1));
            }
            return LocalDate.date(year, monthOfYear, dayOfMonth);
        }
    }

    //-----------------------------------------------------------------------
    /**
     * Returns the part lenient resolver, which adjusts the date to be
     * valid by moving it to the next month by the number of days that
     * are invalid up to the 31st of the month.
     *
     * @return the part lenient resolver, never null
     */
    public static DateResolver partLenient() {
        return PartLenient.INSTANCE;
    }

    /**
     * Class implementing partLenient resolver.
     */
    private static class PartLenient implements DateResolver {
        /** The singleton instance. */
        private static final DateResolver INSTANCE = new PartLenient();

        /** {@inheritDoc} */
        public LocalDate resolveDate(Year year, MonthOfYear monthOfYear, DayOfMonth dayOfMonth) {
            int len = monthOfYear.lengthInDays(year);
            if (dayOfMonth.getValue() > len) {
                if (monthOfYear == MonthOfYear.DECEMBER) {
                    year = year.next();
                }
                return LocalDate.date(year, monthOfYear.next(), DayOfMonth.dayOfMonth(dayOfMonth.getValue() - len));
            }
            return LocalDate.date(year, monthOfYear, dayOfMonth);
        }
    }

}