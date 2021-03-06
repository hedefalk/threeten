/*
 * Copyright (c) 2008-2011, Stephen Colebourne & Michael Nascimento Santos
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

import static javax.time.calendar.ISODateTimeRule.ZERO_EPOCH_MONTH;
import static javax.time.calendar.ISOPeriodUnit.CENTURIES;
import static javax.time.calendar.ISOPeriodUnit.YEARS;

import java.io.Serializable;

import javax.time.MathUtils;

/**
 * Mock rule.
 *
 * @author Stephen Colebourne
 */
public final class MockYearOfCenturyFieldRule extends DateTimeRule implements Serializable {

    /** Singleton instance. */
    public static final DateTimeRule INSTANCE = new MockYearOfCenturyFieldRule();
    /** Serialization version. */
    private static final long serialVersionUID = 1L;

    /** Constructor. */
    private MockYearOfCenturyFieldRule() {
        super("YearOfCentury", YEARS, CENTURIES, 0, 99, ZERO_EPOCH_MONTH);
    }

    private Object readResolve() {
        return INSTANCE;
    }

    @Override
    protected DateTimeField deriveFrom(CalendricalNormalizer merger) {
        LocalDate date = merger.getDate(false);
        if (date == null) {
            return null;
        }
        return field(MathUtils.floorMod(date.getYear(), 100));
    }

}
